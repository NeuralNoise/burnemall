package beamMyRefactor.model.items.material;

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
import beamMyRefactor.model.lighting.Beam;
import beamMyRefactor.util.Util;

public class Randomizer extends AbstractPhotosensitive {

	Circle2D shape;
	
	private long timeFrame = 0;
	private Random rand = new Random();
	private double angle;
	
	public Randomizer(@Element(name="center") Point2D center) {
		super(center, 0);
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
		System.out.println(System.currentTimeMillis()/1000+" - " +angle);
		res.setRay(new Ray2D(out, beam.getRay().getAngle()+angle));
		return Util.makeCollection(res);
	}

	@Override
	protected void update() {
		shape = new Circle2D(new Point2D(0, 0), 5);
		Transform2D tr = new Transform2D(coord, angle);
		shape = shape.getTransformed(tr);
	}
	
	@Override
	public Object getShape() {
		return shape;
	}
}
