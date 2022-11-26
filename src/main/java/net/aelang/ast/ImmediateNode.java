package net.aelang.ast;

import net.aelang.Pair;
import net.aelang.Tools;
import net.aelang.codegen.Assemblable;
import net.aelang.codegen.InstructionMnemonic;
import net.aelang.codegen.Register;
import net.aelang.codegen.instruction.Instruction;
import net.aelang.codegen.instruction.SimpleInstruction;
import net.aelang.codegen.parameter.ImmediateParameter;
import net.aelang.codegen.parameter.InstructionParameter;
import net.aelang.codegen.parameter.RegisterParameter;
import net.aelang.tokenizer.Token;
import net.aelang.tokenizer.TokenType;

public class ImmediateNode extends SolvableNode implements RecursiveNodePrint {

    private double value;

    public ImmediateNode(double value) {
        this.value = value;
    }

    public ImmediateNode(Token t) {
        if (t.type() == TokenType.INT_LIT) {
            int n = Integer.parseInt(t.val());
            this.value = (double) n;
        } else if (t.type() == TokenType.REAL_LIT) {
            value = Double.parseDouble(t.val());
        } else {
            throw new RuntimeException("Expected an integer literal or a real literal token.");
        }
    }

    public double value() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String dump(int lpad) {
        return Tools.leftpad(lpad) + " Immediate (" + value + ")\n";
    }

    @Override
    public Pair<Register, Instruction[]> assemble() {
        Register reg = Register.free(64);
        if (reg == null)
            reg = Register.free(32);
        if (reg == null)
            throw new RuntimeException("Ran out of free registers.");
        reg.used = true;

        Instruction i = new SimpleInstruction(InstructionMnemonic.MOV, new InstructionParameter[]{
                new RegisterParameter(reg),
                new ImmediateParameter((int) value)
        });

        return new Pair<>(reg, new Instruction[]{i});
    }
}
