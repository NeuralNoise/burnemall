package beam.model.items;

import math.geom2d.Point2D;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class RefractingArea extends Refractor {

	private static double REFRACTION_INDEX = 1.5;
	@Element
	double width;

	@Element
	double height;
	
	public RefractingArea(@Element(name="center") Point2D center, @Element(name="width") double width, @Element(name="height") double height, @Element(name="angle") double angle) {
		super(center, angle, REFRACTION_INDEX, REFRACTION_INDEX);
		this.width = width;
		this.height = height;
		
		initialShape.addPoint(-width/2, -height/2);
		initialShape.addPoint(+width/2, -height/2);
		initialShape.addPoint(+width/2, +height/2);
		initialShape.addPoint(-width/2, +height/2);
		initialShape.close();
		
		update();
	}
}
