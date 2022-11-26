package net.aelang.codegen.parameter;

import net.aelang.codegen.Register;

public class RegisterParameter extends InstructionParameter {

    private Register reg;

    public RegisterParameter(Register reg) {
        this.reg = reg;
    }

    public String generate() {
        return reg.toString().toLowerCase();
    }
}
