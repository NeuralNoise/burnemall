package beam.model.items;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import math.geom2d.AffineTransform2D;
import math.geom2d.Angle2D;
import math.geom2d.ColinearPoints2DException;
import math.geom2d.Point2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.Ray2D;
import math.geom2d.polygon.Polyline2D;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import beam.model.Beam;
import beam.util.Angle;
import beam.util.Precision;

@Root
public class TriDiffractor extends Item {

	private final static double WIDTH = 10;
	
	Polyline2D pl;
	LineSegment2D collided;
	public ArrayList<Beam> beams = new ArrayList<>();
	
	public TriDiffractor(@Element(name="center") Point2D center, @Element(name="angle") double angle) {
		super(center, angle);
		update();
	}

	@Override
	void update() {
		Polyline2D pl = new Polyline2D();
		pl.addVertex(translate(new Point2D(0, 0), 0, WIDTH));
		pl.addVertex(translate(new Point2D(0, 0), Angle.FULL/3, WIDTH));
		pl.addVertex(translate(new Point2D(0, 0), 2*Angle.FULL/3, WIDTH));
		pl.addVertex(pl.firstPoint());
		
		AffineTransform2D at = AffineTransform2D.createRotation(angle).chain(AffineTransform2D.createTranslation(center.x(), center.y()));
		this.pl = pl.transform(at);
	}

	@Override
	public void draw(Graphics2D g, AffineTransform2D at) {
		g.setColor(Color.LIGHT_GRAY);
		pl.transform(at).draw(g);
	}

	@Override
	public Point2D intersect(Ray2D beam) {
		Point2D nearest = null;
		for(LineSegment2D l : pl.edges()){
			Point2D i = l.intersection(beam);
			if(i != null &&
					(nearest == null ||
					i.distance(beam.firstPoint()) < nearest.distance(beam.firstPoint()))){
				nearest = i;
				collided = l;
			}
		}
		return nearest;
	}

	@Override
	public Collection<Beam> interact(Beam beam, Point2D intersect) {
		if(beams.size() == 2)
			return null;
		for(LineSegment2D l : pl.edges()){
			if(l.almostEquals(collided, Precision.APPROX))
				continue;
//			Beam b = new Beam(beam.getColor(), beam.getWidth());
			Beam b = new Beam(beam);
			b.setRay(new Ray2D(getMiddle(l), l.direction().angle()-Angle.RIGHT));
			beams.add(b);
		}
		return Collections.unmodifiableCollection(beams);
	}
	
	private Point2D translate(Point2D p, double angle, double distance){
		return new Point2D(p.getX() + (Math.cos(angle) * distance), p.getY() + (Math.sin(angle) * distance));
	}
	
	private Point2D getMiddle(LineSegment2D l) {
		Point2D p0 = l.firstPoint();
		Point2D p1 = l.lastPoint();
		return p0.plus(p1.minus(p0).scale(0.5));
	}
	
	@Override
	public void beforeTick() {
		beams.clear();
	}

}
