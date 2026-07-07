package com.kondra.vm.vmx.write;

import com.kondra.vm.common.vmx.VmxExt;
import com.kondra.vm.vmx.ArrayProcessor;
import com.kondra.vm.vmx.MyVmxExt;
import com.kondra.vm.vmx.VmxHeader;
import com.kondra.vm.vmx.extensions.RelocationExtension;

import java.util.List;

public class VmxExtensionWriter {

    public static int writeExtensions(byte[] bytes, List<VmxExt> extensions, int headerSize, int payloadOffset) {
        int offset = VmxHeader.EXT_HEADER_START;

        for (VmxExt extension : extensions) {
            // Align the payload offset
            if (payloadOffset % 4 != 0) {
                payloadOffset += (4 - (payloadOffset % 4));
            }

            int type = extension.getType();
            MyVmxExt ext = (MyVmxExt) extension;

            ArrayProcessor.writeByte(bytes, offset, type);
            ArrayProcessor.writeByte(bytes, offset + 1, ext.getFlags());
            // relative offset
            ArrayProcessor.writeInt(bytes, offset + 4, payloadOffset - headerSize);
            ArrayProcessor.writeInt(bytes, offset + 8, ext.getData().length);
            switch (type) {
                case VmxExt.TYPE_RELOC:
                    RelocationExtension relocExt = (RelocationExtension) extension;

                    int totalRecords = 0;
                    int[] sectionsOrder = {
                            com.kondra.vm.common.vmx.VmxFile.SECTION_TEXT,
                            com.kondra.vm.common.vmx.VmxFile.SECTION_RODATA,
                            com.kondra.vm.common.vmx.VmxFile.SECTION_DATA,
                            com.kondra.vm.common.vmx.VmxFile.SECTION_BSS
                    };
                    for (int section : sectionsOrder) {
                        if (relocExt.getRelocations(section) != null) {
                            totalRecords += relocExt.getRelocations(section).size();
                        }
                    }
                    int actualBytesWritten = 32 + (totalRecords * 8);
                    ArrayProcessor.writeInt(bytes, offset + 8, actualBytesWritten);
                    relocExt.write(bytes, payloadOffset, extensions.size() * 8);
                    payloadOffset += actualBytesWritten;
                    break;
                default:
                    ArrayProcessor.writeInt(bytes, offset + 8, ext.getData().length);
                    System.arraycopy(ext.getData(), 0, bytes, payloadOffset, ext.getData().length);
                    payloadOffset += ext.getData().length;
            }
            offset += VmxExt.HEADER_SIZE;
        }

        return payloadOffset;
    }
}
