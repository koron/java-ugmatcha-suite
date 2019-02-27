package net.kaoriya.ugmatcha.aca;

import java.util.ArrayList;
import java.util.List;

import net.kaoriya.ugmatcha.trie.Node;
import net.kaoriya.ugmatcha.trie.Trie;

/**
 * Matcher implementation.
 */
public class Matcher<T> {

    final Trie<Data<T>> trie;

    /**
     * Create a matcher with trie.
     */
    public Matcher(Trie<Data<T>> trie) {
        this.trie = trie;
    }

    /**
     * match with text.
     *
     * each matches are reported by callback handler.
     * limitated first "max" matches.
     */
    public boolean match(String text, int max, Handler<T> handler) {
        if (max <= 0) {
            max = Integer.MAX_VALUE;
        }
        int count = 0;
        Node<Data<T>> root = this.trie.root();
        Node<Data<T>> curr = root;
        Node<Data<T>> target = null;
        for (int i = 0, L = text.length(); i < L; ++i) {
            char ch = text.charAt(i);
            curr = Utils.getNextNode(curr, root, ch);
            for (target = curr; target != root; ) {
                Data<T> data = target.getValue();
                if (data.key != null) {
                    ++count;
                    if (handler.matched(
                                i - data.key.length() + 1,
                                data.key,
                                data.value) == false
                            || count >= max) {
                        break;
                    }
                }
                target = data.failure;
            }
        }
        return count > 0;
    }

    /**
     * match with text.
     *
     * all matches are reported by callback handler.
     */
    public boolean match(String text,  Handler<T> handler) {
        return match(text, 0, handler);
    }

    /**
     * match with text.
     *
     * each matches are reported by list.
     * limitated first "max" matches.
     */
    public List<Match<T>> matchAll(String text, int max) {
        final ArrayList<Match<T>> list = new ArrayList<>();
        match(text, max, new Handler<T>() {
            public boolean matched(int index, String key, T value) {
                list.add(new Match<T>(index, key, value));
                return true;
            }
        });
        return list;
    }

    /**
     * match with text.
     *
     * all matches are reported by list.
     */
    public List<Match<T>> matchAll(String text) {
        return matchAll(text, 0);
    }
}
