import java.util.concurrent.locks.ReentrantLock;

public interface LazyInterface<T> extends List<T> {
	class Node<T> {
		T item;
		int key;
		boolean marked = false;
		Node<T> next = null;
		ReentrantLock lock = new ReentrantLock();

		Node(T _item) {
			item = _item;
			key = _item.hashCode();
		}
		
		// for setting the key manually
		Node(T _item, int _key) { 
			item = _item;
			key = _key;
		}

		void lock() {
			lock.lock();
		}

		void unlock() {
			lock.unlock();
		}
	}
}
