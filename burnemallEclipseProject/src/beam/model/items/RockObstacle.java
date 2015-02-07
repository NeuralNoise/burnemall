package beam.model.items;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import beam.MyGeometry.geometry.Point2D;

@Root
public class RockObstacle extends GeometricItem {
	
	private static final double SAMPLES = 8;
	private static final double RADIUS = 5;
	private static final double RADIUS_RANGE = 3;

	public RockObstacle(@Element(name="center") math.geom2d.Point2D center) {
		super(center, 0);
		
		double a = 0;
		for(int i=0; i<SAMPLES; i++){
			initialShape.addPoint(Point2D.ORIGIN.getTranslation(a, RADIUS+Math.random()*RADIUS_RANGE));
			a += Math.PI*2/SAMPLES;
		}
		initialShape.close();

		update();
	}
}
