package beam.model.items;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Collection;

import math.geom2d.AffineTransform2D;
import math.geom2d.Angle2D;
import math.geom2d.Point2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.Ray2D;
import math.geom2d.polygon.Polyline2D;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import beam.model.Beam;
import beam.util.Angle;
import beam.util.Precision;
import beam.util.Util;

@Root
public class RefractingArea extends Refractor {

	private static double REFRACTION_INDEX = 1.8;
	@Element
	double width;

	@Element
	double height;
	
	public RefractingArea(@Element(name="center") Point2D center, @Element(name="width") double width, @Element(name="height") double height, @Element(name="angle") double angle) {
		super(center, angle, REFRACTION_INDEX);
		this.width = width;
		this.height = height;
		update();
	}

	@Override
	void update() {
		Polyline2D pl = new Polyline2D();
		pl.addVertex(new Point2D(-width/2, -height/2));
		pl.addVertex(new Point2D(+width/2, -height/2));
		pl.addVertex(new Point2D(+width/2, +height/2));
		pl.addVertex(new Point2D(-width/2, +height/2));
		pl.addVertex(pl.firstPoint());

		AffineTransform2D at = AffineTransform2D.createRotation(angle).chain(AffineTransform2D.createTranslation(center.x(), center.y()));

		this.pl = pl.transform(at);
	}
}
