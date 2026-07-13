package com.kondra.vm.vmx.data;

import com.kondra.vm.common.vmx.ext.Export;

public class ExportRecord implements Export {
    private int symbolOffset;
    private int addressOffset;
    private int section;

    public ExportRecord(int symbolOffset, int word2) {
        this.symbolOffset = symbolOffset;
        this.addressOffset = word2 & 0x03ffffff;
        this.section = word2 >> 26;
    }


    @Override
    public int getSymbolOffset() {
        return symbolOffset;
    }

    @Override
    public void setSymbolOffset(int offset) {
        this.symbolOffset = offset;
    }

    @Override
    public int getAddressOffset() {
        return addressOffset;
    }

    @Override
    public void setAddressOffset(int offset) {
        this.addressOffset = offset;
    }

    @Override
    public int getSection() {
        return section;
    }

    @Override
    public void setSection(int section) {
        this.section = section;
    }

    public int getWord2() {
        int maskedAddress = addressOffset & 0x03FFFFFF;
        int shiftedSection = section << 26;
        return shiftedSection | maskedAddress;
    }
}
