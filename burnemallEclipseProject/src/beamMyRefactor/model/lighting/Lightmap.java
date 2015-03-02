package beamMyRefactor.model.lighting;

import geometry.Point2D;
import collections.Map2D;

public class Lightmap extends Map2D<Double>{

	public Lightmap(int xSize, int ySize) {
		super(xSize, ySize);
	}
	
	public void drawCircle(Point2D center, double radius, double intensity){
		int cX = (int)center.x;
		int cY = (int)center.y;
        for(int x=-(int)radius; x < (int)radius; x++)
            for(int y=-(int)radius; y < (int)radius; y++)
            	if(isInBounds(cX+x, cY+y))
            		if(get(cX+x, cY+y) < intensity)
            			set(cX+x, cY+y, intensity);
	}
	
	@Override
	public void clear() {
		setAll(0d);
	}
	
	
	
	

}
