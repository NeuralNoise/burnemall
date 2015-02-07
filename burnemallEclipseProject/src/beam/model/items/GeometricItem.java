package beam.model.items;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.Collection;

import math.geom2d.AffineTransform2D;
import math.geom2d.line.Ray2D;
import math.geom2d.polygon.Polyline2D;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import beam.MyGeometry.collections.Chain;
import beam.MyGeometry.collections.FacetSerie;
import beam.MyGeometry.geometry.Facet;
import beam.MyGeometry.geometry.Point2D;
import beam.MyGeometry.geometry.Segment2D;
import beam.MyGeometry.math.Angle;
import beam.model.Beam;
import beam.util.Translator;

@Root
public class GeometricItem extends Item {
	
	FacetSerie initialShape = new FacetSerie();
	FacetSerie shape = new FacetSerie();
	
	double collisionNormal;
	private final static Stroke stroke = new BasicStroke(2);

	public GeometricItem(@Element(name="center") math.geom2d.Point2D center, @Element(name="angle")  double angle) {
		super(center, angle);
		update();
	}

	@Override
	public void draw(Graphics2D g, AffineTransform2D at) {
		g.setStroke(stroke);
		g.setColor(Color.LIGHT_GRAY);
		Polyline2D pl = new Polyline2D();
		for(Facet f : shape)
			Translator.toJavaGeom(f).transform(at).draw(g);
	}

	@Override
	public math.geom2d.Point2D intersect(Ray2D ray) {
		Facet intersectedFacet = null;
		Point2D r0 = new Point2D(ray.firstPoint().x(), ray.firstPoint().y()); 
		Point2D r1 = r0.getTranslation(ray.direction().angle(), 99999); 
		Segment2D myRay = new Segment2D(r0, r1);
		
		Point2D nearest = null; 
		for(Facet f : shape) {
			Point2D i = f.getUniqueIntersection(myRay);
			if(i != null &&
					!i.equals(r0) && 
					(nearest == null || i.getDistance(r0) < nearest.getDistance(r0))){
				nearest = i;
				intersectedFacet = f;
			}
		}
		if(nearest == null)
			return null;
		
		double ratio = intersectedFacet.getStart().getDistance(nearest) / intersectedFacet.getLength();
		Point2D n0 = intersectedFacet.getSmoothedNormal0().getScaled(1-ratio);
		Point2D n1 = intersectedFacet.getSmoothedNormal1().getScaled(ratio);
		collisionNormal = n0.getAddition(n1).getAngle();
		if(Angle.getTurn(intersectedFacet.getStart(), intersectedFacet.getEnd(), Translator.toMyGeom(ray.firstPoint())) == Angle.CLOCKWISE)
			collisionNormal += Angle.FLAT;


		return Translator.toJavaGeom(nearest);
	}

	@Override
	public Collection<Beam> interact(Beam beam, math.geom2d.Point2D intersect) {
		return null;
	}

	@Override
	void update() {
		shape.clear();
		for(Facet f : initialShape)
			shape.add(f.getRotation(angle).getTranslation(center.x(), center.y()));
	}
}
