package net.aelang.codegen;

import net.aelang.Pair;
import net.aelang.codegen.instruction.Instruction;

public interface Assemblable {

    public Pair<Register, Instruction[]> assemble();

}
