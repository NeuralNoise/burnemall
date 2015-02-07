package beam.model.items;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import beam.MyGeometry.geometry.Point2D;

@Root
public class NegativeLens extends Lens {

	public NegativeLens(@Element(name="center") math.geom2d.Point2D center, @Element(name="angle") double angle, @Element(name="leftRadius") double leftRadius, @Element(name="rightRadius") double rightRadius){
		super(center, angle, leftRadius, rightRadius);

		// left diopter
		if(leftRadius >= MAX_RADIUS) {
			initialShape.addPoint(-THICKNESS, EXTENT);
			initialShape.addPoint(-THICKNESS, -EXTENT);
		} else {
			double arc = Math.asin(EXTENT/leftRadius);
			double arcStep = -arc*2/NB_FACES;
			Point2D pivot = new Point2D(-leftRadius-THICKNESS, 0);
			initialShape.addPoint(new Point2D(-(1-Math.cos(arc))*leftRadius-THICKNESS, EXTENT));
			for(int i=0;i<NB_FACES; i++)
				initialShape.addPoint(initialShape.getLastPoint().getRotation(arcStep, pivot));
		}
		
		// right diopter
		if(rightRadius >= MAX_RADIUS) {
			initialShape.addPoint(+THICKNESS, -EXTENT);
			initialShape.addPoint(+THICKNESS, EXTENT);
		} else {
			double arc = Math.asin(EXTENT/rightRadius);
			double arcStep = -arc*2/NB_FACES;
			Point2D pivot = new Point2D(rightRadius+THICKNESS, 0);
			initialShape.addPoint(new Point2D((1-Math.cos(arc))*rightRadius+THICKNESS, -EXTENT));
			for(int i=0;i<NB_FACES; i++)
				initialShape.addPoint(initialShape.getLastPoint().getRotation(arcStep, pivot));
		}
		
		initialShape.close();
		initialShape.smoothNormals();
		update();
	}
}
