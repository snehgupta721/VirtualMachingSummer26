package com.kondra.vm.vmx.read;

import com.kondra.vm.vmx.data.ExportExtension;
import com.kondra.vm.vmx.data.SymbolTableExtension;

import java.io.IOException;
import java.io.RandomAccessFile;

import static com.kondra.vm.vmx.ArrayProcessor.readInt;

public class ExportExtReader {

    public ExportExtension read(RandomAccessFile raf, int type, int flag, int offset,
                                int size, SymbolTableExtension symTab) throws IOException {
        ExportExtension ext = new ExportExtension(type, flag, symTab);
        int cursor = offset;
        while (cursor < (offset + size)) {
            int symbolOffset = readInt(raf, cursor);
            int word2 = readInt(raf, cursor + 4);
            ext.addExport(symbolOffset, word2);
            cursor += 8;
        }
        return ext;
    }
}
