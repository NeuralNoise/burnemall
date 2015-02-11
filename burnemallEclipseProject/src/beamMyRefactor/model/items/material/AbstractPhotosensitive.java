package beamMyRefactor.model.items.material;

import geometry.Point2D;
import geometry.Ray2D;

import java.util.ArrayList;
import java.util.Collection;

import org.simpleframework.xml.Element;

import beamMyRefactor.model.items.AbstractItem;
import beamMyRefactor.model.lighting.Beam;

public abstract class AbstractPhotosensitive extends AbstractItem {

	public AbstractPhotosensitive(@Element(name="angle")double angle) {
		super(angle);
	}

	
	public AbstractPhotosensitive(Point2D coord, double angle) {
		super(coord, angle);
	}

	public abstract Point2D intersect(Ray2D beam);
	public abstract Collection<Beam> interact(Beam beam, Point2D intersect); 
	public Collection<Beam> produceBeam() {
		return new ArrayList<>();
	}
		
}
