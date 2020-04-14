
public class Consummer extends Thread {
	private SynchQueue<Integer> q;
	
	public Consummer(SynchQueue<Integer> q) {
		this.q = q;
	} 
	
	public void run() {
		while(true) {
			Integer i = q.Remove();
			System.out.println("Kich thuoc con lai: " + q.size() + "\nPhan tu moi bo ra la: " + i + "\n");
		}
	}
}
