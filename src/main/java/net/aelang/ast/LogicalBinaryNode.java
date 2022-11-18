package net.aelang.ast;

import net.aelang.Tools;

public class LogicalBinaryNode extends Node {

    private Node left;
    private Node right;
    private LogicalOperation op;

    public LogicalBinaryNode(Node left, Node right, LogicalOperation op) {
        this.left = left;
        this.right = right;
        this.op = op;
    }

    public Node left() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node right() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public LogicalOperation op() {
        return op;
    }

    public void setOp(LogicalOperation op) {
        this.op = op;
    }

    @Override
    public String dump(int lpad) {
        String s = Tools.leftpad(lpad) + "Logical binary operation (" + op.toString() + "):\n";
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
