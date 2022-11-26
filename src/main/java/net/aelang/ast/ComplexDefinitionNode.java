package net.aelang.ast;

import net.aelang.Tools;

import java.util.ArrayList;

public class ComplexDefinitionNode extends Node {

    private String id;
    private ArrayList<String> variables;

    public ComplexDefinitionNode(String id, ArrayList<String> variables) {
        this.id = id;
        this.variables = variables;
    }

    public String id() {
        return id;
    }

    public ArrayList<String> variables() {
        return variables;
    }

    @Override
    public String dump(int lpad) {
        String s = Tools.leftpad(lpad) + "Complex Type Definition:\n";
        lpad++;
        s += Tools.leftpad(lpad) + "Variables:\n";
        lpad++;
        for (String variable : variables)
            s += Tools.leftpad(lpad) + variable + "\n";
        return s;
    }
}
