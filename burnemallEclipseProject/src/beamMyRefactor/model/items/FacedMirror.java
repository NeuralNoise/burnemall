package beamMyRefactor.model.items;

import geometry.Point2D;
import geometry.Polyline2D;
import geometry.Ray2D;
import geometry.Segment2D;
import geometry.Transform2D;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.Collection;

import math.Angle;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import beamMyRefactor.model.Beam;
import beamMyRefactor.model.ModelUtil;
import beamMyRefactor.util.Precision;
import beamMyRefactor.util.Util;

@Root
public class FacedMirror extends Item {
	
	private static final double RADIUS = 20;
	private static final int NB_FACES = 30;
	private static final double ARC = Angle.toRadians(90);

	Polyline2D pl;
	Segment2D intersectedFace;
	private final static Stroke stroke = new BasicStroke(2);

	public FacedMirror(@Element(name="center") Point2D center, @Element(name="angle")  double angle) {
		super(center, angle);
		update();
	}

	@Override
	public Point2D intersect(Ray2D beam) {
		Point2D nearest = null; 
		for(Segment2D l : pl) {
			if(beam.intersectAtSinglePoint(l)){
				Point2D i = l.getUniqueIntersection(l);
				if(i != null &&
						!i.equals(beam.getStart()) && 
						(nearest == null || i.getDistance(beam.getStart()) < nearest.getDistance(beam.getStart()))){
					nearest = i;
					intersectedFace = l;
				}
			}
		}
		return nearest;
	}

	@Override
	public Collection<Beam> interact(Beam beam, Point2D intersect) {
		double ang = Angle.getSmallestDifference(beam.getRay().getAngle(), intersectedFace.getAngle())+Math.PI/2;
		Beam res = new Beam(beam);
		res.setRay (new Ray2D(intersect, beam.getRay().getAngle()+Math.PI+2*ang));
		return Util.makeCollection(res);
	}

	@Override
	void update() {
		Polyline2D pl = new Polyline2D();
		pl.addPoint(new Point2D(RADIUS, 0));
		for(int i=0;i<NB_FACES; i++)
			pl.addPoint(pl.getLastPoint().getRotation(ARC/NB_FACES, Point2D.ORIGIN));

		Transform2D tr = new Transform2D(center, angle);
		this.pl = pl.getTransformed(tr);
	}

}
