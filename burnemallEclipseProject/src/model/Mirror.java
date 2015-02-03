package beam.model;

import java.awt.Graphics2D;

import math.geom2d.AffineTransform2D;
import math.geom2d.Point2D;
import math.geom2d.line.LineSegment2D;

public class Mirror {

	private double width, angle;
	Point2D center;	

	LineSegment2D segment;
	LineSegment2D axis;
	Point2D end;

	Mirror(Point2D center, double width, double angle) {
		this.center = center;
		this.width=width;
		this.angle=angle;
		buildSegment();
	}

	private void buildSegment() {
		LineSegment2D seg = new LineSegment2D(-width/2,  0, width/2, 0);
		LineSegment2D sega = new LineSegment2D(0, 0, 0, -width/4);
		AffineTransform2D at = AffineTransform2D.createRotation(angle).chain(AffineTransform2D.createTranslation(center.x(), center.y()));
		segment = seg.transform(at);
		axis= sega.transform(at);
		end = seg.lastPoint().transform(at);
	}

	public LineSegment2D segment() {
		return segment;
	}	
	
	public Point2D end() {
		return end;
	}

	public void addAngle(double d) {
		angle += d;
		buildSegment();
	}

	public void draw(Graphics2D g, AffineTransform2D at) {
		segment.transform(at).draw(g);
		axis.transform(at).draw(g);
		end.transform(at).draw(g,3);
	}	
	
}
