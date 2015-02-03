import geometry.Point2D;


public abstract class Item {

	Point2D coord;
	double angle;

	public Item(Point2D coord, double angle) {
		this.coord = coord;
		this.angle = angle;
	}
	
	public abstract void update();
}
