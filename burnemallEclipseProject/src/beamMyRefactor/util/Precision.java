package beamMyRefactor.util;

public class Precision {
	public static final double APPROX = 0.0001;

	public static boolean areEquals(double p, double q) {
		return p > q - APPROX && p < q + APPROX;
	} 
}
