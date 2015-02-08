package beamMyRefactor.model.items.geometric;

import geometry.Point2D;
import math.Angle;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class Raindrop extends Refractor {
	static final double MIN_REFRACTION_INDEX = 1.333;
	static final double MAX_REFRACTION_INDEX = 1.38;
	static final double RADIUS = 100;
	static final int FACES = 20;
	
	public Raindrop(@Element(name="center")Point2D center, @Element(name="angle") double angle){
		super(center, angle, MIN_REFRACTION_INDEX, MAX_REFRACTION_INDEX);
		
		initialShape.addPoint(0, RADIUS);
		for(int i=0; i<FACES; i++)
			initialShape.addPoint(initialShape.getLastPoint().getRotation(Angle.FULL/FACES));
		initialShape.smoothNormals();
		update();
	}
}
