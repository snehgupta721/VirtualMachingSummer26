package com.kondra.vm.vmx;

import com.kondra.vm.common.Version;
import com.kondra.vm.common.vmx.VmxException;
import com.kondra.vm.common.vmx.VmxExt;
import com.kondra.vm.common.vmx.VmxFile;
import com.kondra.vm.vmx.read.VmxExtensionReader;
import com.kondra.vm.vmx.read.VmxHeaderReader;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;

public class MyVmxFile implements VmxFile {
    private int flags;
    private Version version;
    private int entryOffset;

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

        VmxHeader header = VmxHeaderReader.parseHeader(fileBytes);
        flags = header.getFlags();
        version = header.getVersion();
        entryOffset = header.getEntryOffset();

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
//        int extCount = extensions.size();
//        int headerSize = 24 + 32 + (12 * extCount);
//        int textOffset   = 0;
//        int roDataOffset = textOffset + text.length;
//        int dataOffset   = roDataOffset + rodata.length;
//        int bssOffset    = dataOffset + data.length;
//
//        int cursor = bssOffset;
//        int[] extOffsets = new int[extCount];
//        for (int i = 0; i < extCount; i++) {
//            extOffsets[i] = cursor;
//            MyVmxExt ext = (MyVmxExt) extensions.get(i);
//            cursor += ext.getData().length;
//            // Add padding so that the section is always aligned
//            if (cursor % 4 != 0) {
//                cursor += (4 - (cursor % 4));
//            }
//        }
//        byte[] fileBytes = new byte[headerSize + cursor];
//
//        fileBytes[0] = 'v';
//        fileBytes[1] = 'm';
//        fileBytes[2] = 'x';
//        fileBytes[3] = '\0';
//
//        fileBytes[4] = (byte) extCount;
//        fileBytes[6] = (byte) flags;
//
//        writeShort(fileBytes, 8, (short) version.getMajor());
//        writeShort(fileBytes, 10, (short) version.getMinor());
//        writeInt(fileBytes, 12, version.getBuildNum());
//        writeInt(fileBytes, 20, entryOffset);
//
//        // section headers
//        writeInt(fileBytes, 24, textOffset);
//        writeInt(fileBytes, 28, text.length);
//        writeInt(fileBytes, 32, roDataOffset);
//        writeInt(fileBytes, 36, rodata.length);
//        writeInt(fileBytes, 40, dataOffset);
//        writeInt(fileBytes, 44, data.length);
//        writeInt(fileBytes, 48, bssOffset);
//        writeInt(fileBytes, 52, bss.length);
//
//        System.arraycopy(text, 0, fileBytes, headerSize + textOffset, text.length);
//        System.arraycopy(rodata, 0, fileBytes, headerSize + roDataOffset, rodata.length);
//        System.arraycopy(data, 0, fileBytes, headerSize + dataOffset, data.length);
//
//        cursor = 24 + 32;
//        for (int i = 0; i < extCount; i++) {
//            MyVmxExt ext = (MyVmxExt) extensions.get(i);
//            fileBytes[cursor] = (byte) ext.getType();
//            fileBytes[cursor + 1] = (byte) ext.getFlags();
//            writeInt(fileBytes, cursor + 4, extOffsets[i]);
//            writeInt(fileBytes, cursor + 8, ext.getData().length);
//            System.arraycopy(ext.getData(), 0, fileBytes, headerSize + extOffsets[i], ext.getData().length);
//            cursor += VmxExt.HEADER_SIZE;
//        }
//
//        try (java.io.FileOutputStream fos = new java.io.FileOutputStream(file)) {
//            fos.write(fileBytes);
//        } catch (Exception e) {
//            throw new VmxException("Failed to write VMX to file: " + file.getAbsolutePath(), e);
//        }
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
        return version;
    }

    @Override
    public void setVersion(Version version) {
        this.version = version;
    }

    @Override
    public int getFlags() {
        return flags;
    }

    @Override
    public void setFlags(int flags) {
        this.flags = flags;
    }

    @Override
    public int getEntryOffset() {
        return entryOffset;
    }

    @Override
    public void setEntryOffset(int entryOffset) {
        this.entryOffset = entryOffset;
    }


}
