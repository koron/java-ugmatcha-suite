package net.kaoriya.ugmatcha.trietree;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.function.Consumer;

public class StaticTree {

    public final int[] labels;
    public final int[] starts;
    public final int[] ends;
    public final int[] fails;
    public final int[] edges;

    public /*final*/ int[] levels;

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
            if (dn.edgeID > 0) {
                levels[dn.edgeID - 1] = dn.level;
            }
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

    StaticTree(int n, int edgeNum) {
        labels = new int[n];
        starts = new int[n];
        ends = new int[n];
        fails = new int[n];
        edges = new int[n];
        levels = new int[edgeNum];
    }

    public StaticTree(DynamicTree src) {
        this(src, false);
    }

    StaticTree(DynamicTree src, boolean debug) {
        this(src.root.countAll(), src.lastEdgeID);
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

    int nextNode(int x, int label) {
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

    int find(int start, int end, int label) {
        return Arrays.binarySearch(labels, start, end, label);
    }

    /**
     * scan a string and find keywords.
     */
    public void scan(String s, Consumer<ScanEvent> consumer) {
        ScanReport sr = new ScanReport(consumer, s.length());
        int curr = 0;
        int C = s.codePointCount(0, s.length());
        for (int i = 0; i < C; i++) {
            int cp = s.codePointAt(i);
            int next = nextNode(curr, cp);
            sr.reset(i, cp);
            for (int n = next; n > 0; n = fails[n]) {
                int id = edges[n];
                if (id > 0) {
                    sr.add(id, levels[id - 1]);
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
        out.println(Arrays.toString(levels));
    }

    // load a StaticTree from InputStream.
    public static StaticTree load(InputStream is) throws IOException {
        Reader r = new Reader(is);

        // read nodes.
        long n = r.readLong();
        if (n > Integer.MAX_VALUE || n < 0) {
            throw new IOException(String.format("too large tree for Java: %d", n));
        }
        StaticTree st = new StaticTree((int)n, 0);
        for (int i = 0; i < (int)n; i++) {
            st.labels[i] = r.readRune();
            st.starts[i] = r.readInt();
            st.ends[i] = r.readInt();
            st.fails[i] = r.readInt();
            st.edges[i] = r.readInt();
        }

        // read levels.
        long n2 = r.readLong();
        if (n2 > Integer.MAX_VALUE || n2 < 0) {
            throw new IOException(String.format("too large levels for Java: %d", n2));
        }
        st.levels = new int[(int)n2];
        for (int i = 0; i < (int)n2; i++) {
            st.levels[i] = r.readInt();
        }

        return st;
    }

    /**
     * longestPrefix finds a longest prefix against given s string.
     */
    public String longestPrefix(String s) {
        int last = -1;
        int ilast = 0;
        int curr = 0;
        int C = s.codePointCount(0, s.length());
        for (int i = 0; i < C; i++) {
            int cp = s.codePointAt(i);
            int next = find(starts[curr], ends[curr], cp);
            if (next < 0) {
                break;
            }
            if (edges[next] > 0) {
                last = next;
                ilast = i + 1;
            }
            curr = next;
        }
        if (last < 0) {
            return null;
        }
        return s.substring(0, ilast);
    }
}
