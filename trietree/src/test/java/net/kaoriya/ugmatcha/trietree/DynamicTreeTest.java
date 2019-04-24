package net.kaoriya.ugmatcha.trietree;

import org.junit.Test;
import static org.junit.Assert.*;

public class DynamicTreeTest {

    static DynamicTree put(DynamicTree tree, int offset, String... keys) {
        int exp = offset;
        for (String k : keys) {
            int act = tree.put(k);
            assertEquals("", exp, act);
            exp++;
        }
        tree.fillFailure();
        return tree;
    }

    static DynamicTree put(String... keys) {
        return put(new DynamicTree(), 1, keys);
    }

    void scan(DynamicTree tree, String key, ReportT... exp) {
        ReportConsumer rc = new ReportConsumer();
        tree.scan(key, rc);
        ReportT[] act = rc.reports();
        assertArrayEquals("scan report doesn't match", exp, act);
    }

    @Test
    public void simpleSingle() {
        DynamicTree tree = put("1", "2", "3", "4", "5");
        scan(tree, "1", new ReportT(0, '1', 1));
        scan(tree, "2", new ReportT(0, '2', 2));
        scan(tree, "3", new ReportT(0, '3', 3));
        scan(tree, "4", new ReportT(0, '4', 4));
        scan(tree, "5", new ReportT(0, '5', 5));
        scan(tree, "6", new ReportT(0, '6'));
    }
 
    @Test
    public void simpleMultiple() {
        DynamicTree tree = put("1", "2", "3", "4", "5");
        scan(tree, "1234567890",
                new ReportT(0, '1', 1),
                new ReportT(1, '2', 2),
                new ReportT(2, '3', 3),
                new ReportT(3, '4', 4),
                new ReportT(4, '5', 5),
                new ReportT(5, '6'),
                new ReportT(6, '7'),
                new ReportT(7, '8'),
                new ReportT(8, '9'),
                new ReportT(9, '0'));
    }

    @Test
    public void basic() {
        DynamicTree tree = put("ab", "bc", "bab", "d", "abcde");
        scan(tree, "ab",
                new ReportT(0, 'a'),
                new ReportT(1, 'b', 1));
        scan(tree, "bc",
                new ReportT(0, 'b'),
                new ReportT(1, 'c', 2));
        scan(tree, "bab",
                new ReportT(0, 'b'),
                new ReportT(1, 'a'),
                new ReportT(2, 'b', 3, 1));
        scan(tree, "d",
                new ReportT(0, 'd', 4));
        scan(tree, "abcde",
                new ReportT(0, 'a'),
                new ReportT(1, 'b', 1),
                new ReportT(2, 'c', 2),
                new ReportT(3, 'd', 4),
                new ReportT(4, 'e', 5));
    }

    @Test
    public void count() {
        DynamicTree tree = put("ab", "bc", "bab", "d", "abcde");
        assertEquals(3, tree.root.countChild());
        assertEquals(11, tree.root.countAll());
    }
}
