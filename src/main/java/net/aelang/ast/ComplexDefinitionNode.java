package net.aelang.ast;

import net.aelang.Tools;

import java.util.ArrayList;

public class ComplexDefinitionNode extends Node {

    private String id;
    private ArrayList<String> fields;

    public ComplexDefinitionNode(String id, ArrayList<String> fields) {
        this.id = id;
        this.fields = fields;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getFields() {
        return fields;
    }

    public void setFields(ArrayList<String> fields) {
        this.fields = fields;
    }

    @Override
    public String dump(int lpad) {
        String s = Tools.leftpad(lpad) + "Complex Definition Node (" + id + "):\n";
        lpad ++;
        for (String field : fields) {
            s += Tools.leftpad(lpad) + field + '\n';
        }
        lpad --;
        return s;
    }

}
