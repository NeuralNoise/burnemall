package beam.model.items;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Collection;

import math.geom2d.AffineTransform2D;
import math.geom2d.Angle2D;
import math.geom2d.Point2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.Ray2D;
import math.geom2d.polygon.Polyline2D;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import beam.model.Beam;
import beam.util.Angle;
import beam.util.Precision;
import beam.util.Util;

@Root
public class Lens extends Refractor {

	static final double REFRACTION_INDEX = 0.6;
	static final int NB_FACES = 20;
	static final double EXTENT = 20; 
	static final double THICKNESS = 2.5;
	static final double MAX_RADIUS = EXTENT*5;
	
	@Element
	double leftRadius;
	
	@Element
	double rightRadius;
	
	public Lens(@Element(name="center") Point2D center, @Element(name="angle") double angle, @Element(name="leftRadius") double leftRadius, @Element(name="leftRadius") double rightRadius){
		super(center, angle, REFRACTION_INDEX);
		if(leftRadius<EXTENT || rightRadius<EXTENT)
			throw new IllegalArgumentException("Radii of the lens's diopters can't be smaller than lens extent (default "+EXTENT+").");

		this.leftRadius = leftRadius;
		this.rightRadius = rightRadius;
		update();
	}
}
