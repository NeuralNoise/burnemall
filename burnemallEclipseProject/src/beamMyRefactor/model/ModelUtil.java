package beamMyRefactor.model;

import geometry.Point2D;

import java.util.Collection;

public class ModelUtil {

	public static Point2D nearest(Collection<Point2D> points, Point2D point) {
		Point2D res = null;
		double dist = Double.MAX_VALUE;
		for (Point2D p : points) {
			double d = p.getDistance(point);
			if (d<dist) {
				dist=d;
				res=p;
			}
		}
		return res;
	}

	public static Point2D farthest(Collection<Point2D> points, Point2D point) {
		Point2D res = null;
		double dist = 0;
		for (Point2D p : points) {
			double d = p.getDistance(point);
			if (d>dist) {
				dist=d;
				res=p;
			}
		}
		return res;
	}

}
