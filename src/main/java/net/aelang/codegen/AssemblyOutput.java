package net.aelang.codegen;

import net.aelang.codegen.instruction.Instruction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class AssemblyOutput {

    private List<Instruction> instructions;

    public AssemblyOutput() {
        this.instructions = new ArrayList<>();
    }

    public void add(Instruction instr) {
        this.instructions.add(instr);
    }

    public List<Instruction> instructions() {
        return instructions;
    }

    public String generate() {
        Calendar gc = GregorianCalendar.getInstance();
        StringBuilder asm = new StringBuilder(";; Generated on " + gc.get(GregorianCalendar.DAY_OF_MONTH) + "/" + (gc.get(GregorianCalendar.MONTH) + 1) + "/" + gc.get(GregorianCalendar.YEAR) + '\n');
        instructions.forEach((instr) -> {
            asm.append(instr.generate() + '\n');
        });
        return asm.toString();
    }

}
