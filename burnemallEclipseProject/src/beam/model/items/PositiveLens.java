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
public class PositiveLens extends Lens {

	public PositiveLens(@Element(name="center") Point2D center, @Element(name="angle") double angle, @Element(name="leftRadius") double leftRadius, @Element(name="leftRadius") double rightRadius){
		super(center, angle, leftRadius, rightRadius);
		update();
	}

	@Override
	void update() {
		Polyline2D pl = new Polyline2D();

		// left diopter
		pl.addVertex(new Point2D(-THICKNESS, EXTENT));
		if(leftRadius >= MAX_RADIUS)
			pl.addVertex(new Point2D(-THICKNESS, -EXTENT));
		else {
			double arc = Math.asin(EXTENT/leftRadius);
			Point2D pivot = new Point2D(Math.cos(arc)*leftRadius-THICKNESS, 0);
			for(int i=0;i<NB_FACES; i++)
				pl.addVertex(pl.lastPoint().rotate(pivot, arc*2/NB_FACES));
		}

		// right diopter
		pl.addVertex(new Point2D(+THICKNESS, -EXTENT));
		if(rightRadius >= MAX_RADIUS)
			pl.addVertex(new Point2D(THICKNESS, EXTENT));
		else {
			double arc = Math.asin(EXTENT/rightRadius);
			Point2D pivot = new Point2D(-Math.cos(arc)*rightRadius+THICKNESS, 0);
			for(int i=0;i<NB_FACES; i++)
				pl.addVertex(pl.lastPoint().rotate(pivot, arc*2/NB_FACES));
		}
		pl.addVertex(pl.firstPoint());

		AffineTransform2D at = AffineTransform2D.createRotation(angle).chain(AffineTransform2D.createTranslation(center.x(), center.y()));
		this.pl = pl.transform(at);
	}
}
