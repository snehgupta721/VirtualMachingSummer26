package com.kondra.vm.vmx.read;

import com.kondra.vm.vmx.data.SymbolTableExtension;

import java.io.IOException;
import java.io.RandomAccessFile;

import static com.kondra.vm.vmx.ArrayProcessor.readChar;

public class SymbolTableExtReader {

    public SymbolTableExtension read(RandomAccessFile raf, int type, int flag, int offset, int size) throws IOException {
        SymbolTableExtension ext = new SymbolTableExtension(type, flag);

        int cursor = offset;
        int startOfWord = cursor;
        StringBuilder word = new StringBuilder();
        while (cursor < (offset + size)) {
            char ch = readChar(raf, cursor);
            if (ch == '\0') {
                // reset
                ext.addSymbol(startOfWord - offset, word.toString());
                startOfWord = cursor + 1;
                word.setLength(0);
            } else {
                word.append(ch);
            }
            cursor++;
        }
        return ext;
    }
}
