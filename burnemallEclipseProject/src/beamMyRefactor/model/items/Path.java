package beamMyRefactor.model.items;
import geometry.Circle2D;
import geometry.Point2D;
import geometry.Ray2D;
import geometry.Segment2D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import beamMyRefactor.model.Beam;
import beamMyRefactor.model.pathing.Waypoint;


public class Path extends Item {

	List<Waypoint> waypoints = new ArrayList<>();
	List<Circle2D> shape = new ArrayList<>();
	List<Segment2D> segments = new ArrayList<>();
	
	public Path(Point2D startPoint) {
		super(startPoint, 0);
		waypoints.add(new Waypoint(startPoint));
		shape.add(new Circle2D(startPoint, 2));
	}

	public Waypoint getNext(Waypoint w) {
		int nextIndex = waypoints.indexOf(w)+1;
		if(nextIndex == waypoints.size())
			return null;
		else
			return waypoints.get(nextIndex);
	}
	
	public Waypoint get(int index){
		return waypoints.get(index);
	}
	
	public void add(Waypoint wp){
		waypoints.add(wp);
		shape.add(new Circle2D(wp.coord, 2));
		segments.add(new Segment2D(wp.coord, waypoints.get(waypoints.indexOf(wp)-1).coord));
	}

	@Override
	public Point2D intersect(Ray2D beam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Beam> interact(Beam beam, Point2D intersect) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getShape() {
		List<Object> res = new ArrayList<>();
		res.addAll(shape);
		res.addAll(segments);
		return res;
	}
	
	@Override
	public boolean canRotate() {
		return false;
	}
	
	@Override
	public boolean canMove() {
		return false;
	}
	
	

}
