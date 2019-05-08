package net.kaoriya.ugmatcha.normalizer;

import java.util.ArrayList;

import net.kaoriya.ugmatcha.trietree.DynamicTree;
import net.kaoriya.ugmatcha.trietree.StaticTree;
import net.kaoriya.ugmatcha.trietree.ScanEvent;

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

    public String normalize(String src) {
        final StringBuilder b = new StringBuilder(src.length());
        st.scan(src, (ScanEvent ev) -> {
            if (ev.ids == null) {
                b.append(ev.label);
                return;
            }
            b.append(words[ev.ids[0]]);
        });
        return b.toString();
    }
}
