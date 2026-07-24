package com.kondra.vm.loader.process;

import com.kondra.vm.common.memory.Memory;
import com.kondra.vm.common.vmx.VmxExt;
import com.kondra.vm.loader.MyImage;
import com.kondra.vm.vmx.MyVmxFile;
import com.kondra.vm.vmx.data.RelocationExtension;
import com.kondra.vm.common.vmx.ext.Relocation;
import com.kondra.vm.vmx.data.SymbolTableExtension;

import java.util.List;
import java.util.Map;

public class Relocations {

    public void applyRelocations(MyImage image, MyVmxFile vmxFile, Memory memory) {
        SymbolTableExtension symTab = (SymbolTableExtension) vmxFile.getExtension(VmxExt.TYPE_SYMTAB);
        RelocationExtension ext = (RelocationExtension) vmxFile.getExtension(VmxExt.TYPE_RELOC);
        Map<Integer, List<Relocation>> relocations = ext.getRelocations();
        for (Integer sectionIdx : relocations.keySet()) {
            List<Relocation> relocation = relocations.get(sectionIdx);
            for (Relocation rel : relocation) {
                int targetAddress;
                if (rel.isDynamic()) {
                    String dynamicSymbol = symTab.getSymbol(rel.getDynamicSymbolOffset());
                    targetAddress = image.resolveSymbolAddress(dynamicSymbol);
                } else {
                    targetAddress = image.getSectionAddress(rel.getSection()) + rel.getSectionOffset();
                }

                int fixupAddress = rel.getFixupOffset() + image.getSectionAddress(sectionIdx);
                int hiFixupAddress = 0;

                switch (rel.getType()) {
                    case Relocation.RELOC_32:
                        memory.setInt(fixupAddress, targetAddress);
                        break;
                    case Relocation.RELOC_26:
                        // shift by 2 bits
                        int currentInstruction = memory.getInt(fixupAddress);
                        int shiftedTarget = (targetAddress >>> 2) & 0x03FFFFFF;
                        int newInstruction = (currentInstruction & 0xFC000000) | shiftedTarget;
                        memory.setInt(fixupAddress, newInstruction);
                        break;
                    case Relocation.RELOC_HI16:
                        hiFixupAddress = fixupAddress;
                        break;
                    case Relocation.RELOC_LO16:
                        int lowInstruction = memory.getInt(fixupAddress);
                        int low = (lowInstruction & 0xFFFF0000) | (targetAddress & 0x0000FFFF);
                        memory.setInt(hiFixupAddress, low);

                        int hiInstruction = memory.getInt(hiFixupAddress);
                        int hi = (hiInstruction & 0x0000FFFF) | (targetAddress & 0xFFFF0000);
                        memory.setShort(fixupAddress, (short) (hi & 0x0000FFFF));
                        break;
                }
            }
        }
    }
}
