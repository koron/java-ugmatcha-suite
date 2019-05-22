package net.kaoriya.ugmatcha.trietree;

import java.util.ArrayList;
import java.util.function.Consumer;

class ScanReport {
    final Consumer<ScanEvent> consumer;
    final ScanEvent ev = new ScanEvent();

    final ArrayList<NodeInfo> nodes = new ArrayList<>();
    int idx;

    ScanReport(Consumer<ScanEvent> consumer, int len) {
        this.consumer = consumer;
    }

    void reset(int index, int label) {
        ev.index = index;
        ev.label = label;
        nodes.clear();
    }

    void add(int id, int level) {
        nodes.add(new NodeInfo(id, level));
    }

    void emit() {
        ev.nodes = null;
        if (nodes.size() > 0) {
            ev.nodes = nodes.toArray(new NodeInfo[0]);
        }
        consumer.accept(ev);
    }
}
