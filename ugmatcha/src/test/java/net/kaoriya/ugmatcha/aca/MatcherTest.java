package net.kaoriya.ugmatcha.aca;

import org.junit.Test;
import static org.junit.Assert.*;

import net.kaoriya.ugmatcha.trie.Node;

public class MatcherTest {

    private Matcher<Integer> newTestMatcher1() {
        Builder<Integer> b = new Builder<>();
        b.add("ab", 2);
        b.add("bc", 4);
        b.add("bab", 6);
        b.add("d", 7);
        b.add("abcde", 10);
        return new Matcher(b.compile());
    }

    private void checkNode(
            Node<Data<Integer>> node,
            int size,
            Data<Integer> expected)
    {
        assertEquals(size, node.size());
        Data<Integer> actually = node.getValue();
        assertEquals(expected, actually);
    }

    @Test
    public void treeStructure() {
        Matcher<Integer> m = newTestMatcher1();
        Node<Data<Integer>> r = m.trie.root();
        Data<Integer> invalid = new Data<Integer>(r);
        checkNode(r, 3, invalid);
        Node<Data<Integer>> n1 = r.get('a');
        checkNode(n1, 1, invalid);
        Node<Data<Integer>> n3 = r.get('b');
        checkNode(n3, 2, invalid);
        Node<Data<Integer>> n7 = r.get('d');
        checkNode(n7, 0, invalid);
        Node<Data<Integer>> n2 = n1.get('b');
        checkNode(n2, 1, new Data<Integer>("ab", 2, n3));
        Node<Data<Integer>> n4 = n3.get('c');
        checkNode(n4, 0, new Data<Integer>("bc", 4, r));
        Node<Data<Integer>> n5 = n3.get('a');
        checkNode(n5, 1, new Data<Integer>(n1));
        Node<Data<Integer>> n8 = n2.get('c');
        checkNode(n8, 1, new Data<Integer>(n4));
        Node<Data<Integer>> n6 = n5.get('b');
        checkNode(n6, 0, new Data<Integer>("bab", 6, n2));
        Node<Data<Integer>> n9 = n8.get('d');
        checkNode(n9, 1, new Data<Integer>(n7));
        Node<Data<Integer>> n10 = n9.get('e');
        checkNode(n10, 0, new Data<Integer>("abcde", 10, r));
    }

    @Test
    public void matchResults() {
        Matcher<Integer> m = newTestMatcher1();

        Match<Integer>[] results = m.matchAll("abcde").toArray(new Match[0]);
        assertArrayEquals(new Match[] {
            new Match<Integer>(0, "ab", 2),
            new Match<Integer>(1, "bc", 4),
            new Match<Integer>(3, "d", 7),
            new Match<Integer>(0, "abcde", 10),
        }, results);
    }

    private Matcher<Integer> newTestMatcher2() {
        Builder<Integer> b = new Builder<>();
        b.add("あい", 2);
        b.add("いう", 4);
        b.add("いあい", 6);
        b.add("え", 7);
        b.add("あいうえお", 10);
        return new Matcher(b.compile());
    }

    @Test
    public void matchResults2() {
        Matcher<Integer> m = newTestMatcher2();

        Match<Integer>[] results = m.matchAll("あいうえお").toArray(new Match[0]);
        assertArrayEquals(new Match[] {
            new Match<Integer>(0, "あい", 2),
            new Match<Integer>(1, "いう", 4),
            new Match<Integer>(3, "え", 7),
            new Match<Integer>(0, "あいうえお", 10),
        }, results);
    }
}
