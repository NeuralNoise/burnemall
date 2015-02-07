package beamMyRefactor.model.items;

import geometry.Point2D;
import geometry.Ray2D;
import geometry.Segment2D;
import geometry.Transform2D;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.Collection;

import math.Angle;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import beamMyRefactor.model.Beam;
import beamMyRefactor.util.Util;

@Root
public class Mirror extends Item {

	@Element
	private double width;
	
	Segment2D segment;
	private final static Stroke stroke = new BasicStroke(2);
	
	public Mirror(@Element(name="center") Point2D center, @Element(name="width") double width, @Element(name="angle") double angle) {
		super(center, angle);
		this.width=width;
		update();
	}

	@Override
	void update() {
		Segment2D seg = new Segment2D(new Point2D(-width/2,  0), new Point2D(width/2, 0));
		Segment2D sega = new Segment2D(Point2D.ORIGIN, new Point2D(0, -width/4));
		Transform2D tr = new Transform2D(center, angle);
		segment = seg.getTransformed(tr);
	}

	public Segment2D segment() {
		return segment;
	}	
	
	@Override
	public Point2D intersect(Ray2D beam) {
		return beam.getUniqueIntersection(segment);
	}

	@Override
	public Collection<Beam> interact(Beam beam, Point2D intersect) {
		double ang = Angle.getSmallestDifference(beam.getRay().getAngle(),segment.getAngle())+Math.PI/2;
		Beam res = new Beam(beam);
		res.setRay (new Ray2D(intersect, beam.getRay().getAngle()+Math.PI+2*ang));
		return Util.makeCollection(res);
	}
	
}
