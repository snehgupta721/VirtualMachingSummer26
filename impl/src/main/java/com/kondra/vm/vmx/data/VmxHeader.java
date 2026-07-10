package com.kondra.vm.vmx.data;

import com.kondra.vm.common.Version;
import com.kondra.vm.common.vmx.VmxExt;

public class VmxHeader {
    public static final int OFFSET_EXT_COUNT = 4;
    public static final int OFFSET_VMX_VERSION = 5;
    public static final int OFFSET_FLAGS = 6;
    public static final int OFFSET_VERSION_MAJOR = 8;
    public static final int OFFSET_VERSION_MINOR = 9;
    public static final int OFFSET_VERSION_BUILD_NUM = 10;
    public static final int OFFSET_FILE_SIZE = 12;
    public static final int OFFSET_PROGRAM_SIZE = 16;
    public static final int OFFSET_ENTRY_OFFSET = 20;
    public static final int HEADER_SIZE = 24;   // does NOT include extensions or sections

    private int extCount;
    private int vmxVersion;
    private int flags;
    private Version version;
    private int entryOffset;

    public VmxHeader() {
        extCount = 0;
        vmxVersion = 2;
        flags = 8;
        version = new Version();
        entryOffset = 0;
    }

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

    public void setExtCount(int extCount) {
        this.extCount = extCount;
    }

    public void setVmxVersion(int vmxVersion) {
        this.vmxVersion = vmxVersion;
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

    public int getExtHeaderSize() {
        return VmxExt.HEADER_SIZE * extCount;
    }
}
