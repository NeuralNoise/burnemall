package beamMyRefactor.model.items;

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
		super(Point2D.ORIGIN, 0);
		this.maxSpeed = maxSpeed;
		this.path = path;
		actualWaypoint = path.get(0);
		center = actualWaypoint.coord;
		health = 10;
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
		LogUtil.logger.info("pos : "+center);
		super.update();
	}
	
	private Point2D followPath(){
		Point2D target = actualWaypoint.coord;
		if(center.getDistance(target) < 1)
			actualWaypoint = path.getNext(actualWaypoint);
		
		Point2D steering = actualWaypoint.coord.getSubtraction(center).getTruncation(1); 
		return steering;
		
	}
	
	@Override
	public void beforeTick() {
		super.beforeTick();
		if(!destroyed())
			update();
	}
}
