package beam.model.items;

import geometry.Circle2D;
import geometry.Line2D;
import geometry.Point2D;
import geometry.Ray2D;
import geometry.Transform2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

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
	
	Circle2D shape;
	Circle2D orbit;

	public Blackhole(@Element(name="center") Point2D center) {
		super(center, 0);
		update();
	}

	@Override
	public Point2D intersect(Ray2D beam) {
		// the ray have to be attracted in both situations : it intersect the orbit, or it is cast inside the orbit
		if(orbit.isInside(beam.getStart()))
			// ray is casted inside the orbit. We let it continue its course before attracting it
			return beam.getStart().getTranslation(beam.getAngle(), CURVE_RESOLUTION);

		Point2D i = ModelUtil.nearest(beam.getIntersectionsWithCircle(orbit), beam.getStart()); 
		if(i != null)
			// we translate to be sure that the next beam is casted from inside
			return i.getTranslation(beam.getAngle(), CURVE_RESOLUTION);
		return null;
	}

	@Override
	public Collection<Beam> interact(Beam beam, Point2D intersect) {
		// first check : beam is attracted inside the blackhole
		if(shape.isInside(intersect))
			return null;
		
		Point2D beamVector = Point2D.ORIGIN.getTranslation(beam.getRay().getAngle(), 1);
		Point2D attractionVector = Point2D.ORIGIN.getTranslation(new Line2D(intersect, center).getAngle(), ATTRACTION_FORCE*(1-intersect.getDistance(center)/ORBIT_RADIUS));
		
		double ang = new Line2D(new Point2D(0, 0), beamVector.getAddition(attractionVector)).getAngle();
		Beam res = new Beam(beam);
		res.setRay(new Ray2D(intersect, ang));
		return Util.makeCollection(res);
	}

	@Override
	void update() {
		shape = new Circle2D(new Point2D(0, 0), 5);
		orbit = new Circle2D(new Point2D(0, 0), ORBIT_RADIUS);
		Transform2D tr = new Transform2D(center, angle);
		shape = shape.getTransformed(tr);
		orbit = orbit.getTransformed(tr);
	}
}
