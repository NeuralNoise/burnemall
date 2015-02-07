package beam.model.items;

import math.geom2d.Point2D;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import beam.MyGeometry.math.Angle;

@Root
public class Raindrop extends Refractor {
	static final double MIN_REFRACTION_INDEX = 1.333;
	static final double MAX_REFRACTION_INDEX = 1.38;
	static final double RADIUS = 100;
	static final int FACES = 20;
	
	public Raindrop(@Element(name="center") Point2D center, @Element(name="angle") double angle){
		super(center, angle, MIN_REFRACTION_INDEX, MAX_REFRACTION_INDEX);
		
		initialShape.addPoint(0, RADIUS);
		for(int i=0; i<FACES; i++)
			initialShape.addPoint(initialShape.getLastPoint().getRotation(Angle.FULL/FACES));
		initialShape.smoothNormals();
		update();
	}
}
