package beam.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import math.geom2d.Point2D;
import math.geom2d.line.LineSegment2D;

public class Laser {

	List<LineSegment2D> segments = new ArrayList<>();
	
	public void clear() {
		segments.clear();
	}

	public void addSegment(LineSegment2D segment) {
		segments.add(segment);
		
	}

	public Collection<LineSegment2D> segments() {
		return Collections.unmodifiableList(segments);
	}

}
