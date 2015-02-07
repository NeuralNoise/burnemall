package beam.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.io.File;

import javax.swing.JFileChooser;

import math.geom2d.Point2D;
import beam.MainFrame;
import beam.MyGeometry.math.Angle;
import beam.model.Model;
import beam.model.ModelSerializer;
import beam.model.items.Beamer;
import beam.model.items.Blackhole;
import beam.model.items.Destroyable;
import beam.model.items.FacetedMirror;
import beam.model.items.Goal;
import beam.model.items.ItemHolder;
import beam.model.items.Mirror;
import beam.model.items.NegativeLens;
import beam.model.items.PositiveLens;
import beam.model.items.Prism;
import beam.model.items.Raindrop;
import beam.model.items.Randomizer;
import beam.model.items.RefractingArea;
import beam.model.items.RockObstacle;
import beam.model.items.TriDiffractor;
import beam.model.items.Wall;
import beam.model.items.Wormhole;
import beam.util.LocalProp;
import beam.view.ViewPanel;

public class EditController implements KeyListener, MouseMotionListener  {
	
	/*
	 * X : supprime
	 * M (hold+move) : déplace
	 */
	
	enum Mode {NONE, MOVING}
	
	private Controller cont;
	private Model model;
	private MainFrame frame;
	private ViewPanel view;

	private Mode mode = Mode.NONE;
	
	public EditController(Controller cont, Model model, MainFrame frame, ViewPanel view) {
		System.out.println("edit listener ok");
		System.out.println("    right clic to select an item");
		System.out.println("    maintain right clic to rotate the selection");
		System.out.println("    maintain left clic to drag an item");
		System.out.println("    'x' to delete an item");
		System.out.println("    ctrl+n to create a new level");
		System.out.println("    ctrl+s to save level");
		System.out.println("    ctrl+l to load a level");
		System.out.println("    shit+m creates a mirror");
		System.out.println("    shit+b creates a laser beamer");
		System.out.println("    shit+v creates a light beamer");
		System.out.println("    shit+a creates a curved mirror");
		System.out.println("    shit+g creates a goal");
		System.out.println("    shit+r creates a randomizer");
		System.out.println("    shit+o creates an obstacle");
		System.out.println("    shit+i creates a wall");
		System.out.println("    shit+d creates a bomb");
		System.out.println("    shit+e creates a refracting area");
		System.out.println("    shit+t creates a triangular diffractor");
		System.out.println("    shit+w creates a wormhole (it will link with another lone wormhole if available)");
		System.out.println("    shit+h creates a blackhole");
		System.out.println("    shit+l creates a positive (converging) lens");
		System.out.println("    shit+k creates a negative (diverging) lens");
		System.out.println("    shit+p creates a prism");
		System.out.println("    shit+s creates a raindrop");
		this.cont = cont;
		this.model = model;
		this.frame = frame;
		this.view = view;
		frame.addKeyListener(this);
		view.addMouseMotionListener(this);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		Point2D spacePoint = cont.spacePoint;
		
		if (e.getKeyChar()=='x') {
			if(model.getSelectedItem() != model.getAimedItem())
				model.deleteAimed();
			else {
				model.deleteItem(model.getSelectedItem());
				model.setSelectedItem(null);
			}
		} else if (e.isShiftDown() && e.getKeyChar()=='M') {
			Mirror m = new Mirror(spacePoint, 50, 0);
			model.add(m);
			model.setSelectedItem(m);
		} else if (e.isShiftDown() && e.getKeyChar()=='B') {
			Beamer b = new Beamer(spacePoint, 0);
			model.add(b);
			model.setSelectedItem(b);
		} else if (e.isShiftDown() && e.getKeyChar()=='V') {
			Beamer b = new Beamer(spacePoint, 0);
			b.setAsLight(50, Angle.toRadians(50));
			model.add(b);
			model.setSelectedItem(b);
		} else if (e.isShiftDown() && e.getKeyChar()=='A') {
			FacetedMirror b = new FacetedMirror(spacePoint,0);
			model.add(b);
			model.setSelectedItem(b);
		} else if (e.isShiftDown() && e.getKeyChar()=='G') {
			Goal b = new Goal(spacePoint);
			model.add(b);
			model.setSelectedItem(b);
		} else if (e.isShiftDown() && e.getKeyChar()=='R') {
			Randomizer b = new Randomizer(spacePoint);
			model.add(b);
			model.setSelectedItem(b);
		} else if (e.isShiftDown() && e.getKeyChar()=='O') {
			RockObstacle b = new RockObstacle(spacePoint);
			model.add(b);
			model.setSelectedItem(b);
		} else if (e.isShiftDown() && e.getKeyChar()=='I') {
			Wall b = new Wall(spacePoint);
			model.add(b);
			model.setSelectedItem(b);
		} else if (e.isShiftDown() && e.getKeyChar()=='D') {
			Destroyable b = new Destroyable(spacePoint);
			model.add(b);
			model.setSelectedItem(b);
		} else if (e.isShiftDown() && e.getKeyChar()=='E') {
			RefractingArea b = new RefractingArea(spacePoint, 100, 200, 0);
			model.add(b);
			model.setSelectedItem(b);
		} else if (e.isShiftDown() && e.getKeyChar()=='T') {
			TriDiffractor b = new TriDiffractor(spacePoint, 0);
			model.add(b);
			model.setSelectedItem(b);
		} else if (e.isShiftDown() && e.getKeyChar()=='W') {
			Wormhole b = new Wormhole(spacePoint);
			model.add(b);
			model.setSelectedItem(b);
		} else if (e.isShiftDown() && e.getKeyChar()=='H') {
			Blackhole b = new Blackhole(spacePoint);
			model.add(b);
			model.setSelectedItem(b);
		} else if (e.isShiftDown() && e.getKeyChar()=='L') {
			PositiveLens b = new PositiveLens(spacePoint, 0, 60, 60);
			model.add(b);
			model.setSelectedItem(b);
		} else if (e.isShiftDown() && e.getKeyChar()=='K') {
			NegativeLens b = new NegativeLens(spacePoint, 0, 100, 100);
			model.add(b);
			model.setSelectedItem(b);
		} else if (e.isShiftDown() && e.getKeyChar()=='P') {
			Prism b = new Prism(spacePoint, 0);
			model.add(b);
			model.setSelectedItem(b);
		} else if (e.isShiftDown() && e.getKeyChar()=='S') {
			Raindrop b = new Raindrop(spacePoint, 0);
			model.add(b);
			model.setSelectedItem(b);
		} else if (e.isShiftDown() && e.getKeyChar()=='1') {
			ItemHolder b = new ItemHolder(spacePoint, 0, 40);
			model.add(b);
			model.setSelectedItem(b);
		} else if (e.isShiftDown() && e.getKeyChar()=='2') {
			ItemHolder b = new ItemHolder(spacePoint, 0, 80);
			model.add(b);
			model.setSelectedItem(b);
		} else if (e.isShiftDown() && e.getKeyChar()=='3') {
			ItemHolder b = new ItemHolder(spacePoint, 0, 120);
			model.add(b);
			model.setSelectedItem(b);
		} else if (e.isShiftDown() && e.getKeyChar()=='4') {
			ItemHolder b = new ItemHolder(spacePoint, 0, 160);
			model.add(b);
			model.setSelectedItem(b);
		}
		
		if (e.isControlDown() && e.getKeyCode()==KeyEvent.VK_N) {
			cont.stop = true;
			this.model = new Model();
			frame.init(model);
			this.view = frame.getViewPanel();
			this.cont = new Controller(model,view);
			mode = Mode.NONE;
			view.addMouseMotionListener(this);
			cont.start();
		} else if (e.isControlDown() && e.getKeyCode()==KeyEvent.VK_S) {
			try {
				if (model.fileName!=null) {
					ModelSerializer.write(model);
					System.out.println("Saved OK");
				} else {
					final JFileChooser fc = new JFileChooser(LocalProp.RESOURCES_PATH);
					int returnVal = fc.showSaveDialog(frame);
					if (returnVal==JFileChooser.APPROVE_OPTION) {
						File f = fc.getSelectedFile();
						model.fileName=f.getCanonicalPath();
						ModelSerializer.write(model);
					}					
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (e.isControlDown() && e.getKeyCode()==KeyEvent.VK_L) {
			final JFileChooser fc = new JFileChooser(LocalProp.RESOURCES_PATH);
			int returnVal = fc.showOpenDialog(frame);
			if (returnVal==JFileChooser.APPROVE_OPTION) {
				File f = fc.getSelectedFile();
				try {
					cont.stop = true;
					this.model = ModelSerializer.load(f);
					frame.init(model);
					this.view = frame.getViewPanel();
					this.cont = new Controller(model,view);
					mode = Mode.NONE;
					view.addMouseMotionListener(this);
					cont.start();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}


}
