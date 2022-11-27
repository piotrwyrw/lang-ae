package net.aelang.ast;

import net.aelang.Tools;

public class ComplexAccessNode extends SolvableNode {

    private String instance;
    private String field;

    public ComplexAccessNode(String instance, String field) {
        this.instance = instance;
        this.field = field;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public String dump(int lpad) {
        return Tools.leftpad(lpad) + "Complex Access (Instance \"" + instance + "\", Field \"" + field + "\")\n";
    }
}
