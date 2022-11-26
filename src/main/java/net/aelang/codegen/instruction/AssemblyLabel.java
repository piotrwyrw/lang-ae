package net.aelang.codegen.instruction;

public class AssemblyLabel extends Instruction {

    private String name;

    public AssemblyLabel(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String generate() {
        return name + ":";
    }

}
