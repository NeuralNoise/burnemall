package beamMyRefactor.model.items.material;

import geometry.Point2D;
import geometry.Polyline2D;
import geometry.Ray2D;
import geometry.Segment2D;
import geometry.Transform2D;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import math.Angle;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import beamMyRefactor.model.Beam;

@Root
public class TriDiffractor extends AbstractLightable {

	private final static double WIDTH = 10;
	
	Polyline2D pl;
	Segment2D collided;
	public ArrayList<Beam> beams = new ArrayList<>();
	
	public TriDiffractor(@Element(name="center") Point2D center, @Element(name="angle") double angle) {
		super(center, angle);
		update();
	}

	@Override
	protected void update() {
		Polyline2D pl = new Polyline2D();
		pl.addPoint(Point2D.ORIGIN.getTranslation(0, WIDTH));
		pl.addPoint(Point2D.ORIGIN.getTranslation(Angle.FULL/3, WIDTH));
		pl.addPoint(Point2D.ORIGIN.getTranslation(2*Angle.FULL/3, WIDTH));
		pl.addPoint(pl.getFirstPoint());
		
		Transform2D tr = new Transform2D(coord, angle);
		this.pl = pl.getTransformed(tr);
	}

	@Override
	public Point2D intersect(Ray2D beam) {
		Point2D nearest = null;
		for(Segment2D l : pl){
			if(l.intersectAtSinglePoint(beam)){
				Point2D i = l.getUniqueIntersection(beam);
				if(i != null &&
						(nearest == null ||
						i.getDistance(beam.getStart()) < nearest.getDistance(beam.getStart()))){
					nearest = i;
					collided = l;
				}
			}
		}
		return nearest;
	}

	@Override
	public Collection<Beam> interact(Beam beam, Point2D intersect) {
		if(beams.size() == 2)
			return null;
		for(Segment2D s : pl){
			if(s.equals(collided))
				continue;
//			Beam b = new Beam(beam.getColor(), beam.getWidth());
			Beam b = new Beam(beam);
			b.setRay(new Ray2D(s.getMiddle(), s.getAngle()-Angle.RIGHT));
			beams.add(b);
		}
		return Collections.unmodifiableCollection(beams);
	}
	
	@Override
	public void beforeTick() {
		beams.clear();
	}
	
	@Override
	public Object getShape() {
		return pl;
	}

}
