package net.aelang.ast;

import net.aelang.Tools;

public class VariableDeclarationNode extends Node {

    private String id;
    private SolvableNode def;

    public VariableDeclarationNode(String id, SolvableNode def) {
        this.id = id;
        this.def = def;
    }

    public String id() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SolvableNode def() {
        return def;
    }

    public void setDef(SolvableNode def) {
        this.def = def;
    }

    @Override
    public String dump(int lpad) {
        String s = Tools.leftpad(lpad) + "Variable declaration (" + id + "):\n";
        lpad++;
        s += Tools.leftpad(lpad) + "Default:\n";
        lpad++;
        s += def.dump(lpad);
        lpad--;
        lpad--;
        return s;
    }

}
