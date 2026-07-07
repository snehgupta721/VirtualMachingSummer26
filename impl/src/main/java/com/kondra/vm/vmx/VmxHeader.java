package com.kondra.vm.vmx;

import com.kondra.vm.common.Version;

public class VmxHeader {
    public static final int EXT_HEADER_START = 56;
    public static final int OFFSET_EXT_COUNT = 4;
    public static final int OFFSET_VMX_VERSION = 5;
    public static final int OFFSET_FLAGS = 6;
    public static final int OFFSET_VERSION_MAJOR = 8;
    public static final int OFFSET_VERSION_MINOR = 9;
    public static final int OFFSET_VERSION_BUILD_NUM = 10;
    public static final int OFFSET_FILE_SIZE = 12;
    public static final int OFFSET_PROGRAM_SIZE = 16;
    public static final int OFFSET_ENTRY_OFFSET = 20;
    public static final int OFFSET_TEXT_OFFSET = 24;
    public static final int OFFSET_TEXT_SIZE = 28;
    public static final int OFFSET_RODATA_OFFSET = 32;
    public static final int OFFSET_RODATA_SIZE = 36;
    public static final int OFFSET_DATA_OFFSET = 40;
    public static final int OFFSET_DATA_SIZE = 44;
    public static final int OFFSET_BSS_OFFSET = 48;
    public static final int OFFSET_BSS_SIZE = 52;

    private int extCount;
    private int vmxVersion = 2;
    private int flags;
    private Version version;
    private int entryOffset;
    private int headerSize;

    private int textOffset, textSize;
    private int rodataOffset, rodataSize;
    private int dataOffset, dataSize;
    private int bssOffset, bssSize;

    public VmxHeader() {}

    public int getVmxVersion() {
        return vmxVersion;
    }

    public int getExtCount() {
        return extCount;
    }

    public int getFlags() {
        return flags;
    }

    public Version getVersion() {
        return version;
    }

    public int getEntryOffset() {
        return entryOffset;
    }

    public int getHeaderSize() {
        return headerSize;
    }

    public int getTextOffset() {
        return textOffset;
    }

    public int getTextSize() {
        return textSize;
    }

    public int getRodataOffset() {
        return rodataOffset;
    }

    public int getRodataSize() {
        return rodataSize;
    }

    public int getDataOffset() {
        return dataOffset;
    }

    public int getDataSize() {
        return dataSize;
    }

    public int getBssOffset() {
        return bssOffset;
    }

    public int getBssSize() {
        return bssSize;
    }

    public void setExtCount(int extCount) {
        this.extCount = extCount;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public void setEntryOffset(int entryOffset) {
        this.entryOffset = entryOffset;
    }

    public void setHeaderSize(int headerSize) {
        this.headerSize = headerSize;
    }

    public void setTextOffset(int textOffset) {
        this.textOffset = textOffset;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void setRodataOffset(int rodataOffset) {
        this.rodataOffset = rodataOffset;
    }

    public void setRodataSize(int rodataSize) {
        this.rodataSize = rodataSize;
    }

    public void setDataOffset(int dataOffset) {
        this.dataOffset = dataOffset;
    }

    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }

    public void setBssOffset(int bssOffset) {
        this.bssOffset = bssOffset;
    }

    public void setBssSize(int bssSize) {
        this.bssSize = bssSize;
    }

}
