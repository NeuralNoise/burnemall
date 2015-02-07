package beam.model.items;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import beam.MyGeometry.geometry.Point2D;

@Root
public class PositiveLens extends Lens {

	public PositiveLens(@Element(name="center") math.geom2d.Point2D center, @Element(name="angle") double angle, @Element(name="leftRadius") double leftRadius, @Element(name="rightRadius") double rightRadius){
		super(center, angle, leftRadius, rightRadius);

		// left diopter
		initialShape.addPoint(-THICKNESS, EXTENT);
		if(leftRadius >= MAX_RADIUS)
			initialShape.addPoint(-THICKNESS, -EXTENT);
		else {
			double arc = Math.asin(EXTENT/leftRadius);
			Point2D pivot = new Point2D(Math.cos(arc)*leftRadius-THICKNESS, 0);
			for(int i=0;i<NB_FACES; i++)
				initialShape.addPoint(initialShape.getLastPoint().getRotation(arc*2/NB_FACES, pivot));
		}
		
		// right diopter
		initialShape.addPoint(+THICKNESS, -EXTENT);
		if(rightRadius >= MAX_RADIUS)
			initialShape.addPoint(+THICKNESS, EXTENT);
		else {
			double arc = Math.asin(EXTENT/rightRadius);
			Point2D pivot = new Point2D(-Math.cos(arc)*rightRadius+THICKNESS, 0);
			for(int i=0;i<NB_FACES; i++)
				initialShape.addPoint(initialShape.getLastPoint().getRotation(arc*2/NB_FACES, pivot));
		}
		
		initialShape.close();
		initialShape.smoothNormals();
		update();
	}
}
