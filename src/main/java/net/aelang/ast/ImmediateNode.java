package net.aelang.ast;

import net.aelang.Tools;
import net.aelang.tokenizer.Token;
import net.aelang.tokenizer.TokenType;

public class ImmediateNode extends SolvableNode implements RecursiveNodePrint {

    private double value;

    public ImmediateNode(double value) {
        this.value = value;
    }

    public ImmediateNode(Token t) {
        if (t.type() == TokenType.INT_LIT) {
            int n = Integer.parseInt(t.val());
            this.value = (double) n;
        } else if (t.type() == TokenType.REAL_LIT) {
            value = Double.parseDouble(t.val());
        } else {
            throw new RuntimeException("Expected an integer literal or a real literal token.");
        }
    }

    public double value() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String dump(int lpad) {
        return Tools.leftpad(lpad) + " Immediate (" + value + ")\n";
    }
}
