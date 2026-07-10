package com.kondra.vm.vmx.data;

public class SectionHeader {
    // Offsets on VMX file that hold values
    // NOT the actual values
    public static final int OFFSET_TEXT_OFFSET = 24;
    public static final int OFFSET_TEXT_SIZE = 28;
    public static final int OFFSET_RODATA_OFFSET = 32;
    public static final int OFFSET_RODATA_SIZE = 36;
    public static final int OFFSET_DATA_OFFSET = 40;
    public static final int OFFSET_DATA_SIZE = 44;
    public static final int OFFSET_BSS_OFFSET = 48;
    public static final int OFFSET_BSS_SIZE = 52;
    public static final int HEADER_SIZE = 32;
}
