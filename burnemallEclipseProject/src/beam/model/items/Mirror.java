package beam.model.items;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.Collection;

import math.geom2d.AffineTransform2D;
import math.geom2d.Angle2D;
import math.geom2d.Point2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.Ray2D;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import beam.model.Beam;
import beam.util.Util;

@Root
public class Mirror extends Item {

	@Element
	private double width;
	
	LineSegment2D segment;
	private final static Stroke stroke = new BasicStroke(2);
	
	public Mirror(@Element(name="center") Point2D center, @Element(name="width") double width, @Element(name="angle") double angle) {
		super(center, angle);
		this.width=width;
		update();
	}

	@Override
	void update() {
		LineSegment2D seg = new LineSegment2D(-width/2,  0, width/2, 0);
		LineSegment2D sega = new LineSegment2D(0, 0, 0, -width/4);
		AffineTransform2D at = AffineTransform2D.createRotation(angle).chain(AffineTransform2D.createTranslation(center.x(), center.y()));
		segment = seg.transform(at);
	}

	public LineSegment2D segment() {
		return segment;
	}	
	
	@Override
	public void draw(Graphics2D g, AffineTransform2D at) {
		g.setStroke(stroke);
		g.setColor(Color.LIGHT_GRAY);
		segment.transform(at).draw(g);
	}

	@Override
	public Point2D intersect(Ray2D beam) {
		return segment.intersection(beam);
	}

	@Override
	public Collection<Beam> interact(Beam beam, Point2D intersect) {
		double ang = Angle2D.angle(beam.getRay(),segment)+Math.PI/2;
		Beam res = new Beam(beam);
		res.setRay (new Ray2D(intersect, beam.getRay().direction().angle()+Math.PI+2*ang));
		return Util.makeCollection(res);
	}
	
}
