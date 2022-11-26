package net.aelang.codegen;

import net.aelang.ast.BinaryOperation;

/**
 * x86 NASM Assembly instruction mnemonics
 */
public enum InstructionMnemonic {

    // Direct register and memory manipulation
    MOV,
    LEA,

    // Arithmetic operations
    ADD,
    SUB,
    IDIV,
    MUL,
    IMUL,

    // Flow control
    JMP,
    JE,
    JL,
    JG,
    JGE,
    JZ,
    JNZ,
    CALL,
    RET,

    // Boolean logic
    CMP,

    // Defines
    DB,
    DW,
    DD,
    DQ,

    // Stack operations
    PUSH,
    POP,

    // Placeholder
    NOP;

    public static InstructionMnemonic fromBinary(BinaryOperation op) {
        switch (op) {
            case ADD:
                return ADD;
            case SUB:
                return SUB;
            case DIV:
                return IDIV;
            case MUL:
                return IMUL;
            default:
                return NOP;
        }
    }

}
