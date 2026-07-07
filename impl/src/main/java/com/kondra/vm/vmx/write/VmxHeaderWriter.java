package com.kondra.vm.vmx.write;

import com.kondra.vm.common.Version;
import com.kondra.vm.vmx.VmxHeader;

import static com.kondra.vm.vmx.ArrayProcessor.*;

public class VmxHeaderWriter {
    public static void writeHeader(VmxHeader header, byte[] bytes, int fileSize, int programSize) {
        bytes[0] = 'v';
        bytes[1] = 'm';
        bytes[2] = 'x';
        bytes[3] = '\0';

        writeByte(bytes, VmxHeader.OFFSET_EXT_COUNT, header.getExtCount());
        writeByte(bytes, VmxHeader.OFFSET_VMX_VERSION, header.getVmxVersion());
        writeByte(bytes, VmxHeader.OFFSET_FLAGS, header.getFlags());

        Version version = header.getVersion();
        writeByte(bytes, VmxHeader.OFFSET_VERSION_MAJOR, version.getMajor());
        writeByte(bytes, VmxHeader.OFFSET_VERSION_MINOR, version.getMinor());
        writeShort(bytes, VmxHeader.OFFSET_VERSION_BUILD_NUM, version.getBuildNum());

        writeInt(bytes, VmxHeader.OFFSET_FILE_SIZE, fileSize);
        writeInt(bytes, VmxHeader.OFFSET_PROGRAM_SIZE, programSize);
        writeInt(bytes, VmxHeader.OFFSET_ENTRY_OFFSET, header.getEntryOffset());

        writeInt(bytes, VmxHeader.OFFSET_TEXT_OFFSET, header.getTextOffset());
        writeInt(bytes, VmxHeader.OFFSET_TEXT_SIZE, header.getTextSize());
        writeInt(bytes, VmxHeader.OFFSET_RODATA_OFFSET, header.getRodataOffset());
        writeInt(bytes, VmxHeader.OFFSET_RODATA_SIZE, header.getRodataSize());
        writeInt(bytes, VmxHeader.OFFSET_DATA_OFFSET, header.getDataOffset());
        writeInt(bytes, VmxHeader.OFFSET_DATA_SIZE, header.getDataSize());
        writeInt(bytes, VmxHeader.OFFSET_BSS_OFFSET, header.getBssOffset());
        writeInt(bytes, VmxHeader.OFFSET_BSS_SIZE, header.getBssSize());
    }
}
