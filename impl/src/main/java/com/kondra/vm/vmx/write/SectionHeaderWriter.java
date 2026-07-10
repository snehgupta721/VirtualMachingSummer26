package com.kondra.vm.vmx.write;

import com.kondra.vm.vmx.data.SectionHeader;
import com.kondra.vm.vmx.data.SectionOffsets;
import com.kondra.vm.vmx.data.VmxHeader;

import java.io.IOException;
import java.io.RandomAccessFile;

import static com.kondra.vm.vmx.ArrayProcessor.writeInt;

public class SectionHeaderWriter {
    public SectionHeaderWriter() {
    }

    public void write(RandomAccessFile raf, SectionOffsets offsets) throws IOException {
        writeInt(raf, SectionHeader.OFFSET_TEXT_OFFSET, offsets.textOffset());
        writeInt(raf, SectionHeader.OFFSET_TEXT_SIZE, offsets.textSize());
        writeInt(raf, SectionHeader.OFFSET_RODATA_OFFSET, offsets.rodataOffset());
        writeInt(raf, SectionHeader.OFFSET_RODATA_SIZE, offsets.rodataSize());
        writeInt(raf, SectionHeader.OFFSET_DATA_OFFSET, offsets.dataOffset());
        writeInt(raf, SectionHeader.OFFSET_DATA_SIZE, offsets.dataSize());
        writeInt(raf, SectionHeader.OFFSET_BSS_OFFSET, offsets.bssOffset());
        writeInt(raf, SectionHeader.OFFSET_BSS_SIZE, offsets.bssSize());
    }
}
