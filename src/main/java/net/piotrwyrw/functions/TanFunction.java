package net.piotrwyrw.functions;

public class TanFunction extends BuiltinFunction {

    public TanFunction() {
        super("tan");
    }

    @Override
    public double run(double d) {
        return Math.tan(d);
    }
}
