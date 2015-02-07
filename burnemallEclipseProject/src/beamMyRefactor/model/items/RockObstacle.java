package beamMyRefactor.model.items;

import geometry.Point2D;
import geometry.Polyline2D;
import geometry.Ray2D;
import geometry.Transform2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Collection;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import beamMyRefactor.model.Beam;
import beamMyRefactor.model.ModelUtil;

@Root
public class RockObstacle extends Item {
	
	private static final double SAMPLES = 8;
	private static final double RADIUS = 5;
	private static final double RADIUS_RANGE = 3;
	
	Polyline2D pl;

	public RockObstacle(@Element(name="center") Point2D center) {
		super(center, 0);
		update();
	}

	@Override
	public Point2D intersect(Ray2D beam) {
		return ModelUtil.nearest(pl.getIntersection(beam).points, beam.getStart());
	}

	@Override
	public Collection<Beam> interact(Beam beam, Point2D intersect) {
		return null;
	}

	@Override
	void update() {
		Polyline2D pl = new Polyline2D();
		double a = 0;
		for(int i=0; i<SAMPLES; i++){
			Point2D proj = Point2D.ORIGIN.getTranslation(a, RADIUS+Math.random()*RADIUS_RANGE);
			pl.addPoint(proj);

			a += Math.PI*2/SAMPLES;
		}
		
		pl.addPoint(pl.getFirstPoint());
		
		Transform2D tr = new Transform2D(center, angle);
		this.pl = pl.getTransformed(tr);

	}

	@Override
	public void beforeTick() {
	}
	
}
