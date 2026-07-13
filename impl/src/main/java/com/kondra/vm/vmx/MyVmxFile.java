package com.kondra.vm.vmx;

import com.kondra.vm.common.Version;
import com.kondra.vm.common.vmx.VmxException;
import com.kondra.vm.common.vmx.VmxExt;
import com.kondra.vm.common.vmx.VmxFile;
import com.kondra.vm.vmx.data.SectionOffsets;
import com.kondra.vm.vmx.data.VmxHeader;
import com.kondra.vm.vmx.read.SectionHeaderReader;
import com.kondra.vm.vmx.read.SectionReader;
import com.kondra.vm.vmx.read.VmxExtensionReader;
import com.kondra.vm.vmx.read.VmxHeaderReader;
import com.kondra.vm.vmx.data.Section;
import com.kondra.vm.vmx.data.SectionHeader;
import com.kondra.vm.vmx.write.*;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.*;

public class MyVmxFile implements VmxFile {
    private final VmxHeader header;
    private final List<VmxExt> extensions;
    private final Map<Integer, Section> sections;

    public MyVmxFile() {
        header = new VmxHeader();
        extensions = new ArrayList<>();
        SectionReader sectionReader = new SectionReader();
        sections = sectionReader.getEmptySections();
    }

    public MyVmxFile(File file) throws VmxException {
        // read file
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            VmxHeaderReader headerReader = new VmxHeaderReader();
            header = headerReader.read(raf);
            int totalHeaderSize = VmxHeader.HEADER_SIZE + SectionHeader.HEADER_SIZE + header.getExtHeaderSize();

            SectionHeaderReader sectionHeaderReader = new SectionHeaderReader(raf);
            SectionReader sectionReader = new SectionReader();
            sections = sectionReader.read(raf, sectionHeaderReader, totalHeaderSize);

            VmxExtensionReader extensionReader = new VmxExtensionReader();
            extensions = extensionReader.read(raf, header.getExtCount(), totalHeaderSize);
        } catch (Exception e) {
            throw new VmxException("Error reading file " + file.getAbsolutePath(), e);
        }
    }

    @Override
    public void write(File file) throws VmxException {
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            // clear the contents of the file
            raf.setLength(0);
            int programSize = getProgramSize();

            VmxExtensionWriter extensionWriter = new VmxExtensionWriter();
            int fileSize = extensionWriter.write(raf, extensions, programSize);

            SectionWriter sectionWriter = new SectionWriter();
            int headerSize = VmxHeader.HEADER_SIZE + SectionHeader.HEADER_SIZE +
                                    (VmxExt.HEADER_SIZE * extensions.size());
            SectionOffsets sectionOffsets = sectionWriter.write(raf, sections, headerSize);

            SectionHeaderWriter sectionHeaderWriter = new SectionHeaderWriter();
            sectionHeaderWriter.write(raf, sectionOffsets);

            VmxHeaderWriter headerWriter = new VmxHeaderWriter();
            headerWriter.write(raf, header, programSize, fileSize);
        } catch (Exception e) {
            throw new VmxException("Error writing file " + file.getAbsolutePath(), e);
        }
    }

    private int getProgramSize() {
        int total = 0;
        for (int sectionIdx : sections.keySet()) {
            if (sectionIdx != VmxFile.SECTION_BSS) {
                Section section = sections.get(sectionIdx);
                total += section.getSize();
            }
        }
        return total;
    }

    @Override
    public byte[] getSection(int section) {
        return sections.get(section).getData();
    }

    @Override
    public void setSection(int section, byte[] data) {
        sections.get(section).setData(data);
    }

    @Override
    public List<VmxExt> getExtensions() {
        return extensions;
    }

    @Override
    public VmxExt getExtension(int type) {
        for (VmxExt ext : extensions) {
            if (ext.getType() == type) {
                return ext;
            }
        }
        return null;
    }

    @Override
    public Version getVersion() {
        return header.getVersion();
    }

    @Override
    public void setVersion(Version version) {
        this.header.setVersion(version);
    }

    @Override
    public int getFlags() {
        return header.getFlags();
    }

    @Override
    public void setFlags(int flags) {
        header.setFlags(flags);
    }

    @Override
    public int getEntryOffset() {
        return header.getEntryOffset();
    }

    @Override
    public void setEntryOffset(int entryOffset) {
        header.setEntryOffset(entryOffset);
    }
}
