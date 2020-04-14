import java.util.LinkedList;

public class SynchQueue<T> {
	public LinkedList<T> l;
	int limit;

	SynchQueue(int limit) {
		l = new LinkedList<T>();
		this.limit = limit;
	}

	public synchronized void Add(T element) {
		
		while (l.size() == limit) {
			try {
				wait();
			} catch (Exception e) {}
		} 
		l.addLast(element);
		if(l.size() == 1)
			notify();
	}

	public synchronized T Remove() {
		if(l.size() == limit/2)
			notify();
		while (l.size() == 0) {
			try {
				wait();
			} catch (Exception e) {}
		}
		T x = l.removeFirst();
		return x;
	}
	
	public int size() {
		return l.size();
	}
}
