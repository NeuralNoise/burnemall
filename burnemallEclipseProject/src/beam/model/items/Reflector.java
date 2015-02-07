package beam.model.items;

import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.Point2D;
import math.geom2d.line.Ray2D;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import beam.MyGeometry.math.Angle;
import beam.model.Beam;

@Root
public class Reflector extends GeometricItem {

	
	public Reflector(@Element(name="center") Point2D center, @Element(name="angle") double angle) {
		super(center, angle);
	}


	@Override
	public Collection<Beam> interact(Beam beam, Point2D intersect) {
		ArrayList<Beam> res = new ArrayList<>();
		double incident = Angle.getOrientedDifference(collisionNormal, beam.getRay().direction().angle()+Angle.FLAT);
		Beam b = new Beam(beam);
		b.setRay(new Ray2D(intersect, collisionNormal-incident));
		res.add(b);
		return res;
	}
}
