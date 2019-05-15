package net.kaoriya.ugmatcha.normalizer;

import java.util.ArrayList;
import java.util.function.Consumer;

import net.kaoriya.ugmatcha.trietree.DynamicTree;
import net.kaoriya.ugmatcha.trietree.NodeInfo;
import net.kaoriya.ugmatcha.trietree.ScanEvent;
import net.kaoriya.ugmatcha.trietree.StaticTree;

public class Normalizer {

    static class Builder {
        DynamicTree dt;
        ArrayList<String> words;

        public Builder() {
            this(0);
        }

        public Builder(int initialCapacity) {
            dt = new DynamicTree();
            words = new ArrayList<>(initialCapacity + 1);
            words.add("");
        }

        public int put(String src, String dst) {
            int n = dt.put(src);
            if (n < words.size()) {
                words.set(n, dst);
                return n;
            }
            words.add(dst);
            return n;
        }

        public void putEach(String src, String dst) {
            putEach(src, 1, dst, 1);
        }

        public void putEach(String src, int srcUnit, String dst, int dstUnit) {
            if (src.length() * dstUnit != dst.length() * srcUnit) {
                throw new IllegalArgumentException("character length mismatch");
            }
            int n = dst.length() / dstUnit;
            int x = 0;
            int y = 0;
            for (int i = 0; i < n; i++) {
                put(src.substring(x, x+srcUnit), dst.substring(y, y+dstUnit));
                x += srcUnit;
                y += dstUnit;
            }
        }

        public void putMap(String dst, String... sources) {
            for (String src : sources) {
                put(src, dst);
            }
        }

        public Normalizer build() {
            StaticTree st = new StaticTree(dt);
            String[] ws = words.toArray(new String[0]);
            return new Normalizer(st, ws);
        }
    }

    StaticTree st;
    String[] words;

    Normalizer(StaticTree st, String[] words) {
        this.st = st;
        this.words = words;
    }

    static class Item {
        int id;
        int start;
        int last;
        Item(int id, int start, int last) {
            this.id = id;
            this.start = start;
            this.last = last;
        }
        Item(ScanEvent ev, int n) {
            id = ev.nodes[n].id;
            start = ev.index - ev.nodes[n].level;
            last = ev.index;
        }
    }

    class Context implements Consumer<ScanEvent> {
        boolean debug;

        final String src;
        final int[] ids;
        final int[] lasts;

        Context(String src, boolean debug) {
            this.debug = debug;
            this.src = src;
            int n = src.length();
            ids = new int[n];
            lasts = new int[n];
            for (int i = 0; i < n; i++) {
                lasts[i] = i;
            }
        }

        public void accept(ScanEvent ev) {
            if (ev.nodes == null) {
                return;
            }
            if (debug) {
                System.out.printf("accept: %s\n", ev.toString());
            }
            for (NodeInfo info : ev.nodes) {
                int start = ev.index - info.level + 1;
                if (debug) {
                    System.out.printf("  index=%d level=%d start=%d\n", ev.index, info.level, start);
                }
                int last = ev.index;
                if (ids[start] == 0 || last > lasts[start]) {
                    ids[start] = info.id;
                    lasts[start] = last;
                }
            }
        }

        String finish() {
            StringBuilder b = new StringBuilder();
            for (int i = 0; i < src.length(); i++) {
                int id = ids[i];
                if (id == 0) {
                    b.append(src.charAt(i));
                } else {
                    b.append(words[id]);
                }
                i = lasts[i];
            }
            return b.toString();
        }
    }

    public String normalize(String src) {
        return normalize(src, false);
    }

    public String normalize(String src, boolean debug) {
        Context ctx = new Context(src, debug);
        st.scan(src, ctx);
        return ctx.finish();
    }
}
