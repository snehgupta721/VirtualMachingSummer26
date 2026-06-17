package com.kondra.vm.mmu;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Collection of blocks
 */
public class MemoryBlocks {
    Map<Integer, Block> startToBlock;
    Map<Integer, Block> endToBlock;

    public MemoryBlocks() {
        startToBlock = new HashMap<>();
        endToBlock = new HashMap<>();
    }

    public Block addBlock(int start, int size) {
        Block block = new Block(start, size);
        startToBlock.put(start, block);
        endToBlock.put(block.getEnd(), block);
        return block;
    }

    public void removeBlock(Block block) {
        startToBlock.remove(block.getStart());
        endToBlock.remove(block.getEnd());
    }

    public Block getBlockFromStart(int start) {
        return startToBlock.get(start);
    }

    public Block getBlockFromEnd(int end) {
        return endToBlock.get(end);
    }

    public void reset() {
        startToBlock.clear();
        endToBlock.clear();
    }

    public Collection<Block> getBlocks() {
        return startToBlock.values();
    }

    public Block getPrevBlock(Block block) {
        return endToBlock.getOrDefault(block.getStart() - 1, null);
    }

    public Block getNextBlock(Block block) {
        return startToBlock.getOrDefault(block.getEnd() + 1, null);
    }
}
