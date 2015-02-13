package beamMyRefactor.model.items.material.circular;

import geometry.Point2D;
import geometry.Ray2D;
import java.util.Collection;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import beamMyRefactor.model.ModelUtil;
import beamMyRefactor.model.lighting.Beam;

@Root
public class Goal extends AbstractCircular {
	
	boolean hit = true;

	public Goal(@Element(name="angle")double angle,
			@Element(name="radius") double radius) {
		this(Point2D.ORIGIN, angle, radius);
	}
	public Goal(Point2D coord) {
		this(coord, 0, 5);
	}
	public Goal(Point2D coord, double angle, double radius) {
		super(coord, 0, radius);
		update();
	}

	@Override
	public Point2D intersect(Ray2D beam) {
		return ModelUtil.nearest(beam.getIntersection(shape).getAll(), beam.getStart());
	}

	@Override
	public Collection<Beam> interact(Beam beam, Point2D intersect) {
		hit = true;
		return null;
	}

	@Override
	public void beforeTick() {
		hit=false;
	}
}
