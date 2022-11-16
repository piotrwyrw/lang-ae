package net.piotrwyrw.ast;

import net.piotrwyrw.Tools;

public class VariableDeclarationNode extends Node {

    private String id;
    private Node def;

    public VariableDeclarationNode(String id, Node def) {
        this.id = id;
        this.def = def;
    }

    public String id() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Node def() {
        return def;
    }

    public void setDef(Node def) {
        this.def = def;
    }

    @Override
    public String dump(int lpad) {
        String s = Tools.leftpad(lpad) + "Variable declaration (" + id + "):\n";
        lpad ++;
            s += Tools.leftpad(lpad) + "Default:\n";
            lpad ++;
                s += def.dump(lpad);
            lpad --;
        lpad --;
        return s;
    }

}
