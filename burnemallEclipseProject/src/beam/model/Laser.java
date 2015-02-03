package beam.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import math.geom2d.Point2D;
import math.geom2d.line.LineSegment2D;

public class Laser {

	List<Beam> beams = new ArrayList<>();
	
	public void clear() {
		beams.clear();
	}

	public Collection<Beam> beams() {
		return Collections.unmodifiableList(beams);
	}

	public void addBeam(Beam beam) {
		beams.add(beam);
	}

}
