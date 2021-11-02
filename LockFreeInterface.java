import java.util.concurrent.atomic.AtomicMarkableReference;

public interface LockFreeInterface<T> extends List<T> {
	class Node<T> {
		T item;
		int key;
		AtomicMarkableReference<Node<T>> next = 
			new AtomicMarkableReference<Node<T>>(null, false);

		Node(T i) {
			item = i;
			key = i.hashCode();
		}

		// for setting the key manually
		Node(T i, int k) {
			item = i;
			key = k;
		}
	}	
}
