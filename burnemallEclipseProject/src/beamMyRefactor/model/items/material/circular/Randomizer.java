package beamMyRefactor.model.items.material.circular;

import geometry.Circle2D;
import geometry.Point2D;
import geometry.Ray2D;
import geometry.Transform2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Collection;
import java.util.Random;

import org.simpleframework.xml.Element;

import beamMyRefactor.model.ModelUtil;
import beamMyRefactor.model.items.material.AbstractPhotosensitive;
import beamMyRefactor.model.lighting.Beam;
import beamMyRefactor.util.Util;

public class Randomizer extends AbstractCircular {

	private long timeFrame = 0;
	private Random rand = new Random();
	private double angle;
	
	public Randomizer(@Element(name="angle")double angle,
			@Element(name="radius") double radius) {
		this(Point2D.ORIGIN, angle, radius);
	}
	public Randomizer(Point2D coord) {
		this(coord, 0, 5);
	}
	public Randomizer(Point2D coord, double angle, double radius) {
		super(coord, 0, radius);
		update();
	}

	@Override
	public Point2D intersect(Ray2D beam) {
		return ModelUtil.nearest(beam.getIntersection(shape).getAll(), beam.getStart());
	}

	@Override
	public Collection<Beam> interact(Beam beam, Point2D intersect) {
		Point2D out = ModelUtil.farthest(beam.getRay().getIntersection(shape).getAll(), beam.getRay().getStart());
		Beam res = new Beam(beam);
		long timeFrame = System.currentTimeMillis()/300;
		if (this.timeFrame!=timeFrame) {
			this.timeFrame=timeFrame;
			angle = rand.nextDouble()-0.5;
		}
		res.setRay(new Ray2D(out, beam.getRay().getAngle()+angle));
		return Util.makeCollection(res);
	}
}
