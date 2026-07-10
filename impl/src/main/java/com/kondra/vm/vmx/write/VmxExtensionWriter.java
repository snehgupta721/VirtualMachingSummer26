package com.kondra.vm.vmx.write;

import com.kondra.vm.common.vmx.VmxExt;
import com.kondra.vm.vmx.data.RelocationExtension;
import com.kondra.vm.vmx.data.SectionHeader;
import com.kondra.vm.vmx.data.SymbolTableExtension;
import com.kondra.vm.vmx.data.VmxHeader;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import static com.kondra.vm.vmx.ArrayProcessor.writeByte;
import static com.kondra.vm.vmx.ArrayProcessor.writeInt;

public class VmxExtensionWriter {

    public int write(RandomAccessFile raf, List<VmxExt> extensions, int programSize) throws IOException {
        int extHeaderOffset = VmxHeader.HEADER_SIZE + SectionHeader.HEADER_SIZE;
        int headerSize = extHeaderOffset + (VmxExt.HEADER_SIZE * extensions.size());
        int payloadOffset = headerSize + programSize;

        for (VmxExt extension : extensions) {
            // Align the payload offset
            if (payloadOffset % 4 != 0) {
                payloadOffset += (4 - (payloadOffset % 4));
            }

            int type = extension.getType();
            int size = extension.getSize();

            // Write the extension header
            writeByte(raf, extHeaderOffset, type);
            writeByte(raf, extHeaderOffset + 1, extension.getFlag());
            // relative offset
            writeInt(raf, extHeaderOffset + 4, payloadOffset - headerSize);
            writeInt(raf, extHeaderOffset + 8, size);
            switch (type) {
                case VmxExt.TYPE_RELOC:
                    RelocationExtension relocExt = (RelocationExtension) extension;
                    new RelocationExtWriter().write(raf, relocExt, payloadOffset);
                    break;
                case VmxExt.TYPE_SYMTAB:
                    SymbolTableExtension symbolTableExt = (SymbolTableExtension) extension;
                    new SymbolTableExtWriter().write(raf, symbolTableExt, payloadOffset, size);
                    break;
                case VmxExt.TYPE_PRELOAD:
                    break;
                case VmxExt.TYPE_LABEL:
                    break;
                case VmxExt.TYPE_EXPORT:
                    break;
                case VmxExt.TYPE_AFFINITY:
                    break;
                default:
                    break;
            }
            extHeaderOffset += VmxExt.HEADER_SIZE;
            payloadOffset += size;
        }
        return payloadOffset;
    }
}
