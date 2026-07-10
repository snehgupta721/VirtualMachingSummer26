package com.kondra.vm.vmx.read;

import com.kondra.vm.vmx.data.SectionHeader;

import java.io.IOException;
import java.io.RandomAccessFile;

import static com.kondra.vm.vmx.ArrayProcessor.readInt;

public class SectionHeaderReader {
    private RandomAccessFile raf;

    public SectionHeaderReader(RandomAccessFile raf) throws IOException {
        this.raf = raf;
    }


    public int getTextOffset() throws IOException {
        return readInt(raf, SectionHeader.OFFSET_TEXT_OFFSET);
    }

    public int getTextSize() throws IOException {
        return readInt(raf, SectionHeader.OFFSET_TEXT_SIZE);
    }

    public int getRodataOffset() throws IOException {
        return readInt(raf, SectionHeader.OFFSET_RODATA_OFFSET);
    }

    public int getRodataSize() throws IOException {
        return readInt(raf, SectionHeader.OFFSET_RODATA_SIZE);
    }

    public int getDataOffset() throws IOException {
        return readInt(raf, SectionHeader.OFFSET_DATA_OFFSET);
    }

    public int getDataSize() throws IOException {
        return readInt(raf, SectionHeader.OFFSET_DATA_SIZE);
    }

    public int getBssSize() throws IOException {
        return readInt(raf, SectionHeader.OFFSET_BSS_SIZE);
    }
}
