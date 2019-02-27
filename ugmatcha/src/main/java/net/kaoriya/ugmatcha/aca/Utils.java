package net.kaoriya.ugmatcha.aca;

import net.kaoriya.ugmatcha.trie.Node;

public class Utils {

    public static <T> void fillFailure(
            Node<Data<T>> node,
            Node<Data<T>> parent,
            Node<Data<T>> root)
    {
        Data<T> data = node.getValue();
        if (data == null) {
            data = new Data<T>();
            node.setValue(data);
        }
        if (parent == root) {
            data.failure = root;
            return;
        }
        data.failure = getNextNode(getFailureNode(parent, root), root,
                node.label);
    }

    public static <T> Node<Data<T>> getNextNode(
            Node<Data<T>> node,
            Node<Data<T>> root,
            char ch)
    {
        while (true) {
            Node<Data<T>> next = node.get(ch);
            if (next != null) {
                return next;
            } else if (node == root) {
                return root;
            }
            node = getFailureNode(node, root);
        }
    }

    public static <T> Node<Data<T>> getFailureNode(
            Node<Data<T>> node,
            Node<Data<T>> root)
    {
        Node<Data<T>> next = node.getValue().failure;
        return next != null ? next : root;
    }
}
