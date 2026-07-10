package com.kondra.vm.vmx.write;

import com.kondra.vm.common.vmx.ext.Relocation;
import com.kondra.vm.vmx.data.RelocationExtension;
import com.kondra.vm.vmx.data.RelocationRecord;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import static com.kondra.vm.vmx.ArrayProcessor.writeInt;

public class RelocationExtWriter {
    public RelocationExtWriter() {
    }

    public void write(RandomAccessFile raf, RelocationExtension ext, int offset) throws IOException {
        int[] sectionsOrder = {
                com.kondra.vm.common.vmx.VmxFile.SECTION_TEXT,
                com.kondra.vm.common.vmx.VmxFile.SECTION_RODATA,
                com.kondra.vm.common.vmx.VmxFile.SECTION_DATA,
                com.kondra.vm.common.vmx.VmxFile.SECTION_BSS
        };

        int cursor = offset;
        int payloadCursor = (4 * 8) + offset;

        for (int section : sectionsOrder) {
            List<Relocation> list = ext.getRelocations().getOrDefault(section, null);
            if (list == null) {
                // If the section is missing, just continue
                cursor += 8;
                continue;
            }

            writeInt(raf, cursor, payloadCursor - offset);
            writeInt(raf, cursor + 4, list.size() * RelocationRecord.SIZE);
            writeRecords(raf, list, payloadCursor);
            cursor += 8;
            payloadCursor += list.size() * RelocationRecord.SIZE;
        }
    }

    private void writeRecords(RandomAccessFile raf, List<Relocation> relocs, int offset) throws IOException {
        int cursor = offset;
        for (Relocation reloc : relocs) {
            RelocationRecord record = (RelocationRecord) reloc;
            writeInt(raf, cursor, record.getWord1());
            writeInt(raf, cursor + 4, record.getWord2());
            cursor += 8;
        }
    }
}
