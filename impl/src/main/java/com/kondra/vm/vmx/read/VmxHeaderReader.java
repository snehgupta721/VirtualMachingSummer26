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

        header.setExtCount(readByteUnsigned(bytes, VmxHeader.OFFSET_EXT_COUNT));
        header.setFlags(readByteUnsigned(bytes, VmxHeader.OFFSET_FLAGS));
        // This will always be little endian
        if (header.getFlags() != 8) {
            throw new VmxException("Incorrect flag: Should be little endian");
        }

        int major = readByteSigned(bytes, VmxHeader.OFFSET_VERSION_MAJOR);   // signed
        int minor = readByteSigned(bytes, VmxHeader.OFFSET_VERSION_MINOR);   // signed
        short buildNum = readShort(bytes, VmxHeader.OFFSET_VERSION_BUILD_NUM);
        header.setVersion(new Version(major, minor, buildNum));

        header.setEntryOffset(readInt(bytes, VmxHeader.OFFSET_ENTRY_OFFSET));

        // Section Headers (Absolute file offsets)
        header.setTextOffset(readInt(bytes, VmxHeader.OFFSET_TEXT_OFFSET));
        header.setTextSize(readInt(bytes, VmxHeader.OFFSET_TEXT_SIZE));
        header.setRodataOffset(readInt(bytes, VmxHeader.OFFSET_RODATA_OFFSET));
        header.setRodataSize(readInt(bytes, VmxHeader.OFFSET_RODATA_SIZE));
        header.setDataOffset(readInt(bytes, VmxHeader.OFFSET_DATA_OFFSET));
        header.setDataSize(readInt(bytes, VmxHeader.OFFSET_DATA_SIZE));
        header.setBssOffset(readInt(bytes, VmxHeader.OFFSET_BSS_OFFSET));
        header.setBssSize(readInt(bytes, VmxHeader.OFFSET_BSS_SIZE));

        header.setHeaderSize(VmxHeader.EXT_HEADER_START + (12 * header.getExtCount()));
        return header;
    }
}
