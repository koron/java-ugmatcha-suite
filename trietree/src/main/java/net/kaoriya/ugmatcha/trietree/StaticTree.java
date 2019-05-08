package net.kaoriya.ugmatcha.trietree;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.function.Consumer;

public class StaticTree {

    public final char[] labels;
    public final int[] starts;
    public final int[] ends;
    public final int[] fails;
    public final int[] edges;

    class Filler {
        int z;
        boolean debug;

        Filler(boolean debug) {
            this.z = 1;
            this.debug = debug;
        }

        void fill(int x, DynamicNode dn, int i) {
            int start = 0;
            int end = 0;
            int n = dn.countChild();
            if (n > 0) {
                start = z;
                end = z + n;
                z = end;
            }
            labels[x] = dn.label;
            starts[x] = start;
            ends[x] = end;
            edges[x] = dn.edgeID;
            if (debug) {
                System.err.printf("x=%d i=%d n=%d start=%d end=%d dn.label='%c' dn.edgeID=%d\n", x, i, n, start, end, dn.label, dn.edgeID);
            }
            if (dn.child != null) {
                final int i2 = i + 1;
                final int[] y = new int[]{ start };
                DynamicNode.eachSiblings(dn.child, (DynamicNode dn2) -> {
                    fill(y[0], dn2, i2);
                    y[0]++;
                });
            }
        }
    }

    StaticTree(int n) {
        labels = new char[n];
        starts = new int[n];
        ends = new int[n];
        fails = new int[n];
        edges = new int[n];
    }

    public StaticTree(DynamicTree src) {
        this(src, false);
    }

    StaticTree(DynamicTree src, boolean debug) {
        this(src.root.countAll());
        Filler f = new Filler(debug);
        f.fill(0, src.root, 0);
        fillFailure(0);
    }

    void fillFailure(int x) {
        if (starts[x] == 0) {
            return;
        }
        for (int i = starts[x]; i < ends[x]; i++) {
            fails[i] = nextNode(fails[x], labels[i]);
            if (fails[i] == i) {
                fails[i] = 0;
            }
            fillFailure(i);
        }
    }

    int nextNode(int x, char label) {
        while (true) {
            int next = find(starts[x], ends[x], label);
            if (next >= 0) {
                return next;
            }
            if (x == 0) {
                return 0;
            }
            x = fails[x];
        }
    }

    int find(int start, int end, char label) {
        return Arrays.binarySearch(labels, start, end, label);
    }

    /**
     * scan a string and find keywords.
     */
    public void scan(String s, Consumer<ScanEvent> consumer) {
        ScanReport sr = new ScanReport(consumer, s.length());
        int curr = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int next = nextNode(curr, c);
            sr.reset(i, c);
            for (int n = next; n > 0; n = fails[n]) {
                if (edges[n] > 0) {
                    sr.add(edges[n]);
                }
            }
            sr.emit();
            curr = next;
        }
    }

    void dump(PrintStream out) {
        out.println(Arrays.toString(labels));
        out.println(Arrays.toString(starts));
        out.println(Arrays.toString(ends));
        out.println(Arrays.toString(fails));
        out.println(Arrays.toString(edges));
    }
}
