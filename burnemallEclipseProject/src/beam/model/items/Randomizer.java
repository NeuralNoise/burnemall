package beam.model.items;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Collection;
import java.util.Random;

import math.geom2d.AffineTransform2D;
import math.geom2d.Point2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.conic.EllipseShape2D;
import math.geom2d.line.Ray2D;

import org.simpleframework.xml.Element;

import beam.model.Beam;
import beam.model.ModelUtil;
import beam.util.Util;

public class Randomizer extends Item {

	EllipseShape2D c;
	
	private long timeFrame = 0;
	private Random rand = new Random();
	private double angle;
	
	public Randomizer(@Element(name="center") Point2D center) {
		super(center, 0);
		update();
	}

	@Override
	public void draw(Graphics2D g, AffineTransform2D at) {
		g.setColor(Color.orange);
		c.transform(at).draw(g);
	}

	@Override
	public Point2D intersect(Ray2D beam) {
		return ModelUtil.nearest(c.intersections(beam), beam.firstPoint());
	}

	@Override
	public Collection<Beam> interact(Beam beam, Point2D intersect) {
		Point2D out = ModelUtil.farthest(c.intersections(beam.getRay()), beam.getRay().firstPoint());
		Beam res = new Beam(beam);
		long timeFrame = System.currentTimeMillis()/300;
		if (this.timeFrame!=timeFrame) {
			this.timeFrame=timeFrame;
			angle = rand.nextDouble()-0.5;
		}
		System.out.println(System.currentTimeMillis()/1000+" - " +angle);
		res.setRay(new Ray2D(out, beam.getRay().direction().angle()+angle));
		return Util.makeCollection(res);
	}

	@Override
	void update() {
		c = new Circle2D(new Point2D(0, 0), 5);
		AffineTransform2D at = AffineTransform2D.createRotation(angle).chain(AffineTransform2D.createTranslation(center.x(), center.y()));
		c = c.transform(at);
	}
}
