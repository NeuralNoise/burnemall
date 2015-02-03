import geometry.Point2D;


public class SootBall extends Item {
	
	double speed = 0;
	final double maxSpeed;
	final Path path;
	Waypoint actualWaypoint;
	
	Point2D velocity;
	
	public SootBall(Point2D coord, double angle, double maxSpeed, Path path) {
		super(coord, angle);
		this.maxSpeed = maxSpeed;
		this.path = path;
		actualWaypoint = path.get(0);
	}
	
	@Override
	public void update(){
		Point2D steering = followPath();
		if(Point2D.ORIGIN.equals(steering))
			velocity = steering;
		else
			velocity = velocity.getAddition(steering).getScaled(maxSpeed);
		
		coord = coord.getAddition(velocity);
	}
	
	private Point2D followPath(){
		if(coord.getDistance(actualWaypoint.coord) < 0.1)
			actualWaypoint = path.getNext(actualWaypoint);
		if(actualWaypoint == null)
			return Point2D.ORIGIN;
		
		Point2D steering = actualWaypoint.coord.getSubtraction(coord).getTruncation(1); 
		return steering;
		
	}

	
}
