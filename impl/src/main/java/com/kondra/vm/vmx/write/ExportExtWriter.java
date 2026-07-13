package com.kondra.vm.vmx.write;

import com.kondra.vm.common.vmx.ext.Export;
import com.kondra.vm.vmx.data.ExportExtension;
import com.kondra.vm.vmx.data.ExportRecord;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import static com.kondra.vm.vmx.ArrayProcessor.writeInt;

public class ExportExtWriter {
    public void write(RandomAccessFile raf, ExportExtension ext, int offset) throws IOException {
        List<Export> exports = ext.getExports();
        int cursor = offset;
        for (Export export : exports) {
            ExportRecord exportRecord = (ExportRecord) export;
            writeInt(raf, cursor, exportRecord.getSymbolOffset());
            writeInt(raf, cursor + 4, exportRecord.getWord2());
            cursor += 8;
        }
    }
}
