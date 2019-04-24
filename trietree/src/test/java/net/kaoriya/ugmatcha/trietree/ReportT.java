package net.kaoriya.ugmatcha.trietree;

import java.util.Arrays;

public class ReportT {
    int index;
    char label;
    int[] ids;

    public ReportT(int index, char label, int... ids) {
        this.index = index;
        this.label = label;
        if (ids != null && ids.length > 0) {
            this.ids = ids;
        }
    }

    public ReportT(ScanEvent ev) {
        index = ev.index;
        label = ev.label;
        if (ev.ids != null && ev.ids.length > 0) {
            ids = ev.ids;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        ReportT other = (ReportT)o;
        if (index != other.index) {
            return false;
        }
        if (label != other.label) {
            return false;
        }
        if (!Arrays.equals(ids, other.ids)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ReportT{index="+index+" label="+label+" ids="+Arrays.toString(ids)+"}";
    }

    // FIXME: should override hashCode() and toString().
}
