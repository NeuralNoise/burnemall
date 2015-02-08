package beamMyRefactor.model.items.geometric;

import geometry.Facet;
import geometry.Point2D;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class Prism extends Refractor {

	static final double MIN_REFRACTION_INDEX = 1.3;
	static final double MAX_REFRACTION_INDEX = 1.5;
	static final double ANGLE = 30; 
	static final double RADIUS = 50; 
	
	public Prism(@Element(name="center")Point2D center, @Element(name="angle")double angle){
		super(center, angle, MIN_REFRACTION_INDEX, MAX_REFRACTION_INDEX);
		
		Point2D a = new Point2D(0, RADIUS);
		Point2D b = new Point2D(-RADIUS, -RADIUS);
		Point2D c = new Point2D(RADIUS, -RADIUS);
		initialShape.add(new Facet(a, b));
		initialShape.add(new Facet(b, c));
		initialShape.add(new Facet(c, a));

		update();
	}
}
