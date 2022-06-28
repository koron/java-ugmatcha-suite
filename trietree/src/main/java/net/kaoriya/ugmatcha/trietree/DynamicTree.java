package net.kaoriya.ugmatcha.trietree;

import java.util.function.Consumer;

public class DynamicTree {

    public DynamicNode root = new DynamicNode(0);
    
    int lastEdgeID = 0;

    /**
     * put a key to tree and emit ID (index).
     */
    public int put(String k) {
        DynamicNode n = root;
        int C = k.codePointCount(0, k.length());
        for (int i = 0; i < C; i++) {
            int cp = k.codePointAt(i);
            n = n.dig(cp);
        }
        if (n.edgeID <= 0) {
            lastEdgeID++;
            n.edgeID = lastEdgeID;
        }
        n.level = C;
        return n.edgeID;
    }

    /**
     * get a node for the key.
     */
    public DynamicNode getNode(String k) {
        DynamicNode n = root;
        int C = k.codePointCount(0, k.length());
        for (int i = 0; i < C; i++) {
            int cp = k.codePointAt(i);
            n = n.get(cp);
            if (n == null) {
                return null;
            }
        }
        return n;
    }

    /**
     * fill all failure nodes in the tree.
     */
    public void fillFailure() {
        root.failure = root;
        fillFailure0(root);
        root.failure = null;
    }

    void fillFailure0(DynamicNode parent) {
        if (parent.child == null) {
            return;
        }
        DynamicNode pf = parent.failure;
        DynamicNode.eachSiblings(parent.child, (DynamicNode curr) -> {
            DynamicNode f = nextNode(pf, curr.label);
            if (f == curr) {
                f = root;
            }
            curr.failure = f;
            fillFailure0(curr);
        });
    }

    DynamicNode nextNode(DynamicNode curr, int label) {
        while (true) {
            DynamicNode next = curr.get(label);
            if (next != null) {
                return next;
            }
            if (curr == root) {
                return root;
            }
            curr = curr.failure;
            if (curr == null) {
                curr = root;
            }
        }
    }

    /**
     * scan a string and find keywords.
     */
    public void scan(String s, Consumer<ScanEvent> consumer) {
        DynamicNode curr = root;
        ScanReport sr = new ScanReport(consumer, s.length());
        int C = s.codePointCount(0, s.length());
        for (int i = 0; i < C; i++) {
            int cp = s.codePointAt(i);
            DynamicNode next = nextNode(curr, cp);
            sr.reset(i, cp);
            for (DynamicNode n = next; n != null; n = n.failure) {
                if (n.edgeID > 0) {
                    sr.add(n.edgeID, n.level);
                }
            }
            sr.emit();
            curr = next;
        }
    }

    /**
     * longestPrefix finds a longest prefix against given s string.
     */
    public String longestPrefix(String s) {
        DynamicNode last = null;
        int ilast = 0;
        DynamicNode curr = root;
        int C = s.codePointCount(0, s.length());
        for (int i = 0; i < C; i++) {
            int cp = s.codePointAt(i);
            DynamicNode next = curr.get(cp);
            if (next == null) {
                break;
            }
            if (next.edgeID > 0) {
                last = root;
                ilast = i + 1;
            }
            curr = next;
        }
        if (last == null) {
            return null;
        }
        return s.substring(0, ilast);
    }
}
