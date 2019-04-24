package net.kaoriya.ugmatcha.trietree;

import java.util.function.Consumer;

public class DynamicTree {

    public DynamicNode root = new DynamicNode((char)0);
    
    int lastEdgeID = 0;

    /**
     * put a key to tree and emit ID (index).
     */
    public int put(String k) {
        DynamicNode n = root;
        for (int i = 0; i < k.length(); i++) {
            char c = k.charAt(i);
            n = n.dig(c);
        }
        if (n.edgeID <= 0) {
            lastEdgeID++;
            n.edgeID = lastEdgeID;
        }
        return n.edgeID;
    }

    /**
     * get a node for the key.
     */
    public DynamicNode getNode(String k) {
        DynamicNode n = root;
        for (int i = 0; i < k.length(); i++) {
            char c = k.charAt(i);
            n = n.get(c);
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

    DynamicNode nextNode(DynamicNode curr, char label) {
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
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            DynamicNode next = nextNode(curr, c);
            sr.reset(i, c);
            for (DynamicNode n = next; n != null; n = n.failure) {
                if (n.edgeID > 0) {
                    sr.add(n.edgeID);
                }
            }
            sr.emit();
            curr = next;
        }
    }
}
