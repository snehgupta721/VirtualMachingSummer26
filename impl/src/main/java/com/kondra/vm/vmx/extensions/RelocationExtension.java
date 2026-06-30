package com.kondra.vm.vmx.extensions;

import com.kondra.vm.common.vmx.VmxFile;
import com.kondra.vm.common.vmx.ext.Relocation;
import com.kondra.vm.common.vmx.ext.RelocationExt;
import com.kondra.vm.vmx.ArrayProcessor;
import com.kondra.vm.vmx.MyVmxExt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Purpose: Guides on how to get the four sections of data
 */
public class RelocationExtension extends MyVmxExt implements RelocationExt {
    private final Map<Integer, List<Relocation>> relocations;

    public RelocationExtension(int type, int flags, byte[] data) {
        super(type, flags, data);
        relocations = new HashMap<>();

        int textOffset = ArrayProcessor.readInt(data, 0);
        int textSize = ArrayProcessor.readInt(data, 4);
        int rodataOffset = ArrayProcessor.readInt(data, 8);
        int rodataSize = ArrayProcessor.readInt(data, 12);
        int dataOffset = ArrayProcessor.readInt(data, 16);
        int dataSize = ArrayProcessor.readInt(data, 20);
        int bssOffset = ArrayProcessor.readInt(data, 24);
        int bssSize = ArrayProcessor.readInt(data, 28);

        parseRelocationRecords(VmxFile.SECTION_TEXT, textOffset, textSize, data);
        parseRelocationRecords(VmxFile.SECTION_RODATA, rodataOffset, rodataSize, data);
        parseRelocationRecords(VmxFile.SECTION_DATA, dataOffset, dataSize, data);
        parseRelocationRecords(VmxFile.SECTION_BSS, bssOffset, bssSize, data);
    }

    private void parseRelocationRecords(int section, int offset, int size, byte[] data) {
        List<Relocation> currRelocs = new ArrayList<>();
        int cursor = offset;
        while (cursor < offset + size) {
            int word1 = ArrayProcessor.readInt(data, cursor);
            int word2 = ArrayProcessor.readInt(data, cursor + 4);
            currRelocs.add(new RelocationRecord(word1, word2));
            cursor += 8;
        }

        relocations.put(section, currRelocs);
    }

    @Override
    public List<Relocation> getRelocations(int section) {
        return relocations.getOrDefault(section, List.of());
    }
}
