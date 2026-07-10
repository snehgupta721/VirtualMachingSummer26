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

            int payloadOffset = headerSize + offset;
            VmxExt curr = null;
            switch (type) {
                // parse extension
                case VmxExt.TYPE_LABEL:
                    System.out.println("Label ext at " + payloadOffset);
                    break;
                case VmxExt.TYPE_RELOC:
                    System.out.println("Relocation ext at " + payloadOffset);
                    curr = new RelocationExtReader().read(raf, type, extFlag, payloadOffset);
                    break;
                case VmxExt.TYPE_SYMTAB:
                    System.out.println("SymTab ext at " + payloadOffset);
                    curr = new SymbolTableExtReader().read(raf, type, extFlag, payloadOffset, size);
                    break;
                case VmxExt.TYPE_PRELOAD:
                    System.out.println("Preload ext at " + payloadOffset);
                    break;
                case VmxExt.TYPE_EXPORT:
                    System.out.println("Export ext at " + payloadOffset);
                    break;
                case VmxExt.TYPE_AFFINITY:
                    System.out.println("Affinity ext at " + payloadOffset);
                    break;
                default:
                    break;
            }
            if (curr != null) extensions.add(curr);
            cursor += VmxExt.HEADER_SIZE;
        }
        return extensions;
    }
}
