package net.aelang.codegen;

public enum Register {

    AL      (8),
    AH      (8),
    AX      (16),
    EAX     (32),
    RAX     (64),

    BL      (8),
    BH      (8),
    BX      (16),
    EBX     (32),
    RBX     (64),

    CL      (8),
    CH      (8),
    CX      (16),
    ECX     (32),
    RCX     (64),

    DL      (8),
    DH      (8),
    DX      (16),
    EDX     (32),
    RDX     (64),

    NONE    (0);

    public final long bits;
    public boolean used;

    Register(long bits) {
        this.bits = bits;
        this.used = false;
    }

    public static Register free(int bits) {
        for (Register r : values())
            if (r.bits == bits && !r.used && r != RAX)
                return r;
        return null;
    }

}
