package beamMyRefactor.model.items.material.circular;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import math.MyRandom;
import tools.LogUtil;
import beamMyRefactor.model.items.ItemPool;
import beamMyRefactor.model.items.immaterial.Path;
import beamMyRefactor.model.items.immaterial.Waypoint;
import beamMyRefactor.model.pathing.PathManager;
import geometry.Point2D;

@Root
public class Sootball extends Destroyable {
	private static final double HEALTH = 5;
	
	@Element
	int pathID;
	@Element
	final double maxSpeed;

	Path path;
	Waypoint actualWaypoint;
	
	Point2D velocity = Point2D.ORIGIN;
	
	public Sootball(@Element(name="maxSpeed")double maxSpeed,
			@Element(name="pathID")int pathID,
			@Element(name="angle")double angle,
			@Element(name="radius") double radius){
		super(Point2D.ORIGIN, angle, radius);
		this.maxSpeed = maxSpeed;
		this.pathID = pathID;
	}
	
	public Sootball(double maxSpeed, Path path) {
		super(Point2D.ORIGIN, 0, MyRandom.between(5, 9));
		this.maxSpeed = maxSpeed;
		this.path = path;
		pathID = path.getID();
		actualWaypoint = path.get(0);
		coord = actualWaypoint.getCoord();
		inithealth = HEALTH;
		health = inithealth;
	}
	public Sootball(Sootball other){
		this(other.maxSpeed, other.path);
	}
	
	@Override
	public void update(){
		super.update();
		if(actualWaypoint == null)
			return;
		Point2D steering = followPath();
		if(Point2D.ORIGIN.equals(steering))
			velocity = steering;
		else
			velocity = velocity.getAddition(steering).getScaled(maxSpeed);
		
		coord = coord.getAddition(velocity);
		super.update();
	}
	
	private Point2D followPath(){
		Point2D target = actualWaypoint.getCoord();
		if(coord.getDistance(target) < 1)
			actualWaypoint = path.getNext(actualWaypoint);
		
		if(actualWaypoint != null)
			return actualWaypoint.getCoord().getSubtraction(coord).getTruncation(1);
		else
			return Point2D.ORIGIN;
	}
	
	@Override
	public void beforeTick() {
		super.beforeTick();
		if(!destroyed())
			update();
	}
	
	public void findPath(PathManager pm){
		path = pm.getPath(pathID);
		actualWaypoint = path.get(0);
	}
}
