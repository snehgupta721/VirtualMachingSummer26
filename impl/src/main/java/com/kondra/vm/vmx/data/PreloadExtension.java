package com.kondra.vm.vmx.data;

import com.kondra.vm.common.vmx.ext.PreloadExt;

import java.util.ArrayList;
import java.util.List;

public class PreloadExtension implements PreloadExt {
    private final int type;
    private final int flag;
    private final List<Integer> offsets;

    public PreloadExtension(int type, int flag) {
        this.type = type;
        this.flag = flag;
        offsets = new ArrayList<>();
    }

    @Override
    public void addSymbolOffset(int offset) {
        offsets.add(offset);
    }

    @Override
    public List<Integer> getSymbolOffsets() {
        return offsets;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public int getFlag() {
        return flag;
    }

    @Override
    public int getSize() {
        return 4 * offsets.size();
    }
}
