/*
 * Common functionality of the lists you'll be implementing
 */
public interface List<T> {
    boolean add(T item);
    boolean remove(T item);
    boolean contains(T item);
    boolean isEmpty();
}
