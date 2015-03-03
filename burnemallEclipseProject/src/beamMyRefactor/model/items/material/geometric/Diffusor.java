package beamMyRefactor.model.items.material.geometric;

import geometry.Point2D;
import geometry.Ray2D;

import java.util.ArrayList;
import java.util.Collection;

import math.Angle;

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
		double angle = collisionNormal + Angle.RIGHT;
		for(int i = 0; i<=DIFFUSION_COUNT; i++){
			angle -= Angle.FLAT*0.99/DIFFUSION_COUNT;
			Beam b = new Beam(beam);
			b.intensity = beam.intensity*0.5;
			b.setRay(new Ray2D(intersect, angle));
			res.add(b);
		}
		return res;
	}
}
