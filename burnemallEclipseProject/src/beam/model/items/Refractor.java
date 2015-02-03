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
public class Refractor extends Item {


	@Element
	double incidence;
	
	Polyline2D pl;
	LineSegment2D intersectedFace;
	
	public Refractor(@Element(name="center") Point2D center, @Element(name="angle") double angle, @Element(name="incidence") double incidence) {
		super(center, angle);
		this.incidence = incidence;
	}

	@Override
	void update() {
	}

	@Override
	public void draw(Graphics2D g, AffineTransform2D at) {
		g.setColor(Color.LIGHT_GRAY);
		pl.transform(at).draw(g);
	}

	@Override
	public Point2D intersect(Ray2D beam) {
		Point2D nearest = null; 
		for(LineSegment2D l : pl.edges()) {
			Point2D i = l.intersection(beam);
			if(i != null &&
					!i.almostEquals(beam.firstPoint(), Precision.APPROX) && 
					(nearest == null || i.distance(beam.firstPoint()) < nearest.distance(beam.firstPoint()))){
				nearest = i;
				intersectedFace = l;
			}
		}
		return nearest;
	}

	@Override
	public Collection<Beam> interact(Beam beam, Point2D intersect) {
		// TODO we may cast a residual reflexion beam
		
		// check if the beam enters or leaves
		double incidence = pl.isInside(beam.getRay().firstPoint())? 1/this.incidence: this.incidence;
				
		// compute normal angle depending on the face direction
		double normal = intersectedFace.direction().angle()+Angle.RIGHT;
		if(Point2D.ccw(intersectedFace.firstPoint(), intersectedFace.lastPoint(), beam.getRay().firstPoint()) == -1)
			normal += Angle.FLAT;
		
		double incident = Angle.getOrientedDifference(normal, beam.getRay().direction().angle()+Angle.FLAT);
		double critical = Math.asin(1/incidence);
		double refraction;
		if(incidence < 1 || Math.abs(incident) < critical)
			refraction = Math.asin(Math.sin(incident)*incidence);
		else
			// total refraction, only remains reflexion
			refraction = Angle.FLAT-incident;
		
		Beam res = new Beam(beam);
		res.setRay (new Ray2D(intersect, normal+Angle.FLAT+refraction));
		return Util.makeCollection(res);
	}
	
}
