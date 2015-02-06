package beam.model.items;

import geometry.Point2D;
import geometry.Polyline2D;
import geometry.Transform2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Collection;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import beam.model.Beam;
import beam.util.Precision;
import beam.util.Util;

@Root
public class NegativeLens extends Lens {

	public NegativeLens(@Element(name="center") Point2D center, @Element(name="angle") double angle, @Element(name="leftRadius") double leftRadius, @Element(name="rightRadius") double rightRadius){
		super(center, angle, leftRadius, rightRadius);
		update();
	}

	@Override
	void update() {
		Polyline2D pl = new Polyline2D();

		// left diopter
		if(leftRadius >= MAX_RADIUS){
			pl.addPoint(new Point2D(-THICKNESS, EXTENT));
			pl.addPoint(new Point2D(-THICKNESS, -EXTENT));
		} else {
			Point2D pivot = new Point2D(-leftRadius-THICKNESS, 0);
			double arc = Math.asin(EXTENT/leftRadius);
			pl.addPoint(new Point2D(-(1-Math.cos(arc))*leftRadius-THICKNESS, EXTENT));
			double arcStep = -arc*2/NB_FACES;
			for(int i=0; i<NB_FACES; i++)
				pl.addPoint(pl.getLastPoint().getRotation(arcStep, pivot));
		}
		

		// right diopter
		if(rightRadius >= MAX_RADIUS){
			pl.addPoint(new Point2D(THICKNESS, -EXTENT));
			pl.addPoint(new Point2D(THICKNESS, EXTENT));
		} else {
			Point2D pivot = new Point2D(rightRadius+THICKNESS, 0);
			double arc = Math.asin(EXTENT/rightRadius);
			pl.addPoint(new Point2D((1-Math.cos(arc))*rightRadius+THICKNESS, -EXTENT));
			double arcStep = -arc*2/NB_FACES;
			for(int i=0;i<NB_FACES; i++)
				pl.addPoint(pl.getLastPoint().getRotation(arcStep, pivot));
		}
		pl.addPoint(pl.getFirstPoint());

		Transform2D tr = new Transform2D(center, angle);
		this.pl = pl.getTransformed(tr);
	}
}
