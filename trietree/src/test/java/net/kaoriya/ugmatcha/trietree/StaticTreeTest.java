package net.kaoriya.ugmatcha.trietree;

import org.junit.Test;
import static org.junit.Assert.*;

public class StaticTreeTest {

    static StaticTree put(String... keys) {
        DynamicTree dt = DynamicTreeTest.put(keys);
        return new StaticTree(dt);
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
        scan(tree, "1", new ReportT(0, '1', 1));
        scan(tree, "2", new ReportT(0, '2', 2));
        scan(tree, "3", new ReportT(0, '3', 3));
        scan(tree, "4", new ReportT(0, '4', 4));
        scan(tree, "5", new ReportT(0, '5', 5));
        scan(tree, "6", new ReportT(0, '6'));
    }

    @Test
    public void simpleMultiple() {
        StaticTree tree = put("1", "2", "3", "4", "5");
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
        StaticTree tree = put("ab", "bc", "bab", "d", "abcde");
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
}
