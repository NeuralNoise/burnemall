package beam.model.items;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.Collection;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import beam.model.Beam;
import beam.model.ModelUtil;
import beam.util.Angle;
import beam.util.Precision;
import beam.util.Util;
import math.geom2d.AffineTransform2D;
import math.geom2d.Angle2D;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.Ray2D;
import math.geom2d.polygon.Polyline2D;
import math.geom2d.spline.QuadBezierCurve2D;

@Root
public class FacedMirror extends Item {
	
	private static final double RADIUS = 20;
	private static final int NB_FACES = 30;
	private static final double ARC = Angle.toRadians(90);

	Polyline2D pl;
	LineSegment2D intersectedFace;
	private final static Stroke stroke = new BasicStroke(2);

	public FacedMirror(@Element(name="center") Point2D center, @Element(name="angle")  double angle) {
		super(center, angle);
		update();
	}

	@Override
	public void draw(Graphics2D g, AffineTransform2D at) {
		g.setStroke(stroke);
		g.setColor(Color.LIGHT_GRAY);
		pl.transform(at).draw(g);
	}

	@Override
	public Point2D intersect(Ray2D beam) {
		Point2D nearest = null; 
		for(LineSegment2D l : pl.edges()) {
			Point2D i = l.intersection(beam);
			if(i != null &&
					!i.almostEquals(beam.firstPoint(), Precision.APPROX) && 
					(nearest == null || i.distance(beam.firstPoint()) < nearest.distance(beam.firstPoint()))){
				nearest = i;
				intersectedFace = l;
			}
		}
		return nearest;
	}

	@Override
	public Collection<Beam> interact(Beam beam, Point2D intersect) {
		double ang = Angle2D.angle(beam.getRay(), intersectedFace)+Math.PI/2;
		Beam res = new Beam(beam);
		res.setRay (new Ray2D(intersect, beam.getRay().direction().angle()+Math.PI+2*ang));
		return Util.makeCollection(res);
	}

	@Override
	void update() {
		Polyline2D pl = new Polyline2D();
		pl.addVertex(new Point2D(RADIUS, 0));
		for(int i=0;i<NB_FACES; i++)
			pl.addVertex(pl.lastPoint().rotate(new Point2D(0, 0), ARC/NB_FACES));

		AffineTransform2D at = AffineTransform2D.createRotation(angle).chain(AffineTransform2D.createTranslation(center.x(), center.y()));
		this.pl = pl.transform(at);
	}

}
