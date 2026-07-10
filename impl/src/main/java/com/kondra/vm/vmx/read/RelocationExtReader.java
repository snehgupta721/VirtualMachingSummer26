package com.kondra.vm.vmx.read;

import com.kondra.vm.common.vmx.VmxFile;
import com.kondra.vm.common.vmx.ext.Relocation;
import com.kondra.vm.vmx.data.RelocationExtension;
import com.kondra.vm.vmx.data.RelocationRecord;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

import static com.kondra.vm.vmx.ArrayProcessor.readInt;

public class RelocationExtReader {
    public RelocationExtReader() {
    }

    public RelocationExtension read(RandomAccessFile raf, int type, int flags, int offset, int size) throws IOException {
        Map<Integer, List<Relocation>> relocations = new HashMap<>();
        // Parse relocation extension header
        int textOffset = readInt(raf, offset + RelocationExtension.REL_OFFSET_TEXT);
        int textSize = readInt(raf, offset + RelocationExtension.REL_OFFSET_TEXT_SIZE);
        int rodataOffset = readInt(raf, offset + RelocationExtension.REL_OFFSET_RODATA);
        int rodataSize = readInt(raf, offset + RelocationExtension.REL_OFFSET_RODATA_SIZE);
        int dataOffset = readInt(raf, offset + RelocationExtension.REL_OFFSET_DATA);
        int dataSize = readInt(raf, offset + RelocationExtension.REL_OFFSET_DATA_SIZE);
        int bssOffset = readInt(raf, offset + RelocationExtension.REL_OFFSET_BSS);
        int bssSize = readInt(raf, offset + RelocationExtension.REL_OFFSET_BSS_SIZE);

        // Parse relocation extension content
        relocations.put(VmxFile.SECTION_TEXT,
                parseRelocationRecords(raf, textOffset, VmxFile.SECTION_TEXT, textSize));
        relocations.put(VmxFile.SECTION_RODATA,
                parseRelocationRecords(raf, rodataOffset, VmxFile.SECTION_RODATA, rodataSize));
        relocations.put(VmxFile.SECTION_DATA,
                parseRelocationRecords(raf, dataOffset, VmxFile.SECTION_DATA, dataSize));
        relocations.put(VmxFile.SECTION_BSS,
                parseRelocationRecords(raf, bssOffset, VmxFile.SECTION_BSS, bssSize));

        return new RelocationExtension(type, flags, relocations);
    }

    private List<Relocation> parseRelocationRecords(RandomAccessFile raf, int offset, int section, int size) throws IOException {
        List<Relocation> currRelocs = new ArrayList<>();
        int cursor = offset;
        while (cursor < offset + size) {
            int word1 = readInt(raf, cursor);
            int word2 = readInt(raf, cursor + 4);
            currRelocs.add(new RelocationRecord(word1, word2));
            cursor += 8;
        }

        return currRelocs;
    }
}
