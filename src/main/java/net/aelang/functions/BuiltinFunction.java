package net.aelang.functions;

public abstract class BuiltinFunction {

    private String id;

    public BuiltinFunction(String identifier) {
        this.id = identifier;
    }

    public String id() {
        return id;
    }

    public abstract double run(double d);

}
