package beam.MyGeometry.geometry.algorithm;

import java.util.ArrayList;

import beam.MyGeometry.collections.PointRing;
import beam.MyGeometry.geometry.Point2D;
import beam.MyGeometry.geometry.Polygon;
import beam.MyGeometry.geometry.Segment2D;


public class Triangulator {

	private Polygon p;
	
	boolean computed;
	private ArrayList<Integer> indices;

	public Triangulator(Polygon p) {
		indices = new ArrayList<Integer>();
		this.p = p;
		computed = false;
	}
	
	public ArrayList<Integer> getIndices(){
		compute();
		return indices;
	}

	public void compute() {
		if(computed)
			return;
		
		Polygon work = new Polygon(p);
		while (work.size() > 3) {
//			PointRing remainingPoints = work.points;
			PointRing remainingPoints = new PointRing(work.points);
			Point2D ear = null;
			for (Point2D point : remainingPoints) {
				if (isValidEar(point, work)) {
					ear = point;
					indices.add(p.points.indexOf(ear));
					indices.add(p.points.indexOf(work.points.getPrevious(ear)));
					indices.add(p.points.indexOf(work.points.getNext(ear)));
					
					break;
				}
			}
			if(ear == null) {
				throw new RuntimeException("Polygon has no more ear which is impossible.");
			}
			remainingPoints.remove(ear);
			work = new Polygon(remainingPoints);
		}

		if (work.size() == 3) {
			indices.add(p.points.indexOf(work.points.get(0)));
			indices.add(p.points.indexOf(work.points.get(2)));
			indices.add(p.points.indexOf(work.points.get(1)));
		}
		computed = true;
	}

	private boolean isValidEar(Point2D ear, Polygon polygon) {
		Segment2D diagonal = new Segment2D(polygon.points.getPrevious(ear), polygon.points.getNext(ear));
		return polygon.hasFullyInternalDiagonal(diagonal);
	}
}
