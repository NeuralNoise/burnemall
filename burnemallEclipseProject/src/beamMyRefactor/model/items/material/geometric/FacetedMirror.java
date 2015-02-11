package beamMyRefactor.model.items.material.geometric;

import geometry.Point2D;
import geometry.Ray2D;
import java.util.Collection;

import math.Angle;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import beamMyRefactor.model.Beam;
import beamMyRefactor.util.Util;

@Root
public class FacetedMirror extends GeometricItem {
	private static final double EXTENT = 40;
	private static final int NB_FACES = 8;
	private static final double ARC = Angle.toRadians(90);

	public FacetedMirror(@Element(name="center")Point2D center, @Element(name="angle")double angle) {
		super(center, angle);

		Point2D pivot;
		if(ARC < Angle.FLAT){
			initialShape.addPoint(0, -Math.sin(ARC/2)*EXTENT);
			pivot = new Point2D(-Math.cos(ARC/2)*EXTENT, 0);
		} else {
			initialShape.addPoint(Math.cos(ARC/2)*EXTENT, -Math.sin(ARC/2)*EXTENT);
			pivot = new Point2D(0, 0);
		}
			
		for(int i=0;i<NB_FACES; i++)
			initialShape.addPoint(initialShape.getLastPoint().getRotation(ARC/NB_FACES, pivot));

		initialShape.smoothNormals();
		update();
	}

	@Override
	public Collection<Beam> interact(Beam beam, Point2D intersect) {
		double incident = Angle.getOrientedDifference(collisionNormal, beam.getRay().getAngle()+Angle.FLAT);

		Beam res = new Beam(beam);
		res.setRay(new Ray2D(intersect, collisionNormal-incident));
		return Util.makeCollection(res);
	}
}
