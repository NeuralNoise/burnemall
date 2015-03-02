package beamMyRefactor.model.items.material.geometric;

import java.util.Collection;

import geometry.Point2D;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import tools.LogUtil;
import beamMyRefactor.model.lighting.Beam;

@Root
public class Wall extends Diffusor {
	
	private static final double THICKNESS = 2.5;
	private static final double EXTENT = 40;

	public Wall(@Element(name="angle")double angle) {
		this(Point2D.ORIGIN, angle);
	}
	
	public Wall(Point2D coord, double angle) {
		super(coord, angle);

		initialShape.addPoint(+THICKNESS, +EXTENT);
		initialShape.addPoint(+THICKNESS, -EXTENT);
		initialShape.addPoint(-THICKNESS, -EXTENT);
		initialShape.addPoint(-THICKNESS, +EXTENT);
		initialShape.close();
		update();
	}
}
