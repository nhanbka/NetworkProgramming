
public class Producer extends Thread{
	private SynchQueue<Integer> q;
	private int curr;
	
	public Producer (SynchQueue<Integer> q) {
		this.q = q;
		curr = 1;
	}
	
	public void run() {
		while(true) {
			Integer i = new Integer(curr);
			q.Add(i);
			curr++;
		}
	}
}
