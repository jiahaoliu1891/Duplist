/*
 * Class implementing our LazyDupList
 */
public class LazyDupList<T> implements LazyInterface<T> {
	/*
	 * TODO: Add the necessary fields and implement all the methods given in this
	 * file to create a Lazy List that supports duplicate elements
	 */

	private Node<T> head;
	private Node<T> tail;

	LazyDupList() {
		// initialization
		Object max = Integer.MAX_VALUE;
		Object min = Integer.MIN_VALUE;
		this.head = new Node<T>(null, min.hashCode());
		this.tail = new Node<T>(null, max.hashCode());
		head.next = tail;
	}

	public boolean isEmpty() {
		return head.next == tail;
	}

	public boolean add(T item) {
		int key = item.hashCode();
		Object max = Integer.MAX_VALUE;
		Object min = Integer.MIN_VALUE;
		if (key ==  max.hashCode() || key == min.hashCode()) {
			return false;
		}

		while (true) {
			Node<T> pred = head;
			Node<T> curr = pred.next;
			while (curr.key < key) {
				pred = curr;
				curr = curr.next;
			}
			try {
				pred.lock();
				try {
					curr.lock();
					if (validate(pred, curr)) {
						Node<T> node = new Node<>(item);
						node.next = curr;
						pred.next = node;
						return true;
					}
				} finally {
					curr.unlock();
				}
			} finally {
				pred.unlock();
			}

		}
	}

	public boolean remove(T item) {
		int key = item.hashCode();
		
		Object max = Integer.MAX_VALUE;
		Object min = Integer.MIN_VALUE;
		if (key ==  max.hashCode() || key == min.hashCode()) {
			return false;
		}

		while (true) {
			Node<T> pred = head;
			Node<T> curr = head.next;
			while (curr.key < key) {
				pred = curr;
				curr = curr.next;
			}
			pred.lock();
			try {
				curr.lock();
				try {
					if (validate(pred, curr)) {
						if (curr.key == key) {
							curr.marked = true;
							pred.next = curr.next;
							return true;
						} else {
							return false;
						}

					}
				} finally {
					curr.unlock();
				}
			} finally {
				pred.unlock();
			}
		}

	}

	public boolean contains(T item) {
		int key = item.hashCode();
		Node<T> cur = this.head;
		while (cur.key <= key) {
			if (cur.key == key && cur.marked == false) {
				return true;
			}
			cur = cur.next;
		}
		return false;
	}

	/* Validate is unique only to the Optimistic and Lazy Lists */
	private boolean validate(Node<T> pred, Node<T> curr) {
		return !pred.marked && !curr.marked && pred.next == curr;
	}
}
