package beam.model.items;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Collection;

import math.geom2d.AffineTransform2D;
import math.geom2d.Point2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.conic.EllipseShape2D;
import math.geom2d.line.Ray2D;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import beam.model.Beam;
import beam.model.ModelUtil;
import beam.util.Util;

@Root
public class Wormhole extends Item {
	
	EllipseShape2D c;
	
	Wormhole binome = null;

	public Wormhole(@Element(name="center") Point2D center) {
		super(center, 0);
		update();
	}

	@Override
	public void draw(Graphics2D g, AffineTransform2D at) {
		g.setColor(Color.yellow);
		c.transform(at).draw(g);
	}

	@Override
	public Point2D intersect(Ray2D beam) {
		if(c.isInside(beam.firstPoint()))
			return null;
		return ModelUtil.nearest(c.intersections(beam), beam.firstPoint());
	}

	@Override
	public Collection<Beam> interact(Beam beam, Point2D intersect) {
		if(binome == null)
			return null;
		Beam res = new Beam(beam);
		res.setRay(new Ray2D(binome.center, beam.getRay().direction().angle()));
		return Util.makeCollection(res);
	}

	@Override
	void update() {
		c = new Circle2D(new Point2D(0, 0), 7.5);
		AffineTransform2D at = AffineTransform2D.createRotation(angle).chain(AffineTransform2D.createTranslation(center.x(), center.y()));
		c = c.transform(at);
	}

	@Override
	public void beforeTick() {
	}
	
	public void link(Wormhole other){
		binome = other;
		other.binome = this;
	}
	
	public boolean isLone(){
		return binome == null;
	}
	
	public Wormhole getBinome(){
		return binome;
	}
}
