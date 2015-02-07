package beam.MyGeometry.geometry.algorithm;

import beam.MyGeometry.geometry.Line2D;
import beam.MyGeometry.geometry.Point2D;
import beam.MyGeometry.math.Angle;
import beam.MyGeometry.math.Precision;



/**
 * Intersector computes the intersection between two lines defined by four vectors given in parameters. It works in two
 * times : - first, it find a result of the intersection and gives some boolean methods to check it, - then it computes
 * the single intersection point or intersection zone if (and only if) it's requested.
 * 
 * Intersector makes no difference between lines and segments in its intersection calculation, so you must check the
 * booleans before asking for the intersection.
 * 
 * For example, if you gives two segments and ask for the single intersection point, it will return the intersection
 * point of the lines defined by the segments. you must check the intersectSegmentToSegment() method to be sure.
 * 
 * Please note that the intersection calculation is computed with division and is not absolutely precise.
 * 
 * For information, the Angle.getTurn(a, b, c) method uses determinant to check the turning sense. * @author Beno�t
 * Dumas
 * 
 * @author Beno�t Dumas
 * @version $Id$
 */
public class Intersector {

	enum Type {UNKNOWN, PARALLEL, INTERSECT, COLLINEAR, INVALID}

	private Point2D p0, p1, q0, q1;

	private Point2D intersection, intersectionStart, intersectionEnd;
	private Type result;
	private boolean inPLimits;
	private boolean inQLimits;

	/**
	 * Constructs a new Intersector with four line's vectors.
	 */
	public Intersector(Point2D p0, Point2D p1, Point2D q0, Point2D q1) {
		intersection = null;
		intersectionStart = null;
		intersectionEnd = null;
		inPLimits = false;
		inQLimits = false;
		this.p0 = p0;
		this.p1 = p1;
		this.q0 = q0;
		this.q1 = q1;

		// We compute the result only if the access methods are called.
		// Doing that, we may launch some very fast tests for segment to segment intersection
		// and increase Intersector performance.
		result = Type.UNKNOWN;
	}

	/**
	 * Constructs a new Intersector with two lines.
	 */
	public Intersector(Line2D p, Line2D q) {
		this(p.getStart(), p.getEnd(), q.getStart(), q.getEnd());
		
	}	
	
	public boolean hasLineToLineIntersection() {
		if (result == Type.UNKNOWN)
			result = computeIntersectionResult();
		return result == Type.INTERSECT || result == Type.COLLINEAR;
	}

	public boolean hasUniqueLineToLineIntersection() {
		if (result == Type.UNKNOWN)
			result = computeIntersectionResult();
		return result == Type.INTERSECT;
	}

	public boolean hasSegmentToLineIntersection() {
		if (result == Type.UNKNOWN)
			result = computeIntersectionResult();
		return result == Type.INTERSECT && inPLimits || result == Type.COLLINEAR && inPLimits;
	}

	public boolean hasUniqueSegmentToLineIntersection() {
		if (result == Type.UNKNOWN)
			result = computeIntersectionResult();
		return result == Type.INTERSECT && inPLimits;
	}

	public boolean hasSegmentToSegmentIntersection() {
		if (result == Type.UNKNOWN)
			result = computeIntersectionResult();
		return result == Type.INTERSECT && inPLimits && inQLimits || result == Type.COLLINEAR && inPLimits && inQLimits;
	}

	public boolean hasUniqueSegmentToSegmentIntersection() {
		if (result == Type.UNKNOWN)
			result = computeIntersectionResult();
		return result == Type.INTERSECT && inPLimits && inQLimits;
	}

	public boolean isCollinear() {
		if (result == Type.UNKNOWN)
			result = computeIntersectionResult();
		return result == Type.COLLINEAR;
	}

	/**
	 * This method returns an intersection point only if there is only one.
	 * 
	 * @return single intersection point, or null if none or more than one exists.
	 */
	public Point2D getUniqueIntersection() {
		if (result == Type.UNKNOWN)
			result = computeIntersectionResult();
		if (result == Type.COLLINEAR || result == Type.PARALLEL)
			return null;

		if (intersection == null)
			computeIntersectionPoint();
		return intersection;
	}

	/**
	 * This method returns any intersection point found.
	 * 
	 * @return a single intersection point, or the middle point of the intersection zone, or null if none exists.
	 */
	public Point2D getAnyIntersection() {
		if (result == Type.UNKNOWN)
			result = computeIntersectionResult();
		if (intersection == null)
			computeIntersectionPoint();
		return intersection;
	}

	/**
	 * This method returns the start of the intersection zone. If the lines are intersecting on a single point, it
	 * returns this point.
	 * 
	 * @return the end point of the intersection zone, or the single intersection point, or null if none exists.
	 */
	public Point2D getIntersectionZoneStart() {
		if (result == Type.UNKNOWN)
			result = computeIntersectionResult();
		if (intersection == null)
			computeIntersectionPoint();
		if (intersectionStart == null)
			return intersection;
		return intersectionStart;
	}

	/**
	 * This method returns the end of the intersection zone. If the lines are intersecting on a single point, it returns
	 * this point.
	 * 
	 * @return the end point of the intersection zone, or the single intersection point, or null if none exists.
	 */
	public Point2D getIntersectionZoneEnd() {
		if (result == Type.UNKNOWN)
			result = computeIntersectionResult();
		if (intersection == null)
			computeIntersectionPoint();
		if (intersectionEnd == null)
			return intersection;
		return intersectionEnd;
	}

	private Type computeIntersectionResult() {
		// for each end point, find the side of the other line.
		// if two end points lie on opposite sides of the other line, then the lines are crossing.
		// if all end points lie on opposite sides, then the segments are crossing.

		double pAngle = p1.getSubtraction(p0).getAngle();
		double qAngle = q1.getSubtraction(q0).getAngle();
		
		double Pq0 = Angle.getTurn(p0, p1, q0);
		double Pq1 = Angle.getTurn(p0, p1, q1);

		double Qp0 = Angle.getTurn(q0, q1, p0);
		double Qp1 = Angle.getTurn(q0, q1, p1);

		// check if all turn have none angle. In this case, lines are collinear.
		if (Pq0 == Angle.NONE && Pq1 == Angle.NONE || Qp0 == Angle.NONE && Qp1 == Angle.NONE) {
			// at this point, we know that lines are collinear.
			// we must check if they overlap for segments intersection
			if (q0.getDistance(p0) <= p0.getDistance(p1) && q0.getDistance(p1) <= p0.getDistance(p1)) {
				// then q0 is in P limits and p0 or p1 is in Q limits
				// TODO this check is no sufficient
				inPLimits = true;
				inQLimits = true;
			}
			return Type.COLLINEAR;
		}
		// check if q0 and q1 lie around P AND p0 and p1 lie around Q.
		// in this case, the two segments intersect
		else if (Pq0 * Pq1 <= 0 && Qp0 * Qp1 <= 0) {
			// else if(Pq0 <= 0 && Pq1 >= 0 && Qp0 <= 0 && Qp1 >= 0 ||
			// Pq0 >= 0 && Pq1 <= 0 && Qp0 >= 0 && Qp1 <= 0){

			inPLimits = true;
			inQLimits = true;
			return Type.INTERSECT;
		}

		// At this point, we know that segments are not crossing
		// check if q0 and q1 lie around P or p0 and p1 lie around Q.
		// in this case, a segment cross a line
		else if (Pq0 * Pq1 <= 0) {
			inQLimits = true;
			return Type.INTERSECT;
		} else if (Qp0 * Qp1 <= 0) {
			inPLimits = true;
			return Type.INTERSECT;
		}

		// At this point, we know that each segment lie on one side of the other
		// We now check the slope to know if lines are Type.PARALLEL
		double pSlope = p0.getSlope(p1);
		double qSlope = q0.getSlope(q1);
		if (Precision.areEquals(pSlope, qSlope))
			// TODO check the infinity case
			// this test works even if the slopes are "Double.infinity" due to the verticality of the lines and division
			// by 0
			return Type.PARALLEL;
		else
			return Type.INTERSECT;
	}

	private void computeIntersectionPoint() {
		if (result == Type.INTERSECT) {
			/*
			 * Single point intersection
			 * 
			 * This calculation method needs divisions, which may cause approximation problems. The intersection point,
			 * once rounded to double precision, may be out the line bounding. If "on-the-line" intersection point is
			 * needed, you will have to use a more robust method.
			 */
			double x;
			double y;

			double pSlope = p0.getSlope(p1);
			double qSlope = q0.getSlope(q1);
			double pOrdinate = p0.y - pSlope * p0.x;
			double qOrdinate = q0.y - qSlope * q0.x;

			// At this point, we already know that pSlope != qSlope (checked in previously launched method)
			// So the divide by 0 case should never happen.
			if(qSlope - pSlope == 0)
				throw new RuntimeException("Division by zero.");

			// We must check if the lines are verticals (infinite slope)
			if (Double.isInfinite(pSlope) && Double.isInfinite(qSlope))
				throw new RuntimeException("The two lines have infinte slope (collinear), not intersecting.");
			
			else if (Double.isInfinite(pSlope)) {
				x = p0.x;
				y = qSlope * x + qOrdinate;
			} else if (Double.isInfinite(qSlope)) {
				x = q0.x;
				y = pSlope * x + pOrdinate;
			} else {
				x = (pOrdinate - qOrdinate) / (qSlope - pSlope);
				y = pSlope * x + pOrdinate;
			}
			intersection = new Point2D(x, y);
			
		} else if (result == Type.COLLINEAR) {
			/*
			 * Collinear intersection zone
			 * 
			 * At this point, we have to find the two points that enclose the intersection zone. We use distances to
			 * check if a segment's end is inside the other segment. The single intersection point is set to the middle
			 * of the intersection zone.
			 * 
			 * Note that if P and Q are not overlapping, then there is no intersection zone and all intersection points
			 * are set to "null". For segments, it means that there is no intersection. For lines, it means that
			 * intersection zone is infinite.
			 */
			if (q0.getDistance(p0) <= p0.getDistance(p1) && q0.getDistance(p1) <= p0.getDistance(p1)) {
				// then q0 is in P
				intersectionStart = q0;
				if (q1.getDistance(p0) <= p0.getDistance(p1) && q1.getDistance(p1) <= p0.getDistance(p1)) {
					// then q0 and q1 are both in P
					System.out.println("(collinear) Q is in P");
					intersectionEnd = q1;
				} else // then q0 is in P but q1 is out of P
				if (p0.getDistance(q0) <= q0.getDistance(q1) && q0.getDistance(p1) <= q0.getDistance(q1))
					// then q0 is in P and p0 is in Q
					intersectionEnd = p0;
				else
					intersectionEnd = p1;
			} else // then q0 is out of p
			if (q1.getDistance(p0) <= p0.getDistance(p1) && q1.getDistance(p1) <= p0.getDistance(p1)) {
				// then q0 is out of P and q1 is in P
				intersectionStart = q1;
				if (p0.getDistance(q0) <= q0.getDistance(q1) && q0.getDistance(p1) <= q0.getDistance(q1))
					// then q1 is in P and p0 is in Q
					intersectionEnd = p0;
				else
					intersectionEnd = p1;
			} else { // then q0 and q1 are both out of P
				if (p0.getDistance(q0) <= q0.getDistance(q1) && q0.getDistance(p1) <= q0.getDistance(q1)) {
					// then P is in Q
					System.out.println("(collinear) P is in Q");
					intersectionStart = p0;
					intersectionEnd = p1;
				} else {
					System.out.println(Intersector.class.getName() + " (collinear) and Q and P are not overlapped.");
					intersectionStart = null;
					intersectionEnd = null;
					intersection = null;
					return;
				}
			}
			intersection = intersectionEnd.getSubtraction(intersectionStart).getDivision(2);
		} else
			throw new RuntimeException("Intersection point cannot be computed if result is parallel or invalild.");
	}

}
