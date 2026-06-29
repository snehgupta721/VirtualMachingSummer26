package com.kondra.vm.vmx;

import com.kondra.vm.common.Version;
import com.kondra.vm.common.vmx.VmxException;

import static com.kondra.vm.vmx.ArrayProcessor.*;

public class VmxHeaderReader {
    public final int extCount;
    public final int flags;
    public final Version version;
    public final int entryOffset;
    public final int headerSize;

    public final int textOffset, textSize;
    public final int rodataOffset, rodataSize;
    public final int dataOffset, dataSize;
    public final int bssOffset, bssSize;

    public VmxHeaderReader(byte[] bytes) throws VmxException {
        if ((bytes[0] & 0xFF) != 'v' || (bytes[1] & 0xFF) != 'm' ||
                (bytes[2] & 0xFF) != 'x' || (bytes[3] & 0xFF) != '\0') {
            throw new VmxException("File signature incorrect");
        }

        extCount = readByte(bytes, 4);
        flags = readByte(bytes, 6);
        // This will always be little endian
        if (flags != 8) {
            throw new VmxException("Incorrect flag: Should be little endian");
        }

        int major = bytes[8];   // signed
        int minor = bytes[9];   // signed
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
}
