package com.kondra.vm.vmx.data;

import com.kondra.vm.common.vmx.ext.SymbolTableExt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolTableExtension implements SymbolTableExt {
    private final int type;
    private final int flag;
    private final Map<String, Integer> symbolToOffset;
    private final Map<Integer, String> offsetToSymbol;
    private int nextOffset;

    public SymbolTableExtension(int type, int flag) {
        this.type = type;
        this.flag = flag;
        nextOffset = 0;
        symbolToOffset = new HashMap<>();
        offsetToSymbol = new HashMap<>();
    }

    /**
     * Just add the symbol to the end
     */
    @Override
    public void addSymbol(int offset, String symbol) {
        symbolToOffset.put(symbol, nextOffset);
        offsetToSymbol.put(nextOffset, symbol);
        nextOffset += symbol.length() + 1;
    }

    @Override
    public String getSymbol(int offset) {
        return offsetToSymbol.getOrDefault(offset, null);
    }

    @Override
    public int getOffset(String symbol) {
        return symbolToOffset.getOrDefault(symbol, -1);
    }

    @Override
    public List<String> getSymbols() {
        return new ArrayList<>(symbolToOffset.keySet());
    }

    @Override
    public int getNextOffset() {
        return nextOffset;
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
        return nextOffset;
    }
}
