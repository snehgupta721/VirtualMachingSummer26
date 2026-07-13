package com.kondra.vm.vmx.read;

import com.kondra.vm.common.vmx.VmxException;
import com.kondra.vm.common.vmx.VmxExt;
import com.kondra.vm.vmx.data.SymbolTableExtension;
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
                    curr = new LabelExtReader().read(raf, type, extFlag, payloadOffset, size);
                    break;
                case VmxExt.TYPE_RELOC:
                    curr = new RelocationExtReader().read(raf, type, extFlag, payloadOffset);
                    break;
                case VmxExt.TYPE_SYMTAB:
                    curr = new SymbolTableExtReader().read(raf, type, extFlag, payloadOffset, size);
                    break;
                case VmxExt.TYPE_PRELOAD:
                    curr = new PreloadExtReader().read(raf, type, extFlag, payloadOffset, size);
                    break;
                case VmxExt.TYPE_EXPORT:
                    // Assumption: Symbol table gets loaded first
                    SymbolTableExtension symTab = null;
                    for (VmxExt ext : extensions) {
                        if (ext instanceof SymbolTableExtension) {
                            symTab = (SymbolTableExtension) ext;
                            break;
                        }
                    }

                    if (symTab == null) {
                        throw new VmxException("Symbol table was not loaded before export extension.");
                    }
                    curr = new ExportExtReader().read(raf, type, extFlag, payloadOffset, size, symTab);
                    break;
                case VmxExt.TYPE_AFFINITY:
                    curr = new AffinityExtReader().read(raf, type, extFlag, payloadOffset, size);
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
