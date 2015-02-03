package beam.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;

import beam.model.Mirror;
import beam.model.Model;
import beam.view.View;

public class Controller implements MouseListener {

	private Model model;
	private View view;
	
	public Controller(Model model, View view) {
		this.model = model;
		this.view = view;
		view.getPanel().addMouseListener(this);
	}

	private Mirror moving = null;
	private boolean clockwise = false;
	
	public void start() {
		new Thread(
				new Runnable() {
					@Override
					public void run() {
						while (true) {
							try {
								Thread.sleep(20);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if (moving!=null) {
								moving.addAngle((clockwise?1:-1)*Math.PI/200);
							}
							model.tick();
							view.repaint();
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
		AffineTransform at = model.getReverseTransform();
		Mirror m = model.getNearestMirror(at.transform(e.getPoint(), null));
		moving = m;
		clockwise = e.getButton()==3;
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		moving = null;
	}

}
