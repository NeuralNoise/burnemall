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
import beam.util.Util;
import math.geom2d.AffineTransform2D;
import math.geom2d.Angle2D;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.line.Ray2D;
import math.geom2d.spline.QuadBezierCurve2D;

@Root
public class QuadBezier extends Item {

	QuadBezierCurve2D segment;
	private final static Stroke stroke = new BasicStroke(2);

	public QuadBezier(@Element(name="center") Point2D center, @Element(name="angle")  double angle) {
		super(center, angle);
		update();
	}

	@Override
	public void draw(Graphics2D g, AffineTransform2D at) {
		g.setStroke(stroke);
		g.setColor(Color.LIGHT_GRAY);
		segment.transform(at).draw(g);
	}

	@Override
	public Point2D intersect(Ray2D beam) {
		return ModelUtil.nearest(segment.intersections(beam), beam.firstPoint());
	}

	@Override
	public Collection<Beam> interact(Beam beam, Point2D intersect) {
		double pos = segment.position(intersect);
		Vector2D tangeant = segment.tangent(pos);
		double ang = Angle2D.angle(beam.getRay().direction(),tangeant)+Math.PI/2;
		Beam res = new Beam(beam);
		res.setRay (new Ray2D(intersect, beam.getRay().direction().angle()+Math.PI+2*ang));
		return Util.makeCollection(res);
	}

	@Override
	void update() {
		QuadBezierCurve2D seg = new QuadBezierCurve2D(new Point2D(20,0),new Point2D(0,20), new Point2D(-20,0));
		AffineTransform2D at = AffineTransform2D.createRotation(angle).chain(AffineTransform2D.createTranslation(center.x(), center.y()));
		segment = seg.transform(at);
	}

}
