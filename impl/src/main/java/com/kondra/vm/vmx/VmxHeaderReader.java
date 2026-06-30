package com.kondra.vm.vmx;

import com.kondra.vm.common.Version;
import com.kondra.vm.common.vmx.VmxException;

import static com.kondra.vm.vmx.ArrayProcessor.*;

public class VmxHeaderReader {
    private final int extCount;
    private final int flags;
    private final Version version;
    private final int entryOffset;
    private final int headerSize;

    private final int textOffset, textSize;
    private final int rodataOffset, rodataSize;
    private final int dataOffset, dataSize;
    private final int bssOffset, bssSize;

    public VmxHeaderReader(byte[] bytes) throws VmxException {
        if ((bytes[0] & 0xFF) != 'v' || (bytes[1] & 0xFF) != 'm' ||
                (bytes[2] & 0xFF) != 'x' || (bytes[3] & 0xFF) != '\0') {
            throw new VmxException("File signature incorrect");
        }

        extCount = readByteUnsigned(bytes, 4);
        flags = readByteUnsigned(bytes, 6);
        // This will always be little endian
        if (flags != 8) {
            throw new VmxException("Incorrect flag: Should be little endian");
        }

        int major = readByteSigned(bytes, 8);   // signed
        int minor = readByteSigned(bytes, 9);   // signed
        short buildNum = readShort(bytes, 10);
        version = new Version(major, minor, buildNum);

        entryOffset = readInt(bytes, 20);

        // Section Headers (Absolute file offsets)
        textOffset   = readInt(bytes, 24);
        textSize     = readInt(bytes, 28);
        rodataOffset = readInt(bytes, 32);
        rodataSize   = readInt(bytes, 36);
        dataOffset   = readInt(bytes, 40);
        dataSize     = readInt(bytes, 44);
        bssOffset    = readInt(bytes, 48);
        bssSize      = readInt(bytes, 52);

        headerSize = 24 + 32 + (12 * extCount);
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
}
