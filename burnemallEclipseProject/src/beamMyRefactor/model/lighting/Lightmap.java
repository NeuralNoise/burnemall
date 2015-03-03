package beamMyRefactor.model.lighting;

import tools.LogUtil;
import geometry.Point2D;
import collections.Map2D;

public class Lightmap extends Map2D<Double>{

	public Lightmap(int xSize, int ySize) {
		super(xSize, ySize);
		clear();
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
            			if(get(x, y)<interpolatedIntensity)
            				set(x, y, Math.min(1, interpolatedIntensity));
            		}
            	}
            }
	}
	
	public void drawPoint(Point2D p, double intensity){
		if(get((int)p.x, (int)p.y)<intensity)
			set((int)p.x, (int)p.y, Math.min(1, intensity));
		
	}
	
    /*
     * Fast Voxel Traversal Algorithm for Ray Tracing
     * John Amanatides
     * Andrew Woo
     */
	public void drawline(Point2D start, Point2D end, double intensity){
		// calculate the direction of the ray (linear algebra)
        double dirX = end.x-start.x;
        double dirY = end.y-start.y;
        double length = Math.sqrt(dirX * dirX + dirY * dirY);
        dirX /= length; // normalize the direction vector
        dirY /= length;
        double tDeltaX = 1/Math.abs(dirX); // how far we must move in the ray direction before we encounter a new voxel in x-direction
        double tDeltaY = 1/Math.abs(dirY); // same but y-direction
 
        // start voxel coordinates
        int x = (int)Math.floor(start.x);  // use your transformer function here
        int y = (int)Math.floor(start.y);
 
        // end voxel coordinates
        int endX = (int)Math.floor(end.x);
        int endY = (int)Math.floor(end.y);
 
        // decide which direction to start walking in
        int stepX = (int) Math.signum(dirX);
        int stepY = (int) Math.signum(dirY);
 
        double tMaxX, tMaxY;
        // calculate distance to first intersection in the voxel we start from
        if(dirX < 0)
            tMaxX = ((double)x-start.x)/dirX;
        else
            tMaxX = ((double)(x+1)-start.x)/dirX;
 
        if(dirY < 0)
            tMaxY = ((double)y-start.y)/dirY;
        else
            tMaxY = ((double)(y+1)-start.y) / dirY;
 
        // check if first is occupied
        drawPoint(start, intensity);
        int i = 0;
        boolean reachedX = false, reachedY = false;
        while(!reachedX || !reachedY){
            if(tMaxX < tMaxY){
                tMaxX += tDeltaX;
                x += stepX;
            }else{
                tMaxY += tDeltaY;
                y += stepY;
            }
            if(!isInBounds(x, y))
            	return;
            drawPoint(new Point2D(x, y), intensity);

            if(stepX > 0){
                if (x >= endX)
                    reachedX = true;
            }else if (x <= endX)
                reachedX = true;
 
            if(stepY > 0){
                if (y >= endY)
                    reachedY = true;
            }else if (y <= endY)
                reachedY = true;
            i++;
        }
        LogUtil.logger.info("nb pixels drawn : "+i);
	}
	
	@Override
	public void clear() {
		setAll(0d);
	}
	
	
	
	

}
