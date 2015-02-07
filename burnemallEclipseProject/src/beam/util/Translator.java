package beam.util;

import beam.MyGeometry.geometry.Point2D;
import beam.MyGeometry.geometry.Segment2D;
import math.geom2d.line.LineSegment2D;

public class Translator {

	public static LineSegment2D toJavaGeom(Segment2D s){
		return new LineSegment2D(toJavaGeom(s.getStart()), toJavaGeom(s.getEnd()));
	}
	
	public static Segment2D toMyGeom(LineSegment2D s){
		return new Segment2D(toMyGeom(s.firstPoint()), toMyGeom(s.lastPoint()));
		
	}
	
	public static math.geom2d.Point2D toJavaGeom(Point2D p){
		return new math.geom2d.Point2D(p.x, p.y);
	}
	
	public static Point2D toMyGeom(math.geom2d.Point2D p){
		return new Point2D(p.x(), p.y());
	}
	
}
