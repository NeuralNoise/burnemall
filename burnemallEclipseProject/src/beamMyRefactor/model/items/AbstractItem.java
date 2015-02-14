package beamMyRefactor.model.items;

import geometry.Point2D;
import geometry.Transform2D;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import tools.LogUtil;

@Root
public abstract class AbstractItem {
	
	protected Point2D coord;
	@Element
	protected double angle;
	
	protected int thickness = 1;
	protected Color color = Color.GRAY;

	public AbstractItem(@Element(name="angle")double angle) {
		this.angle = angle;
	}
	
	public AbstractItem(Point2D coord, double angle){
		this.coord = coord;
		this.angle = angle;
	}
	
	public Point2D getCoord() {
		return coord;
	}

	public void move(Point2D coord) {
		this.coord = coord;
		update();
	}

	public void addAngle(double d) {
		angle += d;
		update();
	}

	public void setAngle(double a) {
		angle = a;
		update();
	}
	
	protected void update(){}
	public void beforeTick() {}
	public abstract Object getShape();

	public int getThickness() {
		return thickness;
	}

	public Color getColor() {
		return color;
	}

	public boolean canRotate() {
		return true;
	}

	public boolean canMove() {
		return true;
	}
	
	@ElementList
	public final void setXMLCoord(List<Double> coords){
		coord = new Point2D(coords.get(0), coords.get(1));
		update();
	}
	
	@ElementList
	public final List<Double> getXMLCoord(){
		List<Double> res = new ArrayList<>();
		res.add(coord.x);
		res.add(coord.y);
		return res;
	}
	
	protected Transform2D getTranform(){
		return new Transform2D(coord, angle);
	}

}