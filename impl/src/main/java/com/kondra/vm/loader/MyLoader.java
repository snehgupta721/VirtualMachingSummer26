package com.kondra.vm.loader;

import com.kondra.vm.common.loader.Image;
import com.kondra.vm.common.loader.ImageLoadException;
import com.kondra.vm.common.loader.Loader;
import com.kondra.vm.common.loader.VmxFinder;
import com.kondra.vm.common.memory.MemoryMgr;
import com.kondra.vm.common.vmx.VmxException;
import com.kondra.vm.common.vmx.VmxFile;
import com.kondra.vm.vmx.MyVmxFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyLoader implements Loader {
    private final MemoryMgr memoryMgr;
    private final int[] sectionsOrder = {
            VmxFile.SECTION_TEXT,
            VmxFile.SECTION_RODATA,
            VmxFile.SECTION_DATA,
            VmxFile.SECTION_BSS
    };
    private final List<MyImage> images;
    private final MyVmxFinder finder;
    private List<String> exportSymbols;
    private List<String> preloadNames;

    public MyLoader(MemoryMgr memoryMgr) {
        this.memoryMgr = memoryMgr;
        this.images = new ArrayList<>();
        this.finder = new MyVmxFinder();
    }

    @Override
    public Image loadProgram(String name) throws ImageLoadException {
        return load(name, false);
    }

    @Override
    public Image loadLibrary(String name) throws ImageLoadException {
        return load(name, true);
    }

    private Image load(String rawName, boolean isLibrary) throws ImageLoadException {
        String name = normalizeName(rawName);

        // If it is a library, and it already has an image, just bump count and return
        if (isLibrary) {
            Image curr = getImageByName(name);
            if (curr != null) {
                curr.lock();;
                return curr;
            }
        }

        // programs and libraries can now be treated the same
        if (!rawName.endsWith(".vmx")) { rawName = rawName + ".vmx"; }
        File file = finder.find(rawName);
        if (file == null) {
            file = finder.find(name);
        }
        if (file == null) {
            throw new ImageLoadException("Could not find VMX file " + name);
        }

        MyVmxFile vmxFile;
        try {
            vmxFile = new MyVmxFile(file);
        } catch (VmxException e) {
            throw new ImageLoadException("Could not parse VMX file " + name, e);
        }

        MyImage image = new MyImage(name, vmxFile.getVersion(), file);
        image.lock();

        // Load sections
        for (int sectionIdx : sectionsOrder) {
            byte[] sectionData = vmxFile.getSection(sectionIdx);
            int address = memoryMgr.allocate(sectionData.length);   // how to get the system memory manager?
            // assign the data to be correct?
            image.setSection(sectionIdx, address, sectionData.length);
        }

        // Exports
        exportSymbols = vmxFile.getExportedSymbols();
        for (String symbol : exportSymbols) {
            int address = memoryMgr.allocate(symbol.length());
            // assign data to this block of adddress?
            image.setSymbolAddress(symbol, address);
        }

        // preloads
        preloadNames = vmxFile.getPreloadSymbols();
        for (String preloadName : preloadNames) {
            Image preload = loadLibrary(preloadName);
            image.setPreload(preload);
        }

        return image;
    }

    private String normalizeName(String rawName) throws ImageLoadException {
        if (rawName == null) return "";
        String name = rawName.replace('\\', '/');
        String[] names = rawName.split("/");
        name = names[names.length - 1];
        if (name.toLowerCase().endsWith(".vmx")) return name;
        return name + ".vmx";
    }

    @Override
    public void unloadImage(Image image) {
        if (image == null) return;
        image.unlock();

        // free all section data related to this image
        for (int sectionIdx : sectionsOrder) {
            memoryMgr.free(image.getSectionAddress(sectionIdx));
        }

        // free all export symbols related to this image
        for (String symbol : exportSymbols) {
            memoryMgr.free(image.getSymbolAddress(symbol));
        }

        // free all preloads related to this image
        for (String preload : preloadNames) {
            memoryMgr.free(image.getSymbolAddress(preload));
        }
    }

    @Override
    public List<? extends Image> getImages() {
        return images;
    }

    @Override
    public Image getImageByName(String name) {
        for (MyImage image : images) {
            if (image.getName().equals(name)) {
                return image;
            }
        }
        return null;
    }

    @Override
    public Image getImageByAddress(int addr) {
        return null;
    }

    @Override
    public VmxFinder getVmxFinder() {
        return finder;
    }

    @Override
    public void reset() {
        memoryMgr.reset();
        this.images.clear();
        this.finder.reset();
    }
}
