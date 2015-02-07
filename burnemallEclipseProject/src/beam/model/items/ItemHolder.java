package beam.model.items;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import math.geom2d.AffineTransform2D;
import math.geom2d.Angle2D;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.conic.EllipseShape2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.Ray2D;
import beam.MyGeometry.math.Angle;
import beam.model.Beam;

public class ItemHolder extends Item {
	
	@ElementList
	List<Item> items = new ArrayList<>();
	
	@Element
	double extent;
	
	EllipseShape2D pivot;
	EllipseShape2D bound;
	
	Item intersected;

	public ItemHolder(@Element(name="center") Point2D center, @Element(name="angle")double angle, @Element(name="extent")double extent) {
		super(center, angle);
		this.extent = extent;
		update();
	}
	
	public void attach(Item i){
		double angle = new LineSegment2D(center, i.center).direction().angle();
		i.move(translate(center, angle, extent));
		items.add(i);
	}
	
	public Collection<Item> getItems(){
		return items;
	}

	@Override
	public void draw(Graphics2D g, AffineTransform2D at) {
		g.setColor(Color.DARK_GRAY);
		pivot.transform(at).draw(g);
		bound.transform(at).draw(g);
		for(Item i : items)
			i.draw(g, at);
	}

	@Override
	public Point2D intersect(Ray2D beam) {
		double dist = Double.MAX_VALUE;
		Point2D res = null;
		intersected = null;
		for(Item i : items){
			Point2D inter = i.intersect(beam);
			if(inter != null && inter.distance(beam.firstPoint()) < dist){
				dist = inter.distance(beam.firstPoint());
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
	void update() {
		pivot = new Circle2D(0, 0, 2.5);
		bound = new Circle2D(0, 0, extent);
		AffineTransform2D at = AffineTransform2D.createRotation(angle).chain(AffineTransform2D.createTranslation(center.x(), center.y()));
		pivot = pivot.transform(at);
		bound = bound.transform(at);
	}
	
	@Override
	public void move(Point2D center) {
		AffineTransform2D at = AffineTransform2D.createTranslation(new Vector2D(this.center, center));
		for(Item i : items)
			i.move(i.center.transform(at));
		super.move(center);
	}
	
	@Override
	public void setAngle(double a) {
		double diff = Angle.getOrientedDifference(angle, a); 
		super.setAngle(a);
		AffineTransform2D at = AffineTransform2D.createRotation(center, diff);
		for(Item i : items){
			i.move(i.center.transform(at));
			i.addAngle(diff);
		}
	}
	
	@Override
	public Collection<Beam> produceBeam() {
		ArrayList<Beam> res = new ArrayList<>();
		for(Item i : items)
			res.addAll(i.produceBeam());
		return res;
	}
	
	private Point2D translate(Point2D p, double angle, double distance){
		return new Point2D(p.getX() + (Math.cos(angle) * distance), p.getY() + (Math.sin(angle) * distance));
	}

	
}
