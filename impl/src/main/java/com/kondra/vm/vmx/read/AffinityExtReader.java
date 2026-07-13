package com.kondra.vm.vmx.read;

import com.kondra.vm.vmx.data.AffinityExtension;
import com.kondra.vm.vmx.data.AffinityRecord;

import java.io.IOException;
import java.io.RandomAccessFile;

import static com.kondra.vm.vmx.ArrayProcessor.readInt;
import static com.kondra.vm.vmx.ArrayProcessor.readShort;

public class AffinityExtReader {

    public AffinityExtension read(RandomAccessFile raf, int type, int flag, int offset, int size) throws IOException {
        AffinityExtension ext = new AffinityExtension(type, flag);
        int cursor = offset;
        while (cursor < (offset + size)) {
            int major = readShort(raf, cursor);
            int minor = readShort(raf, cursor + 2);
            int symbolOffset = readInt(raf, cursor + 4);
            cursor += AffinityRecord.SIZE;
        }
        return ext;
    }
}
