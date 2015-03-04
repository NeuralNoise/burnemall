package beamMyRefactor.model.items.material.geometric;

import geometry.Point2D;
import geometry.Ray2D;

import java.util.ArrayList;
import java.util.Collection;

import math.Angle;
import math.MyRandom;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import beamMyRefactor.model.lighting.Beam;


@Root
public class Diffusor extends AbstractGeometry {
	private static int DIFFUSION_COUNT = 10;

	
	public Diffusor(@Element(name="angle") double angle) {
		this(Point2D.ORIGIN, angle);
	}
	public Diffusor(Point2D coord, double angle) {
		super(coord, angle);
	}


	@Override
	public Collection<Beam> interact(Beam beam, Point2D intersect) {
		ArrayList<Beam> res = new ArrayList<>();
		double randomAngle = Angle.normalize(collisionNormal + Angle.RIGHT*MyRandom.between(-1d, +1d));
		Beam b = new Beam(beam);
		b.setRay(new Ray2D(intersect, randomAngle));
		res.add(b);
		return res;
	}
}
