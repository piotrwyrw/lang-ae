package net.piotrwyrw.ast;

import net.piotrwyrw.Tools;

public class VariableCallNode extends SolvableNode {

    private String id;

    public VariableCallNode(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String dump(int lpad) {
        return Tools.leftpad(lpad) + "Variable: " + id + '\n';
    }

}
