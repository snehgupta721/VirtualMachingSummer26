package com.kondra.vm.vmx.read;

import com.kondra.vm.common.vmx.VmxFile;
import com.kondra.vm.vmx.data.Section;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

public class SectionReader {
    public Map<Integer, Section> getEmptySections() {
        Map<Integer, Section> sections = new HashMap<>();
        sections.put(VmxFile.SECTION_TEXT, new Section(new byte[0]));
        sections.put(VmxFile.SECTION_RODATA, new Section(new byte[0]));
        sections.put(VmxFile.SECTION_DATA, new Section(new byte[0]));
        sections.put(VmxFile.SECTION_BSS, new Section(new byte[0]));
        return sections;
    }

    public Map<Integer, Section> read(RandomAccessFile raf, SectionHeaderReader sectionHeaderReader, int totalHeaderSize) throws IOException {
        Map<Integer, Section> sections = new HashMap<>();

        int textStart = totalHeaderSize + sectionHeaderReader.getTextOffset();
        int rodataStart = totalHeaderSize + sectionHeaderReader.getRodataOffset();
        int dataStart = totalHeaderSize + sectionHeaderReader.getDataOffset();

        // TEXT SECTION
        byte[] textBytes = new byte[sectionHeaderReader.getTextSize()];
        raf.seek(textStart);
        raf.readFully(textBytes);
        sections.put(VmxFile.SECTION_TEXT, new Section(textBytes));

        // RODATA SECTION
        byte[] rodataBytes = new byte[sectionHeaderReader.getRodataSize()];
        raf.seek(rodataStart);
        raf.readFully(rodataBytes);
        sections.put(VmxFile.SECTION_RODATA, new Section(rodataBytes));

        // DATA SECTION
        byte[] dataBytes = new byte[sectionHeaderReader.getDataSize()];
        raf.seek(dataStart);
        raf.readFully(dataBytes);
        sections.put(VmxFile.SECTION_DATA, new Section(dataBytes));

        // BSS SECTION (Stays empty memory buffer)
        sections.put(VmxFile.SECTION_BSS, new Section(new byte[sectionHeaderReader.getBssSize()]));

        return sections;
    }
}
