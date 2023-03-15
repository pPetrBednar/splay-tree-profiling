package io.github.ppetrbednar.stp.logic.structures;

public interface ISplayTree<K extends Comparable<K>, V> {
    V get(K key);

    void add(K key, V value);

    V remove(K key);

    int depth();
}
