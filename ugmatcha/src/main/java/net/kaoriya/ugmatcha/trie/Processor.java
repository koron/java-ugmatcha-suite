package net.kaoriya.ugmatcha.trie;

/**
 * Node processing interface.
 */
public interface Processor<S> {

    /**
     * process a node.
     *
     * return true to continue, otherwise break.
     */
    boolean process(Node<S> node);
}
