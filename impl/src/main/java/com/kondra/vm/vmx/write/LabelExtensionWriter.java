package com.kondra.vm.vmx.write;

import com.kondra.vm.vmx.data.LabelExtension;

import java.io.IOException;
import java.io.RandomAccessFile;

import static com.kondra.vm.vmx.ArrayProcessor.*;

public class LabelExtensionWriter {

    public void write(RandomAccessFile raf, LabelExtension ext, int offset, int size) throws IOException {
        long milliseconds = ext.getTimestamp().getTime();
        long seconds = milliseconds / 1000L;
        int cursor = offset;
        writeInt(raf, cursor, (int) seconds);
        cursor += 4;

        writeString(raf, cursor, ext.getLabel());
        cursor += ext.getLabel().length();
        writeChar(raf, cursor, '\0');
        cursor++;

        // as a good citizen just clean up the rest until size + offset
        if (cursor < offset + size) {
            byte[] padding = new byte[offset + size - cursor];
            writeArray(raf, cursor, padding);
        }
    }
}
