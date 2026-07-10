package com.kondra.vm.vmx.write;

import com.kondra.vm.common.vmx.VmxFile;
import com.kondra.vm.vmx.data.Section;
import com.kondra.vm.vmx.data.SectionHeader;
import com.kondra.vm.vmx.data.SectionOffsets;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;

public class SectionWriter {
    public SectionWriter() {
    }

    public SectionOffsets write(RandomAccessFile raf, Map<Integer, Section> sections, int offset) throws IOException {
        int[] sectionsOrder = {
                VmxFile.SECTION_TEXT,
                VmxFile.SECTION_RODATA,
                VmxFile.SECTION_DATA,
                VmxFile.SECTION_BSS
        };

        int currOffset = offset;

        int textOffset = 0, textSize = 0;
        int rodataOffset = 0, rodataSize = 0;
        int dataOffset = 0, dataSize = 0;
        int bssOffset = 0, bssSize = 0;
        for (int sectionIdx : sectionsOrder) {
            Section section = sections.get(sectionIdx);

            if (sectionIdx != VmxFile.SECTION_BSS) {
                raf.seek(currOffset);
                raf.write(section.getData(), 0, section.getSize());
            }

            int relativeOffset = currOffset - offset;
            switch (sectionIdx) {
                case VmxFile.SECTION_TEXT -> {
                    textOffset = relativeOffset;
                    textSize = section.getSize();
                }
                case VmxFile.SECTION_RODATA -> {
                    rodataOffset = relativeOffset;
                    rodataSize = section.getSize();
                }
                case VmxFile.SECTION_DATA -> {
                    dataOffset = relativeOffset;
                    dataSize = section.getSize();
                }
                case VmxFile.SECTION_BSS -> {
                    bssOffset = relativeOffset;
                    bssSize = section.getSize();
                }
            }
            currOffset += section.getSize();
        }

        return new SectionOffsets(textOffset, textSize,
                                  rodataOffset, rodataSize,
                                  dataOffset, dataSize,
                                  bssOffset, bssSize);
    }
}
