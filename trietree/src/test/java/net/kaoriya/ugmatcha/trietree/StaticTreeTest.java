package net.kaoriya.ugmatcha.trietree;

import java.io.FileInputStream;

import org.junit.Test;
import static org.junit.Assert.*;

public class StaticTreeTest {

    static StaticTree put(String... keys) {
        DynamicTree dt = DynamicTreeTest.put(keys);
        return new StaticTree(dt);
    }

    static StaticTree putDbg( String... keys) {
        DynamicTree dt = DynamicTreeTest.put(keys);
        StaticTree st = new StaticTree(dt, true);
        st.dump(System.err);
        return st;
    }

    void scan(StaticTree tree, String key, ReportT... exp) {
        ReportConsumer rc = new ReportConsumer();
        tree.scan(key, rc);
        ReportT[] act = rc.reports();
        assertArrayEquals("scan report doesn't match", exp, act);
    }

    @Test
    public void simpleSingle() {
        StaticTree tree = put("1", "2", "3", "4", "5");
        scan(tree, "1", new ReportT(0, '1', 1, 1));
        scan(tree, "2", new ReportT(0, '2', 2, 1));
        scan(tree, "3", new ReportT(0, '3', 3, 1));
        scan(tree, "4", new ReportT(0, '4', 4, 1));
        scan(tree, "5", new ReportT(0, '5', 5, 1));
        scan(tree, "6", new ReportT(0, '6'));
    }

    @Test
    public void simpleMultiple() {
        StaticTree tree = put("1", "2", "3", "4", "5");
        scan(tree, "1234567890",
                new ReportT(0, '1', 1, 1),
                new ReportT(1, '2', 2, 1),
                new ReportT(2, '3', 3, 1),
                new ReportT(3, '4', 4, 1),
                new ReportT(4, '5', 5, 1),
                new ReportT(5, '6'),
                new ReportT(6, '7'),
                new ReportT(7, '8'),
                new ReportT(8, '9'),
                new ReportT(9, '0'));
    }

    @Test
    public void basic() {
        StaticTree tree = put("ab", "bc", "bab", "d", "abcde");
        scan(tree, "ab",
                new ReportT(0, 'a'),
                new ReportT(1, 'b', 1, 2));
        scan(tree, "bc",
                new ReportT(0, 'b'),
                new ReportT(1, 'c', 2, 2));
        scan(tree, "bab",
                new ReportT(0, 'b'),
                new ReportT(1, 'a'),
                new ReportT(2, 'b', 3, 3, 1, 2));
        scan(tree, "d",
                new ReportT(0, 'd', 4, 1));
        scan(tree, "abcde",
                new ReportT(0, 'a'),
                new ReportT(1, 'b', 1, 2),
                new ReportT(2, 'c', 2, 2),
                new ReportT(3, 'd', 4, 1),
                new ReportT(4, 'e', 5, 5));
    }

    //@Test
    public void load() throws Exception {
        StaticTree st;
        try (FileInputStream fs = new FileInputStream("../tmp/wikiwords.stt");) {
            st = StaticTree.load(fs);
        }
    }

    @Test
    public void longestPrefix() {
        StaticTree tree = put("ab", "bc", "bab", "d", "abcde");
        assertEquals(null, tree.longestPrefix("a"));
        assertEquals("ab", tree.longestPrefix("ab"));
        assertEquals("ab", tree.longestPrefix("abc"));
        assertEquals("abcde", tree.longestPrefix("abcdefg"));

        assertEquals(null, tree.longestPrefix("b"));
        assertEquals("bc", tree.longestPrefix("bc"));
        assertEquals("bc", tree.longestPrefix("bczzz"));
        assertEquals("bab", tree.longestPrefix("babbab"));

        assertEquals(null, tree.longestPrefix("bac"));
        assertEquals(null, tree.longestPrefix("bbc"));
        assertEquals(null, tree.longestPrefix("zzz"));
    }
}
