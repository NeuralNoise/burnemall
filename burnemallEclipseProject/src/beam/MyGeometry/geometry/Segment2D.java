package beam.MyGeometry.geometry;

import java.util.ArrayList;

import beam.MyGeometry.geometry.algorithm.Intersector;

public class Segment2D extends Line2D {

	public Segment2D(Point2D p0, Point2D p1) {
		super(p0, p1);
	}

	public Segment2D(Segment2D other) {
		super(other);
	}

	/**
	 * Depending on the type of the "other" argument, this methods return the unique Segment-to-Line or
	 * Segment-to-Segment intersection.
	 * 
	 * It considers collinear segments not intersecting.
	 * 
	 * @return the intersection point if there is one and only one, or "null" if there is no intersection, or more than
	 *         one.
	 */
	public Point2D getUniqueIntersection(Line2D other) {
		Intersector intersector = new Intersector(p0, p1, other.p0, other.p1);

		if (other instanceof Segment2D && !intersector.hasUniqueSegmentToSegmentIntersection())
			return null;
		if (!intersector.hasUniqueSegmentToLineIntersection())
			return null;
		return intersector.getUniqueIntersection();
	}

	/**
	 * Depending on the type of the "other" argument, this methods return any of the Segment-to-Line or
	 * Segment-to-Segment intersections.
	 * 
	 * It considers collinear segments intersecting.
	 * 
	 * @return the unique intersection point, or the middle of the intersection zone, or "null" if there is no
	 *         intersection.
	 */
	public Point2D getAnyIntersection(Line2D other) {
		Intersector intersector = new Intersector(p0, p1, other.p0, other.p1);

		if (other instanceof Segment2D && !intersector.hasSegmentToSegmentIntersection())
			return null;
		if (!intersector.hasSegmentToLineIntersection())
			return null;
		return intersector.getAnyIntersection();
	}

	/**
	 * Depending on the type of the "other" argument, this methods says if a unique Segment-to-Line or
	 * Segment-to-Segment exists.
	 * 
	 * It considers collinear segments not intersecting.
	 * 
	 * @return true if a unique intersection exists or else in the other case.
	 */
	public boolean intersectAtSinglePoint(Line2D other) {
		Intersector intersector = new Intersector(p0, p1, other.p0, other.p1);

		if (other instanceof Segment2D && !intersector.hasSegmentToSegmentIntersection())
			return intersector.hasUniqueSegmentToSegmentIntersection();
		else
			return intersector.hasUniqueSegmentToLineIntersection();

	}

	/**
	 * Depending on the type of the "other" argument, this methods says if a unique Segment-to-Line or
	 * Segment-to-Segment exists.
	 * 
	 * It considers collinear segments intersecting.
	 * 
	 * @return true if a unique intersection exists or else in the other case.
	 */
	public boolean intersect(Line2D other) {
		Intersector intersector = new Intersector(p0, p1, other.p0, other.p1);

		if (other instanceof Segment2D)
			return intersector.hasSegmentToSegmentIntersection();
		else
			return intersector.hasSegmentToLineIntersection();

	}

	public Segment2D getMirror() {
		return new Segment2D(p1, p0);
	}

	public double getLength() {
		return p0.getDistance(p1);
	}

	public Line2D getBiscector() {
		Point2D middlePoint = getMiddle();
		Point2D directionPoint = middlePoint.getTranslation(getAngle() + Math.PI / 2, 1);
		return new Line2D(middlePoint, directionPoint);
	}

	public Point2D getMiddle() {
		return new Point2D(p0.getAddition(p1.getSubtraction(p0).getDivision(2)));
	}

	@Override
	public String toString() {
		return "Segment " + p0 + " - " + p1;
	}

	public Line2D getLine() {
		return new Line2D(p0, p1);
	}

	public Point2D toVector() {
		return p1.getSubtraction(p0);
	}

	public boolean hasPoint(Point2D point) {
		return p0.equals(point) || p1.equals(point);
	}

	@Override
	public Segment2D getTranslation(double angle, double distance) {
		return new Segment2D(p0.getTranslation(angle, distance), p1.getTranslation(angle, distance));
	}

	public double getOrdinate() {
		return p0.y - getSlope() * p0.x;
	}

	public double getShortestDistance(Point2D p) {
		double sqrLength = (p0.x - p1.x) * (p0.x - p1.x) + (p0.y - p1.y) * (p0.y - p1.y); // just to avoid a sqrt
		if (sqrLength == 0)
			return p0.getDistance(p);
		double t = p.getSubtraction(p0).getDotProduct(p1.getSubtraction(p0)) / sqrLength;
		if (t <= 0)
			return p0.getDistance(p);
		if (t >= 1)
			return p1.getDistance(p);
		Point2D proj = p0.getAddition(p1.getSubtraction(p0).getMult(t));
		return p.getDistance(proj);
	}

	public Line2D getRotation(double angle) {
		return new Line2D(p0, p1.getRotation(angle, p0));
	}
        
        /**
         * look for the intersection between segment and a circle
         * 
         * two points are computed : the first is always the nearest of p0 (start of the segment
         * 
         * if tangent, the intersection is the first.
         * @param center
         * @param radius
         * @return 
         */
        public ArrayList<Point2D> getIntersectionsWithCircle(Point2D center, double radius) {
            ArrayList<Point2D> res = new ArrayList<>();
            // d = direction vector of the segment
            Point2D d = p1.getSubtraction(p0);
            // f = vector from center to ray start
            Point2D f = p0.getSubtraction(center);
            
            double a = d.getDotProduct(d);
            double b = 2*f.getDotProduct(d);
            double c = f.getDotProduct(f)-radius*radius;

            double discriminant = b*b-4*a*c;
            if( discriminant < 0 ){
              // no intersection
                res.add(null);
                res.add(null);
                return res;
            }else{
              // ray didn't totally miss sphere,
              // so there is a solution to
              // the equation.

              discriminant = Math.sqrt(discriminant);

              // either solution may be on or off the ray so need to test both
              // t1 is always the smaller value, because BOTH discriminant and
              // a are nonnegative.
              double t1=(-b-discriminant)/(2*a);
              double t2=(-b+discriminant)/(2*a);

              // 3x HIT cases:
              //          -o->             --|-->  |            |  --|->
              // Impale(t1 hit,t2 hit), Poke(t1 hit,t2>1), ExitWound(t1<0, t2 hit), 

              // 3x MISS cases:
              //       ->  o                     o ->              | -> |
              // FallShort (t1>1,t2>1), Past (t1<0,t2<0), CompletelyInside(t1<0, t2>1)

              if(t1 >= 0 && t1 <= 1){
                // t1 is the intersection, and it's closer than t2
                // (since t1 uses -b - discriminant)
                // Impale, Poke
                  res.add(p0.getTranslation(p0.getAngle(), t1));
              } else
                  res.add(null);

              // here t1 didn't intersect so we are either started
              // inside the sphere or completely past it
              if(t2 >= 0 && t2 <= 1) {
                // ExitWound
                res.add(p0.getTranslation(p0.getAngle(), t2));
              } else
                  res.add(null);
              // no intn: FallShort, Past, CompletelyInside
            }
            return res;
        }

    public boolean isHorinzontal() {
        return p0.y == p1.y;
    }
    public boolean isVertical() {
        return p0.x == p1.x;
    }

    public boolean containsProjected(Point2D p) {
        double sign1 = p.getSubtraction(p0).getDotProduct(p1.getSubtraction(p0));
        double sign2 = p.getSubtraction(p1).getDotProduct(p1.getSubtraction(p0));
        if(sign1*sign2 > 0)
            return false;
        return true;
    }

}
