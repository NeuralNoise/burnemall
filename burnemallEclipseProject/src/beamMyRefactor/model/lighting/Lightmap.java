package beamMyRefactor.model.lighting;

import tools.LogUtil;
import geometry.Point2D;
import collections.Map2D;

public class Lightmap extends Map2D<Double>{

	public Lightmap(int xSize, int ySize) {
		super(xSize, ySize);
	}
	
	public void drawCircle(Point2D center, double radius, double intensity){
		int cX = (int)center.x;
		int cY = (int)center.y;
        for(int x=cX-(int)radius; x < cX+(int)radius; x++)
            for(int y=cY-(int)radius; y < cY+(int)radius; y++){
            	if(isInBounds(x, y)){
            		double dist = new Point2D(x, y).getDistance(center);
            		if(dist<=radius){
            			double interpolatedIntensity = intensity*(1-dist/radius);
            			set(x, y, Math.min(intensity, get(x, y)+interpolatedIntensity));
            		}
            	}
            }
	}
	
	@Override
	public void clear() {
		setAll(0d);
	}
	
	
	
	

}
