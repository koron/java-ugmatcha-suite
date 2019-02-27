package net.kaoriya.ugmatcha.aca;

import net.kaoriya.ugmatcha.trie.Node;

/**
 * Node data for Aho-Corasick argorithm.
 */
class Data<T> {

    String key;
    T value;
    Node<Data<T>> failure;

    Data(String key, T value, Node<Data<T>> failure) {
        this.key = key;
        this.value = value;
        this.failure = failure;
    }

    Data(String key, T value) {
        this(key, value, null);
    }

    Data(Node<Data<T>> failure) {
        this(null, null, failure);
    }

    Data() {
        this(null, null, null);
    }

    // to comparison in equals method.
    @SuppressWarnings("unchecked")
    private Data<T> asData(Object o) {
        return (Data<T>)o;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        Data<T> t = asData(o);
        if (t == null) {
            return false;
        }
        // compare member fields.
        if (this.key != t.key && this.key != null &&
                !this.key.equals(t.key)) {
            return false;
                }
        if (this.value != t.value && this.value != null &&
                !this.value.equals(t.value)) {
            return false;
                }
        if (this.failure != t.failure) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Data<>{")
            .append("key=").append(this.key)
            .append(" value=").append(this.value)
            .append(" failure=").append(this.failure)
            .append("}");
        return s.toString();
    }
}
