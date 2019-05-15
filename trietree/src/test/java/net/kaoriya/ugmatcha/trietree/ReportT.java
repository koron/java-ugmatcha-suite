package net.kaoriya.ugmatcha.trietree;

import java.util.Arrays;

public class ReportT {

    static class Node {
        int id;
        int level;
        Node(int id, int level) {
            this.id = id;
            this.level = level;
        }

        @Override
        public String toString() {
            StringBuilder s = new StringBuilder();
            s.append("Node{")
                .append("id=").append(id)
                .append(" level=").append(level)
                .append("}");
            return s.toString();
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
            Node other = (Node)o;
            if (id != other.id) {
                return false;
            }
            if (level != other.level) {
                return false;
            }
            return true;
        }
    }

    int index;
    char label;
    Node[] nodes;

    public ReportT(int index, char label, int... pairsOfIdAndLevel) {
        this.index = index;
        this.label = label;
        if (pairsOfIdAndLevel != null && pairsOfIdAndLevel.length >= 2) {
            this.nodes = new Node[pairsOfIdAndLevel.length / 2];
            for (int i = 0; i < pairsOfIdAndLevel.length - 1; i += 2) {
                this.nodes[i/2] = new Node(
                        pairsOfIdAndLevel[i],
                        pairsOfIdAndLevel[i+1]);
            }
        }
    }

    public ReportT(ScanEvent ev) {
        index = ev.index;
        label = ev.label;
        if (ev.nodes != null) {
            nodes = new Node[ev.nodes.length];
            for (int i = 0; i < ev.nodes.length; i++) {
                nodes[i] = new Node(ev.nodes[i].id, ev.nodes[i].level);
            }
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
        if (!Arrays.equals(nodes, other.nodes)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ReportT{index="+index+" label="+label+" nodes="+
            Arrays.toString(nodes)+"}";
    }

    // FIXME: should override hashCode() and toString().
}
