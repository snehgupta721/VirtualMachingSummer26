package com.kondra.vm.vmx.data;

import com.kondra.vm.common.vmx.ext.Affinity;

public class AffinityRecord implements Affinity {
    public static final int SIZE = 12;
    private int major;
    private int minor;
    private int symbolOffset;

    public AffinityRecord(int major, int minor, int symbolOffset) {
        this.major = major;
        this.minor = minor;
        this.symbolOffset = symbolOffset;
    }

    @Override
    public int getMajorVersion() {
        return major;
    }

    @Override
    public void setMajorVersion(int major) {
        this.major = major;
    }

    @Override
    public int getMinorVersion() {
        return minor;
    }

    @Override
    public void setMinorVersion(int minor) {
        this.minor = minor;
    }

    @Override
    public int getSymbolOffset() {
        return symbolOffset;
    }

    @Override
    public void setSymbolOffset(int offset) {
        this.symbolOffset = offset;
    }
}
