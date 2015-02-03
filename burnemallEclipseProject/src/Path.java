import java.util.ArrayList;


public class Path extends ArrayList<Waypoint>{

	public Waypoint getNext(Waypoint w) {
		int nextIndex = indexOf(w)+1;
		if(nextIndex == size())
			return null;
		else
			return get(nextIndex);
	}
	
	

}
