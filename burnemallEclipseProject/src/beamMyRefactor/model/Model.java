package beamMyRefactor.model;

import geometry.Point2D;
import geometry.Transform2D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import math.MyRandom;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import tools.LogUtil;
import beamMyRefactor.model.items.AbstractItem;
import beamMyRefactor.model.items.immaterial.ItemHolder;
import beamMyRefactor.model.items.immaterial.Path;
import beamMyRefactor.model.items.material.AbstractLightable;
import beamMyRefactor.model.items.material.SootBall;
import beamMyRefactor.model.items.material.Wormhole;
import beamMyRefactor.model.pathing.PathManager;
import beamMyRefactor.model.pathing.Wave;
import beamMyRefactor.util.Recorder;
import beamMyRefactor.util.StopWatch;

@Root
public class Model implements PathManager{

	private static final double MIN_AUTO_HIT_DISTANCE = 0.00001;

	public String fileName;

	@ElementList(required=false)
	private List<AbstractLightable> items = new ArrayList<>();
	private List<Path> paths = new ArrayList<>();
	private List<ItemHolder> holders = new ArrayList<>();
	
	
	private Wave wave;

	private AbstractItem selectedItem = null;
	private AbstractItem aimedItem;
	
	

	// the laser object stores all beams
	private Laser laser = new Laser();
	public int producedBeamCount;

	// this affine transforms screen space to model space
	private Transform2D screen2model = null;

	public Model() {
		wave = new Wave(this);
	}

	// main method : this method updates the model and recomputes all beams for each frame
	public synchronized void tick() {
		StopWatch chrono = new StopWatch("Model");
		
		// - clear all previous beams
		laser.clear();
		wave.update();
		
		// - first step : ask each item if it's able to produce beams and add these beams to the initial list
		List<Beam> activeBeams = new ArrayList<>();
		for (AbstractLightable i : items) {
			i.beforeTick();
			Collection<Beam> b = i.produceBeam();
			activeBeams.addAll(b);
		}
		int iter = 1;
		producedBeamCount = activeBeams.size();
		List<Beam> producedBeams = new ArrayList<>();
		do {
			iter++;
			producedBeams.clear();
			// - for each active beam
			for (Beam activeBeam : activeBeams) {
				AbstractLightable nearestItem = null;
				double nearestDist = Double.MAX_VALUE;
				// - find the nearest interacting item, if any
				for (AbstractLightable item : items) {
					Point2D intersect = item.intersect(activeBeam.ray);
					if (intersect!=null) { 
						double dist = intersect.getDistance(activeBeam.ray.getStart());
						// check minimal distance to avoid infinite intersection with last object
						// the solution is not very good, we should check objects list instead
						if (dist>MIN_AUTO_HIT_DISTANCE && dist<nearestDist) {
							nearestDist = dist;
							nearestItem = item;
						}
					}
				}
				// - if an intersecting item is found
				if (nearestItem!=null) {
					// - compute intersection, interrupts activeBeam at this point, store it in the laser
					Point2D intersect = nearestItem.intersect(activeBeam.ray);
					activeBeam.endPoint = intersect;
					laser.addBeam(activeBeam);
					// - ask item if new beams should be produced, if so, put them in producedBeams, 
					// they will be processed next step
					Collection<Beam> resultBeams = nearestItem.interact(activeBeam, intersect);
					if (resultBeams!=null && !resultBeams.isEmpty())
						producedBeams.addAll(resultBeams);
				} else {
					// - if no intersecting item, store activeBeam as an infinite ray  
					laser.addBeam(activeBeam);
				}
			}
			// - end of active beams, produced beams become active
			activeBeams.clear();
			for(Beam b : producedBeams)
				if(!b.attenuated()){
					activeBeams.add(b);
					producedBeamCount++;
				}
			// - until no new beams have been produced
		} while (producedBeams.size()!=0);

		Recorder.record(chrono);
	}
	
	public Laser getLaser() {
		return laser;
	}
	
//	public Transform2D getToModelTransform() {
//		return screen2model;
//	} 

	public void putToModelTransform(Transform2D tr) {
		screen2model = tr; 
	}

	public void deleteItem(AbstractItem item) {		
		// ugly trick
		if(item instanceof Wormhole)
			items.remove(((Wormhole)item).getBinome());
		// end ugly trick
		items.remove(item);
		for(ItemHolder h : holders)
			h.getItems().remove(item);

	}

	public AbstractItem getNearestItem(Point2D point) {
		AbstractItem res = null;
		double minDist = Double.MAX_VALUE;
		for (AbstractItem m : items) {
			double dist = point.getDistance(m.getCoord());
			if (dist<minDist) {
				minDist = dist;
				res = m;
			}
		}
		return res;
	}

	public Collection<AbstractLightable> getItems() {
		return Collections.unmodifiableList(items);
	}

	public void setSelectedItem(AbstractItem item) {
		this.selectedItem = item;
	}

	public AbstractItem getSelectedItem() {
		return selectedItem;
	}

	public void add(AbstractLightable item) {
		assert item!=null;
		
		// registering special items
		if(item instanceof ItemHolder)
			holders.add((ItemHolder)item);
		if(item instanceof Path)
			paths.add((Path)item);
		
		
		// another ugly trick to link whormholes and itemholders
		if(item instanceof Wormhole)
			for(AbstractItem i : items)
				if(i instanceof Wormhole && ((Wormhole)i).isLone())
					((Wormhole)i).link((Wormhole)item);
		// end ugly trick
		
		if(selectedItem != null && selectedItem instanceof ItemHolder)
			((ItemHolder)selectedItem).attach(item);
		else
			items.add(item);
	}

	public Point2D transformFromScreenToModel(Point2D point) {
		if (screen2model == null || point == null)
			return null;
		return point.getTransformed(screen2model);
	}

	public void aimItem(Point2D point){
		List<AbstractLightable> all = new ArrayList<AbstractLightable>();
		all.addAll(items);
		for(ItemHolder h : holders)
			all.addAll(h.getItems());
		aimedItem = null;
		double minDist = 20;
		for (AbstractItem m : all) {
			double dist = point.getDistance(m.getCoord());
			if (dist<minDist) {
				minDist = dist;
				aimedItem = m;
			}
		}
	}
	
	public void selectAimed(){
		if(aimedItem != null)
			selectedItem = aimedItem;
	}
	
	public AbstractItem getAimedItem(){
		return aimedItem;
	}

	public void deleteAimed() {
		deleteItem(aimedItem);
		aimedItem = null;
	}

	public void addToWave(SootBall sb){
		LogUtil.logger.info("Adding ball at timer "+wave.addSootball(sb)/1000+" seconds."); 
	}
	
	public void restartWave(){
		LogUtil.logger.info("Wave restarted"); 
		wave = new Wave(wave);
		List<AbstractLightable> toRemove = new ArrayList<>();
		for(AbstractLightable i : items)
			if(i instanceof SootBall)
				toRemove.add(i);
		items.removeAll(toRemove);
	}
	public void resetWave(){
		LogUtil.logger.info("Wave reset"); 
		wave = new Wave(this);
		List<AbstractLightable> toRemove = new ArrayList<>();
		for(AbstractLightable i : items)
			if(i instanceof SootBall)
				toRemove.add(i);
		items.removeAll(toRemove);
	}

	@Override
	public int giveID() {
		int id = 0;
		while(true){
			boolean available = true;
			for(Path path : paths)
				if(path.getID() == id){
					available = false;
					break;
				}
			if(available)
				return id;
			else
				id++;
		}
	}

	@Override
	public Path getPath(int id) {
		for(Path path : paths)
			if(path.getID() == id)
				return path;
		throw new RuntimeException("String with id "+id+" doesn't exist.");
	}
}

