package io.github.ppetrbednar.stp.logic.structures;

import java.util.HashMap;

public class SplayTree<K extends Comparable<K>, V> implements ISplayTree<K, V> {
    private class Node implements Comparable<Node> {
        private final K key;
        private V value;

        private Node parent;
        private Node left;
        private Node right;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public boolean equals(Node obj) {
            return key.equals(obj.key);
        }

        @Override
        public int compareTo(Node o) {
            return key.compareTo(o.key);
        }

        public boolean isLeft(Node node) {
            return left != null && left.equals(node);
        }

        public boolean isRight(Node node) {
            return right != null && right.equals(node);
        }
    }

    private Node root;
    private HashMap<K, Node> index = new HashMap<>();
    private int counter = 1;

    public SplayTree() {
    }

    public SplayTree(Node root) {
        this.root = root;
    }

    @Override
    public V get(K key) {
        splay(key);
        return root.value;
    }

    @Override
    public void add(K key, V value) {

        Node node = new Node(key, value);
        if (root == null) {
            root = node;
            index.put(key, node);
            print(true);
            return;
        }

        splay(searchNearest(key));

        if (node.compareTo(root) > 0) {
            node.right = root.right;
            if (node.right != null) {
                node.right.parent = node;
            }
            root.right = null;
            root.parent = node;
            node.left = root;
        } else {
            node.left = root.left;
            if (node.left != null) {
                node.left.parent = node;
            }
            root.left = null;
            root.parent = node;
            node.right = root;
        }
        root = node;
        index.put(key, node);

        print(true);
    }

    private void print(boolean debug) {
        if (debug) {
            System.out.println("Cycle: " + counter++);
        }
        index.forEach((k, node) -> {
            System.out.println(node.key + " (" + (node.parent == null ? "null" : node.parent.key) + ", " + (node.left == null ? "null" : node.left.key) + ", " + (node.right == null ? "null" : node.right.key) + ")");
        });
        System.out.println();
    }

    @Override
    public V remove(K key) {
        splay(key);

        root.left.parent = null;
        root.right.parent = null;
        SplayTree<K, V> left = new SplayTree<>(root.left);
        SplayTree<K, V> right = new SplayTree<>(root.right);

        var union = union(left, right);

        V removed = root.value;
        root = union.root;
        index.remove(key);

        return removed;
    }

    private Node searchNearest(K key) {

        Node current = root;
        Node target = null;

        while (current != null) {
            target = current;
            current = key.compareTo(target.key) > 0 ? current.right : current.left;
        }
        return target;
    }

    private Node searchMax(Node current) {
        return current.right == null ? current.left : searchMax(current.right);
    }

    private void splay(K key) {
        splay(index.get(key));
    }

    private void splay(Node current) {

        while (current.parent != null) {

            if (current.parent.parent == null) {
                if (current.parent.isLeft(current)) {
                    rightRotation(current);
                  //  print(false);
                } else {
                    leftRotation(current);
                   // print(false);
                }
            } else {
                Node parent = current.parent;
                Node grandParent = parent.parent;

                if (parent.isLeft(current) && grandParent.isLeft(parent)) {
                    rightRotation(parent);
                   // print(false);
                    rightRotation(current);
                   // print(false);
                } else if (parent.isRight(current) && grandParent.isRight(parent)) {
                    leftRotation(parent);
                   // print(false);
                    leftRotation(current);
                   // print(false);
                } else if (parent.isLeft(current) && grandParent.isRight(parent)) {
                    rightRotation(parent);
                   // print(false);
                    leftRotation(current);
                   // print(false);
                } else if (parent.isRight(current) && grandParent.isLeft(parent)) {
                    leftRotation(parent);
                   // print(false);
                    rightRotation(current);
                    //print(false);
                } else {
                    System.err.println("err");
                }
            }
        }

        root = current;
    }

    private void leftRotation(Node current) {
        // Current (Parent, B, C) Parent (null, A, Current)
        Node parent = current.parent;

        // Current (Parent, B, C) Parent (null, A, B)
        parent.right = current.left;

        if (parent.right != null) {
            parent.right.parent = parent;
        }

        // Current (Parent, Parent, C) Parent (null, A, B)
        current.left = parent;

        // Current (null, Parent, C) Parent (null, A, B)
        current.parent = parent.parent;

        if (current.parent != null) {
            if (current.parent.left.equals(parent)) {
                current.parent.left = current;
                System.out.println("lP l");
            } else {
                current.parent.right = current;
                System.out.println("lP r");
            }
        }

        // Current (null, Parent, C) Parent (Current, A, B)
        parent.parent = current;
    }

    private void rightRotation(Node current) {
        // Current (Parent, A, B) Parent (null, Current, C)
        Node parent = current.parent;

        // Current (Parent, A, B) Parent (null, B, C)
        parent.left = current.right;

        if (parent.left != null) {
            parent.left.parent = parent;
        }

        // Current (Parent, A, Parent) Parent (null, B, C)
        current.right = parent;

        // Current (null, A, Parent) Parent (null, B, C)
        current.parent = parent.parent;

        if (current.parent != null) {
            if (current.parent.left.equals(parent)) {
                current.parent.left = current;
                System.out.println("rP l");
            } else {
                current.parent.right = current;
                System.out.println("rP r");
            }
        }

        // Current (null, A, Parent) Parent (Current, B, C)
        parent.parent = current;
    }

    private SplayTree<K, V> union(SplayTree<K, V> left, SplayTree<K, V> right) {
        left.splay(left.searchMax(left.root));
        left.root.right = right.root;
        return left;
    }
}
