package beamMyRefactor.model.items.material.circular;

import geometry.Circle2D;
import geometry.Point2D;
import geometry.Ray2D;
import geometry.Transform2D;
import geometry.intersection.Intersection;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Collection;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import tools.LogUtil;
import beamMyRefactor.model.ModelUtil;
import beamMyRefactor.model.items.material.AbstractPhotosensitive;
import beamMyRefactor.model.lighting.Beam;

@Root
public class Destroyable extends AbstractCircular {
	
	private static final double HP = 100;
	private static final double DPS = 5;
	
	double inithealth;

	double health = HP;
	Color initCol;
	double lastHit = 0;
	boolean hit = false;
	

	public Destroyable(@Element(name="angle")double angle,
			@Element(name="radius") double radius) {
		this(Point2D.ORIGIN, angle, radius);
	}
	public Destroyable(Point2D center, double radius) {
		this(center, 0, radius);
	}
	public Destroyable(Point2D center, double angle, double radius) {
		super(center, angle, radius);
		inithealth = HP;
		initialShape = new Circle2D(new Point2D(0, 0), radius);
		initCol = new Color(color.getRed(), color.getGreen(), color.getBlue());
		update();
	}
	
	@Override
	public Point2D intersect(Ray2D ray) {
		if(destroyed())
			return null;
		
		Intersection intr = ray.getIntersection(shape);
		if(intr.exist())
			hit = true;
		
		return ModelUtil.nearest(intr.getAll(), ray.getStart());
	}

	@Override
	public Collection<Beam> interact(Beam beam, Point2D intersect) {
		if(lastHit != 0)
			health -= (System.currentTimeMillis()-lastHit)/1000*DPS;
		lastHit = System.currentTimeMillis();
		
		if(!destroyed())
			color = new Color(initCol.getRed()+(int)((1-health/inithealth)*(255-initCol.getRed())), initCol.getGreen(), initCol.getBlue());
		else
			color = Color.DARK_GRAY;
		
		return null;
	}

	@Override
	protected void update() {
		Transform2D tr = new Transform2D(coord, angle);
		shape = initialShape.getTransformed(tr);
	}

	@Override
	public void beforeTick() {
		if(!hit)
			lastHit = 0;
		hit = false;
	}
	
	boolean destroyed(){
		return health <= 0;
	}
	
	@Override
	public Object getShape() {
		return shape;
	}
}
