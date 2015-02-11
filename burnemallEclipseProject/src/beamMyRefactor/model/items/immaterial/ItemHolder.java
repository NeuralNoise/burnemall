package beamMyRefactor.model.items.immaterial;

import geometry.Circle2D;
import geometry.Point2D;
import geometry.Ray2D;
import geometry.Segment2D;
import geometry.Transform2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import math.Angle;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import beamMyRefactor.model.Beam;
import beamMyRefactor.model.items.AbstractItem;
import beamMyRefactor.model.items.material.AbstractLightable;


public class ItemHolder extends AbstractLightable {
	
	@ElementList
	List<AbstractLightable> items = new ArrayList<>();
	
	@Element
	double extent;
	
	Circle2D pivot;
	Circle2D bound;
	
	AbstractLightable intersected;

	public ItemHolder(@Element(name="center")Point2D center, @Element(name="angle")double angle, @Element(name="extent")double extent) {
		super(center, angle);
		this.extent = extent;
		update();
	}
	
	public void attach(AbstractLightable i){
		double angle = new Segment2D(coord, i.getCoord()).getAngle();
		i.move(coord.getTranslation(angle, extent));
		items.add(i);
	}
	
	public Collection<AbstractLightable> getItems(){
		return items;
	}

	@Override
	public Point2D intersect(Ray2D ray) {
		double dist = Double.MAX_VALUE;
		Point2D res = null;
		intersected = null;
		for(AbstractLightable i : items){
			Point2D inter = i.intersect(ray);
			if(inter != null && inter.getDistance(ray.getStart()) < dist){
				dist = inter.getDistance(ray.getStart());
				intersected = i;
				res = inter;
			}
		}
		return res;
	}

	@Override
	public Collection<Beam> interact(Beam beam, Point2D intersect) {
		return intersected.interact(beam, intersect);
	}

	@Override
	protected void update() {
		pivot = new Circle2D(Point2D.ORIGIN, 2.5);
		bound = new Circle2D(Point2D.ORIGIN, extent);
		Transform2D tr = new Transform2D(coord, angle);
		pivot = pivot.getTransformed(tr);
		bound = bound.getTransformed(tr);
	}
	
	@Override
	public void move(Point2D center) {
		Transform2D tr = new Transform2D(this.coord.getSubtraction(center));
		
		for(AbstractItem i : items)
			i.move(i.getCoord().getTransformed(tr));
		super.move(center);
	}
	
	@Override
	public void setAngle(double a) {
		double diff = Angle.getOrientedDifference(angle, a); 
		super.setAngle(a);
		Transform2D tr = new Transform2D(diff, coord);
		for(AbstractItem i : items){
			i.move(i.getCoord().getTransformed(tr));
			i.addAngle(diff);
		}
	}
	
	@Override
	public Collection<Beam> produceBeam() {
		ArrayList<Beam> res = new ArrayList<>();
		for(AbstractLightable i : items)
			res.addAll(i.produceBeam());
		return res;
	}

	@Override
	public Object getShape() {
		return null;
	}
	
}
