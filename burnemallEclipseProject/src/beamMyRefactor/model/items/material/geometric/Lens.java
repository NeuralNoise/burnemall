package beamMyRefactor.model.items.material.geometric;

import geometry.Point2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Collection;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import beamMyRefactor.model.Beam;
import beamMyRefactor.util.Util;

@Root
public class Lens extends Refractor {

	static final double MIN_REFRACTION_INDEX = 1.6;
	static final double MAX_REFRACTION_INDEX = 1.6;
	static final int NB_FACES = 6;
	static final double EXTENT = 40; 
	static final double THICKNESS = 2.5;
	static final double MAX_RADIUS = EXTENT*5;
	
	@Element
	double leftRadius;
	
	@Element
	double rightRadius;
	
	public Lens(@Element(name="center") Point2D center, @Element(name="angle") double angle, @Element(name="leftRadius") double leftRadius, @Element(name="leftRadius") double rightRadius){
		super(center, angle, MIN_REFRACTION_INDEX, MAX_REFRACTION_INDEX);
		if(leftRadius<EXTENT || rightRadius<EXTENT)
			throw new IllegalArgumentException("Radii of the lens's diopters can't be smaller than lens extent (default "+EXTENT+").");

		this.leftRadius = leftRadius;
		this.rightRadius = rightRadius;
		update();
	}
}
