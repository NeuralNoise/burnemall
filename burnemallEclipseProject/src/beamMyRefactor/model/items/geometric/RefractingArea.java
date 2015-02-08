package beamMyRefactor.model.items.geometric;

import geometry.Point2D;
import geometry.Polyline2D;
import geometry.Transform2D;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class RefractingArea extends Refractor {

	private static double REFRACTION_INDEX = 1.8;
	@Element
	double width;

	@Element
	double height;
	
	public RefractingArea(@Element(name="center") Point2D center, @Element(name="width") double width, @Element(name="height") double height, @Element(name="angle") double angle) {
		super(center, angle, REFRACTION_INDEX, REFRACTION_INDEX);
		this.width = width;
		this.height = height;
		initialShape.addPoint(new Point2D(-width/2, -height/2));
		initialShape.addPoint(new Point2D(+width/2, -height/2));
		initialShape.addPoint(new Point2D(+width/2, +height/2));
		initialShape.addPoint(new Point2D(-width/2, +height/2));
		initialShape.close();
		update();
	}
}
