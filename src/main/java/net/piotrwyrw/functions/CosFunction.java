package net.piotrwyrw.functions;

public class CosFunction extends BuiltinFunction {

    public CosFunction() {
        super("cos");
    }

    @Override
    public double run(double d) {
        return Math.cos(d);
    }
}
