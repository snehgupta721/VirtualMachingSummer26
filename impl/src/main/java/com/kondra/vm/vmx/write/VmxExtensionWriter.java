package com.kondra.vm.vmx.write;

import com.kondra.vm.common.vmx.VmxExt;
import com.kondra.vm.vmx.ArrayProcessor;
import com.kondra.vm.vmx.MyVmxExt;
import com.kondra.vm.vmx.VmxHeader;
import com.kondra.vm.vmx.extensions.RelocationExtension;

import java.util.List;

public class VmxExtensionWriter {

    public static int writeExtensions(byte[] bytes, List<VmxExt> extensions, int headerSizeWithExt, int payloadOffset) {
        int extHeaderOffset = VmxHeader.EXT_HEADER_START;

        for (VmxExt extension : extensions) {
            // Align the payload offset
            if (payloadOffset % 4 != 0) {
                payloadOffset += (4 - (payloadOffset % 4));
            }

            int type = extension.getType();
            MyVmxExt ext = (MyVmxExt) extension;

            // Write the extension header
            ArrayProcessor.writeByte(bytes, extHeaderOffset, type);
            ArrayProcessor.writeByte(bytes, extHeaderOffset + 1, ext.getFlags());
            // relative offset
            ArrayProcessor.writeInt(bytes, extHeaderOffset + 4, payloadOffset - headerSizeWithExt);
            ArrayProcessor.writeInt(bytes, extHeaderOffset + 8, ext.getData().length);
            switch (type) {
                case VmxExt.TYPE_RELOC:
                    RelocationExtension relocExt = (RelocationExtension) extension;
                    relocExt.write(bytes, payloadOffset);
                    break;
                default:
                    break;
                    // System.arraycopy(ext.getData(), 0, bytes, payloadOffset, ext.getData().length);
            }
            extHeaderOffset += VmxExt.HEADER_SIZE;
            payloadOffset += ext.getData().length;
        }

        return payloadOffset;
    }
}
