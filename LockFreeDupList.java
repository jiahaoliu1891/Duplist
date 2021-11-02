import java.util.concurrent.atomic.AtomicMarkableReference;

/*
 * Class implementing our LockFreeDupList
 */
public class LockFreeDupList<T> implements LockFreeInterface<T> {
	/*
	 * TODO: Add the necessary fields and implement all of the methods in this file
	 * to create a Lock-Free List that supports duplicate elements. Feel free to add
	 * whatever helper methods you deem necessary
	 */

	private Node<T> head;
	private Node<T> tail;

	LockFreeDupList() {
		Object max = Integer.MAX_VALUE;
		Object min = Integer.MIN_VALUE;
		this.head = new Node<T>(null, min.hashCode());
		this.tail = new Node<T>(null, max.hashCode());
		head.next.compareAndSet(null, tail, false, false);
	}

	public boolean isEmpty() {
		return head.next.getReference() == tail;
	}

	class Window {
		public Node<T> pred, curr;
		Window(Node<T> myPred, Node<T> myCurr) {
			pred = myPred;
			curr = myCurr;
		}
	}

	Window find(Node<T> head, int key) {
		Node<T> pred = null, curr = null, succ = null;
		boolean[] marked = {false};
		boolean snip;
		while (true) {
			pred = head;
			curr = pred.next.getReference();
			boolean flag = false;
			while (true) {
				succ = curr.next.get(marked);
				// delete the marked node
				while (marked[0]) {
					snip = pred.next.compareAndSet(curr, succ, false, false);
					if (!snip) {
						flag = true;
						break;
					}
					curr = succ;
					succ = curr.next.get(marked);
				}
				if (flag) break;
				if (curr.key >= key) return new Window(pred, curr);
				pred = curr;
				curr = succ;
			}
		}
	}

	public boolean add(T item) {
		int key = item.hashCode();
		while(true) {
			Window window = find(head, key);
			Node<T> pred = window.pred, curr = window.curr;
			Node<T> node = new Node<>(item);
			node.next.compareAndSet(null, curr, false, false);
			if (pred.next.compareAndSet(curr, node, false, false)) {
				return true;
			}
		}
	}
	// 1 ->   2  ->  2   -> 2
	// pred  cur    succ
	// what if: A.remove(2), B.remove(2)
	public boolean remove(T item) {
		int key = item.hashCode();
		boolean snip;
		while (true) {
			Window window = find(head, key);
			Node<T> pred = window.pred, curr = window.curr;
			if (curr.key != key) {
				return false;
			} else {
				Node<T> succ = curr.next.getReference();
				snip = curr.next.compareAndSet(succ, succ, false, true);
				if(!snip) 
					continue;
				pred.next.compareAndSet(curr, succ, false, false);
				return true;
			}
		}
	}

	public boolean contains(T item) {
		int key = item.hashCode();
		Node<T> cur = this.head;
		while (cur.key <= key) {
			if (cur.key == key && cur.next.isMarked() == false) {
				return true;
			}
			cur = cur.next.getReference();
		}
		return false;
	}
}
