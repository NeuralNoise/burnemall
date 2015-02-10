package beamMyRefactor.model.items;

import math.MyRandom;
import tools.LogUtil;
import beamMyRefactor.model.pathing.Waypoint;
import geometry.Point2D;

public class SootBall extends Destroyable {
	
	double speed = 0;
	final double maxSpeed;
	final Path path;
	Waypoint actualWaypoint;
	
	Point2D velocity = Point2D.ORIGIN;
	
	public SootBall(double maxSpeed, Path path) {
		super(Point2D.ORIGIN, 0, MyRandom.between(5, 9));
		this.maxSpeed = maxSpeed;
		this.path = path;
		actualWaypoint = path.get(0);
		center = actualWaypoint.coord;
		health = 10;
		inithealth = 10;
		
	}
	public SootBall(SootBall other){
		super(Point2D.ORIGIN, 0, MyRandom.between(5, 9));
		maxSpeed = other.maxSpeed;
		path = other.path;
		actualWaypoint = path.get(0);
		center = actualWaypoint.coord;
		health = 10;
		inithealth = 10;
	}
	
	@Override
	public void update(){
		if(actualWaypoint == null)
			return;
		Point2D steering = followPath();
		if(Point2D.ORIGIN.equals(steering))
			velocity = steering;
		else
			velocity = velocity.getAddition(steering).getScaled(maxSpeed);
		
		center = center.getAddition(velocity);
		super.update();
	}
	
	private Point2D followPath(){
		Point2D target = actualWaypoint.coord;
		if(center.getDistance(target) < 1)
			actualWaypoint = path.getNext(actualWaypoint);
		
		if(actualWaypoint != null)
			return actualWaypoint.coord.getSubtraction(center).getTruncation(1);
		else
			return Point2D.ORIGIN;
	}
	
	@Override
	public void beforeTick() {
		super.beforeTick();
		if(!destroyed())
			update();
	}
}
