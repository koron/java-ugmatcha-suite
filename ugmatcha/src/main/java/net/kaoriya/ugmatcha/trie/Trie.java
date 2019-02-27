package net.kaoriya.ugmatcha.trie;

import java.util.LinkedList;

/**
 * Ternary Trie implementation.
 */
public class Trie<T> {

    private final Node<T> root = new Node<>('\u0000');

    /**
     * Get root node.
     */
    public Node<T> root() {
        return this.root;
    }

    /**
     * Get a node for a key, otherwise null.
     */
    public Node<T> get(String key) {
        Node<T> node = this.root;
        for (char ch : key.toCharArray()) {
            node = node.get(ch);
            if (node == null) {
                break;
            }
        }
        return node;
    }

    /**
     * Put a data with key, return a node for the key.
     */
    public Node<T> put(String key, T value) {
        Node<T> node = this.root;
        for (char ch : key.toCharArray()) {
            node = node.dig(ch);
        }
        node.setValue(value);
        return node;
    }

    /**
     * Size returns count of nodes in this trie.
     */
    public int size() {
        Counter<T> counter = new Counter<>();
        eachDepth(counter);
        return counter.count;
    }

    /**
     * re-balance all nodes.
     */
    public void balance() {
        eachDepth(new Processor<T>() {
            public boolean process(Node<T> node) {
                node.balance();
                return true;
            }
        });
    }

    /**
     * iterate all nodes by depth.
     */
    public void eachDepth(final Processor<T> proc) {
        this.root.each(new Processor<T>() {
            public boolean process(Node<T> node) {
                node.each(this);
                return proc.process(node);
            }
        });
    }

    /**
     * iterate all nodes by width.
     */
    public void eachWidth(Processor<T> proc) {
        final LinkedList<Node<T>> queue = new LinkedList<>();
        queue.add(this.root);
        Processor<T> myproc = new Processor<T>() {
            public boolean process(Node<T> node) {
                queue.add(node);
                return true;
            }
        };
        while (queue.size() != 0) {
            Node<T> node = queue.poll();
            if (!proc.process(node)) {
                break;
            }
            node.each(myproc);
        }
    }
}
