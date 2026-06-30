package com.kondra.vm.vmx.read;

import com.kondra.vm.common.Version;
import com.kondra.vm.common.vmx.VmxException;
import com.kondra.vm.vmx.VmxHeader;

import static com.kondra.vm.vmx.ArrayProcessor.*;

public class VmxHeaderReader {
    public static VmxHeader parseHeader(byte[] bytes) throws VmxException {
        VmxHeader header = new VmxHeader();
        if ((bytes[0] & 0xFF) != 'v' || (bytes[1] & 0xFF) != 'm' ||
                (bytes[2] & 0xFF) != 'x' || (bytes[3] & 0xFF) != '\0') {
            throw new VmxException("File signature incorrect");
        }

        header.setExtCount(readByteUnsigned(bytes, 4));
        header.setFlags(readByteUnsigned(bytes, 6));
        // This will always be little endian
        if (header.getFlags() != 8) {
            throw new VmxException("Incorrect flag: Should be little endian");
        }

        int major = readByteSigned(bytes, 8);   // signed
        int minor = readByteSigned(bytes, 9);   // signed
        short buildNum = readShort(bytes, 10);
        header.setVersion(new Version(major, minor, buildNum));

        header.setEntryOffset(readInt(bytes, 20));

        // Section Headers (Absolute file offsets)
        header.setTextOffset(readInt(bytes, 24));
        header.setTextSize(readInt(bytes, 28));
        header.setRodataOffset(readInt(bytes, 32));
        header.setRodataSize(readInt(bytes, 36));
        header.setDataOffset(readInt(bytes, 40));
        header.setDataSize(readInt(bytes, 44));
        header.setBssOffset(readInt(bytes, 48));
        header.setBssSize(readInt(bytes, 52));

        header.setHeaderSize(VmxHeader.EXT_HEADER_START + (12 * header.getExtCount()));
        return header;
    }
}
