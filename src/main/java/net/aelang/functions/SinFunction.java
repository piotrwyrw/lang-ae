package net.aelang.functions;

public class SinFunction extends BuiltinFunction {

    public SinFunction() {
        super("sin");
    }

    @Override
    public double run(double d) {
        return Math.sin(d);
    }

}
