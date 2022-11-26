package net.aelang;

import net.aelang.codegen.*;
import net.aelang.codegen.instruction.AssemblyLabel;
import net.aelang.codegen.instruction.SimpleInstruction;
import net.aelang.codegen.parameter.ImmediateParameter;
import net.aelang.codegen.parameter.InstructionParameter;
import net.aelang.codegen.parameter.RegisterParameter;
import net.aelang.codegen.parameter.StringParameter;

/**
 * @author Piotr K. Wyrwas
 */

public class Main {

    public static void main(String[] args) {
        AssemblyOutput output = new AssemblyOutput();
        output.add(new SimpleInstruction(InstructionMnemonic.DB, new InstructionParameter[]{new StringParameter("Hello, World!"), new ImmediateParameter(10), new ImmediateParameter(0)}));
        output.add(new AssemblyLabel("main"));
        output.add(new SimpleInstruction(InstructionMnemonic.CMP, new InstructionParameter[]{new RegisterParameter(Register.RAX), new ImmediateParameter(123)}));
        System.out.println(output.generate());
        new InputLoop();
    }

}
