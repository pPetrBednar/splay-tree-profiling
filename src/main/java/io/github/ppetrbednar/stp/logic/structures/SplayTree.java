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
    }

    private Node root;
    private HashMap<K, Node> index;

    @Override
    public V get(K key) {
        splay(index.get(key));
        return root.value;
    }
/*
    private Node searchTree(Node node, K key) {
        if (node == null || key.equals(node.key)) {
            return node;
        }

        return searchTree(key.compareTo(node.key) < 0 ? node.left : node.right, key);
    }*/

    @Override
    public void add(K key, V value) {
        Node node = new Node(key, value);
        Node nearest = searchNearest(key);

        node.parent = nearest;
        if (nearest == null) {
            root = node;
        } else if (node.key.compareTo(nearest.key) < 0) {
            nearest.left = node;
        } else {
            nearest.right = node;
        }

        splay(node);
    }

    @Override
    public V remove(K key) {
        splay(index.get(key));

        root.left.parent = null;
        root.right.parent = null;

        Node left = root.left;
        Node right = root.right;

        V removed = root.value;
        root = union(left, right);
        index.remove(key);

        return removed;
    }

    private void splay(Node current) {
        while (current.parent != null) {
            if (current.parent.parent == null) {
                if (current == current.parent.left) {
                    rotateRight(current.parent);
                } else {
                    rotateLeft(current.parent);
                }
            } else if (current == current.parent.left && current.parent == current.parent.parent.left) {
                rotateRight(current.parent.parent);
                rotateRight(current.parent);
            } else if (current == current.parent.right && current.parent == current.parent.parent.right) {
                rotateLeft(current.parent.parent);
                rotateLeft(current.parent);
            } else if (current == current.parent.right && current.parent == current.parent.parent.left) {
                rotateLeft(current.parent);
                rotateRight(current.parent);
            } else {
                rotateRight(current.parent);
                rotateLeft(current.parent);
            }
        }
    }

    private void rotateLeft(Node parent) {
        Node current = parent.right;
        Node grandParent = parent.parent;

        parent.right = current.left;
        if (current.left != null) {
            current.left.parent = parent;
        }
        current.parent = grandParent;
        if (grandParent == null) {
            root = current;
        } else if (parent == grandParent.left) {
            grandParent.left = current;
        } else {
            grandParent.right = current;
        }

        current.left = parent;
        parent.parent = current;
    }

    private void rotateRight(Node parent) {
        Node current = parent.left;
        Node grandParent = parent.parent;

        parent.left = current.right;
        if (current.right != null) {
            current.right.parent = parent;
        }
        current.parent = grandParent;
        if (grandParent == null) {
            root = current;
        } else if (parent == grandParent.right) {
            grandParent.right = current;
        } else {
            grandParent.left = current;
        }

        current.right = parent;
        parent.parent = current;
    }

    private Node union(Node left, Node right) {
        if (left == null || right == null) {
            return left == null ? right : left;
        }

        Node root = max(left);
        splay(root);
        root.right = right;
        right.parent = root;
        return root;
    }

    public Node min(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    public Node max(Node node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }


    private Node searchNearest(K key) {
        Node current = this.root;
        Node target = null;

        while (current != null) {
            target = current;
            if (key.compareTo(current.key) < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return target;
    }
}
