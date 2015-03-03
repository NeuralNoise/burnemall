package beamMyRefactor.controller;

import geometry.Point2D;
import geometry.Segment2D;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javafx.scene.input.TouchPoint;
import tools.LogUtil;
import beamMyRefactor.model.Model;
import beamMyRefactor.model.items.AbstractItem;
import beamMyRefactor.model.items.immaterial.Path;
import beamMyRefactor.model.items.material.circular.Sootball;
import beamMyRefactor.model.items.material.geometric.Mirror;
import beamMyRefactor.view.ViewPanel;

public class Controller implements MouseListener, MouseMotionListener {
	private static final double DEFAULT_FPS = 25;

	private enum Action {NONE, CLICK, RCLICK};
	
	private Model model;
	private ViewPanel view;
	boolean stop =false;
	Point2D modelPoint;
	boolean drag = false;
	long pressionTime;
	double angle;

	
	long lastRepaint;
	double fps = DEFAULT_FPS;

		
	Action action = Action.NONE;
	
	public Controller(Model model, ViewPanel view) {
		this.model = model;
		this.view = view;
		view.addMouseListener(this);
		view.addMouseMotionListener(this);
	}

	public void start() {
		new Thread(
				new Runnable() {
					@Override
					public void run() {
						while (true) {
							AbstractItem sel = model.getSelectedItem();
							if(action != Action.NONE && sel != null){
								boolean toUpdate = false;
								if(action == Action.RCLICK
										&& sel.canRotate() &&
										pressionTime+100 < System.currentTimeMillis()){
									model.getSelectedItem().setAngle(angle);
									toUpdate = true;
								} else if(drag && sel.canMove()){
									model.getSelectedItem().move(modelPoint);
								}
								synchronized (model) {
									model.lighter.beams.clear();
									model.lighter.lightmap.clear();
								}
							}
							model.tick();

							double elpasedTime = System.currentTimeMillis()-lastRepaint;
							if(elpasedTime > 1000/fps){
								view.repaint();
								lastRepaint = System.currentTimeMillis();
							}
							if (stop)
								break;
						}
					}
				}		
			).start();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(model.getAimedItem() == null)
			model.setSelectedItem(null);
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
	}
	@Override
	public void mousePressed(MouseEvent e) {
		model.selectAimed();
		if (e.getButton()==3){
			action = Action.CLICK;
			if(model.getSelectedItem() != null)
				drag = modelPoint.getDistance(model.getSelectedItem().getCoord()) < 20 ? true : false;
		}else {
			action = Action.RCLICK;
			drag = false;
		}
		pressionTime = System.currentTimeMillis();
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		action = Action.NONE;
		if(model.getAimedItem() instanceof Path)
			model.wave.summon(new Sootball(0.3, (Path)model.getAimedItem()));
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		updateAiming(new Point2D(e.getX(), e.getY()));
		if(model.getSelectedItem() != null){
			Segment2D moment = new Segment2D(model.getSelectedItem().getCoord(), modelPoint);
			angle = moment.getAngle();
		}
	}

	@Override 
	public void mouseMoved(MouseEvent e) {
		updateAiming(new Point2D(e.getX(), e.getY()));
	}
	
	private void updateAiming(Point2D mouseCoord){
		Point2D click = model.transformFromScreenToModel(mouseCoord);
		modelPoint = click;
		model.aimItem(click);
	}

}
