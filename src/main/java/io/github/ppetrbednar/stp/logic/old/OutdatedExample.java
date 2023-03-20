package io.github.ppetrbednar.stp.logic.old;

import io.github.ppetrbednar.stp.logic.structures.SplayTree;

public final class OutdatedExample {
    private OutdatedExample() {
    }

    /*
    private void add(SplayTree.Node node) {
        splay(searchNearest(node.key));

        if (root == null) {
            root = node;
            index.put(node.key, node);
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
        index.put(node.key, node);
    }*/
}
