package beamMyRefactor.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
