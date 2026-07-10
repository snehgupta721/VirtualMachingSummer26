package com.kondra.vm.vmx.data;

import com.kondra.vm.common.vmx.ext.Relocation;

public class RelocationRecord implements Relocation {
    public static final int SIZE = 8;

    private int type;
    private int fixupOffset;
    private int section;
    private int sectionOffset;
    private int dynamicSymbolOffset;
    private int affinityTableIndex;
    private boolean dynamic;

    public RelocationRecord(int word1, int word2) {
        type = word1 & 0x3;
        fixupOffset = word1 & 0xfffffffc;
        section = (word2 >> 26) & 0xf;
        sectionOffset = word2 & 0x03ffffff;
        dynamicSymbolOffset = word2 & 0xffff;
        affinityTableIndex = (word2 >> 16) & 0xff;
        dynamic = (word2 & 0x40000000) != 0;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getFixupOffset() {
        return fixupOffset;
    }

    @Override
    public void setFixupOffset(int offset) {
        this.fixupOffset = offset;
    }

    @Override
    public int getSection() {
        return section;
    }

    @Override
    public void setSection(int section) {
        this.section = section;
    }

    @Override
    public int getSectionOffset() {
        return sectionOffset;
    }

    @Override
    public void setSectionOffset(int offset) {
        this.sectionOffset = offset;
    }

    @Override
    public boolean isDynamic() {
        return dynamic;
    }

    @Override
    public void setDynamic(boolean val) {
        this.dynamic = val;
    }

    @Override
    public int getDynamicSymbolOffset() {
        return dynamicSymbolOffset;
    }

    @Override
    public void setDynamicSymbolOffset(int offset) {
        dynamicSymbolOffset = offset;
    }

    @Override
    public int getAffinityTableIndex() {
        return affinityTableIndex;
    }

    @Override
    public void setAffinityTableIndex(int idx) {
        this.affinityTableIndex = idx;
    }

    public int getWord1() {
        return (type & 0x3) | (fixupOffset & 0xfffffffc);
    }

    public int getWord2() {
        int word2 = 0;
        if (dynamic) {
            // Set the dynamic flag bit
            word2 |= 0x40000000;

            word2 |= (affinityTableIndex & 0xff) << 16;
            word2 |= (dynamicSymbolOffset & 0xffff);
        } else {
            word2 |= (section & 0xf) << 26;
            word2 |= (sectionOffset & 0x03ffffff);
        }
        return word2;
    }
}
