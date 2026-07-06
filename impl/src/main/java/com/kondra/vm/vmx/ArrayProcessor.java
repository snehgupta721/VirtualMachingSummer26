package com.kondra.vm.vmx;

public class ArrayProcessor {
    /**
     * Helper method to convert little endian to big endian 4 bytes at a time
     */
    public static int readInt(byte[] bytes, int offset) {
        int one   = bytes[offset] & 0xFF;
        int two   = (bytes[offset+1] & 0xFF) << 8;
        int three = (bytes[offset+2] & 0xFF) << 16; // 8 * 2
        int four  = (bytes[offset+3] & 0xFF) << 24; // 8 * 3
        return one | two | three | four;
    }

    /**
     * Helper method to convert little endian to big endian 2 bytes at a time
     */
    public static short readShort(byte[] bytes, int offset) {
        int one = bytes[offset] & 0xFF;
        int two = (bytes[offset + 1] & 0xFF) << 8;
        return (short) (one | two);
    }

    public static int readByteUnsigned(byte[] bytes, int offset) {
        return bytes[offset] & 0xFF;
    }

    public static int readByteSigned(byte[] bytes, int offset) {
        return bytes[offset];
    }

    public static void writeInt(byte[] dest, int offset, int value) {
        dest[offset]     = (byte) value;
        dest[offset + 1] = (byte) (value >>> 8);
        dest[offset + 2] = (byte) (value >>> 16);
        dest[offset + 3] = (byte) (value >>> 24);
    }

    public static void writeShort(byte[] dest, int offset, int value) {
        dest[offset]     = (byte) value;
        dest[offset + 1] = (byte) (value >>> 8);
    }

    public static void writeByte(byte[] dest, int offset, int value) {
        dest[offset]     = (byte) value;
    }
}
