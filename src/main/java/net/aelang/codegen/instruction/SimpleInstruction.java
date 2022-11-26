package net.aelang.codegen.instruction;

import net.aelang.codegen.Generatable;
import net.aelang.codegen.InstructionMnemonic;
import net.aelang.codegen.parameter.InstructionParameter;

public class SimpleInstruction extends Instruction implements Generatable {

    private InstructionMnemonic mnemonic;
    private InstructionParameter[] parameters;

    public SimpleInstruction(InstructionMnemonic mnemonic, InstructionParameter[] parameters) {
        this.mnemonic = mnemonic;
        this.parameters = parameters;
    }

    public InstructionMnemonic mnemonic() {
        return mnemonic;
    }

    public void setMnemonic(InstructionMnemonic mnemonic) {
        this.mnemonic = mnemonic;
    }

    public InstructionParameter[] parameters() {
        return parameters;
    }

    public void setParameters(InstructionParameter[] parameters) {
        this.parameters = parameters;
    }

    @Override
    public String generate() {
        if (mnemonic == InstructionMnemonic.NOP)
            return "nop";

        String s = mnemonic.toString().toLowerCase() + " ";
        for (int i = 0; i < parameters.length; i++) {
            boolean last = i + 1 == parameters.length;
            s += parameters[i].generate() + ((last) ? "" : ", ");
        }
        return s;
    }
}
