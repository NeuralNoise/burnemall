package beamMyRefactor.model.lighting;

import geometry.Point2D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import beamMyRefactor.model.items.AbstractItem;
import beamMyRefactor.model.items.ItemPool;
import beamMyRefactor.model.items.material.AbstractPhotosensitive;


public class Lighter {
	private static final double MIN_AUTO_HIT_DISTANCE = 0.00001;

	private List<Beam> beams = new ArrayList<>();
	final ItemPool pool;
	
	public Lighter(ItemPool pool){
		this.pool = pool;
	}
	
	public void castLight(){
		beams.clear();
		// - first step : ask each item if it's able to produce beams and add these beams to the initial list
		List<Beam> activeBeams = new ArrayList<>();
		for(AbstractPhotosensitive ph : pool.photosensitives) {
			Collection<Beam> b = ph.produceBeam();
			activeBeams.addAll(b);
		}

		List<Beam> producedBeams = new ArrayList<>();
		do {
			producedBeams.clear();
			// - for each active beam
			for (Beam activeBeam : activeBeams) {
				AbstractPhotosensitive nearestItem = null;
				double nearestDist = Double.MAX_VALUE;
				// - find the nearest interacting item, if any
				for (AbstractPhotosensitive ph : pool.photosensitives) {
					Point2D intersect = ph.intersect(activeBeam.ray);
					if (intersect!=null) { 
						double dist = intersect.getDistance(activeBeam.ray.getStart());
						// check minimal distance to avoid infinite intersection with last object
						// the solution is not very good, we should check objects list instead
						if (dist>MIN_AUTO_HIT_DISTANCE && dist<nearestDist) {
							nearestDist = dist;
							nearestItem = ph;
						}
					}
				}
				// - if an intersecting item is found
				if (nearestItem!=null) {
					// - compute intersection, interrupts activeBeam at this point, store it in the laser
					Point2D intersect = nearestItem.intersect(activeBeam.ray);
					activeBeam.endPoint = intersect;
					beams.add(activeBeam);
					// - ask item if new beams should be produced, if so, put them in producedBeams, 
					// they will be processed next step
					Collection<Beam> resultBeams = nearestItem.interact(activeBeam, intersect);
					if (resultBeams!=null && !resultBeams.isEmpty())
						producedBeams.addAll(resultBeams);
				} else {
					// - if no intersecting item, store activeBeam as an infinite ray  
					beams.add(activeBeam);
				}
			}
			// - end of active beams, produced beams become active
			activeBeams.clear();
			for(Beam b : producedBeams)
				if(!b.attenuated())
					activeBeams.add(b);
			// - until no new beams have been produced
		} while (producedBeams.size()!=0);
	}
	
	public List<Beam> getBeams(){
		return beams;
	}
}