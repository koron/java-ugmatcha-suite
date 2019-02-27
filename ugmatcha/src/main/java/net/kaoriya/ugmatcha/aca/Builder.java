package net.kaoriya.ugmatcha.aca;

import net.kaoriya.ugmatcha.trie.Node;
import net.kaoriya.ugmatcha.trie.Processor;
import net.kaoriya.ugmatcha.trie.Trie;

public class Builder<T> {

    final Trie<Data<T>> trie = new Trie<>();

    public Builder() {
        // nothing to do.
    }

    /**
     * Adds a keyword with value.
     */
    public void add(String key, T value) {
        this.trie.put(key, new Data<T>(key, value));
    }

    /**
     * Get a finalized tree as usable as aho-corasick algorithm.
     */
    public Trie<Data<T>> compile() {
        this.trie.balance();
        final Node<Data<T>> root = this.trie.root();
        root.setValue(new Data<T>(root));
        this.trie.eachWidth(new Processor<Data<T>>() {
            public boolean process(final Node<Data<T>> parent) {
                parent.each(new Processor<Data<T>>() {
                    public boolean process(Node<Data<T>> node) {
                        Utils.fillFailure(node, parent, root);
                        return true;
                    }
                });
                return true;
            }
        });
        return this.trie;
    }
}
