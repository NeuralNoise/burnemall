package beamMyRefactor.model.items.material;

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

import beamMyRefactor.model.items.AbstractItem;
import beamMyRefactor.model.lighting.Beam;


public class ItemHolder extends AbstractPhotosensitive {
	
	@ElementList
	List<AbstractItem> items = new ArrayList<>();
	
	@Element
	double extent;
	
	Circle2D pivot;
	Circle2D bound;
	
	AbstractPhotosensitive intersected;

	public ItemHolder(@Element(name="center")Point2D center, @Element(name="angle")double angle, @Element(name="extent")double extent) {
		super(center, angle);
		this.extent = extent;
		update();
	}
	
	public void attach(AbstractItem i){
		double angle = new Segment2D(coord, i.getCoord()).getAngle();
		i.move(coord.getTranslation(angle, extent));
		items.add(i);
	}
	
	public Collection<AbstractItem> getItems(){
		return items;
	}

	@Override
	public Point2D intersect(Ray2D ray) {
		double dist = Double.MAX_VALUE;
		Point2D res = null;
		intersected = null;
		for(AbstractItem i : items){
			if(i instanceof AbstractPhotosensitive){
				AbstractPhotosensitive ph = (AbstractPhotosensitive)i;
				Point2D inter = ph.intersect(ray);
				if(inter != null && inter.getDistance(ray.getStart()) < dist){
					dist = inter.getDistance(ray.getStart());
					intersected = ph;
					res = inter;
				}
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
	public Collection<Beam> produceAllBeams() {
		ArrayList<Beam> res = new ArrayList<>();
		for(AbstractItem i : items)
			if(i instanceof AbstractPhotosensitive){
				AbstractPhotosensitive ph = (AbstractPhotosensitive)i;
				res.addAll(ph.produceAllBeams());
			}
		return res;
	}

	@Override
	public Object getShape() {
		return null;
	}
	
}
