package beam.model.items;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import beam.MyGeometry.geometry.Point2D;

@Root
public class Wall extends GeometricItem {
	
	private static final double THICKNESS = 2.5;
	private static final double EXTENT = 40;

	public Wall(@Element(name="center") math.geom2d.Point2D center) {
		super(center, 0);

		initialShape.addPoint(+THICKNESS, +EXTENT);
		initialShape.addPoint(+THICKNESS, -EXTENT);
		initialShape.addPoint(-THICKNESS, -EXTENT);
		initialShape.addPoint(-THICKNESS, +EXTENT);
		initialShape.close();

		update();
	}
}
