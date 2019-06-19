package net.kaoriya.ugmatcha.wikidict;

import java.util.ArrayList;
import java.util.function.Consumer;

import net.kaoriya.ugmatcha.trietree.NodeInfo;
import net.kaoriya.ugmatcha.trietree.ScanEvent;

public class Scanner implements Consumer<ScanEvent> {

    final int[] rs;
    final int[] ids;
    final int[] lasts;
    int ridx;

    public Scanner(String s) {
        int n = s.codePointCount(0, s.length());
        rs = new int[n];
        lasts = new int[n];
        ids = new int[n];
        for (int i = 0; i < n; i++) {
            rs[i] = s.codePointAt(i);
            lasts[i] = i;
        }
    }

    public void accept(ScanEvent ev) {
        int curr = ridx++;
        if (ev.nodes == null) {
            return;
        }
        for (NodeInfo ni : ev.nodes) {
            int start = curr - ni.level + 1;
            int last = curr;
            if (ids[start] == 0 || last > lasts[start]) {
                ids[start] = ni.id;
                lasts[start] = last;
            }
        }
    }

    public int[] finish() {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < rs.length; i++) {
            int id = ids[i];
            if (id != 0) {
                list.add(id);
            }
            i = lasts[i];
        }
        return list.stream().mapToInt(i -> i).toArray();
    }
}
