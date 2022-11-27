package net.aelang.runtime.elements;

public abstract class Element {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Element(String id) {
        this.id = id;
    }

}
