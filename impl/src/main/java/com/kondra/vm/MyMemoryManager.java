package com.kondra.vm;

import com.kondra.vm.common.memory.InsufficientMemoryException;
import com.kondra.vm.common.memory.Memory;
import com.kondra.vm.common.memory.MemoryMgr;
import com.kondra.vm.mmu.Block;
import com.kondra.vm.mmu.MemoryBlocks;

public class MyMemoryManager implements MemoryMgr {
    private Memory memory;
    private MemoryBlocks free;
    private MemoryBlocks allocated;

    @Override
    public void setMemory(Memory memory) {
        this.memory = memory;
        this.free = new MemoryBlocks();
        free.addBlock(4, memory.size() - 4);   // All memory is free, except first word
        this.allocated = new MemoryBlocks();
    }

    @Override
    public Memory getMemory() {
        return memory;
    }

    @Override
    public int allocate(int size) throws InsufficientMemoryException {
        // look for the smallest free block that meets the size requirement
        int sizeReq = ((size + 3) / 4) * 4;      // bytes
        if (sizeReq == 0) sizeReq = 4;
        Block freeBlock = null;
        for (Block currFreeBlock : free.getBlocks()) {
            if (currFreeBlock.getSize() >= sizeReq) {
                if (freeBlock == null) {
                    freeBlock = currFreeBlock;
                } else if (currFreeBlock.getSize() < freeBlock.getSize()) {
                    freeBlock = currFreeBlock;
                }
            }
        }
        if (freeBlock == null) {
            throw new InsufficientMemoryException();
        }

        // Allocate memory
        Block allocatedBlock = allocated.addBlock(freeBlock.getStart(), sizeReq);

        // Remove the free block
        free.removeBlock(freeBlock);

        // If the free block if big enough to split, add it back reduced size
        if (freeBlock.getSize() >= sizeReq + 4) {
            free.addBlock(allocatedBlock.getEnd() + 1, freeBlock.getSize() - sizeReq);
        }

        return allocatedBlock.getStart();
    }

    @Override
    public void free(int addr) {
        // Find the block in allocated
        Block allocatedBlock = allocated.getBlockFromStart(addr);

        // Move it from allocated to free
        allocated.removeBlock(allocatedBlock);

        // Check neighbors to see if they can be combined
        Block prevBlock = free.getPrevBlock(allocatedBlock);
        Block nextBlock = free.getNextBlock(allocatedBlock);
        int start = allocatedBlock.getStart();
        int end = allocatedBlock.getEnd();
        if (prevBlock != null) {
            free.removeBlock(prevBlock);
            start = prevBlock.getStart();
        }
        if (nextBlock != null) {
            free.removeBlock(nextBlock);
            end = nextBlock.getEnd();
        }
        free.addBlock(start, end - start + 1);
    }

    @Override
    public void reset() {
        free.reset();
        allocated.reset();
        // TODO: reset memeory array??
    }
}
