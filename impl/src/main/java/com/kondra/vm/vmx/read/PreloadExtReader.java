package com.kondra.vm.vmx.read;

import com.kondra.vm.vmx.data.PreloadExtension;

import java.io.IOException;
import java.io.RandomAccessFile;

import static com.kondra.vm.vmx.ArrayProcessor.readInt;

public class PreloadExtReader {

    public PreloadExtension read(RandomAccessFile raf, int type, int flag, int offset, int size) throws IOException {
        PreloadExtension ext = new PreloadExtension(type, flag);
        int cursor = offset;
        while (cursor < (offset + size)) {
            ext.addSymbolOffset(readInt(raf, cursor));
            cursor += 4;
        }
        return ext;
    }
}
