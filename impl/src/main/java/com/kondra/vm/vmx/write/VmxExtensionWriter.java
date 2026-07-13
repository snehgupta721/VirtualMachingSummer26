package com.kondra.vm.vmx.write;

import com.kondra.vm.common.vmx.VmxExt;
import com.kondra.vm.vmx.data.*;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import static com.kondra.vm.vmx.ArrayProcessor.writeByte;
import static com.kondra.vm.vmx.ArrayProcessor.writeInt;

public class VmxExtensionWriter {

    public int write(RandomAccessFile raf, List<VmxExt> extensions, int programSize) throws IOException {
        int extHeaderOffset = VmxHeader.HEADER_SIZE + SectionHeader.HEADER_SIZE;
        // int headerSize = extHeaderOffset + (VmxExt.HEADER_SIZE * extensions.size());
        int headerSize = extHeaderOffset + (VmxExt.HEADER_SIZE * 5);
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
                case VmxExt.TYPE_LABEL:
                    System.out.println("Write Label ext at " + payloadOffset);
                    LabelExtension labelExt = (LabelExtension) extension;
                    new LabelExtensionWriter().write(raf, labelExt, payloadOffset, size);
                    break;
                case VmxExt.TYPE_RELOC:
                    System.out.println("Write Reloc ext at " + payloadOffset);
                    RelocationExtension relocExt = (RelocationExtension) extension;
                    new RelocationExtWriter().write(raf, relocExt, payloadOffset);
                    break;
                case VmxExt.TYPE_SYMTAB:
                    System.out.println("Write Symtab ext at " + payloadOffset);
                    SymbolTableExtension symbolTableExt = (SymbolTableExtension) extension;
                    new SymbolTableExtWriter().write(raf, symbolTableExt, payloadOffset, size);
                    break;
                case VmxExt.TYPE_PRELOAD:
                    System.out.println("Write preload ext at " + payloadOffset);
                    PreloadExtension preloadExt = (PreloadExtension) extension;
                    new PreloadExtWriter().write(raf, preloadExt, payloadOffset);
                    break;
                case VmxExt.TYPE_EXPORT:
                    System.out.println("Write export ext at " + payloadOffset);
                    ExportExtension exportExt = (ExportExtension) extension;
                    new ExportExtWriter().write(raf, exportExt, payloadOffset);
                    break;
                case VmxExt.TYPE_AFFINITY:
                    System.out.println("Write affinity ext at " + payloadOffset);
                    AffinityExtension affinityExt = (AffinityExtension) extension;
                    new AffinityExtWriter().write(raf, affinityExt, payloadOffset);
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
