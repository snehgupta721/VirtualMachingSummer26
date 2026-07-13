package com.kondra.vm.vmx.read;

import com.kondra.vm.vmx.data.LabelExtension;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;

import static com.kondra.vm.vmx.ArrayProcessor.readChar;
import static com.kondra.vm.vmx.ArrayProcessor.readInt;

public class LabelExtReader {

    public LabelExtension read(RandomAccessFile raf, int type, int flag, int offset, int size) throws IOException {
        LabelExtension ext = new LabelExtension(type, flag);

        long seconds = Integer.toUnsignedLong(readInt(raf, offset));
        Date timestamp = new Date(seconds * 1000L);
        ext.setTimestamp(timestamp);

        int cursor = offset + 4;
        StringBuilder word = new StringBuilder();
        while (cursor < (offset + size)) {
            char ch = readChar(raf, cursor);
            if (ch == '\0') {
                break;
            } else {
                word.append(ch);
            }
            cursor++;
        }
        ext.setLabel(word.toString());
        return ext;
    }
}
