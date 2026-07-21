package com.kondra.vm.loader.data;

public class Section {
    private int address;
    private int size;

    public Section(int address, int size) {
        this.address = address;
        this.size = size;
    }

    public int getAddress() {
        return address;
    }

    public int getSize() {
        return size;
    }
}
