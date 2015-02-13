package beamMyRefactor.model.pathing;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import tools.LogUtil;
import beamMyRefactor.model.Model;
import beamMyRefactor.model.items.AbstractItem;
import beamMyRefactor.model.items.ItemPool;
import beamMyRefactor.model.items.material.circular.Sootball;

@Root
public class Wave {

	ItemPool itemPool;
	long startTimer;
	@ElementList
	final public List<Sootball> sootballs;
	@ElementList
	final private List<Long> timers;
	
	int index = 0;
	
	public Wave(@ElementList(name="sootballs")List<Sootball> sootballs,
			@ElementList(name="timers")List<Long> timers){
		startTimer = System.currentTimeMillis();
		this.sootballs = sootballs;
		this.timers = timers;
	}
	
	public Wave(ItemPool itemPool) {
		startTimer = System.currentTimeMillis();
		this.itemPool = itemPool;
		sootballs = new ArrayList<>();
		timers = new ArrayList<>();
	}
	
	public Wave(Wave other){
		itemPool = other.itemPool;
		startTimer = System.currentTimeMillis();
		sootballs = other.sootballs;
		timers = other.timers;
	}
	
	public void update(){
		if(index == sootballs.size())
			return;
		if(startTimer + timers.get(index) < System.currentTimeMillis()){
			itemPool.register(new Sootball(sootballs.get(index)));
			index++;
			update();
		}
	}
	
	public long summon(Sootball sb){
		sootballs.add(sb);
		long t = System.currentTimeMillis()-startTimer;
		timers.add(t);
		LogUtil.logger.info("nb ball : "+sootballs.size());
		return t;
	}
	
	public void restart(){
		startTimer = System.currentTimeMillis();
		itemPool.unregisterAllSootballs();
		index = 0;
	}
	
	public void reset(){
		sootballs.clear();
		timers.clear();
		restart();
	}
	
	public void setPool(ItemPool pool){
		this.itemPool = pool;
	}
}
