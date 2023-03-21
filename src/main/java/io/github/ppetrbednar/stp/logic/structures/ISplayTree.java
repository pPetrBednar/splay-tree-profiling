package io.github.ppetrbednar.stp.logic.structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

public interface ISplayTree<K extends Comparable<K>, V> {
    V get(K key) throws NoSuchElementException;

    V getOrNull(K key);

    boolean contains(K key);

    int size();

    void add(K key, V value);

    V remove(K key) throws NoSuchElementException;

    V removeOrNull(K key);

    void clear();

    Iterator<K> inorderKeyIterator();

    Iterator<V> inorderValueIterator();

    int depth();

    String print();

    String printLight();
}
