package beamMyRefactor.model.items.immaterial;

import geometry.Point2D;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import beamMyRefactor.model.items.AbstractItem;

@Root
public class Waypoint extends AbstractItem {

	public Waypoint(@Element(name="angle")double angle){
		super(angle);
	}
	
	public Waypoint(Point2D coord) {
		super(coord, 0);
	}

	@Override
	public Object getShape() {
		return null;
	}
}
