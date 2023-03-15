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

        @Override
        public int compareTo(Node o) {
            return key.compareTo(o.key);
        }
    }

    private Node root;
    private HashMap<K, Node> index;

    public SplayTree() {
        index = new HashMap<>();
    }

    private SplayTree(Node root) {
        this.root = root;
    }

    @Override
    public V get(K key) {
        splay(index.get(key));
        return root.value;
    }

    @Override
    public void add(K key, V value) {
        Node node = new Node(key, value);
        splay(searchNearest(key));

        if (root == null) {
            root = node;
            index.put(key, node);
            return;
        }

        root.parent = node;
        if (node.compareTo(root) < 0) {
            node.left = root.left;
            if (node.left != null) {
                node.left.parent = node;
            }
            root.left = null;
            node.right = root;
        } else {
            node.right = root.right;
            if (node.right != null) {
                node.right.parent = node;
            }
            root.right = null;
            node.left = root;
        }

        root = node;
        index.put(key, node);
    }

    @Override
    public V remove(K key) {
        splay(index.get(key));

        root.left.parent = null;
        root.right.parent = null;

        Node left = root.left;
        Node right = root.right;

        V removed = root.value;
        root = union(new SplayTree<>(left), new SplayTree<>(right)).root;
        index.remove(key);

        return removed;
    }

    @Override
    public int depth() {
        return depth(root);
    }

    private void splay(Node current) {

        if (current == null) {
            return;
        }

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
            } else if (current == current.parent.left && current.parent == current.parent.parent.right) {
                rotateRight(current.parent);
                rotateLeft(current.parent);
            } else {
                System.out.println("err");
            }
        }
        root = current;
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

    private SplayTree<K, V> union(SplayTree<K, V> left, SplayTree<K, V> right) {
        if (left.root == null || right.root == null) {
            return left.root == null ? right : left;
        }

        left.splay(left.max(left.root));
        left.root.right = right.root;
        right.root.parent = left.root;

        return left;
    }

    private Node max(Node node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }


    private Node searchNearest(K key) {
        Node current = root;
        Node target = null;

        while (current != null) {
            target = current;
            current = key.compareTo(current.key) < 0 ? current.left : current.right;
        }
        return target;
    }

    private StringBuilder traversePreOrder(Node root) {

        if (root == null) {
            return new StringBuilder();
        }

        StringBuilder output = new StringBuilder();
        output.append(root.key);

        String pointerRight = "└──";
        String pointerLeft = root.right != null ? "├──" : "└──";

        traverseNodes(output, "", pointerLeft, root.left, root.right != null);
        traverseNodes(output, "", pointerRight, root.right, false);

        return output;
    }

    private void traverseNodes(StringBuilder sb, String padding, String pointer, Node node, boolean hasRightSibling) {
        if (node != null) {
            sb.append("\n");
            sb.append(padding);
            sb.append(pointer);
            sb.append(node.key);

            StringBuilder paddingBuilder = new StringBuilder(padding);
            if (hasRightSibling) {
                paddingBuilder.append("│  ");
            } else {
                paddingBuilder.append("   ");
            }

            String paddingForBoth = paddingBuilder.toString();
            String pointerRight = "└──";
            String pointerLeft = (node.right != null) ? "├──" : "└──";

            traverseNodes(sb, paddingForBoth, pointerLeft, node.left, node.right != null);
            traverseNodes(sb, paddingForBoth, pointerRight, node.right, false);
        }
    }

    public StringBuilder visualize() {
        return traversePreOrder(root);
    }

    private int depth(Node node) {
        if (node == null) {
            return 0;
        }

        int leftDepth = depth(node.left);
        int rightDepth = depth(node.right);

        return leftDepth > rightDepth ? ++leftDepth : ++rightDepth;

    }
}
