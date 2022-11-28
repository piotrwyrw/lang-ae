package net.aelang.ast;

import net.aelang.Tools;

public class InstantiationNode extends Node {

    private String type;
    private String id;

    public InstantiationNode(String type, String id) {
        this.type = type;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String dump(int lpad) {
        return Tools.leftpad(lpad) + "Instantiation of \"" + type + "\" as \"" + id + "\"\n";
    }

}
