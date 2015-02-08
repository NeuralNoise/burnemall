package beamMyRefactor.model.items.geometric;

import geometry.Facet;
import geometry.Point2D;
import geometry.Polyline2D;
import geometry.Ray2D;
import geometry.Segment2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;

import math.Angle;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import beamMyRefactor.model.Beam;
import beamMyRefactor.util.Util;

@Root
public class Refractor extends GeometricItem {

	private static final int DISPERSED_RAYS = 20;
	@Element
	double minRefract;
	@Element
	double maxRefract;
	
	/*
	 * To create a non dispersing refractor, set the min and max to the same value 
	 */
	public Refractor(@Element(name="center") Point2D center, @Element(name="angle") double angle, @Element(name="minRefract") double minRefract, @Element(name="maxRefract") double maxRefract) {
		super(center, angle);
		this.minRefract = minRefract;
		this.maxRefract = maxRefract;
	}

	@Override
	public Collection<Beam> interact(Beam beam, Point2D intersect) {
		ArrayList<Beam> res = new ArrayList<>();

		if(minRefract == maxRefract) {
			// Here this is the same refraction index for all the color specter
			double refractionIndex = minRefract;
			res.addAll(getRefracted(beam, intersect, refractionIndex));
		} else if(beam.isDispersed() || !beam.isLight()) {
			// Here the beam as already been dispersed.
			double refractionIndex = minRefract+(maxRefract-minRefract)*beam.getSpectralRate();
			res.addAll(getRefracted(beam, intersect, refractionIndex));
		} else {
			for(int i=DISPERSED_RAYS-1; i>=0; i--){
				double spectralRate = (double)i/DISPERSED_RAYS;

				double refractionIndex = minRefract+spectralRate*(maxRefract-minRefract);
				ArrayList<Beam> rt = getRefracted(beam, intersect, refractionIndex);

				res.add(rt.get(0));
				if(rt.size() > 1){
					rt.get(1).disperse(spectralRate);
					res.add(rt.get(1));
				}
			}
		}
		return res;
	}
	
	private ArrayList<Beam> getRefracted(Beam beam, Point2D start, double refractionIndex){
		// check if the beam enters or leaves
		boolean enters = true;
		for(Facet f : shape)
			if(f.contains(beam.getRay().getStart())){
				enters = false;
				break;
			}
		refractionIndex = enters ? refractionIndex : 1/refractionIndex;

		double incident = Angle.getOrientedDifference(collisionNormal, beam.getRay().getAngle()+Angle.FLAT);

		double critical = Math.asin(refractionIndex);
		double refractionAngle;
		if(refractionIndex > 1 || Math.abs(incident) < critical)
			refractionAngle = Math.asin(Math.sin(incident)/refractionIndex);
		else
			// total refraction, only remains reflexion
			refractionAngle = Double.NaN;
		
		// Fresnel equations for reflexion/refraction intensity;
		double reflexionCoef, refractionCoef;
		if(Double.isNaN(refractionAngle)){
			reflexionCoef = 1;
			refractionCoef = 0;
		} else {
			double n1 = 1;
			double n2 = refractionIndex;
			if(!enters){
				n1 = refractionIndex;
				n2 = 1;
			}
			double cosI = Math.cos(incident);
			double cosT = Math.cos(refractionAngle);
			
			double SPolarized = (n1*cosI-n2*cosT)/(n1*cosI+n2*cosT);
			SPolarized = SPolarized*SPolarized;
			double PPolarized = (n1*cosT-n2*cosI)/(n1*cosT+n2*cosI);
			PPolarized = PPolarized*PPolarized;
			reflexionCoef = Math.max(0, (SPolarized+PPolarized)/2);
			refractionCoef = 1-reflexionCoef;
		}

		ArrayList<Beam> res = new ArrayList<>();

		Beam reflexion = new Beam(beam);
		reflexion.setRay(new Ray2D(start, collisionNormal-incident));
		reflexion.intensity = beam.intensity*reflexionCoef;
		res.add(reflexion);
		
		if(!Double.isNaN(refractionAngle)){
			Beam refraction = new Beam(beam);
			refraction.setRay (new Ray2D(start, collisionNormal+Angle.FLAT+refractionAngle));
			refraction.intensity = beam.intensity*refractionCoef;
			res.add(refraction);
		}
		
		return res;
	}	
}
