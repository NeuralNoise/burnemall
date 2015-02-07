package beam.MyGeometry.geometry.algorithm;

import beam.MyGeometry.collections.PointRing;
import beam.MyGeometry.geometry.Polygon;
import beam.MyGeometry.geometry.Segment2D;
import beam.MyGeometry.math.Angle;

public class SegmentExtruder {

	private Segment2D segment;
	public Polygon extrusion;
	private double extrudeWidth;
	
	public SegmentExtruder(Segment2D segment, double width) {
		this.segment = segment;
		extrudeWidth = width;
		compute();
	}

	private void compute() {
		double normal = segment.getAngle() - Angle.RIGHT;
		Segment2D projection = (Segment2D) segment.getTranslation(normal, extrudeWidth);
		
		PointRing ring = new PointRing();
		ring.add(segment.getEnd());
		ring.add(segment.getStart());
		ring.add(projection.getStart());
		ring.add(projection.getEnd());
		extrusion = new Polygon(ring);
	}
	
	
}
