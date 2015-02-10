package beamMyRefactor.model.pathing;
import java.util.ArrayList;
import java.util.List;

import tools.LogUtil;
import beamMyRefactor.model.Model;
import beamMyRefactor.model.items.SootBall;


public class Wave {

	final private Model model; 
	final long startTimer;
	final private List<SootBall> sootballs;
	final private List<Long> timers;
	
	int index = 0;
	
	public Wave(Model model) {
		startTimer = System.currentTimeMillis();
		this.model = model;
		sootballs = new ArrayList<>();
		timers = new ArrayList<>();
	}
	
	public Wave(Wave other){
		model = other.model;
		startTimer = System.currentTimeMillis();
		sootballs = other.sootballs;
		timers = other.timers;
	}
	
	public void update(){
		if(index == sootballs.size())
			return;
		if(startTimer + timers.get(index) < System.currentTimeMillis()){
			model.add(new SootBall(sootballs.get(index)));
			index++;
			update();
		}
	}
	
	public long addSootball(SootBall sb){
		sootballs.add(sb);
		long t = System.currentTimeMillis()-startTimer;
		timers.add(t);
		LogUtil.logger.info("nb ball : "+sootballs.size());
		return t;
		
	}
}
