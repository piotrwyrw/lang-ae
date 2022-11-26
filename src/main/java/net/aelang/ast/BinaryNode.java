package net.aelang.ast;

import net.aelang.Pair;
import net.aelang.Tools;
import net.aelang.codegen.Assemblable;
import net.aelang.codegen.InstructionMnemonic;
import net.aelang.codegen.Register;
import net.aelang.codegen.instruction.Instruction;
import net.aelang.codegen.instruction.SimpleInstruction;
import net.aelang.codegen.parameter.InstructionParameter;
import net.aelang.codegen.parameter.RegisterParameter;

import java.awt.geom.RectangularShape;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BinaryNode extends SolvableNode implements RecursiveNodePrint, Assemblable {

    private SolvableNode left;
    private SolvableNode right;
    private BinaryOperation op;

    public BinaryNode(SolvableNode left, SolvableNode right, BinaryOperation op) {
        this.left = left;
        this.right = right;
        this.op = op;
    }

    public Node left() {
        return left;
    }

    public void setLeft(SolvableNode left) {
        this.left = left;
    }

    public Node right() {
        return right;
    }

    public void setRight(SolvableNode right) {
        this.right = right;
    }

    public BinaryOperation op() {
        return op;
    }

    public void setOp(BinaryOperation op) {
        this.op = op;
    }

    @Override
    public String dump(int lpad) {
        String s = Tools.leftpad(lpad) + "Binary (" + op.toString() + "):\n";
        lpad++;
        s += Tools.leftpad(lpad) + "Left:\n";
        lpad++;
        s += left.dump(lpad);
        lpad--;
        s += Tools.leftpad(lpad) + "Right:\n";
        lpad++;
        s += right.dump(lpad);
        lpad--;
        lpad--;
        return s;
    }

    @Override
    public Pair<Register, Instruction[]> assemble() {
        Pair<Register, Instruction[]> lasm = left.assemble();
        Pair<Register, Instruction[]> rasm = right.assemble();

        InstructionMnemonic mnemonic = InstructionMnemonic.fromBinary(op);

        if (mnemonic == InstructionMnemonic.ADD || mnemonic == InstructionMnemonic.SUB) {
            Instruction[] instr = {
                    new SimpleInstruction(mnemonic, new InstructionParameter[]{new RegisterParameter(lasm.key()), new RegisterParameter(rasm.key())})
            };

            rasm.key().used = false;

            return new Pair<>(lasm.key(), Instruction.merge(lasm.val(), rasm.val(), instr));
        }

        if (mnemonic == InstructionMnemonic.IDIV) {
            Instruction[] inss = {
                    // Preserve the RAX and RDX registers
                    new SimpleInstruction(InstructionMnemonic.PUSH, new InstructionParameter[]{new RegisterParameter(Register.RAX)}),
                    new SimpleInstruction(InstructionMnemonic.PUSH, new InstructionParameter[]{new RegisterParameter(Register.RDX)}),

                    // Move the left expression into the RAX register
                    new SimpleInstruction(InstructionMnemonic.MOV, new InstructionParameter[]{new RegisterParameter(Register.RAX), new RegisterParameter(lasm.key())}),

                    // Divide by the right register
                    new SimpleInstruction(InstructionMnemonic.IDIV, new InstructionParameter[]{new RegisterParameter(rasm.key())}),

                    // Store the result in the left register
                    new SimpleInstruction(InstructionMnemonic.MOV, new InstructionParameter[]{new RegisterParameter(lasm.key()), new RegisterParameter(Register.RAX)}),

                    // Restore the registers RAX and RDX from the stack
                    new SimpleInstruction(InstructionMnemonic.POP, new InstructionParameter[]{new RegisterParameter(Register.RDX)}),
                    new SimpleInstruction(InstructionMnemonic.POP, new InstructionParameter[]{new RegisterParameter(Register.RAX)})
            };

            rasm.key().used = false;

            return new Pair<>(lasm.key(), Instruction.merge(lasm.val(), rasm.val(), inss));
        }

        return null;

    }
}
