package com.kondra.vm.vmx.write;

import com.kondra.vm.vmx.data.SymbolTableExtension;

import java.io.IOException;
import java.io.RandomAccessFile;

import static com.kondra.vm.vmx.ArrayProcessor.writeChar;
import static com.kondra.vm.vmx.ArrayProcessor.writeString;

public class SymbolTableExtWriter {

    public void write(RandomAccessFile raf, SymbolTableExtension ext, int offset, int size) throws IOException {
        System.out.println("Write at offset " + offset + " size " + size);
        for (String symbol : ext.getSymbols()) {
            int currOffset = ext.getOffset(symbol) + offset;
            writeString(raf, currOffset, symbol);
            writeChar(raf, currOffset + symbol.length(), '\0');
        }
    }
}
