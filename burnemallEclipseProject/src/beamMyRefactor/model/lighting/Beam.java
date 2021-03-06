package beamMyRefactor.model.lighting;

import tools.LogUtil;
import math.MyRandom;
import beamMyRefactor.model.MyColor;
import geometry.Point2D;
import geometry.Ray2D;
import geometry.Segment2D;

public class Beam {
	private static final double FAR = 500.0;
	private static final int MAX_INTERACT = 5;
	public static final double ATTENUATION = 0.001; 
	public static final double MAX_LENGTH = 800; 
	
	Ray2D ray;
	Point2D endPoint;
	MyColor color;
	double spectralRate = -1;
	double rayWidth;
	boolean light = false;
	public double intensity = 1;
	int interactions = 0;
	double length;
	
	public Beam(Beam beam) {
		color = beam.color;
		rayWidth = beam.rayWidth;
		light = beam.light;
		spectralRate = beam.spectralRate;
		interactions = beam.interactions+1;
		intensity = beam.intensity-beam.getSegment().getLength()*ATTENUATION;
		intensity = Math.max(0, intensity);
		length = beam.length-beam.getSegment().getLength();
	}

	public Beam(MyColor color, double rayWidth) {
		this.color = color;
		this.rayWidth = rayWidth;
		length = MyRandom.between(0, MAX_LENGTH);
	}

	@Override
	public String toString() {
		return ray.toString();
	}

	public void setRay(Ray2D ray) {
		this.ray = ray;
	}

	public Ray2D getRay() {
		return ray;
	}

	public MyColor getColor() {
		return color;
	}

	public Segment2D getSegment() {
		if (endPoint==null)
			return new Segment2D(ray.getStart(), ray.getPointAt(FAR));
		else
			return new Segment2D(ray.getStart(), endPoint);
	}

	public double getWidth() {
		return rayWidth;
	}
	
	public void setAsLight() {
		light = true;
	}
	
	public boolean isLight(){
		return light;
	}
	
	public boolean isDispersed(){
		return spectralRate != -1;
	}
	
	public void disperse(double spectralRate){
		this.spectralRate = spectralRate;
		color = MyColor.getSpectralColor(spectralRate);
	}
	
	public double getSpectralRate(){
		return spectralRate;
	}

	public boolean attenuated() {
		if(interactions > MAX_INTERACT || intensity < 0.01)
			return true;
		return false;
	}
}
