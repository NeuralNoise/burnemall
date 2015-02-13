package beamMyRefactor.model;

import geometry.Point2D;
import geometry.Transform2D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import math.MyRandom;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import tools.LogUtil;
import beamMyRefactor.model.items.AbstractItem;
import beamMyRefactor.model.items.ItemPool;
import beamMyRefactor.model.items.immaterial.Path;
import beamMyRefactor.model.items.material.AbstractPhotosensitive;
import beamMyRefactor.model.items.material.ItemHolder;
import beamMyRefactor.model.items.material.circular.Sootball;
import beamMyRefactor.model.items.material.circular.Wormhole;
import beamMyRefactor.model.lighting.Lighter;
import beamMyRefactor.model.pathing.PathManager;
import beamMyRefactor.model.pathing.Wave;
import beamMyRefactor.util.Recorder;
import beamMyRefactor.util.StopWatch;

@Root
public class Model{
	public String fileName;
	
	@Element
	public ItemPool itemPool;
	@Element
	public Wave wave;
	
	public Lighter lighter;

	private AbstractItem selectedItem = null;
	private AbstractItem aimedItem;
	
	// this affine transforms screen space to model space
	private Transform2D screen2model = null;

	public Model(@Element(name="itemPool")ItemPool itemPool,
			@Element(name="wave")Wave wave){
		this.itemPool = itemPool;
		this.wave = wave;
		wave.setPool(itemPool);
		for(Sootball sb : wave.sootballs)
			sb.findPath(itemPool);
		lighter = new Lighter(itemPool);
	}
	
	public Model() {
		itemPool = new ItemPool();
		wave = new Wave(itemPool);
		lighter = new Lighter(itemPool);
	}

	// main method : this method updates the model and recomputes all beams for each frame
	public synchronized void tick() {
		StopWatch chrono = new StopWatch("Model");
		for (AbstractItem i : itemPool.allItems)
			i.beforeTick();

		wave.update();
		lighter.castLight();

		Recorder.record(chrono);
	}
	
	public void putToModelTransform(Transform2D tr) {
		screen2model = tr; 
	}
	public Point2D transformFromScreenToModel(Point2D point) {
		if (screen2model == null || point == null)
			return null;
		return point.getTransformed(screen2model);
	}

	public AbstractItem getNearestItem(Point2D point) {
		AbstractItem res = null;
		double minDist = Double.MAX_VALUE;
		for (AbstractItem m : itemPool.getAllAndHolded()) {
			double dist = point.getDistance(m.getCoord());
			if (dist<minDist) {
				minDist = dist;
				res = m;
			}
		}
		return res;
	}

	public void setSelectedItem(AbstractItem item) {
		this.selectedItem = item;
	}

	public AbstractItem getSelectedItem() {
		return selectedItem;
	}

	public void aimItem(Point2D point){
		aimedItem = null;
		double minDist = 20;
		for (AbstractItem m : itemPool.getAllAndHolded()) {
			double dist = point.getDistance(m.getCoord());
			if(dist<minDist) {
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
		itemPool.unregister(aimedItem);
		aimedItem = null;
	}
}

