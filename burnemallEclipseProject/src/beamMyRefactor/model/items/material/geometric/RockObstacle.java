package beamMyRefactor.model.items.material.geometric;

import geometry.Point2D;
import geometry.Polyline2D;
import geometry.Ray2D;
import geometry.Transform2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Collection;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import beamMyRefactor.model.ModelUtil;
import beamMyRefactor.model.lighting.Beam;

@Root
public class RockObstacle extends AbstractGeometry {
	
	private static final double SAMPLES = 8;
	private static final double RADIUS = 5;
	private static final double RADIUS_RANGE = 3;
	
	Polyline2D pl;

	public RockObstacle(@Element(name="center") Point2D center) {
		super(center, 0);
		
		double a = 0;
		for(int i=0; i<SAMPLES; i++){
			initialShape.addPoint(Point2D.ORIGIN.getTranslation(a, RADIUS+Math.random()*RADIUS_RANGE));
			a += Math.PI*2/SAMPLES;
		}
		initialShape.close();

		update();
	}

	@Override
	public Collection<Beam> interact(Beam beam, Point2D intersect) {
		// TODO Auto-generated method stub
		return null;
	}
}
