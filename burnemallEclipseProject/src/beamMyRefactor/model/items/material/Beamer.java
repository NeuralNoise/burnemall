package beamMyRefactor.model.items.material;

import geometry.Point2D;
import geometry.Polyline2D;
import geometry.Ray2D;
import geometry.Transform2D;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import math.MyRandom;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import beamMyRefactor.model.MyColor;
import beamMyRefactor.model.lighting.Beam;

@Root
public class Beamer extends AbstractPhotosensitive {

	private static final int LENGTH=20;
	private static final int WIDTH=10;
	
	public Polyline2D pl;
	
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

	public Beamer(@Element(name="angle") double angle) {
		this(Point2D.ORIGIN, angle);
	}
	
	public Beamer(Point2D coord, double angle) {
		super(coord, angle);
		this.angle = angle;
		update();
	}

	@Override
	protected void update() {
		Polyline2D pl = new Polyline2D();
		pl.addPoint(new Point2D(0, 0));
		pl.addPoint(new Point2D(-LENGTH, WIDTH/2));
		pl.addPoint(new Point2D(-LENGTH, -WIDTH/2));
		pl.addPoint(new Point2D(0, 0));
		Transform2D tr = new Transform2D(coord, angle);
		this.pl = pl.getTransformed(tr);
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
			b.setRay (new Ray2D(coord, angle));
			res.add(b);
		} else {
			double step = spread/nbRays;
			double start = angle-spread/2;
			for (int i=0; i<nbRays; i++) {
				Beam b = new Beam(color, rayWidth);
				b.setRay (new Ray2D(coord, start));
				b.setAsLight();
				start+=step;
				res.add(b);				
			}
		}
		return res;
	}

	private boolean hasProduced = false;

	public Beam produceSingleBeam(){
		Beam res;
		if (nbRays==1) {
			if(hasProduced)
				return null;
			res = new Beam(color, rayWidth);
			res.setRay (new Ray2D(coord, angle));
		} else {
			double start = angle-spread/2+spread*MyRandom.next();
			res = new Beam(color, rayWidth);
			res.setRay (new Ray2D(coord, start));
			res.setAsLight();
		}
		hasProduced = true;
		return res;
	}
	
	public void setAsLight(int nbRays, double spreadAngle){
		this.nbRays = nbRays;
		this.spread = spreadAngle;
	}
	
	@Override
	public Polyline2D getShape() {
		return pl;
	}
	
}
