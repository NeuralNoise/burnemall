package beam.MyGeometry.collections;

import java.util.Collection;

import beam.MyGeometry.geometry.Point2D;


public class PointRing extends Ring<Point2D>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PointRing(Collection<Point2D> col) {
		super(col);
	}
	
	public PointRing() {
		
	}
}
