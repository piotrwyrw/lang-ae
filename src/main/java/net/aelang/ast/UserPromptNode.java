package net.aelang.ast;

import net.aelang.Pair;
import net.aelang.Tools;
import net.aelang.codegen.Register;
import net.aelang.codegen.instruction.Instruction;

public class UserPromptNode extends SolvableNode {

    private String message;

    public UserPromptNode(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String dump(int lpad) {
        return Tools.leftpad(lpad) + "Prompt (" + message + ")\n";
    }

    @Override
    public Pair<Register, Instruction[]> assemble() {
        return new Pair<>(Register.NONE, new Instruction[0]);
    }
}
