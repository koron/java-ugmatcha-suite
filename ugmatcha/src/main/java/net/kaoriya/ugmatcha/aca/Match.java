package net.kaoriya.ugmatcha.aca;

/**
 * Matched data.
 */
public class Match<T> {

    /** start posion of this match. */
    public final int index;

    /** found key. */
    public final String key;

    /** corresponding data value with key. */
    public final T value;

    /**
     * Create a matched data.
     */
    public Match(int index, String key, T value) {
        this.index = index;
        this.key = key;
        this.value = value;
    }

    // to comparison in equals method.
    @SuppressWarnings("unchecked")
    private Match<T> asMatch(Object o) {
        return (Match<T>)o;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        Match<T> t = asMatch(o);
        if (t == null) {
            return false;
        }
        // compare member fields.
        if (this.index != t.index) {
            return false;
        }
        if (this.key != t.key && this.key != null &&
                this.key.equals(t.key)) {
            return false;
        }
        if (this.value != t.value && this.value != null &&
                !this.value.equals(t.value)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Match<>{")
            .append("index=").append(this.index)
            .append(" key=").append(this. key)
            .append(" value=").append(this.value)
            .append("}");
        return s.toString();
    }
}
