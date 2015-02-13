package beamMyRefactor.model.items.material.circular;

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

import beamMyRefactor.model.ModelUtil;
import beamMyRefactor.model.items.material.AbstractPhotosensitive;
import beamMyRefactor.model.lighting.Beam;
import beamMyRefactor.util.Util;

@Root
public class Blackhole extends AbstractCircular {
	
	private static final double ORBIT_RADIUS = 30;
	private static final double ATTRACTION_FORCE = 0.3;
	private static final double CURVE_RESOLUTION = 5;
	
	Circle2D initialOrbit;
	Circle2D orbit;
	public Blackhole(@Element(name="angle")double angle,
			@Element(name="radius") double radius) {
		this(Point2D.ORIGIN, angle, radius);
	}

	public Blackhole(Point2D coord, double angle, double radius){
		super(coord, angle);
		initialOrbit = new Circle2D(new Point2D(0, 0), ORBIT_RADIUS);
		update();
	}

	@Override
	public Point2D intersect(Ray2D beam) {
		// the ray have to be attracted in both situations : it intersect the orbit, or it is cast inside the orbit
		if(orbit.hasInside(beam.getStart()))
			// ray is casted inside the orbit. We let it continue its course before attracting it
			return beam.getStart().getTranslation(beam.getAngle(), CURVE_RESOLUTION);

		Point2D i = ModelUtil.nearest(beam.getIntersection(orbit).getAll(), beam.getStart()); 
		if(i != null)
			// we translate to be sure that the next beam is casted from inside
			return i.getTranslation(beam.getAngle(), CURVE_RESOLUTION);
		return null;
	}

	@Override
	public Collection<Beam> interact(Beam beam, Point2D intersect) {
		// first check : beam is attracted inside the blackhole
		if(shape.hasInside(intersect))
			return null;
		
		Point2D beamVector = Point2D.ORIGIN.getTranslation(beam.getRay().getAngle(), 1);
		Point2D attractionVector = Point2D.ORIGIN.getTranslation(new Line2D(intersect, coord).getAngle(), ATTRACTION_FORCE*(1-intersect.getDistance(coord)/ORBIT_RADIUS));
		
		double ang = beamVector.getAddition(attractionVector).getAngle();
		Beam res = new Beam(beam);
		res.setRay(new Ray2D(intersect, ang));
		return Util.makeCollection(res);
	}

	@Override
	protected void update() {
		super.update();
		orbit = initialOrbit.getTransformed(getTranform());
	}
}
