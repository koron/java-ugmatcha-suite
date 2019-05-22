package net.kaoriya.ugmatcha.trietree;

import java.util.function.Consumer;

public class DynamicNode {

    public int label;

    public DynamicNode low;
    public DynamicNode high;
    public DynamicNode child;

    public int edgeID;
    public int level;

    public DynamicNode failure;

    DynamicNode(int label) {
        this.label = label;
    }

    /**
     * dig a child node for label:c.
     */
    DynamicNode dig(int c) {
        if (child == null) {
            child = new DynamicNode(c);
            return child;
        }
        DynamicNode p = child;
        while (true) {
            if (c == p.label) {
                return p;
            }
            if (c < p.label) {
                if (p.low == null) {
                    p.low = new DynamicNode(c);
                    return p.low;
                }
                p = p.low;
            } else {
                if (p.high == null) {
                    p.high = new DynamicNode(c);
                    return p.high;
                }
                p = p.high;
            }
        }
    }

    public DynamicNode get(int c) {
        for (DynamicNode p = child; p != null;) {
            if (c == p.label) {
                return p;
            }
            if (c < p.label) {
                p = p.low;
            } else {
                p = p.high;
            }
        }
        return null;
    }

    static void eachSiblings(DynamicNode n, Consumer<DynamicNode> proc) {
        if (n == null) {
            return;
        }
        eachSiblings(n.low, proc);
        proc.accept(n);
        eachSiblings(n.high, proc);
    }

    /**
     * count child nodes.
     */
    public int countChild() {
        final int[] c = new int[]{0};
        eachSiblings(this.child, (DynamicNode n) -> { c[0]++; });
        return c[0];
    }

    /**
     * count all descended nodes.
     */
    public int countAll() {
        final int[] c = new int[]{1};
        eachSiblings(this.child, (DynamicNode n) -> {
            c[0] += n.countAll();
        });
        return c[0];
    }
}
