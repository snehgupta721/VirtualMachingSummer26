package com.kondra.vm.vmx.data;

public class Section {
    private byte[] data;

    public Section(byte[] data) {
        this.data = data;
    }

    public int getSize() {
        return data.length;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
