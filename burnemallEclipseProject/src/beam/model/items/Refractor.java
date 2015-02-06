package beam.model.items;

import geometry.Point2D;
import geometry.Polyline2D;
import geometry.Ray2D;
import geometry.Segment2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Collection;

import math.Angle;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import beam.model.Beam;
import beam.util.Precision;
import beam.util.Util;

@Root
public class Refractor extends Item {


	@Element
	double incidence;
	
	Polyline2D pl;
	Segment2D intersectedFace;
	
	public Refractor(@Element(name="center") Point2D center, @Element(name="angle") double angle, @Element(name="incidence") double incidence) {
		super(center, angle);
		this.incidence = incidence;
	}

	@Override
	void update() {
	}

	@Override
	public Point2D intersect(Ray2D beam) {
		Point2D nearest = null; 
		for(Segment2D l : pl) {
			if(beam.intersectAtSinglePoint(l)){
				Point2D i = beam.getUniqueIntersection(l);
				if(i != null &&
						!i.equals(beam.getStart()) && 
						(nearest == null || i.getDistance(beam.getStart()) < nearest.getDistance(beam.getStart()))){
					nearest = i;
					intersectedFace = l;
				}
			}
		}
		return nearest;
	}

	@Override
	public Collection<Beam> interact(Beam beam, Point2D intersect) {
		// TODO we may cast a residual reflexion beam
		
		// check if the beam enters or leaves
		double incidence = pl.hasInside(beam.getRay().getStart())? 1/this.incidence: this.incidence;
				
		// compute normal angle depending on the face direction
		double normal = intersectedFace.getAngle()+Angle.RIGHT;
		if(Angle.getTurn(intersectedFace.getStart(), intersectedFace.getEnd(), beam.getRay().getStart()) == Angle.CLOCKWISE)
			normal += Angle.FLAT;
		
		double incident = Angle.getOrientedDifference(normal, beam.getRay().getAngle()+Angle.FLAT);
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
