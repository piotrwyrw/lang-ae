package net.aelang.runtime;

import net.aelang.Pair;
import net.aelang.ast.ComplexAccessNode;
import net.aelang.runtime.elements.Element;
import net.aelang.runtime.elements.Function;
import net.aelang.runtime.elements.Instance;
import net.aelang.runtime.elements.Variable;

import java.util.HashMap;

public class PersistentEnvironment {

    private static PersistentEnvironment env = null;
    private HashMap<String, Element> elements;

    public PersistentEnvironment() {
        this.elements = new HashMap<>();
    }

    public boolean putElement(Element el) {
        if (elements.containsKey(el.getId()))
            return false;
        elements.put(el.getId(), el);
        return true;
    }

    public Pair<Class<?>, Element> findElement(String id) {
        if (!elements.containsKey(id))
            return null;
        Element elmnt = elements.get(id);
        return new Pair<>(elmnt.getClass(), elmnt);
    }

    public boolean deleteElement(String id) {
        if (!elements.containsKey(id))
            return false;
        elements.remove(id);
        return true;
    }

    public static PersistentEnvironment getInstance() {
        if (env == null)
            env = new PersistentEnvironment();
        return env;
    }

}
