package beam.model.items;

import geometry.Circle2D;
import geometry.Point2D;
import geometry.Ray2D;
import geometry.Transform2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Collection;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import beam.model.Beam;
import beam.model.ModelUtil;

@Root
public class Destroyable extends Item {
	
	private static final double HP = 10;
	private static final double DPS = 5;
	
	Circle2D c;
	
	double h = HP;
	Color initCol = Color.gray;
	Color col = initCol;
	double lastHit = 0;
	boolean hit = false;

	public Destroyable(@Element(name="center") Point2D center) {
		super(center, 0);
		update();
	}

	@Override
	public Point2D intersect(Ray2D beam) {
		if(destroyed())
			return null;
		
		Collection<Point2D> i = beam.getIntersectionsWithCircle(c);
		if(!i.isEmpty())
			hit = true;
		
		return ModelUtil.nearest(i, beam.getStart());
	}

	@Override
	public Collection<Beam> interact(Beam beam, Point2D intersect) {
		if(lastHit != 0)
			h -= (System.currentTimeMillis()-lastHit)/1000*DPS;
		lastHit = System.currentTimeMillis();
		
		if(!destroyed())
			col = new Color(initCol.getRed()+(int)((1-h/HP)*(255-initCol.getRed())), initCol.getGreen(), initCol.getBlue());
		else
			col = Color.DARK_GRAY;
		
		return null;
	}

	@Override
	void update() {
		c = new Circle2D(new Point2D(0, 0), 5);
		Transform2D tr = new Transform2D(center, angle);
		c = c.getTransformed(tr);
	}

	@Override
	public void beforeTick() {
		if(!hit)
			lastHit = 0;
	}
	
	boolean destroyed(){
		return h <= 0;
	}
}
