package com.kondra.vm.vmx;

import com.kondra.vm.common.vmx.VmxExt;

public class MyVmxExt implements VmxExt {
    private final int type;
    private final int flags;
    private byte[] data;

    public MyVmxExt(int type, int flags, byte[] data) {
        this.type = type;
        this.flags = flags;
        this.data = data;
    }

    @Override
    public int getType() {
        return type;
    }

    public int getFlags() {
        return flags;
    }

    public byte[] getData() {
        return data;
    }
}
