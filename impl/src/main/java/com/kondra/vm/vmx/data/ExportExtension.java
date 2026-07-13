package com.kondra.vm.vmx.data;

import com.kondra.vm.common.vmx.ext.Export;
import com.kondra.vm.common.vmx.ext.ExportExt;

import java.util.ArrayList;
import java.util.List;

public class ExportExtension implements ExportExt {
    private final int type;
    private final int flag;
    private final List<Export> exports;
    private final SymbolTableExtension symTab;

    public ExportExtension(int type, int flag, SymbolTableExtension symTab) {
        this.type = type;
        this.flag = flag;
        exports = new ArrayList<>();
        this.symTab = symTab;
    }

    public void addExport(int symbolOffset, int word2) {
        Export export = new ExportRecord(symbolOffset, word2);
        // Exports are sorted in the data
        exports.add(export);
        // What if more added later? TODO sort when add
    }

    public Export findExport(String targetSymbol) {
        int low = 0;
        int high = exports.size() - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            Export midExport = exports.get(mid);

            // Resolve the string name using our bound symbol table
            String midSymbol = symTab.getSymbol(midExport.getSymbolOffset());
            int cmp = midSymbol.compareTo(targetSymbol);

            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return midExport;
            }
        }
        return null;  // Export not found in this extension
    }

    @Override
    public List<Export> getExports() {
        return exports;
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
        return 8 * exports.size();
    }
}
