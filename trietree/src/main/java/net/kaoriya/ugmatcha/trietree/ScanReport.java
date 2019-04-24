package net.kaoriya.ugmatcha.trietree;

import java.util.Arrays;
import java.util.function.Consumer;

class ScanReport {
    final Consumer<ScanEvent> consumer;
    final ScanEvent ev = new ScanEvent();

    final int[] ids;
    int idx;

    ScanReport(Consumer<ScanEvent> consumer, int len) {
        this.consumer = consumer;
        this.ids = new int[len];
    }

    void reset(int index, char label) {
        ev.index = index;
        ev.label = label;
        idx = 0;
    }

    void add(int id) {
        ids[idx] = id;
        idx++;
    }

    void emit() {
        // setup ev.ids
        if (idx == 0) {
            ev.ids = null;
        } else {
            ev.ids = Arrays.copyOf(ids, idx);
        }
        consumer.accept(ev);
    }
}
