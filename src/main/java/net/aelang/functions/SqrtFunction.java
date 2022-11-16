package net.aelang.functions;

public class SqrtFunction extends BuiltinFunction {

    public SqrtFunction() {
        super("sqrt");
    }

    @Override
    public double run(double d) {
        return Math.sqrt(d);
    }
}
