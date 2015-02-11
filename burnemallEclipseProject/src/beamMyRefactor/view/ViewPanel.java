package beamMyRefactor.view;

import geometry.Circle2D;
import geometry.Facet;
import geometry.Point2D;
import geometry.Polyline2D;
import geometry.Segment2D;
import geometry.Transform2D;

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
import java.util.List;

import javax.swing.JPanel;

import collections.FacetSerie;
import tools.LogUtil;
import beamMyRefactor.model.Beam;
import beamMyRefactor.model.Model;
import beamMyRefactor.model.items.AbstractItem;
import beamMyRefactor.util.Prop;
import beamMyRefactor.util.Recorder;
import beamMyRefactor.util.StopWatch;


public class ViewPanel extends JPanel {

	private static final Stroke SELECT_STROKE = new BasicStroke(2);
	private static final Color SELECT_COLOR = new Color(50, 50, 50, 250);
	private static final int BEAM_GLOW_RADIUS = 3;
	
	private Model model;
	private Transform2D toScreenTransform;
	private long frameNb = 0;
	
	public ViewPanel(Model model) {
		this.model = model;
		setPreferredSize(new Dimension(Prop.PANEL_WIDTH, Prop.PANEL_HEIGHT));
		
		toScreenTransform = new Transform2D();
		toScreenTransform.setScale(2, 2);
//		toScreenTransform.setTranslation(100, 100);
		
		model.putToModelTransform(toScreenTransform.getInverse());
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
				draw(g, model.getSelectedItem().getCoord(), 25);

			// draw items
			for (AbstractItem i : model.getItems()){
				g.setStroke(new BasicStroke(i.getThickness()));
				g.setColor(i.getColor());
				draw(g, i.getShape());
			}
 
	        Composite transp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
	        Composite opaque = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);

			// draw light
//			int i = 0;
			for(int i=BEAM_GLOW_RADIUS; i>=0; i--){
				for (Beam b : model.getLaser().beams()){
					if(b.isLight()){
						if(i!=0)
							// to draw light rays only once.
							continue;
						g.setStroke(new BasicStroke(2));
						g.setColor(b.getColor().getAwt());
				        transp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max((float)b.intensity*0.2f, 0.08f));
				        g.setComposite(transp);
					} else {
						g.setStroke(new BasicStroke((float)b.getWidth()+i));
						g.setColor(b.getColor().getScaled(1-(double)i/BEAM_GLOW_RADIUS).getAwt());
				        transp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max((float)b.intensity, 0.08f));
				        g.setComposite(transp);
					}
					draw(g, b.getSegment());
				}
			}
			
	        g.setComposite(opaque);
			g.setColor(Color.lightGray);
			g.setFont(new Font("Arial",Font.PLAIN,12));
			g.drawString(Recorder.str()+"Beams : "+model.producedBeamCount, 0+4,  getHeight()-4);
		}
		Recorder.record(chrono);
		
	}
	
	private void draw(Graphics2D g, Object object){
		if(object instanceof Segment2D)
			draw(g, (Segment2D)object);
		else if(object instanceof Circle2D)
			draw(g, (Circle2D)object);
		else if(object instanceof List)
			for(Object o : (List)object)
				draw(g, o);
	}
	
	private void draw(Graphics2D g, Point2D p, int diameter){
		p = p.getTransformed(toScreenTransform);
		g.fillOval((int)Math.round(p.x)-diameter/2, (int)Math.round(p.y)-diameter/2, diameter, diameter);
	}
	private void draw(Graphics2D g, Circle2D c){
		c = c.getTransformed(toScreenTransform);
		g.drawOval((int)Math.round(c.center.x-c.radius),
				(int)Math.round(c.center.y-c.radius),
				(int)Math.round(c.radius*2),
				(int)Math.round(c.radius*2));
	}
	
	private void draw(Graphics2D g, Segment2D s){
		s = s.getTransformed(toScreenTransform);
		g.drawLine((int)Math.round(s.getStart().x),
				(int)Math.round(s.getStart().y),
				(int)Math.round(s.getEnd().x),
				(int)Math.round(s.getEnd().y));
	}
}
