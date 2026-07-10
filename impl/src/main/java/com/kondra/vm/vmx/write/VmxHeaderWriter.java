package com.kondra.vm.vmx.write;

import com.kondra.vm.common.Version;
import com.kondra.vm.vmx.data.VmxHeader;

import java.io.IOException;
import java.io.RandomAccessFile;

import static com.kondra.vm.vmx.ArrayProcessor.*;

public class VmxHeaderWriter {

    public VmxHeaderWriter() {

    }

    public void write(RandomAccessFile raf, VmxHeader header, int programSize, int fileSize) throws IOException {
        writeByte(raf, 0, 'v');
        writeByte(raf, 1, 'm');
        writeByte(raf, 2, 'x');
        writeByte(raf, 3, '\0');

        writeByte(raf, VmxHeader.OFFSET_EXT_COUNT, header.getExtCount());
        writeByte(raf, VmxHeader.OFFSET_EXT_COUNT, header.getExtCount());
        writeByte(raf, VmxHeader.OFFSET_VMX_VERSION, header.getVmxVersion());
        writeByte(raf, VmxHeader.OFFSET_FLAGS, header.getFlags());

        Version version = header.getVersion();
        writeByte(raf, VmxHeader.OFFSET_VERSION_MAJOR, version.getMajor());
        writeByte(raf, VmxHeader.OFFSET_VERSION_MINOR, version.getMinor());
        writeShort(raf, VmxHeader.OFFSET_VERSION_BUILD_NUM, version.getBuildNum());

        writeInt(raf, VmxHeader.OFFSET_FILE_SIZE, fileSize);
        writeInt(raf, VmxHeader.OFFSET_PROGRAM_SIZE, programSize);
        writeInt(raf, VmxHeader.OFFSET_ENTRY_OFFSET, header.getEntryOffset());
    }
}
