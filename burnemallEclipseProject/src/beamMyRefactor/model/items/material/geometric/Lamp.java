package beamMyRefactor.model.items.material.geometric;

import java.util.Collection;

import geometry.Point2D;
import geometry.Ray2D;
import math.Angle;
import math.MyRandom;

import org.simpleframework.xml.Element;

import beamMyRefactor.model.MyColor;
import beamMyRefactor.model.lighting.Beam;

public class Lamp extends AbstractGeometry{
	private static double X_EXTENT = 30;
	private static double Y_EXTENT = 2;
	
	private MyColor color = MyColor.get("#63CAEB");
	double incidence = 0;
	
	public Lamp(@Element(name="angle") double angle) {
		this(Point2D.ORIGIN, angle);
	}
	
	public Lamp(Point2D coord, double angle) {
		super(coord, angle);
		this.angle = angle;
		initialShape.addPoint(new Point2D(-X_EXTENT, -Y_EXTENT));
		initialShape.addPoint(new Point2D(X_EXTENT, -Y_EXTENT));
		initialShape.addPoint(new Point2D(X_EXTENT, Y_EXTENT));
		initialShape.addPoint(new Point2D(-X_EXTENT, Y_EXTENT));
		initialShape.close();
		update();
	}
	
	@Override
	public Point2D intersect(Ray2D ray) {
		return null;
	}
	
	@Override
	public Collection<Beam> interact(Beam beam, Point2D intersect) {
		return null;
	}
	
	@Override
	public Beam produceRandomBeam() {
		Beam res;
		Point2D source = coord.getAddition(MyRandom.between(-X_EXTENT, X_EXTENT), MyRandom.between(-Y_EXTENT, Y_EXTENT));
		double randomAngle = MyRandom.between(-Angle.FLAT, Angle.FLAT);
		res = new Beam(color, 1);
		res.setRay (new Ray2D(source, randomAngle));
		res.setAsLight();
		return res;
	}


}
