package com.kondra.vm.vmx.write;

import com.kondra.vm.vmx.data.PreloadExtension;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import static com.kondra.vm.vmx.ArrayProcessor.writeInt;

public class PreloadExtWriter {

    public void write(RandomAccessFile raf, PreloadExtension ext, int offset) throws IOException {
        List<Integer> symbolOffsets = ext.getSymbolOffsets();
        int cursor = offset;
        for (Integer symbolOffset : symbolOffsets) {
            writeInt(raf, cursor, symbolOffset);
            cursor += 4;
        }
    }
}
