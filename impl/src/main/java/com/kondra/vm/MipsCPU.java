package com.kondra.vm;

import com.kondra.vm.common.CPU;
import com.kondra.vm.common.StackOverflowException;
import com.kondra.vm.common.concurrent.VmThread;
import com.kondra.vm.common.memory.Memory;

public class MipsCPU implements CPU {
    private final Memory systemMemory;
    private final VmThread thread;
    private final int[] registers;
    private int iPtr;
    private int nextPtr;
    private int hi;
    private int lo;

    public MipsCPU(VmThread thread, Memory systemMemory) {
        this.thread = thread;
        this.systemMemory = systemMemory;
        registers = new int[32];
        iPtr = 0;
        nextPtr = 0;
        hi = 0;
        lo = 0;
    }

    @Override
    public int getRegister(int i) {
        if (i == CPU.REG_HI) {
            return hi;
        } else if (i == CPU.REG_LO) {
            return lo;
        }
        return registers[i];
    }

    @Override
    public void setRegister(int i, int i1) {
        if (i == CPU.REG_HI) {
            hi = i1;
        } else if (i == CPU.REG_LO) {
            lo = i1;
        } else {
            registers[i] = i1;
        }
    }

    @Override
    public int getIPtr() {
        return iPtr;
    }

    @Override
    public void setIPtr(int i) {
        iPtr = i;
        nextPtr = i + 4;
    }

    @Override
    public int getNextIPtr() {
        return nextPtr;
    }

    @Override
    public void setNextIPtr(int i) {
        nextPtr = i;
    }

    @Override
    public Memory getMemory() {
        return systemMemory;
    }

    @Override
    public void execute(int i) throws StackOverflowException {
        for (int j = 0; j < i; j++) {
            int instruction = systemMemory.getInt(iPtr);
            iPtr = nextPtr;
            nextPtr += 4;
            decode(instruction);

        }
    }

    @Override
    public void execute() throws StackOverflowException {

    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public VmThread getThread() {
        return thread;
    }

    private void decode(int instruction) {
        short opcode = (short) ((instruction >>> 26) & 0x3F);  // Bits 31-26
        switch (opcode) {
            case 0x00:
                // R type
                rType(instruction);
                break;
            case 0x02:
                // J type: jump
                break;
            case 0x03:
                // J type: jump and link
                break;
            default:
                // I type
                iType(instruction);
                break;
        }
    }

    private void iType(int instruction) {
        short opcode = (short) ((instruction >>> 26) & 0x3F);  // Bits 31-26
        short rs = (short) ((instruction >>> 21) & 0x1F);    // Bits 25-21
        short rd    = (short) ((instruction >>> 11) & 0x1F); // Bits 15-11
        short rt = (short) ((instruction >>> 16) & 0x1F);    // Bits 20-16
        short immediate = (short) (instruction & 0x0000FFFF);  // Bits 15-0
        switch (opcode) {
            case 0x08:
                // addi
                registers[rt] = registers[rs] + immediate;
                break;
            case 0x09:
                // addiu
                registers[rt] = registers[rs] + immediate;
                break;
            case 0x0c:
                // andi
                registers[rt] = registers[rs] & immediate;
                break;
            case 0xd:
                // ori
                registers[rt] = registers[rs] | immediate;
                break;
            case 0xe:
                // xori
                registers[rt] = registers[rs] ^ immediate;
                break;
            case 0xf:
                // lui
                registers[rt] = immediate << 16;
                break;
            case 0x0a:
                // slti
                if (registers[rs] < immediate) {
                    registers[rt] = 1;
                } else {
                    registers[rt] = 0;
                }
                break;
            case 0x0b:
                // sltiu
                long left = ((long) registers[rs]) & 0x00000000ffffffffL;
                long right= ((long) immediate) & 0x00000000ffffffffL;
                if (left < right) {
                    registers[rt] = 1;
                } else {
                    registers[rt] = 0;
                }
                break;
        }
    }

    private void jType(int instruction) {

    }

    private void rType(int instruction) {
        short rs = (short) ((instruction >>> 21) & 0x1F);    // Bits 25-21
        short rt = (short) ((instruction >>> 16) & 0x1F);    // Bits 20-16
        short rd    = (short) ((instruction >>> 11) & 0x1F); // Bits 15-11
        short shamt = (short) ((instruction >>> 6)  & 0x1F);    // Bits 10-6
        short funct = (short) (instruction          & 0x3F);    // Bits 5-0
        switch (funct) {
            case 0x00:
                // sll
                registers[rd] = registers[rt] << shamt;
                break;
            case 0x02:
                // srl
                registers[rd] = registers[rt] >>> shamt;
                break;
            case 0x03:
                // sra
                registers[rd] = registers[rt] >> shamt;
                break;
            case 0x04:
                // sllv
                registers[rd] = registers[rt] << registers[rs];
                break;
            case 0x06:
                // srlv
                registers[rd] = registers[rt] >>> registers[rs];
                break;
            case 0x07:
                // srav
                registers[rd] = registers[rt] >> registers[rs];
                break;
            case 0x20:
                // add. Integer overflow exception
                registers[rd] = registers[rs] + registers[rt];
                break;
            case 0x21:
                // addu. No exception
                registers[rd] = registers[rs] + registers[rt];
                break;
            case 0x22:
                // sub
                registers[rd] = registers[rs] - registers[rt];
                break;
            case 0x23:
                // subu
                registers[rd] = registers[rs] - registers[rt];
                break;
            case 0x27:
                // nor
                registers[rd] = ~(registers[rs] | registers[rt]);
                break;
            case 0x25:
                // or
                registers[rd] = registers[rs] | registers[rt];
                break;
            case 0x26:
                // xor
                registers[rd] = registers[rs] ^ registers[rt];
                break;
            case 0x24:
                // and
                registers[rd] = registers[rs] & registers[rt];
                break;
            case 0x10:
                // mfhi
                registers[rd] = hi;
                break;
            case 0x11:
                // mthi
                hi = registers[rs];
                break;
            case 0x12:
                // mflo
                registers[rd] = lo;
                break;
            case 0x13:
                // mtlo
                lo = registers[rs];
                break;
            case 0x18:
                // mult
                long t = (long) registers[rs] * (long) registers[rt];
                lo = (int) (t & 0x00000000ffffffffL);
                hi = (int) (t >>> 32);
                break;
            case 0x19:
                // multu
                long a = ((long) registers[rs]) & 0x00000000ffffffffL;
                long b = ((long) registers[rt]) & 0x00000000ffffffffL;
                long product = a * b;
                lo = (int) (product & 0x00000000ffffffffL);
                hi = (int) (product >>> 32);
                break;
            case 0x1a:
                // div
                if (registers[rt] != 0) {
                    lo = registers[rs] / registers[rt];
                    hi = registers[rs] % registers[rt];
                }
                break;
            case 0x1b:
                // divu
                long c = ((long) registers[rs]) & 0x00000000ffffffffL;
                long d = ((long) registers[rt]) & 0x00000000ffffffffL;
                if (registers[rt] != 0) {
                    lo = (int) (c / d);
                    hi = (int) (c % d);
                }
                break;
            case 0x2a:
                // slt
                if (registers[rs] < registers[rt]) {
                    registers[rd] = 1;
                } else {
                    registers[rd] = 0;
                }
                break;
            case 0x2b:
                // sltu
                long left = ((long) registers[rs]) & 0x00000000ffffffffL;
                long right= ((long) registers[rt]) & 0x00000000ffffffffL;
                if (left < right) {
                    registers[rd] = 1;
                } else {
                    registers[rd] = 0;
                }
                break;
        }
    }
}
