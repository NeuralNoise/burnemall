package beam.model.items;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import math.geom2d.AffineTransform2D;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.conic.EllipseShape2D;
import math.geom2d.line.Line2D;
import math.geom2d.line.Ray2D;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import beam.model.Beam;
import beam.model.ModelUtil;
import beam.util.Util;

@Root
public class Blackhole extends Item {
	
	private static final double ORBIT_RADIUS = 30;
	private static final double ATTRACTION_FORCE = 0.3;
	private static final double CURVE_RESOLUTION = 3;
	
	EllipseShape2D shape;
	EllipseShape2D orbit;

	public Blackhole(@Element(name="center") Point2D center) {
		super(center, 0);
		update();
	}

	@Override
	public void draw(Graphics2D g, AffineTransform2D at) {
		g.setColor(Color.pink);
		shape.transform(at).draw(g);
	}

	@Override
	public Point2D intersect(Ray2D beam) {
		// the ray have to be attracted in both situations : it intersect the orbit, or it is cast inside the orbit
		if(orbit.isInside(beam.firstPoint()))
			// ray is casted inside the orbit. We let it continue its course before attracting it
			return translate(beam.firstPoint(), beam.direction().angle(), CURVE_RESOLUTION);

		Point2D i = ModelUtil.nearest(orbit.intersections(beam), beam.firstPoint()); 
		if(i != null)
			// we translate to be sure that the next beam is cats from inside
			return translate(i, beam.direction().angle(), CURVE_RESOLUTION);
		return null;
	}

	@Override
	public Collection<Beam> interact(Beam beam, Point2D intersect) {
		// first check : beam is attracted inside the blackhole
		if(shape.isInside(intersect))
			return null;
		
		Point2D beamVector = translate(new Point2D(0, 0), beam.getRay().direction().angle(), 1);
		Point2D attractionVector = translate(new Point2D(0, 0), new Line2D(intersect, center).direction().angle(), ATTRACTION_FORCE*(1-intersect.distance(center)/ORBIT_RADIUS));
		
		double ang = new Line2D(new Point2D(0, 0), beamVector.plus(attractionVector)).direction().angle();
		Beam res = new Beam(beam);
		res.setRay(new Ray2D(intersect, ang));
		return Util.makeCollection(res);
	}

	@Override
	void update() {
		shape = new Circle2D(new Point2D(0, 0), 5);
		orbit = new Circle2D(new Point2D(0, 0), ORBIT_RADIUS);
		AffineTransform2D at = AffineTransform2D.createRotation(angle).chain(AffineTransform2D.createTranslation(center.x(), center.y()));
		shape = shape.transform(at);
		orbit = orbit.transform(at);
	}

	// to refactor
	private Point2D translate(Point2D p, double angle, double distance){
		return new Point2D(p.getX() + (Math.cos(angle) * distance), p.getY() + (Math.sin(angle) * distance));
	}

}
