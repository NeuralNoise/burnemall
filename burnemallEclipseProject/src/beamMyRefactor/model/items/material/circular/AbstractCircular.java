package beamMyRefactor.model.items.material.circular;

import geometry.Circle2D;
import geometry.Facet;
import geometry.Point2D;
import geometry.Polyline2D;
import geometry.Ray2D;
import geometry.Segment2D;
import geometry.Transform2D;
import geometry.intersection.Intersection;

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
public abstract class AbstractCircular extends AbstractPhotosensitive {
	
	@Element
	final double radius;
	
	Circle2D initialShape;
	Circle2D shape;
	
	double collisionNormal;
	
	public AbstractCircular(@Element(name="angle")double angle,
			@Element(name="radius") double radius) {
		this(Point2D.ORIGIN, angle, radius);
	}
	public AbstractCircular(Point2D coord, double radius) {
		this(coord, 0, radius);
	}
	public AbstractCircular(Point2D coord, double angle, double radius) {
		super(coord, angle);
		this.radius = radius;
		initialShape = new Circle2D(Point2D.ORIGIN, radius);
	}

	@Override
	public Point2D intersect(Ray2D ray) {
		Point2D nearest = null; 
		Intersection intr = ray.getIntersection(shape); 
		if(intr.exist()){
			Point2D i = intr.getAll().get(0);
			if(i != null &&
					!i.equals(ray.getStart()) && 
					(nearest == null || i.getDistance(ray.getStart()) < nearest.getDistance(ray.getStart()))){
				nearest = i;
			}
		}
		if(nearest == null)
			return null;
		
		collisionNormal = nearest.getSubtraction(shape.center).getAngle();


		return nearest;
	}

	@Override
	protected void update() {
		shape = initialShape.getTransformed(getTranform());
	}
	
	@Override
	public Object getShape() {
		return shape;
	}
}
