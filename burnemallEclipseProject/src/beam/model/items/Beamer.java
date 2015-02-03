package beam.model.items;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import math.geom2d.AffineTransform2D;
import math.geom2d.Point2D;
import math.geom2d.line.Ray2D;
import math.geom2d.polygon.Polyline2D;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import beam.model.Beam;
import beam.model.MyColor;

@Root
public class Beamer extends Item {

	private static final int LENGTH=20;
	private static final int WIDTH=10;
	
	private Polyline2D pl;
	
	@Element(required=false)
	private MyColor color = MyColor.get("#63CAEB");
	
	@Element(required=false)
	private int rayWidth = 1;
	
	@Attribute(required=false)
	private int nbRays = 1;
	
	@Element(required=false)
	private double spread = 0;
	
	private final static Stroke stroke = new BasicStroke(2);
	private static final Color DRAW_COLOR = new Color(48,110,18);

	public Beamer(double x, double y, double angle, MyColor color) {
		super(x,y,angle);
		this.color = color;
		update();
	}

	public Beamer(@Element(name="center") Point2D center, @Element(name="angle") double angle) {
		super(center,angle);
		this.angle = angle;
		update();
	}

	@Override
	void update() {
		Polyline2D pl = new Polyline2D();
		pl.addVertex(new Point2D(0, 0));
		pl.addVertex(new Point2D(-LENGTH, WIDTH/2));
		pl.addVertex(new Point2D(-LENGTH, -WIDTH/2));
		pl.addVertex(new Point2D(0, 0));
		AffineTransform2D at = AffineTransform2D.createRotation(angle).chain(AffineTransform2D.createTranslation(center.x(), center.y()));;
		this.pl = pl.transform(at);
	}
		
	@Override
	public void draw(Graphics2D g, AffineTransform2D at) {
		g.setStroke(stroke);
		g.setColor(DRAW_COLOR);
		pl.transform(at).draw(g);
	}

	public double getAngle() {
		return angle;
	}

	@Override
	public Point2D intersect(Ray2D beam) {
		// the beamer never interacts with the beam
		return null;
	}


	@Override
	public Collection<Beam> interact(Beam beam, Point2D intersect) {
		// the beamer never interacts with the beam
		return null;
	}

	@Override
	public Collection<Beam> produceBeam() {
		List<Beam> res = new ArrayList<Beam>();
		if (nbRays==1) {
			Beam b = new Beam(color, rayWidth);
			b.setRay (new Ray2D(center, angle));
			res.add(b);
		} else {
			double step = spread/nbRays;
			double start = angle-spread/2;
			for (int i=0; i<nbRays; i++) {
				Beam b = new Beam(color, rayWidth);
				b.setRay (new Ray2D(center, start));
				b.setAsLight();
				start+=step;
				res.add(b);				
			}
		}
		return res;
	}
	
	public void setAsLight(int nbRays, double spreadAngle){
		this.nbRays = nbRays;
		this.spread = spreadAngle;
	}
	
}
