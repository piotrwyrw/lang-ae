package net.aelang.codegen.parameter;

import net.aelang.codegen.Generatable;

public class ImmediateParameter extends InstructionParameter implements Generatable {

    private int integer;

    public ImmediateParameter(int integer) {
        this.integer = integer;
    }

    @Override
    public String generate() {
        return "0x" + Integer.toHexString(integer);
    }

}
