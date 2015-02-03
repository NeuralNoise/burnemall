package beam.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

import javax.swing.JPanel;

import math.geom2d.AffineTransform2D;
import math.geom2d.line.LineSegment2D;
import beam.model.Mirror;
import beam.model.Model;


public class ViewPanel extends JPanel {

	private Model model;
	
	public ViewPanel(Model model) {
		this.model = model;
		setPreferredSize(new Dimension(View.WIDTH, View.HEIGHT));
	}

	@Override
	public void paint(Graphics g1) {
		Graphics2D g = (Graphics2D)g1;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, View.WIDTH,  View.HEIGHT);
		g.setStroke(new BasicStroke(2));
		
		AffineTransform at = new AffineTransform();
		at.translate(100, 100);
		at.scale(2, -2);
		AffineTransform2D atg = new AffineTransform2D(at);
		try {
			model.storeReverseTransform(at.createInverse());
		} catch (NoninvertibleTransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		g.setColor(Color.green);
		for (LineSegment2D s : model.getLaser().segments()) {
			draw(g, atg, s);
		}
		
		g.setColor(Color.lightGray);
		for (Mirror m : model.segments()) {
			m.draw(g, atg);
		}
	}

	private void draw(Graphics2D g, AffineTransform2D at, Mirror m) {
		m.segment().transform(at).draw(g);
		m.end().transform(at).draw(g,3);
	}

	private void draw(Graphics2D g, AffineTransform2D at, LineSegment2D s) {
		s.transform(at).draw(g);
	}

}
