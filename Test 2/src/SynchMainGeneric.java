
public class SynchMainGeneric {
	
	public static int limit = 1000;
	public static Producer p;
	public static Consummer c;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SynchQueue<Integer> q = new SynchQueue<Integer>(limit);
		Producer p = new Producer(q);
		Consummer c = new Consummer(q);
		p.start();
		c.start();
	}

}
