package beam.model;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import math.geom2d.Point2D;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import beam.model.items.Beamer;
import beam.model.items.Item;
import beam.model.items.ItemHolder;
import beam.model.items.Wormhole;
import beam.util.Recorder;
import beam.util.StopWatch;

/* 
 * Fix #22 <- test :)
 * Cette classe n'est pas si illisible? 170 lignes, essentiellement des getters/setters. Il reste une
 * méthode un peu compliquée. Je l'ai refactorée, commentée et j'ai renommé quelques variables. 
 */
@Root
public class Model {

	private static final double MIN_AUTO_HIT_DISTANCE = 0.00001;

	public String fileName;

	@ElementList
	private List<Item> items = new ArrayList<>();
	private Item selectedItem = null;

	private List<ItemHolder> holders = new ArrayList<>();

	// the laser object stores all beams
	private Laser laser = new Laser();
	public int producedBeamCount;


	// this affine transforms screen space to model space
	private AffineTransform screen2modelAT = null;
	
	Item aimedItem;

	public Model() {
	}

	// main method : this method updates the model and recomputes all beams for each frame
	public synchronized void tick() {
		StopWatch chrono = new StopWatch("Model");
		
		// - clear all previous beams
		laser.clear();
		
		// - first step : ask each item if it's able to produce beams and add these beams to the initial list
		List<Beam> activeBeams = new ArrayList<>();
		for (Item i : items) {
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
				Item nearestItem = null;
				double nearestDist = Double.MAX_VALUE;
				// - find the nearest interacting item, if any
				for (Item item : items) {
					Point2D intersect = item.intersect(activeBeam.ray);
					if (intersect!=null) { 
						double dist = intersect.distance(activeBeam.ray.firstPoint());
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
	
	public AffineTransform getScreen2modelAT() {
		return screen2modelAT;
	} 

	public void putScreen2modelAT(AffineTransform at) {
		screen2modelAT = at; 
	}

	public void deleteItem(Item item) {		
		// ugly trick
		if(item instanceof Wormhole)
			items.remove(((Wormhole)item).getBinome());
		// end ugly trick
		items.remove(item);
		for(ItemHolder h : holders)
			h.getItems().remove(item);
	}

	public Item getNearestItemFromModelSpace(java.awt.geom.Point2D point) {
		Item res = null;
		double minDist = Double.MAX_VALUE;
		Point2D mouse = new Point2D(point);
		for (Item m : items) {
			double dist = mouse.distance(m.getCenter());
			if (dist<minDist) {
				minDist = dist;
				res = m;
			}
		}
		return res;
	}

	public Collection<Item> getItems() {
		return Collections.unmodifiableList(items);
	}

	public void setSelectedItem(Item item) {
		this.selectedItem = item;
	}

	public Item getSelectedItem() {
		return selectedItem;
	}

	public void add(Item item) {
		assert item!=null;
		if(item instanceof ItemHolder)
			holders.add((ItemHolder)item);
		// another ugly trick to link whormholes and itemholders
		if(item instanceof Wormhole)
			for(Item i : items)
				if(i instanceof Wormhole && ((Wormhole)i).isLone())
					((Wormhole)i).link((Wormhole)item);
		// end ugly trick
		if(selectedItem != null && selectedItem instanceof ItemHolder)
			((ItemHolder)selectedItem).attach(item);
		else
			items.add(item);
	}

	public Point2D screenToSpace(java.awt.geom.Point2D point) {
		if (screen2modelAT==null || point==null)
			return null;
		Point2D spacePoint = new Point2D(screen2modelAT.transform(point, null));
		return spacePoint;
	}
	
	public void aimItem(Point2D point){
		List<Item> all = new ArrayList<Item>();
		all.addAll(items);
		for(ItemHolder h : holders)
			all.addAll(h.getItems());
		aimedItem = null;
		double minDist = 20;
		for (Item m : all) {
			double dist = point.distance(m.getCenter());
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
	
	public Item getAimedItem(){
		return aimedItem;
	}

	public void deleteAimed() {
		deleteItem(aimedItem);
		aimedItem = null;
	}
}

