package net.aelang.ast;

import net.aelang.Tools;

import java.util.List;

public class StatementSequenceNode extends Node {

    private List<Node> nodes;

    public StatementSequenceNode(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Node> nodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    @Override
    public String dump(int lpad) {
        String s = Tools.leftpad(lpad) + "Nested Statements:\n";
        lpad++;
        for (int i = 0; i < nodes.size(); i++) {
            s += nodes.get(i).dump(lpad);
        }
        lpad--;
        return s;
    }

}
