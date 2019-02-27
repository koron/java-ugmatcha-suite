package net.kaoriya.ugmatcha.aca;

/**
 * matched data handler.
 */
public interface Handler<T> {

    /**
     * Called when find pattern.
     *
     * @param index Position where pattern starts.
     * @param pattern Found pattern.
     * @param value Value mapped to that pattern.
     */
    boolean matched(int index, String key, T value);

}
