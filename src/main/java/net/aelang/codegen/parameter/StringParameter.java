package net.aelang.codegen.parameter;

public class StringParameter extends InstructionParameter {

    private String value;

    public StringParameter(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String generate() {
        return '\"' + value + '\"';
    }

}
