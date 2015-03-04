package beamMyRefactor.model.lighting;

import math.MyRandom;
import tools.LogUtil;
import geometry.Point2D;
import geometry.Segment2D;
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
//		if(get((int)p.x, (int)p.y)<intensity)
//			set((int)p.x, (int)p.y, Math.min(1, intensity));
		intensity /= 10;
		int x = (int)p.x;
		int y = (int)p.y;
		double addedIntensity = get(x, y)+intensity;
		addedIntensity = Math.min(1, addedIntensity); 
		set(x, y, addedIntensity);
	}
	
	@Override
	public void set(int x, int y, Double value) {
		if(value > 1 || value < 0)
			throw new RuntimeException("value needs to be betwwen 0 & 1 : "+value);
		super.set(x, y, value);
		
	}
	
    /*
     * Fast Voxel Traversal Algorithm for Ray Tracing
     * John Amanatides
     * Andrew Woo
     */
	public void drawLine(Point2D start, Point2D end, double intensity){
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
            Point2D p = new Point2D(x, y);
			double itAtPoint = intensity-p.getDistance(start)*Beam.ATTENUATION;
			if(itAtPoint <= 0)
				return;
			drawPoint(p, itAtPoint);
            
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
	}
	
	public void drawSegmentSamples(Segment2D s, double intensity){
		Point2D p = s.getStart();
		double length = s.getLength();
		double angle = s.getAngle();
		p = p.getTranslation(angle, MyRandom.between(0.1d, 10d));
		while(p.getDistance(s.getStart()) <= length){
			if(!isInBounds((int)p.x, (int)p.y))
				break;
			double itAtPoint = intensity-p.getDistance(s.getStart())*Beam.ATTENUATION;
			if(itAtPoint <= 0)
				break;
			drawPoint(p, itAtPoint);
			p = p.getTranslation(angle, MyRandom.between(0, 10d));
		} 

	}
	
	@Override
	public void clear() {
		setAll(0d);
	}
	
	public Lightmap getBlurred(int radius){
		return getHorBlur(radius).getVertBlur(radius);
	}
	private Lightmap getHorBlur(int radius) {
		Lightmap res = new Lightmap(xSize, ySize);
        for(int y=0; y<ySize; y++) {
            for(int x=0; x<xSize; x++) {
                int total = 0;
                for(int kx=-radius; kx<=radius; kx++)
                    total += get(x + kx, y);
                res.set(x, y, (double)total/(radius * 2 + 1));
            }
        }
        return res;
//        for(int y=0; y<ySize; y++) {
//            int total = 0;
//            for(int kx=-radius; kx<=radius; kx++)
//                total += get(kx, y);
//            dest.set(0, y, (double)total/(radius*2+1));
//
//            // Subsequent pixels just update window total
//            for (int x=1; x<xSize; x++) {
//                // Subtract pixel leaving window
//                total -= get(x-radius-1, y);
//                
//                // Add pixel entering window
//                total += get(x+radius, y);
//
//                if(total<0)
//                    dest.set(x, y, 0d);
//                else
//                	dest.set(x, y, (double)total/(radius*2+1));
//            }
//        }
    }
	private Lightmap getVertBlur(int radius) {
		Lightmap res = new Lightmap(xSize, ySize);
        for(int x=0; x<xSize; x++) {
            for(int y=0; y<ySize; y++) {
                int total = 0;
                for(int ky=-radius; ky<=radius; ky++)
                    total += get(x, y+ky);
                res.set(x, y, (double)total/(radius * 2 + 1));
            }
        }
        return res;
    }
	
	@Override
	public Double get(int x, int y) {
		if(!isInBounds(x, y))
			return 0d;
		return super.get(x, y);
	}
	
	
	
	
	

}
