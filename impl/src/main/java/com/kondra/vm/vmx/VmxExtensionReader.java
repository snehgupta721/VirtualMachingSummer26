package com.kondra.vm.vmx;

import com.kondra.vm.common.vmx.VmxExt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.kondra.vm.vmx.ArrayProcessor.readInt;

public class VmxExtensionReader {
    private static final int EXT_HEADER_START = 56; // 24 + 32

    public static List<VmxExt> parseExtensions(byte[] fileBytes, int extCount, int headerSize) {
        List<VmxExt> extensions = new ArrayList<>();
        int cursor = EXT_HEADER_START;

        for (int i = 0; i < extCount; i++) {
            int type    = fileBytes[cursor] & 0xFF;
            int extFlag = fileBytes[cursor + 1] & 0xFF;
            int offset  = readInt(fileBytes, cursor + 4);
            int size    = readInt(fileBytes, cursor + 8);

            // Using absolute offset directly
            byte[] extData = Arrays.copyOfRange(fileBytes, headerSize + offset, headerSize + offset + size);

            extensions.add(new MyVmxExt(type, extFlag, extData));
            cursor += VmxExt.HEADER_SIZE;
        }
        return extensions;
    }
}
