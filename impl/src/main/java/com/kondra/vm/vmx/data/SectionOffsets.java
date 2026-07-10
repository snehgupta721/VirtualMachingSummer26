package com.kondra.vm.vmx.data;

/**
 * Record section offsets during the writing process. NOT maintained
 */
public record SectionOffsets(int textOffset, int textSize,
                             int rodataOffset, int rodataSize,
                             int dataOffset, int dataSize,
                             int bssOffset, int bssSize) {
}
