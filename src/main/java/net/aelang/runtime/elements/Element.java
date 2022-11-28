package net.aelang.runtime.elements;

public abstract class Element {

    private String id;

    public Element(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static String classNameOf(Object obj) {
        if (obj instanceof Complex) return "Complex Datatype";
        if (obj instanceof Function) return "Function";
        if (obj instanceof Instance) return "Instance variable";
        if (obj instanceof Variable) return "Variable";
        return "Unknown";
    }

}
