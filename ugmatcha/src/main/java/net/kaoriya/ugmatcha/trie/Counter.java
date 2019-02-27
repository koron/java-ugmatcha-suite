package net.kaoriya.ugmatcha.trie;

/**
 * Counting node processor
 */
public class Counter<S> implements Processor<S> {

    /**
     * count of node.
     */
    public int count = 0;

    @Override
    public boolean process(Node<S> node) {
        ++this.count;
        return true;
    }
}
