package beam.model.items;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Collection;

import math.geom2d.AffineTransform2D;
import math.geom2d.Point2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.conic.EllipseShape2D;
import math.geom2d.line.Ray2D;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import beam.model.Beam;
import beam.model.ModelUtil;

@Root
public class Destroyable extends Item {
	
	private static final double HP = 10;
	private static final double DPS = 5;
	
	EllipseShape2D c;
	
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
	public void draw(Graphics2D g, AffineTransform2D at) {
		g.setColor(col);
		c.transform(at).draw(g);
	}

	@Override
	public Point2D intersect(Ray2D beam) {
		if(destroyed())
			return null;
		
		Collection<Point2D> i = c.intersections(beam);
		if(!i.isEmpty())
			hit = true;
		
		return ModelUtil.nearest(i, beam.firstPoint());
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
		AffineTransform2D at = AffineTransform2D.createRotation(angle).chain(AffineTransform2D.createTranslation(center.x(), center.y()));
		c = c.transform(at);
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
