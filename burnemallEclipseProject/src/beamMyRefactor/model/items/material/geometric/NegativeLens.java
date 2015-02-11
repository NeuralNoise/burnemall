package beamMyRefactor.model.items.material.geometric;

import geometry.Point2D;
import geometry.Polyline2D;
import geometry.Transform2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Collection;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import beamMyRefactor.model.Beam;
import beamMyRefactor.util.Util;

@Root
public class NegativeLens extends Lens {

	public NegativeLens(@Element(name="center") Point2D center, @Element(name="angle") double angle, @Element(name="leftRadius") double leftRadius, @Element(name="rightRadius") double rightRadius){
		super(center, angle, leftRadius, rightRadius);

		// left diopter
		if(leftRadius >= MAX_RADIUS){
			initialShape.addPoint(new Point2D(-THICKNESS, EXTENT));
			initialShape.addPoint(new Point2D(-THICKNESS, -EXTENT));
		} else {
			Point2D pivot = new Point2D(-leftRadius-THICKNESS, 0);
			double arc = Math.asin(EXTENT/leftRadius);
			initialShape.addPoint(new Point2D(-(1-Math.cos(arc))*leftRadius-THICKNESS, EXTENT));
			double arcStep = -arc*2/NB_FACES;
			for(int i=0; i<NB_FACES; i++)
				initialShape.addPoint(initialShape.getLastPoint().getRotation(arcStep, pivot));
		}
		

		// right diopter
		if(rightRadius >= MAX_RADIUS){
			initialShape.addPoint(new Point2D(THICKNESS, -EXTENT));
			initialShape.addPoint(new Point2D(THICKNESS, EXTENT));
		} else {
			Point2D pivot = new Point2D(rightRadius+THICKNESS, 0);
			double arc = Math.asin(EXTENT/rightRadius);
			initialShape.addPoint(new Point2D((1-Math.cos(arc))*rightRadius+THICKNESS, -EXTENT));
			double arcStep = -arc*2/NB_FACES;
			for(int i=0;i<NB_FACES; i++)
				initialShape.addPoint(initialShape.getLastPoint().getRotation(arcStep, pivot));
		}
		
		initialShape.close();
		initialShape.smoothNormals();
		update();
	}

}
