package com.kondra.vm.mmu;

import com.kondra.vm.common.memory.Memory;
import lombok.Getter;

@Getter
public class MemoryAccess implements Memory {
    private final byte[] array;

    public MemoryAccess(int size) {
        array = new byte[size];
    }

    @Override
    public byte getByte(int offset) {
        return array[offset];
    }

    @Override
    public short getShort(int offset) {
        int one = array[offset] & 0xFF;    // 000b
        int two = (array[offset+1] & 0xFF) << 8 ;  // 00a0
        return (short) (one | two);   // 00ab
    }

    @Override
    public int getInt(int offset) {
        int one = array[offset] & 0xFF;      // 000d
        int two = (array[offset+1] & 0xFF) << 8;    // 00c0
        int three = (array[offset+2] & 0xFF) << 8 * 2;  // 0b00
        int four = (array[offset+3] & 0xFF) << 8 * 3;   // a000
        return one | two | three | four;  // abcd
    }

    @Override
    public int getAlignedInt(int offset) {
        int alignedOffset = offset & ~0x3;
        return getInt(alignedOffset);
    }

    @Override
    public void setByte(int offset, byte val) {
        array[offset] = val;
    }

    @Override
    public void setShort(int offset, short val) {
        array[offset] = (byte) val;
        array[offset+1] = (byte) (val >>> 8);
    }

    @Override
    public void setInt(int offset, int val) {
        array[offset] = (byte) val;
        array[offset+1] = (byte) (val >>> 8);
        array[offset+2] = (byte) (val >>> 16);
        array[offset+3] = (byte) (val >>> 24);
    }

    @Override
    public void setAlignedInt(int offset, int val) {
        int alignedOffset = offset & ~0x3;
        setInt(alignedOffset, val);
    }

    @Override
    public byte[] getBytes() {
        return array;
    }

    @Override
    public int size() {
        return array.length;
    }
}
