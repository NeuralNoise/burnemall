package beamMyRefactor.util;

public class StopWatch {

	private long t1;
	String name;
	
	public StopWatch(String name) {
		this.name=name;
		t1 = System.nanoTime();
	}

	public StopWatch() {
		t1 = System.nanoTime();
	}
	
	@Override
	public String toString() {
		long t2 = System.nanoTime();
		if (name==null)
			return "Temps " + (t2-t1)/1000000.0 + "ms";
		else
			return name + " : " + (t2-t1)/1000000.0 + "ms";
	}

	public Long time() {
		long t2 = System.nanoTime();
		return t2-t1;
	}
	
}
