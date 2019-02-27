package net.kaoriya.ugmatcha.trie;

import java.util.ArrayList;
import java.util.List;

/**
 * Node implements a node of ternary trie-tree.
 */
public class Node<T> {
    public final char label;
    private Node<T> firstChild;
    private Node<T> low;
    private Node<T> high;
    private T value;

    public Node(char label) {
        this.label = label;
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Node<T> get(char k) {
        Node<T> curr = this.firstChild;
        while (curr != null) {
            if (k == curr.label) {
                return curr;
            } else if (k < curr.label) {
                curr = curr.low;
            } else {
                curr = curr.high;
            }
        }
        return null;
    }

    public Node<T> dig(char k) {
        Node<T> curr = this.firstChild;
        if (curr == null) {
            this.firstChild = new Node<T>(k);
            return this.firstChild;
        }

        while (true) {
            if (k == curr.label) {
                return curr;
            } else if (k < curr.label) {
                if (curr.low == null) {
                    curr.low = new Node<T>(k);
                    return curr.low;
                }
                curr = curr.low;
            } else {
                if (curr.high == null) {
                    curr.high = new Node<T>(k);
                    return curr.high;
                }
                curr = curr.high;
            }
        }
    }

    public int size() {
        if (this.firstChild == null) {
            return 0;
        }
        Counter<T> counter = new Counter<T>();
        each(counter);
        return counter.count;
    }

    public void removeAll() {
        this.firstChild = null;
    }

    public void each(Processor<T> processor) {
        eachNode(processor, this.firstChild);
    }

    private boolean eachNode(Processor<T> processor, Node<T> node) {
        if (node != null) {
            if (!eachNode(processor, node.low) || !processor.process(node)
                    || !eachNode(processor, node.high)) {
                return false;
            }
        }
        return true;
    }

    private List<Node<T>> children() {
        final ArrayList<Node<T>> array
            = new ArrayList<Node<T>>();
        each(new Processor<T>() {
            public boolean process(Node<T> node) {
                array.add(node);
                return true;
            }
        });
        return array;
    }

    public void balance() {
        if (this.firstChild == null) {
            return;
        }
        List<Node<T>> children = children();
        for (Node<T> child : children) {
            child.low = null;
            child.high = null;
        }
        this.firstChild = balance(children, 0, children.size());
        return;
    }

    private Node<T> balance(List<Node<T>> nodes, int start, int end) {
        int count = end - start;
        if (count <= 0) {
            return null;
        } else if (count == 1) {
            return nodes.get(start);
        } else if (count == 2) {
            Node<T> s = nodes.get(start);
            s.high = nodes.get(start + 1);
            return s;
        } else {
            int mid = (start + end) / 2;
            Node<T> n = nodes.get(mid);
            n.low = balance(nodes, start, mid);
            n.high = balance(nodes, mid + 1, end);
            return n;
        }
    }
}
