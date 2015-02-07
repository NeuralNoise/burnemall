package beam.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import math.geom2d.Point2D;
import math.geom2d.line.LineSegment2D;

import beam.MyGeometry.geometry.Segment2D;
import beam.model.Model;
import beam.model.items.Item;
import beam.view.ViewPanel;

public class Controller implements MouseListener, MouseMotionListener {

	private enum Action {NONE, CLICK, RCLICK};
	
	private Model model;
	private ViewPanel view;
	boolean stop =false;
	double angle;
	Point2D spacePoint;
	boolean drag = false;
	long pressionTime;
		
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
										model.getSelectedItem().move(spacePoint);
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
		Point2D clickLoc = model.screenToSpace(e.getPoint());
		model.aimItem(clickLoc);
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
		Point2D clickLoc = model.screenToSpace(e.getPoint());
		model.aimItem(clickLoc);
		model.selectAimed();
		
		if(model.getSelectedItem() != null)
			drag = clickLoc.distance(model.getSelectedItem().center()) < 20 ? true : false;

		if (e.getButton()==3)
			action = Action.CLICK;
		else 
			action = Action.RCLICK;
		pressionTime = System.currentTimeMillis();
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		Point2D clickLoc = model.screenToSpace(e.getPoint());
		model.aimItem(clickLoc);
		action = Action.NONE;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Point2D clickLoc = model.screenToSpace(e.getPoint());
		if(model.getSelectedItem() != null){
			LineSegment2D moment = new LineSegment2D(model.getSelectedItem().center(), new math.geom2d.Point2D(clickLoc.getX(), clickLoc.getY()));
			angle = moment.direction().angle();
		}
		spacePoint = clickLoc;
	}

	@Override 
	public void mouseMoved(MouseEvent e) {
		Point2D clickLoc = model.screenToSpace(e.getPoint());
		spacePoint = clickLoc;
		model.aimItem(clickLoc);
	}

}
