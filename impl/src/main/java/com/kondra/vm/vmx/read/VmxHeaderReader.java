package com.kondra.vm.vmx.read;

import com.kondra.vm.common.Version;
import com.kondra.vm.common.vmx.VmxException;
import com.kondra.vm.vmx.data.VmxHeader;

import java.io.IOException;
import java.io.RandomAccessFile;

import static com.kondra.vm.vmx.ArrayProcessor.*;

public class VmxHeaderReader {

    public VmxHeader read(RandomAccessFile raf) throws VmxException, IOException {
        VmxHeader header = new VmxHeader();
        byte[] magic = new byte[4];
        raf.seek(0);
        raf.readFully(magic);
        if ((magic[0] & 0xFF) != 'v' || (magic[1] & 0xFF) != 'm' ||
                (magic[2] & 0xFF) != 'x' || (magic[3] & 0xFF) != '\0') {
            throw new VmxException("File signature incorrect");
        }

        header.setExtCount(readByteUnsigned(raf, VmxHeader.OFFSET_EXT_COUNT));
        header.setVmxVersion(readByteUnsigned(raf, VmxHeader.OFFSET_VMX_VERSION));
        if (header.getVmxVersion() != 2) {
            throw new VmxException("Invalid VMX version");
        }
        header.setFlags(readByteUnsigned(raf, VmxHeader.OFFSET_FLAGS));
        // This will always be little endian
        if (header.getFlags() != 8) {
            throw new VmxException("Incorrect flag: Should be little endian");
        }

        int major = readByteSigned(raf, VmxHeader.OFFSET_VERSION_MAJOR);   // signed
        int minor = readByteSigned(raf, VmxHeader.OFFSET_VERSION_MINOR);   // signed
        short buildNum = readShort(raf, VmxHeader.OFFSET_VERSION_BUILD_NUM);
        header.setVersion(new Version(major, minor, buildNum));

        header.setEntryOffset(readInt(raf, VmxHeader.OFFSET_ENTRY_OFFSET));
        return header;
    }
}
