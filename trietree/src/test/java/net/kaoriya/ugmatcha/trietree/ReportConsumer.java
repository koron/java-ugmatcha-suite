package net.kaoriya.ugmatcha.trietree;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ReportConsumer implements Consumer<ScanEvent> {

    final ArrayList<ReportT> list = new ArrayList<>();

    public void accept(ScanEvent ev) {
        list.add(new ReportT(ev));
    }

    ReportT[] reports() {
        return list.toArray(new ReportT[0]);
    }
}
