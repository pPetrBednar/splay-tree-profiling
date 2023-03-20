package io.github.ppetrbednar.stp.logic.structures;

import java.util.Iterator;

public interface ISplayTree<K extends Comparable<K>, V> {
    V get(K key);

    boolean contains(K key);

    int size();

    void add(K key, V value);

    V remove(K key);

    void clear();

    Iterator<K> inorderKeyIterator();

    Iterator<V> inorderValueIterator();

    int depth();

    String print();

    String printLight();
}
