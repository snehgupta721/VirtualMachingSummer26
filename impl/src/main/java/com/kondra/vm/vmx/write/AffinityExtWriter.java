package com.kondra.vm.vmx.write;

import com.kondra.vm.common.vmx.ext.Affinity;
import com.kondra.vm.common.vmx.ext.AffinityExt;
import com.kondra.vm.vmx.data.AffinityRecord;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import static com.kondra.vm.vmx.ArrayProcessor.writeInt;
import static com.kondra.vm.vmx.ArrayProcessor.writeShort;

public class AffinityExtWriter {
    public void write(RandomAccessFile raf, AffinityExt affinityExt, int offset) throws IOException {
        List<Affinity> affinities = affinityExt.getAffinityList();
        int cursor = offset;
        for (Affinity affinity : affinities) {
            writeShort(raf, cursor, affinity.getMajorVersion());
            writeShort(raf, cursor + 2, affinity.getMinorVersion());
            writeInt(raf, cursor + 4, affinity.getSymbolOffset());
            cursor += AffinityRecord.SIZE;
        }
    }
}
