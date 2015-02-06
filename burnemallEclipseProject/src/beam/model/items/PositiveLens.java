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
public class PositiveLens extends Lens {

	public PositiveLens(@Element(name="center") Point2D center, @Element(name="angle") double angle, @Element(name="leftRadius") double leftRadius, @Element(name="leftRadius") double rightRadius){
		super(center, angle, leftRadius, rightRadius);
		update();
	}

	@Override
	void update() {
		Polyline2D pl = new Polyline2D();

		// left diopter
		pl.addPoint(new Point2D(-THICKNESS, EXTENT));
		if(leftRadius >= MAX_RADIUS)
			pl.addPoint(new Point2D(-THICKNESS, -EXTENT));
		else {
			double arc = Math.asin(EXTENT/leftRadius);
			Point2D pivot = new Point2D(Math.cos(arc)*leftRadius-THICKNESS, 0);
			for(int i=0;i<NB_FACES; i++)
				pl.addPoint(pl.getLastPoint().getRotation(arc*2/NB_FACES, pivot));
		}

		// right diopter
		pl.addPoint(new Point2D(+THICKNESS, -EXTENT));
		if(rightRadius >= MAX_RADIUS)
			pl.addPoint(new Point2D(THICKNESS, EXTENT));
		else {
			double arc = Math.asin(EXTENT/rightRadius);
			Point2D pivot = new Point2D(-Math.cos(arc)*rightRadius+THICKNESS, 0);
			for(int i=0;i<NB_FACES; i++)
				pl.addPoint(pl.getLastPoint().getRotation(arc*2/NB_FACES, pivot));
		}
		pl.addPoint(pl.getFirstPoint());

		Transform2D tr = new Transform2D(center, angle);
		this.pl = pl.getTransformed(tr);
	}
}
