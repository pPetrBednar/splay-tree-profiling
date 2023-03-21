package io.github.ppetrbednar.stp.logic.structures;

import java.util.*;

public class SplayTree<K extends Comparable<K>, V> implements ISplayTree<K, V> {
    private class Node implements Comparable<Node> {
        private final K key;
        private final V value;

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

    private class InorderKeyIterator implements Iterator<K> {

        private final Deque<Node> stack;
        private Node temp;

        public InorderKeyIterator(Node root) {
            stack = new ArrayDeque<>();
            temp = root;
            pushLeftSubTree(temp);
        }

        void pushLeftSubTree(Node temp) {
            while (temp != null) {
                stack.push(temp);
                temp = temp.left;
            }
        }

        @Override
        public K next() {
            temp = stack.pop();
            pushLeftSubTree(temp.right);
            return temp.key;
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }
    }

    private class InorderValueIterator implements Iterator<V> {

        private final Deque<Node> stack;
        private Node temp;

        public InorderValueIterator(Node root) {
            stack = new ArrayDeque<>();
            temp = root;
            pushLeftSubTree(temp);
        }

        void pushLeftSubTree(Node temp) {
            while (temp != null) {
                stack.push(temp);
                temp = temp.left;
            }
        }

        @Override
        public V next() {
            temp = stack.pop();
            pushLeftSubTree(temp.right);
            return temp.value;
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }
    }

    /**
     * Utility for generating formatted strings from binary trees. Code copied and optimized. Core functionality preserved.
     *
     * @author Eirik Halvard Sæther
     * @see <a href="https://github.com/eirikhalvard/binary-tree-to-string">Source repository</a>
     */
    private class TreeString {
        private int treeHeight;
        private int size;
        private int yHeight;
        private int xHeight;
        private int[] maxWordLength;
        private int[][] preorderHeights;
        private char[][] chars;

        private final Node root;

        public TreeString(Node root) {
            this.root = root;
        }

        private String solve() {
            this.treeHeight = height(root);
            this.size = size(root);
            if (size == 0) {
                return "";
            } else if (size == 1) {
                return "[" + root.value + "]";
            }

            this.maxWordLength = new int[treeHeight + 1];
            this.yHeight = computeYLength(root);
            this.xHeight = computeXLength();
            this.chars = new char[yHeight][xHeight];
            fillCharsWithWhitespace();
            traverseAndWrite();
            return charArrayToString();
        }

        private void fillCharsWithWhitespace() {
            for (int i = 0; i < yHeight; i++) {
                for (int j = 0; j < xHeight; j++) {
                    chars[i][j] = ' ';
                }
            }
        }

        private int height(Node n) {
            return n == null ? -1 : (1 + Math.max(height(n.left), height(n.right)));
        }

        private int size(Node n) {
            return n == null ? 0 : (1 + size(n.left) + size(n.right));
        }

        private void traverseAndWrite() {
            preorderHeights = new int[size][3];
            findPreorderHeights(root, 0);
            int rootStartY = preorderHeights[0][1] == 0 ? 0 : preorderHeights[0][1] + 1;

            traverseAndWrite(root, 0, rootStartY, 0, new int[]{0});
        }

        private void traverseAndWrite(Node n, int depth, int startY, int startX, int[] iterator) {
            iterator[0]++;
            String nodeString = valueString(n, depth);
            writeToCharArray(nodeString, startY, startX);
            startX += nodeString.length();
            if (n.left != null) {
                int leftsRightHeight = preorderHeights[iterator[0]][2];
                int leftsInnerHeight = leftsRightHeight == 0 ? 1 : leftsRightHeight + 2;
                int leftStartY = (startY - 1) - leftsInnerHeight;
                writeConnectingLines(startY, leftStartY, startX);
                traverseAndWrite(n.left, depth + 1, leftStartY, startX + 5, iterator);
            }

            if (n.right != null) {
                int rightsLeftHeight = preorderHeights[iterator[0]][1];
                int rightsInnerHeight = rightsLeftHeight == 0 ? 1 : rightsLeftHeight + 2;
                int rightStartY = startY + 1 + rightsInnerHeight;
                writeConnectingLines(startY, rightStartY, startX);
                traverseAndWrite(n.right, depth + 1, rightStartY, startX + 5, iterator);
            }
        }

        private void writeConnectingLines(int startY, int endY, int startX) {
            writeToCharArray("--+", startY, startX);
            if (endY - startY > 0) {
                for (int i = startY + 1; i < endY; i++) {
                    writeToCharArray("|", i, startX + 2);
                }
            } else {
                for (int i = endY + 1; i < startY; i++) {
                    writeToCharArray("|", i, startX + 2);
                }
            }
            writeToCharArray("+--", endY, startX + 2);

        }

        private int[] findPreorderHeights(Node n, int h) {
            if (n.left == null && n.right == null) {
                preorderHeights[h][0] = 1;
                return new int[]{preorderHeights[h][0], h};
            } else if (n.right == null) {
                int[] resultLeft = findPreorderHeights(n.left, h + 1);
                preorderHeights[h][0] = 2 + resultLeft[0];
                preorderHeights[h][1] = resultLeft[0];
                return new int[]{preorderHeights[h][0], resultLeft[1]};
            } else if (n.left == null) {
                int[] resultRight = findPreorderHeights(n.right, h + 1);
                preorderHeights[h][0] = 2 + resultRight[0];
                preorderHeights[h][2] = resultRight[0];
                return new int[]{preorderHeights[h][0], resultRight[1]};
            } else {
                int[] resultLeft = findPreorderHeights(n.left, h + 1);
                int[] resultRight = findPreorderHeights(n.right, resultLeft[1] + 1);
                preorderHeights[h][0] = 3 + resultLeft[0] + resultRight[0];
                preorderHeights[h][1] = resultLeft[0];
                preorderHeights[h][2] = resultRight[0];
                return new int[]{preorderHeights[h][0], resultRight[1]};
            }
        }

        private void writeToCharArray(String line, int y, int x) {
            if (line.length() + x >= xHeight) {
                return;
            }

            for (int i = 0; i < line.length(); i++) {
                chars[y][x + i] = line.charAt(i);
            }
        }

        private String charArrayToString() {
            StringBuilder result = new StringBuilder();
            for (char[] aChar : chars) {
                for (int j = 0; j < chars[0].length; j++) {
                    result.append(aChar[j]);
                }
                result.append("\n");
            }
            return result.substring(0, result.length() - 1); // remove last newline
        }

        private int computeYLength(Node n) {
            if (n.left == null && n.right == null) {
                return 1;
            } else if (n.right == null) {
                return 2 + computeYLength(n.left);
            } else if (n.left == null) {
                return 2 + computeYLength(n.right);
            } else {
                return 3 + computeYLength(n.left) + computeYLength(n.right);
            }
        }

        private int computeXLength() {
            computeMaxWordLength(root, 0);
            int sum = 0;
            for (int i = 0; i < treeHeight; i++) {
                sum += maxWordLength[i] + 7;
            }
            sum += maxWordLength[treeHeight] + 2;
            return sum;
        }

        private void computeMaxWordLength(Node n, int depth) {
            if (n == null) {
                return;
            }

            int nodeStringLength = n.toString().length();
            if (nodeStringLength > maxWordLength[depth]) {
                maxWordLength[depth] = nodeStringLength;
            }

            computeMaxWordLength(n.left, depth + 1);
            computeMaxWordLength(n.right, depth + 1);
        }

        private String valueString(Node n, int depth) {
            int totalCount = maxWordLength[depth] - n.toString().length();
            int leftCount = totalCount / 2;
            int rightCount = totalCount - leftCount;

            return "[" +
                    " ".repeat(leftCount) +
                    n.key +
                    " ".repeat(rightCount) +
                    "]";
        }
    }

    /**
     * Lightweight utility for generating formatted strings from binary trees. Code copied and optimized. Core functionality preserved.
     *
     * @author Yavuz Tas
     * @see <a href="https://www.baeldung.com/java-print-binary-tree-diagram">Source website</a>
     */
    private class TreeStringLight {
        private StringBuilder traversePreOrder(Node root) {

            if (root == null) {
                return new StringBuilder();
            }

            StringBuilder output = new StringBuilder();
            output.append(root.key);

            String pointerLeft = "└─-─";
            String pointerRight = root.left != null ? "├─+─" : "└─+─";

            traverseNodes(output, "", pointerRight, root.right, root.left != null);
            traverseNodes(output, "", pointerLeft, root.left, false);

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
                String pointerLeft = "└─-─";
                String pointerRight = (node.left != null) ? "├─+─" : "└─+─";

                traverseNodes(sb, paddingForBoth, pointerRight, node.right, node.left != null);
                traverseNodes(sb, paddingForBoth, pointerLeft, node.left, false);
            }
        }
    }

    private Node root;
    private int size;

    public SplayTree() {
    }

    private SplayTree(Node root) {
        this.root = root;
    }

    @Override
    public V get(K key) throws NoSuchElementException {
        Node node = getNode(key);

        if (node == null) {
            throw new NoSuchElementException();
        }

        splay(node);
        return root.value;
    }

    @Override
    public V getOrNull(K key) {
        try {
            return get(key);
        } catch (NoSuchElementException ex) {
            return null;
        }
    }

    @Override
    public boolean contains(K key) {
        return getOrNull(key) != null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(K key, V value) {
        Node node = new Node(key, value);
        if (root == null) {
            root = node;
            return;
        }

        Node nearest = searchNearest(node.key);

        if (node.compareTo(nearest) < 0) {
            nearest.left = node;
        } else {
            nearest.right = node;
        }

        node.parent = nearest;
        splay(node);
        size++;
    }

    @Override
    public V remove(K key) throws NoSuchElementException {

        if (!contains(key)) {
            throw new NoSuchElementException();
        }

        root.left.parent = null;
        root.right.parent = null;

        Node left = root.left;
        Node right = root.right;

        V removed = root.value;
        root = union(new SplayTree<>(left), new SplayTree<>(right)).root;
        size--;

        return removed;
    }

    @Override
    public V removeOrNull(K key) {
        try {
            return remove(key);
        } catch (NoSuchElementException ex) {
            return null;
        }
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public Iterator<K> inorderKeyIterator() {
        return new InorderKeyIterator(root);
    }

    @Override
    public Iterator<V> inorderValueIterator() {
        return new InorderValueIterator(root);
    }

    @Override
    public int depth() {
        return depth(root);
    }

    @Override
    public String print() {
        String treeString = new TreeString(root).solve();
        Deque<String> lines = new ArrayDeque<>(List.of(treeString.split("\n")));
        StringBuilder treeStringMirrored = new StringBuilder();

        while (!lines.isEmpty()) {
            treeStringMirrored.append(lines.removeLast().stripTrailing()).append("\n");
        }

        return treeStringMirrored.toString();
    }

    @Override
    public String printLight() {
        return new TreeStringLight().traversePreOrder(root).toString();
    }

    private Node getNode(K key) {
        Node current = root;

        while (current != null && !current.key.equals(key)) {
            current = key.compareTo(current.key) < 0 ? current.left : current.right;
        }
        return current;
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

    private int depth(Node node) {
        if (node == null) {
            return 0;
        }

        int leftDepth = depth(node.left);
        int rightDepth = depth(node.right);

        return leftDepth > rightDepth ? ++leftDepth : ++rightDepth;
    }
}
