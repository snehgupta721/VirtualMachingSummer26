package com.kondra.vm.vmx;

import com.kondra.vm.common.Version;
import com.kondra.vm.common.vmx.VmxException;
import com.kondra.vm.common.vmx.VmxExt;
import com.kondra.vm.common.vmx.VmxFile;
import com.kondra.vm.vmx.read.VmxExtensionReader;
import com.kondra.vm.vmx.read.VmxHeaderReader;
import com.kondra.vm.vmx.write.VmxExtensionWriter;
import com.kondra.vm.vmx.write.VmxHeaderWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;

public class MyVmxFile implements VmxFile {
    VmxHeader header;

    private final List<VmxExt> extensions = new java.util.ArrayList<>();

    private byte[] text;
    private byte[] rodata;
    private byte[] data;
    private byte[] bss;

    public MyVmxFile() {

    }

    public MyVmxFile(File file) throws VmxException {
        // read file
        byte[] fileBytes = new byte[(int) file.length()];
        try {
            FileInputStream fis = new FileInputStream(file);
            fis.read(fileBytes);
        } catch (Exception e) {
            throw new VmxException("Error reading file " + file.getAbsolutePath(), e);
        }

        header = VmxHeaderReader.parseHeader(fileBytes);

        text   = Arrays.copyOfRange(fileBytes, header.getHeaderSize() + header.getTextOffset(),
                header.getHeaderSize() + header.getTextOffset() + header.getTextSize());
        rodata = Arrays.copyOfRange(fileBytes, header.getHeaderSize() + header.getRodataOffset(),
                header.getHeaderSize() + header.getRodataOffset() + header.getRodataSize());
        data   = Arrays.copyOfRange(fileBytes, header.getHeaderSize() + header.getDataOffset(),
                header.getHeaderSize() + header.getDataOffset() + header.getDataSize());
        bss    = new byte[header.getBssSize()];

        extensions.addAll(VmxExtensionReader.parseExtensions(fileBytes, header.getExtCount(), header.getHeaderSize()));
    }

    @Override
    public void write(File file) throws VmxException {
        int headerSize = header.getHeaderSize();
        int total = headerSize + text.length + rodata.length + data.length;
        for (VmxExt ext : extensions) {
            total += ((MyVmxExt) ext).getData().length; // Assuming getData() exists to fetch the raw byte[]
        }

        byte[] bytes = new byte[total];
        VmxHeaderWriter.writeHeader(header, bytes);

        int cursor = VmxExtensionWriter.writeExtensions(bytes, extensions, headerSize);

        System.arraycopy(text, 0, bytes, headerSize + header.getTextOffset(), text.length);
        System.arraycopy(rodata, 0, bytes, headerSize + header.getRodataOffset(), rodata.length);
        System.arraycopy(data, 0, bytes, headerSize + header.getDataOffset(), data.length);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes);
        } catch (Exception e) {
            throw new VmxException("Error writing file " + file.getAbsolutePath(), e);
        }
    }

    @Override
    public byte[] getSection(int section) {
        switch (section) {
            case VmxFile.SECTION_TEXT: return text;
            case VmxFile.SECTION_RODATA: return rodata;
            case VmxFile.SECTION_DATA: return data;
            case VmxFile.SECTION_BSS: return bss;
            default: return null;
        }
    }

    @Override
    public void setSection(int section, byte[] data) {
        switch (section) {
            case VmxFile.SECTION_TEXT:
                text = data;
                break;
            case VmxFile.SECTION_RODATA:
                rodata = data;
                break;
            case VmxFile.SECTION_DATA:
                this.data = data;
                break;
            case VmxFile.SECTION_BSS:
                bss = data;
                break;
        }
    }

    @Override
    public List<VmxExt> getExtensions() {
        return extensions;
    }

    @Override
    public VmxExt getExtension(int type) {
        for (VmxExt ext : extensions) {
            if (ext.getType() == type) {
                return ext;
            }
        }
        return null;
    }

    @Override
    public Version getVersion() {
        return header.getVersion();
    }

    @Override
    public void setVersion(Version version) {
        this.header.setVersion(version);
    }

    @Override
    public int getFlags() {
        return header.getFlags();
    }

    @Override
    public void setFlags(int flags) {
        header.setFlags(flags);
    }

    @Override
    public int getEntryOffset() {
        return header.getEntryOffset();
    }

    @Override
    public void setEntryOffset(int entryOffset) {
        header.setEntryOffset(entryOffset);
    }
}
