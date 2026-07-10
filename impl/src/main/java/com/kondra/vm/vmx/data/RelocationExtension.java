package com.kondra.vm.vmx.data;

import com.kondra.vm.common.vmx.ext.Relocation;
import com.kondra.vm.common.vmx.ext.RelocationExt;

import java.util.List;
import java.util.Map;

/**
 * Purpose: Guides on how to get the four sections of data
 */
public class RelocationExtension implements RelocationExt {
    public static final int REL_OFFSET_TEXT = 0;
    public static final int REL_OFFSET_TEXT_SIZE = 4;
    public static final int REL_OFFSET_RODATA = 8;
    public static final int REL_OFFSET_RODATA_SIZE = 12;
    public static final int REL_OFFSET_DATA = 16;
    public static final int REL_OFFSET_DATA_SIZE = 20;
    public static final int REL_OFFSET_BSS = 24;
    public static final int REL_OFFSET_BSS_SIZE = 28;
    public static final int HEADER_SIZE = 32;

    private final int flags;
    private final int type;
    private final Map<Integer, List<Relocation>> relocations;

    public RelocationExtension(int type, int flags, Map<Integer, List<Relocation>> relocations) {
        this.flags = flags;
        this.type = type;
        this.relocations = relocations;
    }

    @Override
    public List<Relocation> getRelocations(int section) {
        return relocations.getOrDefault(section, List.of());
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public int getFlag() {
        return flags;
    }

    @Override
    public int getSize() {
        int numRelocations = 0;
        for (List<Relocation> list : relocations.values()) {
            numRelocations += list.size();
        }
        return HEADER_SIZE + (RelocationRecord.SIZE * numRelocations);
    }

    public Map<Integer, List<Relocation>> getRelocations() {
        return relocations;
    }
}
