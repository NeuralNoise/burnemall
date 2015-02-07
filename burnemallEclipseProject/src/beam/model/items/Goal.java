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

@Root
public class Goal extends Item {
	
	EllipseShape2D c;
	boolean hit = true;

	public Goal(@Element(name="center") Point2D center) {
		super(center, 0);
		update();
	}

	@Override
	public void draw(Graphics2D g, AffineTransform2D at) {
		if (hit)
			g.setColor(Color.green);
		else
			g.setColor(Color.gray);
		c.transform(at).draw(g);
	}

	@Override
	public Point2D intersect(Ray2D beam) {
		return ModelUtil.nearest(c.intersections(beam), beam.firstPoint());
	}

	@Override
	public Collection<Beam> interact(Beam beam, Point2D intersect) {
		hit = true;
		return null;
	}

	@Override
	void update() {
		c = new Circle2D(new Point2D(0, 0), 5);
		AffineTransform2D at = AffineTransform2D.createRotation(angle).chain(AffineTransform2D.createTranslation(center.x(), center.y()));
		c = c.transform(at);
	}

	@Override
	public void beforeTick() {
		hit=false;
	}
}
