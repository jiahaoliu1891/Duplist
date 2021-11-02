import java.util.concurrent.locks.ReentrantLock;

public interface OptimisticInterface<T> extends List<T> {
	class Node<T> {
		T item;
		int key;
		Node<T> next = null;
		ReentrantLock lock = new ReentrantLock();

		Node(T i) {
			item = i;
			key = i.hashCode();
		}

		// for setting the key manually
		Node(T i, int k) { 
			item = i;
			key = k;
		}

		void lock() {
			lock.lock();
		}

		void unlock() {
			lock.unlock();
		}
	}
}
