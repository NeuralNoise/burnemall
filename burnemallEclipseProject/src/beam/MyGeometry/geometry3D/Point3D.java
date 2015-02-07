package beam.MyGeometry.geometry3D;

import java.text.DecimalFormat;

import beam.MyGeometry.geometry.Point2D;
import beam.MyGeometry.math.Precision;


public class Point3D {

	public double x;
	public double y;
	public double z;
	
	/**
	 * create � new point 3D by : 
	 *  1- elevating the given point 2D,
	 *  2- rotating the given point 2D around Z,
	 *  3- rotating the given point 2D in it's own plane.
	 * @param p
	 * @param val
	 */
	public Point3D(Point2D p, double val, int param){
		this(p, val, param, Point2D.ORIGIN);
	}

	public Point3D(Point2D p, double val, int param, Point2D pivot){
		if(param == 1) {
			x = p.x;
			y = p.y;
			z = val;
		} else if (param == 2) {
			x = (p.x-pivot.x)*Math.cos(val)+pivot.x;
			y = (p.x-pivot.x)*Math.sin(val)+pivot.x;
			z = p.y;
		} else if (param == 3) {
			x = p.x*Math.cos(val);
			y = Point2D.ORIGIN.getDistance(p)*Math.sin(val);
			z = p.y*Math.cos(val);
		}
		else
			throw new IllegalArgumentException("Invalid param");
	}

	public Point3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double getDistance(Point3D other) {
		double dx = x - other.x;
		double dy = y - other.y;
		double dz = z - other.z;
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}
        
        public double getNorm() {
            return Math.sqrt(x*x+y*y+z*z);
        }
        
        public Point3D getCross(Point3D o) {
            double resX = ((y * o.z) - (z * o.y)); 
            double resY = ((z * o.x) - (x * o.z));
            double resZ = ((x * o.y) - (y * o.x));
            return new Point3D(resX, resY, resZ);
        }
        
        public Point3D getSubstraction(Point3D o) {
            return new Point3D(x-o.x, y-o.y, z-o.z);
        }
        
        public Point3D getDivision(double val) {
            return new Point3D(x/val, y/val, z/val);
        }

    public Point3D getAddition(Point3D o) {
        return new Point3D(x+o.x, y+o.y, z+o.z);
    }

    public Point3D getAddition(double x, double y, double z) {
        return new Point3D(this.x+x, this.y+y, this.z+z);
    }
    
    public boolean equals(Object o) {
		if (!(o instanceof Point3D))
			return false;
		Point3D p = (Point3D) o;
		return Math.abs(x - p.x) < Precision.APPROX &&
                        Math.abs(y - p.y) < Precision.APPROX &&
                        Math.abs(z - p.z) < Precision.APPROX;
	}
    
    	private static DecimalFormat df = new DecimalFormat("0.00");
        public String toString() {
                return "(" + df.format(x) + ", " + df.format(y) + ", " + df.format(z) + ")";
        }

}
