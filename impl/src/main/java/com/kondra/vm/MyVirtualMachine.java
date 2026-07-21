package com.kondra.vm;

import java.io.File;

import com.kondra.vm.common.CPU;
import com.kondra.vm.common.VirtualMachine;
import com.kondra.vm.common.concurrent.ProcessCreationException;
import com.kondra.vm.common.concurrent.VmProcess;
import com.kondra.vm.common.concurrent.VmThread;
import com.kondra.vm.common.loader.Loader;
import com.kondra.vm.common.memory.Memory;
import com.kondra.vm.common.memory.MemoryMgr;
import com.kondra.vm.common.vmx.VmxException;
import com.kondra.vm.common.vmx.VmxFile;
import com.kondra.vm.loader.MyLoader;
import com.kondra.vm.mmu.MemoryAccess;
import com.kondra.vm.mmu.MipsCPU;
import com.kondra.vm.mmu.MyMemoryManager;
import com.kondra.vm.vmx.MyVmxFile;

public class MyVirtualMachine implements VirtualMachine {
    private Memory systemMemory;
    private final Loader loader;
    private final MemoryMgr memoryMgr;

    public MyVirtualMachine() {
        memoryMgr = new MyMemoryManager();
        loader = new MyLoader(memoryMgr);
    }

    @Override
    public CPU createCPU(VmThread thread) {
        CPU cpu = new MipsCPU(thread, systemMemory);
        return cpu;
    }

    @Override
    public void setSystemMemory(Memory memory) {
        this.systemMemory = memory;
        memoryMgr.setMemory(memory);
    }

    @Override
    public Memory getSystemMemory() {
        return systemMemory;
    }

    @Override
    public Memory getMemory(int size) {
        return new MemoryAccess(size);
    }

    @Override
    public MemoryMgr getMemoryMgr() {
        return memoryMgr;
    }

    @Override
    public VmxFile loadVmxFile(File file) throws VmxException {
        return new MyVmxFile(file);
    }

    @Override
    public VmxFile emptyVmxFile() {
        return new MyVmxFile();
    }

    @Override
    public Loader getLoader() {
        return loader;
    }

    @Override
    public int getClibJumpTable() {
        return 0;
    }

    @Override
    public int getVmRunAddr() {
        return 0;
    }

    @Override
    public VmProcess createProcess(File file, int stackSize) throws ProcessCreationException {
        return null;
    }

    @Override
    public void reset() {
    }
}
