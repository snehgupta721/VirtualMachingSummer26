package com.kondra.vm.loader;

import com.kondra.vm.common.Version;
import com.kondra.vm.common.loader.Image;
import com.kondra.vm.loader.data.Section;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyImage implements Image {
    private final String name;
    private final Version version;
    private final File file;
    private int useCount;
    private int entry;
    private final Map<Integer, Section> sections;  // section ID to section (address, size)
    private final Map<String, Integer> symbols;    // symbols to address
    private final List<Image> preloads;            // vmx files to preload before loading this image

    public MyImage(String name, Version version, File file) {
        this.name = name;
        this.version = version;
        this.file = file;
        this.useCount = 0;
        this.entry = 0;
        this.sections = new HashMap<>();
        this.symbols = new HashMap<>();
        this.preloads = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public Version getVersion() {
        return version;
    }

    @Override
    public int getUseCount() {
        return useCount;
    }

    @Override
    public void lock() {
        useCount++;
    }

    @Override
    public void unlock() {
        useCount--;
        // unload image if useCount == 0
    }

    @Override
    public int getEntry() {
        return entry;
    }

    @Override
    public int getSymbolAddress(String symbol) {
        return symbols.getOrDefault(symbol, 0);
    }

    public void setSymbolAddress(String symbol, int address) {
        symbols.put(symbol, address);
    }

    public void setSection(int section, int address, int size) {
        sections.put(section, new Section(address, size));
    }

    @Override
    public int getSectionAddress(int section) {
        return sections.get(section).getAddress();
    }

    @Override
    public int getSectionLength(int section) {
        return sections.get(section).getSize();
    }

    @Override
    public List<Image> getPreloads() {
        return preloads;
    }

    public void setPreload(Image image) {
        preloads.add(image);
    }

    public void cleanUp() {
        // free memory
    }
}
