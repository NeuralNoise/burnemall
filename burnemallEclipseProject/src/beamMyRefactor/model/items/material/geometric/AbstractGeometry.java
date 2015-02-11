package beamMyRefactor.model.items.material.geometric;

import geometry.Facet;
import geometry.Point2D;
import geometry.Polyline2D;
import geometry.Ray2D;
import geometry.Segment2D;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.Collection;

import math.Angle;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import beamMyRefactor.model.items.material.AbstractPhotosensitive;
import beamMyRefactor.model.lighting.Beam;
import collections.FacetSerie;

@Root
public abstract class AbstractGeometry extends AbstractPhotosensitive {
	
	public AbstractGeometry(@Element(name="angle")double angle) {
		this(Point2D.ORIGIN, angle);
	}
	public AbstractGeometry(Point2D coord, double angle) {
		super(coord, angle);
	}
	

	FacetSerie initialShape = new FacetSerie();
	FacetSerie shape = new FacetSerie();
	
	double collisionNormal;
	private final static Stroke stroke = new BasicStroke(2);

	@Override
	public Point2D intersect(Ray2D ray) {
		Facet intersectedFacet = null;
		Point2D nearest = null; 
		for(Facet f : shape) {
			if(f.intersectAtSinglePoint(ray)){
				Point2D i = f.getUniqueIntersection(ray);
				if(i != null &&
						!i.equals(ray.getStart()) && 
						(nearest == null || i.getDistance(ray.getStart()) < nearest.getDistance(ray.getStart()))){
					nearest = i;
					intersectedFacet = f;
				}
			}
		}
		if(nearest == null)
			return null;
		
		double ratio = intersectedFacet.getStart().getDistance(nearest) / intersectedFacet.getLength();
		Point2D n0 = intersectedFacet.getSmoothedNormal0().getScaled(1-ratio);
		Point2D n1 = intersectedFacet.getSmoothedNormal1().getScaled(ratio);
		collisionNormal = n0.getAddition(n1).getAngle();
		if(Angle.getTurn(intersectedFacet.getStart(), intersectedFacet.getEnd(), ray.getStart()) == Angle.CLOCKWISE)
			collisionNormal += Angle.FLAT;


		return nearest;
	}

	@Override
	protected void update() {
		shape.clear();
		for(Facet f : initialShape)
			shape.add(f.getRotation(angle).getTranslation(coord.x, coord.y));
	}
	
	@Override
	public Object getShape() {
		return shape;
	}
}
