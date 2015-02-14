package beamMyRefactor.controller;

import geometry.Point2D;
import geometry.Transform2D;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.io.File;

import javax.swing.JFileChooser;

import tools.LogUtil;
import math.Angle;
import beamMyRefactor.MainFrame;
import beamMyRefactor.model.Model;
import beamMyRefactor.model.ModelSerializer;
import beamMyRefactor.model.items.immaterial.Path;
import beamMyRefactor.model.items.immaterial.Waypoint;
import beamMyRefactor.model.items.material.AbstractPhotosensitive;
import beamMyRefactor.model.items.material.ItemHolder;
import beamMyRefactor.model.items.material.TriDiffractor;
import beamMyRefactor.model.items.material.circular.Blackhole;
import beamMyRefactor.model.items.material.circular.Destroyable;
import beamMyRefactor.model.items.material.circular.Goal;
import beamMyRefactor.model.items.material.circular.Randomizer;
import beamMyRefactor.model.items.material.circular.Sootball;
import beamMyRefactor.model.items.material.circular.Wormhole;
import beamMyRefactor.model.items.material.geometric.Beamer;
import beamMyRefactor.model.items.material.geometric.FacetedMirror;
import beamMyRefactor.model.items.material.geometric.Mirror;
import beamMyRefactor.model.items.material.geometric.NegativeLens;
import beamMyRefactor.model.items.material.geometric.PositiveLens;
import beamMyRefactor.model.items.material.geometric.Prism;
import beamMyRefactor.model.items.material.geometric.Raindrop;
import beamMyRefactor.model.items.material.geometric.RefractingArea;
import beamMyRefactor.model.items.material.geometric.RockObstacle;
import beamMyRefactor.model.items.material.geometric.Wall;
import beamMyRefactor.util.LocalProp;
import beamMyRefactor.view.ViewPanel;

public class EditController implements KeyListener {
	
	/*
	 * X : supprime
	 * M (hold+move) : déplace
	 */
	
	enum Mode {NONE, MOVING}
	
	private Controller cont;
	private Model model;
	private MainFrame frame;
	private ViewPanel view;
	private Path actualPath = null;

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
		System.out.println("    shit+k creates a negative (!ldiverging) lens");
		System.out.println("    shit+p creates a prism");
		System.out.println("    shit+s creates a raindrop");
		this.cont = cont;
		this.model = model;
		this.frame = frame;
		this.view = view;
		frame.addKeyListener(this);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		Point2D modelPoint = cont.modelPoint;
		
		if (e.isShiftDown()){
			AbstractPhotosensitive i = null;
			switch(e.getKeyChar()){
			case 'M' : i = new Mirror(modelPoint, 50, 0); break;
			case 'B' : i = new Beamer(modelPoint, 0); break;
			case 'V' : i = new Beamer(modelPoint, 0);
				((Beamer)i).setAsLight(1000, Angle.toRadians(360));
				break;
			case 'A' : i = new FacetedMirror(modelPoint,0); break;
			case 'G' : i = new Goal(modelPoint); break;
			case 'R' : i = new Randomizer(modelPoint); break;
			case 'O' : i = new RockObstacle(modelPoint); break;
			case 'I' : i = new Wall(modelPoint, 0); break;
			case 'D' : i = new Destroyable(modelPoint, 0, 10); break;
			case 'E' : i = new RefractingArea(modelPoint, 100, 200, 0); break;
			case 'T' : i = new TriDiffractor(modelPoint, 0); break;
			case 'W' : i = new Wormhole(modelPoint); break;
			case 'H' : i = new Blackhole(modelPoint, 0, 5); break;
			case 'L' : i = new PositiveLens(modelPoint, 0, 60, 60); break;
			case 'K' : i = new NegativeLens(modelPoint, 0, 100, 100); break;
			case 'P' : i = new Prism(modelPoint, 0); break;
			case 'S' : i = new Raindrop(modelPoint, 0); break;
			case '1' : i = new ItemHolder(modelPoint, 0, 40); break;
			case '2' : i = new ItemHolder(modelPoint, 0, 80); break;
			case '3' : i = new ItemHolder(modelPoint, 0, 120); break;
			case '4' : i = new ItemHolder(modelPoint, 0, 160); break;
			}
			if(i != null){
				model.itemPool.register(i);
				model.setSelectedItem(i);
			}
		} else {
			switch(e.getKeyChar()){
			case 'x' : 
				if(model.getSelectedItem() != model.getAimedItem())
					model.deleteAimed();
				else {
					model.itemPool.unregister(model.getSelectedItem());
					model.setSelectedItem(null);
				}
				break;
			case 'a' :
				if(actualPath == null){
					actualPath = new Path(modelPoint, model.itemPool);
					model.itemPool.register(actualPath);
				} else
					actualPath.add(new Waypoint(modelPoint));
				break;
			case 'q' : actualPath = null; break;
			case ' ' : model.wave.restart(); break;
			case 'r' : model.wave.reset(); break;
			}
		}
		
		if (e.isControlDown())
			if(e.getKeyCode()==KeyEvent.VK_N) {
				cont.stop = true;
				this.model = new Model();
				frame.init(model);
				this.view = frame.getViewPanel();
				this.cont = new Controller(model,view);
				mode = Mode.NONE;
				cont.start();
			} else if(e.getKeyCode()==KeyEvent.VK_S) {
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
			} else if(e.getKeyCode()==KeyEvent.VK_L) {
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
}
