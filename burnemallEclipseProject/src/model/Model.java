package beam.model;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import math.geom2d.Angle2D;
import math.geom2d.Point2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.Ray2D;

public class Model {

	private static final double FAR = 500.0;
	
	private Beamer beamer = new Beamer();
	private Laser laser = new Laser();
	private List<Mirror> mirrors = new ArrayList<>();
	private AffineTransform revat = null;
	
//	Mirror mir = new Mirror(new Point2D(30,0),30,Math.PI/2);
	public Model() {
//		mirrors.add(mir);
		mirrors.add(new Mirror(new Point2D(20,20),30,Math.PI));
		mirrors.add(new Mirror(new Point2D(20,-20),30,0));
		mirrors.add(new Mirror(new Point2D(-30,-30),15,-Math.PI/6));
		mirrors.add(new Mirror(new Point2D(30,0),20,Math.PI/2));
	}
	
	public void tick() {
//		beamer.angle += Math.PI/200;
//		mir.addAngle (Math.PI/100);
		laser.clear();
		
		Ray2D beam = new Ray2D(beamer.position, beamer.angle);
		
		boolean mirrorHit = false;
		int iter = 0;
		do {
			iter ++;
//			System.out.println("iteration " + iter);
//			System.out.println(beam);
			mirrorHit = false;
			for (Mirror m : mirrors) {
				Point2D intersect = beam.intersection(m.segment);
				if (intersect!=null && intersect.distance(beam.firstPoint())>0.1) {
//					System.out.println(m);
					double ang = Angle2D.angle(beam,m.axis);
					laser.addSegment(new LineSegment2D(beam.firstPoint(), intersect));
					beam = new Ray2D(intersect, beam.direction().angle()+Math.PI+2*ang);
					mirrorHit = true;
					break;
				}
			}
			if (iter>10000) {
				break;
//				throw new IllegalStateException("Too many beams");
			}
		} while (mirrorHit);
		
		laser.addSegment(new LineSegment2D(beam.firstPoint(), beam.point(FAR)));
	}
	
	public Laser getLaser() {
		return laser;
	}
	
	public Collection<Mirror> segments() {
		return Collections.unmodifiableList(mirrors);
	}

	public Mirror getNearestMirror(java.awt.geom.Point2D point) {
		Mirror res = null;
		double minDist = Double.MAX_VALUE;
		Point2D mouse = new Point2D(point);
		for (Mirror m : mirrors) {
			double dist = mouse.distance(m.center);
			if (dist<minDist) {
				minDist = dist;
				res = m;
			}
		}
		return res;
	}

	public AffineTransform getReverseTransform() {
		return revat;
	}

	public void storeReverseTransform(AffineTransform at) {
		revat = at; 
	}
}

