package net.aelang.ast;

import net.aelang.Tools;

public class BinaryNode extends SolvableNode implements RecursiveNodePrint {

    private SolvableNode left;
    private SolvableNode right;
    private BinaryOperation op;

    public BinaryNode(SolvableNode left, SolvableNode right, BinaryOperation op) {
        this.left = left;
        this.right = right;
        this.op = op;
    }

    public SolvableNode left() {
        return left;
    }

    public void setLeft(SolvableNode left) {
        this.left = left;
    }

    public SolvableNode right() {
        return right;
    }

    public void setRight(SolvableNode right) {
        this.right = right;
    }

    public BinaryOperation op() {
        return op;
    }

    public void setOp(BinaryOperation op) {
        this.op = op;
    }

    @Override
    public String dump(int lpad) {
        String s = Tools.leftpad(lpad) + "Binary (" + op.toString() + "):\n";
        lpad++;
        s += Tools.leftpad(lpad) + "Left:\n";
        lpad++;
        s += left.dump(lpad);
        lpad--;
        s += Tools.leftpad(lpad) + "Right:\n";
        lpad++;
        s += right.dump(lpad);
        lpad--;
        lpad--;
        return s;
    }
}
