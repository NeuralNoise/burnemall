package beamMyRefactor.model.items.immaterial;
import geometry.Circle2D;
import geometry.Point2D;
import geometry.Ray2D;
import geometry.Segment2D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import beamMyRefactor.model.items.AbstractItem;
import beamMyRefactor.model.items.material.AbstractPhotosensitive;
import beamMyRefactor.model.lighting.Beam;
import beamMyRefactor.model.pathing.PathManager;


public class Path extends AbstractItem {

	@Element
	private int id;
	
	@ElementList
	List<Waypoint> waypoints = new ArrayList<>();
	
	
	List<Circle2D> shape = new ArrayList<>();
	List<Segment2D> segments = new ArrayList<>();
	
	public Path(@Element(name="angle")double angle,
			@Element(name="id")int id,
			@ElementList(name="waypoints")List<Waypoint> waypoints) {
		super(0);
		for(Waypoint wp : waypoints)
			add(wp);
		this.id = id;
	}
	
	public Path(Point2D startPoint, PathManager manager){
		this(0, manager.givePathID(), new ArrayList<>());
		coord = startPoint;
		add(new Waypoint(startPoint));
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
		shape.add(new Circle2D(wp.getCoord(), 2));
		if(waypoints.size() > 1)
			segments.add(new Segment2D(wp.getCoord(), waypoints.get(waypoints.indexOf(wp)-1).getCoord()));
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
	
	public int getID(){
		return id;
	}
	
	

}
