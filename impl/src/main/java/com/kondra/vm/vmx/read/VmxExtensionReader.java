package com.kondra.vm.vmx.read;

import com.kondra.vm.common.vmx.VmxExt;
import com.kondra.vm.vmx.data.VmxHeader;
import com.kondra.vm.vmx.data.SectionHeader;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import static com.kondra.vm.vmx.ArrayProcessor.readInt;
import static com.kondra.vm.vmx.ArrayProcessor.readByteUnsigned;

public class VmxExtensionReader {
    public List<VmxExt> read(RandomAccessFile raf, int extCount, int headerSize) throws IOException {
        List<VmxExt> extensions = new ArrayList<>();
        int cursor = VmxHeader.HEADER_SIZE + SectionHeader.HEADER_SIZE;

        for (int i = 0; i < extCount; i++) {
            // Parse Extension headers
            int type    = readByteUnsigned(raf, cursor);
            int extFlag = readByteUnsigned(raf, cursor + 1);
            int offset  = readInt(raf, cursor + 4);
            int size    = readInt(raf, cursor + 8);

            switch (type) {
                // parse extension
                case VmxExt.TYPE_RELOC:
                    VmxExt curr = new RelocationExtReader().read(raf, type, extFlag, headerSize + offset);
                    extensions.add(curr);
                    break;
                default:
                    break;
            }
            cursor += VmxExt.HEADER_SIZE;
        }
        return extensions;
    }
}
