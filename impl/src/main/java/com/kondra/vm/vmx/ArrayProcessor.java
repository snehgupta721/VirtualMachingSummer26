package com.kondra.vm.vmx;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;

public class ArrayProcessor {
    /**
     * Helper method to convert little endian to big endian 4 bytes at a time
     */
    public static int readInt(RandomAccessFile raf, int offset) throws IOException {
        byte[] buffer = new byte[4];
        raf.seek(offset);
        raf.readFully(buffer);

        int one   = buffer[0] & 0xFF;
        int two   = (buffer[1] & 0xFF) << 8;
        int three = (buffer[2] & 0xFF) << 16;
        int four  = (buffer[3] & 0xFF) << 24;
        return one | two | three | four;
    }

    public static short readShort(RandomAccessFile raf, int offset) throws IOException {
        byte[] buffer = new byte[2];
        raf.seek(offset);
        raf.readFully(buffer);

        int one = buffer[0] & 0xFF;
        int two = (buffer[1] & 0xFF) << 8;
        return (short) (one | two);
    }

    public static int readByteUnsigned(RandomAccessFile raf, int offset) throws IOException {
        raf.seek(offset);
        return raf.readUnsignedByte(); // RandomAccessFile has this built-in!
    }

    public static int readByteSigned(RandomAccessFile raf, int offset) throws IOException {
        raf.seek(offset);
        return raf.readByte(); // RandomAccessFile has this built-in!
    }

    public static char readChar(RandomAccessFile raf, int offset) throws IOException {
        raf.seek(offset);
        return (char) raf.readUnsignedByte();
    }

    /**
     * Helper method to write a 4-byte int to the file in little endian format
     */
    public static void writeInt(RandomAccessFile raf, int offset, int value) throws IOException {
        byte[] buffer = new byte[4];
        buffer[0] = (byte) value;
        buffer[1] = (byte) (value >>> 8);
        buffer[2] = (byte) (value >>> 16);
        buffer[3] = (byte) (value >>> 24);

        raf.seek(offset);
        raf.write(buffer);
    }

    /**
     * Helper method to write a 2-byte short to the file in little endian format
     */
    public static void writeShort(RandomAccessFile raf, int offset, int value) throws IOException {
        byte[] buffer = new byte[2];
        buffer[0] = (byte) value;
        buffer[1] = (byte) (value >>> 8);

        raf.seek(offset);
        raf.write(buffer);
    }

    /**
     * Helper method to write a single byte to the file
     */
    public static void writeByte(RandomAccessFile raf, int offset, int value) throws IOException {
        raf.seek(offset);
        raf.write(value); // RandomAccessFile only writes the lower 8 bits of the int
    }

    public static void writeByte(RandomAccessFile raf, int offset, byte value) throws IOException {
        raf.seek(offset);
        raf.write(value); // RandomAccessFile only writes the lower 8 bits of the int
    }

    public static void writeString(RandomAccessFile raf, int offset, String value) throws IOException {
        raf.seek(offset);
        raf.write(value.getBytes());
    }

    public static void writeChar(RandomAccessFile raf, int offset, char value) throws IOException {
        raf.seek(offset);
        raf.write(value);
    }

    public static void writeArray(RandomAccessFile raf, int offset, byte[] value) throws IOException {
        raf.seek(offset);
        raf.write(value);
    }
}
