package beamMyRefactor.view;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

import javax.swing.JPanel;

import beamMyRefactor.model.Beam;
import beamMyRefactor.model.Model;
import beamMyRefactor.model.items.Item;
import beamMyRefactor.util.Prop;
import beamMyRefactor.util.Recorder;
import beamMyRefactor.util.StopWatch;


public class ViewPanel extends JPanel {

	private static final Stroke SELECT_STROKE = new BasicStroke(2);
	private static final Color SELECT_COLOR = new Color(50, 50, 50, 250);
	private static final int BEAM_GLOW_RADIUS = 3;
	
	private Model model;
	private AffineTransform2D atg;
	private long frameNb = 0;
	
	public ViewPanel(Model model) {
		this.model = model;
		setPreferredSize(new Dimension(Prop.PANEL_WIDTH, Prop.PANEL_HEIGHT));
		AffineTransform at = new AffineTransform();
		at.translate(100, 100);
		at.scale(2, -2);
		atg = new AffineTransform2D(at);
		try {
			model.putScreen2modelAT(at.createInverse());
		} catch (NoninvertibleTransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void paint(Graphics g1) {
		StopWatch chrono = new StopWatch("View");
		frameNb++;
		Graphics2D g = (Graphics2D)g1;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(),  getHeight());

		synchronized(model) {
			// draw selection circle
			g.setStroke(SELECT_STROKE);
			g.setColor(SELECT_COLOR);
			if (model.getSelectedItem()!=null)
				model.getSelectedItem().center().transform(atg).draw(g, 25);

			// draw items
			for (Item i : model.getItems()) {
				draw(g, atg, i);
			}
 
	        Composite transp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
	        Composite opaque = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);

			// draw light
//			int i = 0;
			for(int i=BEAM_GLOW_RADIUS; i>=0; i--){
				for (Beam b : model.getLaser().beams())
					if(b.isLight()){
						if(i!=0)
							// to draw light rays only once.
							continue;
						g.setStroke(new BasicStroke(2));
						g.setColor(b.getColor().getAwt());
				        g.setComposite(transp);
						b.getSegment().transform(atg).draw(g);
					} else {
						g.setStroke(new BasicStroke((float)b.getWidth()+i));
						g.setColor(b.getColor().getScaled(1-(double)i/BEAM_GLOW_RADIUS).getAwt());
				        g.setComposite(opaque);
						b.getSegment().transform(atg).draw(g);
					}
			}
			
	        g.setComposite(opaque);
			g.setColor(Color.lightGray);
			g.setFont(new Font("Arial",Font.PLAIN,12));
			g.drawString(Recorder.str(), 0+4,  getHeight()-4);
		}
		Recorder.record(chrono);
	}

	private void draw(Graphics2D g, AffineTransform2D at, Item i) {
		i.draw(g, atg);
	}

	private void draw(Graphics2D g, AffineTransform2D at, LineSegment2D s) {
		s.transform(at).draw(g);
	}

}
