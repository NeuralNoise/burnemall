package beam.model.items;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.AffineTransform2D;
import math.geom2d.Point2D;
import math.geom2d.line.Ray2D;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import beam.model.Beam;

@Root
public abstract class Item {

	@Element
	Point2D center;
	@Element
	double angle;
	
	public Item(double x, double y, double angle) {
		center = new Point2D(x,y);
		this.angle = angle;
	}

	public Item(Point2D center, double angle) {
		this.center = center;
		this.angle = angle;
	}

	public Point2D center() {
		return center;
	}
	
	public void move(Point2D center) {
		this.center = center;
		update();
	}	

	public void addAngle(double d) {
		angle += d;
		update();
	}
	
	public void setAngle(double a){
		angle = a;
		update();
	}
	
	// bad design : drawing whouldn't be here... to be refactored
	public abstract void draw(Graphics2D g, AffineTransform2D at);
	
	public abstract Point2D intersect(Ray2D beam);
	public abstract Collection<Beam> interact(Beam beam, Point2D intersect); 
	
	// is called after the Item has been altered
	abstract void update();

	public Collection<Beam> produceBeam() {
		return new ArrayList<>();
	}

	public Point2D getCenter() {
		return center;
	}

	public void beforeTick() {}

}
