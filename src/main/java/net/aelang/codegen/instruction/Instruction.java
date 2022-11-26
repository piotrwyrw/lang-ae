package net.aelang.codegen.instruction;

import net.aelang.codegen.Generatable;

import java.util.ArrayList;
import java.util.List;

public abstract class Instruction implements Generatable {

    public static Instruction[] merge(Instruction[]... ins) {
        List<Instruction> instructions = new ArrayList<>();
        for (Instruction[] in : ins)
            for (Instruction instruction : in)
                instructions.add(instruction);
        return instructions.toArray(new Instruction[0]);
    }

}
