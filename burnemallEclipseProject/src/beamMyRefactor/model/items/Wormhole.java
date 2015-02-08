package beamMyRefactor.model.items;

import geometry.Circle2D;
import geometry.Point2D;
import geometry.Ray2D;
import geometry.Transform2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Collection;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import beamMyRefactor.model.Beam;
import beamMyRefactor.model.ModelUtil;
import beamMyRefactor.util.Util;

@Root
public class Wormhole extends Item {
	
	Circle2D shape;
	
	Wormhole binome = null;

	public Wormhole(@Element(name="center") Point2D center) {
		super(center, 0);
		update();
	}

	@Override
	public Point2D intersect(Ray2D beam) {
		if(shape.isInside(beam.getStart()))
			return null;
		return ModelUtil.nearest(beam.getIntersection(shape).getAll(), beam.getStart());
	}

	@Override
	public Collection<Beam> interact(Beam beam, Point2D intersect) {
		if(binome == null)
			return null;
		Beam res = new Beam(beam);
		res.setRay(new Ray2D(binome.center, beam.getRay().getAngle()));
		return Util.makeCollection(res);
	}

	@Override
	protected void update() {
		shape = new Circle2D(new Point2D(0, 0), 7.5);
		
		Transform2D tr = new Transform2D(center, angle);
		shape = shape.getTransformed(tr);
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
	
	@Override
	public Object getShape() {
		return shape;
	}
}
