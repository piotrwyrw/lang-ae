package net.piotrwyrw;

public class Pair<K, V> {

    private K key;
    private V val;

    public Pair(K key, V val) {
        this.key = key;
        this.val = val;
    }

    public K key() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V val() {
        return val;
    }

    public void setVal(V val) {
        this.val = val;
    }
}
