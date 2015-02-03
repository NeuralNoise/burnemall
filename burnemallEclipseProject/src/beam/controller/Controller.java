package beam.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import beam.model.Model;
import beam.model.items.Item;
import beam.model.items.Mirror;
import beam.view.ViewPanel;

public class Controller implements MouseListener, MouseMotionListener {

	private enum Action {NONE, CLICK, RCLICK};
	
	private Model model;
	private ViewPanel view;
	boolean stop =false;
	Point2D point;
		
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
									model.getSelectedItem().addAngle((action==Action.CLICK?1:-1)*Math.PI/200);
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
	public void mouseClicked(MouseEvent arg0) {
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
	}
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton()==3)
			action = Action.CLICK;
		else 
			action = Action.RCLICK;
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		action = Action.NONE;
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
	}

	@Override 
	public void mouseMoved(MouseEvent e) {
		AffineTransform at = model.getScreen2modelAT();
		Item m = model.getNearestItemFromModelSpace(at.transform(e.getPoint(), null));
		model.setSelectedItem(m);
		point = e.getPoint();
	}

}
