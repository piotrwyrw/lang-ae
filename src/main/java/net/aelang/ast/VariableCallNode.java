package net.aelang.ast;

import net.aelang.Pair;
import net.aelang.Tools;
import net.aelang.codegen.Register;
import net.aelang.codegen.instruction.Instruction;

public class VariableCallNode extends SolvableNode {

    private String id;

    public VariableCallNode(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String dump(int lpad) {
        return Tools.leftpad(lpad) + "Variable: " + id + '\n';
    }

    @Override
    public Pair<Register, Instruction[]> assemble() {
        return null;
    }
}
