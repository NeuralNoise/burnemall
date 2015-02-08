package beamMyRefactor.controller;

import geometry.Point2D;
import geometry.Segment2D;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import beamMyRefactor.model.Model;
import beamMyRefactor.model.items.Item;
import beamMyRefactor.model.items.geometric.Mirror;
import beamMyRefactor.view.ViewPanel;

public class Controller implements MouseListener, MouseMotionListener {

	private enum Action {NONE, CLICK, RCLICK};
	
	private Model model;
	private ViewPanel view;
	boolean stop =false;
	Point2D modelPoint;
	boolean drag = false;
	long pressionTime;
	double angle;
	
		
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
							try {
								Thread.sleep(20);
								if (action!=Action.NONE && model.getSelectedItem()!=null) {
									if(action == Action.RCLICK){
										if(pressionTime+100 < System.currentTimeMillis())
											model.getSelectedItem().setAngle(angle);
									} else if(drag)
										model.getSelectedItem().move(modelPoint);
								}
								model.tick();
								view.repaint();
							} catch (Exception e) {
								// OK something went wrong... we're not going to stop everything for such a small thing, are we ?
								e.printStackTrace();
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
		Point2D click = model.transformFromScreenToModel(new Point2D(e.getX(), e.getY()));
		model.aimItem(click);
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
		Point2D click = model.transformFromScreenToModel(new Point2D(e.getX(), e.getY()));
		model.aimItem(click);
		model.selectAimed();
		
		if(model.getSelectedItem() != null)
			drag = click.getDistance(model.getSelectedItem().center()) < 20 ? true : false;

		if (e.getButton()==3)
			action = Action.CLICK;
		else 
			action = Action.RCLICK;
		pressionTime = System.currentTimeMillis();
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		Point2D click = model.transformFromScreenToModel(new Point2D(e.getX(), e.getY()));
		model.aimItem(click);
		action = Action.NONE;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Point2D click = model.transformFromScreenToModel(new Point2D(e.getX(), e.getY()));
		if(model.getSelectedItem() != null){
			Segment2D moment = new Segment2D(model.getSelectedItem().center(), click);
			angle = moment.getAngle();
		}
		modelPoint = click;
	}

	@Override 
	public void mouseMoved(MouseEvent e) {
		Point2D click = model.transformFromScreenToModel(new Point2D(e.getX(), e.getY()));
		modelPoint = click;
		model.aimItem(click);
	}

}
