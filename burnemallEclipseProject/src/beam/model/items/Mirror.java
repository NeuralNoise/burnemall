package beam.model.items;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.Collection;

import math.geom2d.AffineTransform2D;
import math.geom2d.Angle2D;
import math.geom2d.Point2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.Ray2D;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import beam.model.Beam;
import beam.util.Util;

@Root
public class Mirror extends Reflector {

	@Element
	private double width;
	
	public Mirror(@Element(name="center") Point2D center, @Element(name="width") double width, @Element(name="angle") double angle) {
		super(center, angle);
		this.width=width;
		
		initialShape.addPoint(0, width/5);
		initialShape.addPoint(-width/2, 0);
		initialShape.addPoint(width/2, 0);
		initialShape.close();
		update();
	}
}
