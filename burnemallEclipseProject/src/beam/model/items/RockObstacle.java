package beam.model.items;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Collection;

import math.geom2d.AffineTransform2D;
import math.geom2d.Point2D;
import math.geom2d.line.Ray2D;
import math.geom2d.polygon.Polyline2D;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import beam.model.Beam;
import beam.model.ModelUtil;

@Root
public class RockObstacle extends Item {
	
	private static final double SAMPLES = 8;
	private static final double RADIUS = 5;
	private static final double RADIUS_RANGE = 3;
	
	Polyline2D pl;

	public RockObstacle(@Element(name="center") Point2D center) {
		super(center, 0);
		update();
	}

	@Override
	public void draw(Graphics2D g, AffineTransform2D at) {
		g.setColor(Color.gray);
		pl.transform(at).draw(g);
	}

	@Override
	public Point2D intersect(Ray2D beam) {
		return ModelUtil.nearest(pl.intersections(beam), beam.firstPoint());
	}

	@Override
	public Collection<Beam> interact(Beam beam, Point2D intersect) {
		return null;
	}

	@Override
	void update() {
		AffineTransform2D at = AffineTransform2D.createRotation(angle).chain(AffineTransform2D.createTranslation(center.x(), center.y()));

		Polyline2D pl = new Polyline2D();
		double a = 0;
		for(int i=0; i<SAMPLES; i++){
			Point2D proj = translate(new Point2D(0, 0), a, RADIUS+Math.random()*RADIUS_RANGE);
			pl.addVertex(proj);

			a += Math.PI*2/SAMPLES;
		}
		
		pl.addVertex(pl.firstPoint());
		
		this.pl = pl.transform(at);

	}

	@Override
	public void beforeTick() {
	}
	
	private Point2D translate(Point2D p, double angle, double distance){
		return new Point2D(p.getX() + (Math.cos(angle) * distance), p.getY() + (Math.sin(angle) * distance));
	}
}
