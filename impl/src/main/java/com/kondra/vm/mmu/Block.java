package com.kondra.vm.mmu;

/**
 * A single block of memory
 */
public class Block {
    private final int start;    // inclusive
    private final int end;      // inclusive

    public Block(int start, int size) {
        this.start = start;
        this.end = start + size - 1;
    }

    public int getSize() {
        return this.end - this.start + 1;
    }

    public int getStart() {
        return this.start;
    }

    public int getEnd() {
        return this.end;
    }
}
