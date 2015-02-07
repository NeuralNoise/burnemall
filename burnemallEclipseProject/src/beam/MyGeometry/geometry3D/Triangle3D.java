/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beam.MyGeometry.geometry3D;

import beam.MyGeometry.geometry.Point2D;


/**
 *
 * @author Benoît
 */
public class Triangle3D {
    public Point3D a;
    public Point3D b;
    public Point3D c;
    
    public Point3D normal;
    
    public Triangle3D(Point3D a, Point3D b, Point3D c) {
        this.a = a;
        this.b = b;
        this.c = c;
        
        normal = b.getSubstraction(a).getCross(c.getSubstraction(a));
        normal = normal.getDivision(normal.getNorm());
    }
    
    public boolean shareVert(Triangle3D o){
        return a.equals(o.a) || a.equals(o.b) || a.equals(o.c) ||
                b.equals(o.a) || b.equals(o.b) || b.equals(o.c) ||
                c.equals(o.a) || c.equals(o.b) || c.equals(o.c);
    }
    
    /**
     * the plan is defined bye ax+by+cz+d = 0, where (a, b, c) is the normal vector
     * @param p
     * @return 
     */
    public Point3D getElevated(Point2D p) {
        double factorD = -(normal.x*a.x+normal.y*a.y+normal.z*a.z);
        double z = -(normal.x*p.x+normal.y*p.y+factorD)/normal.z;
        return new Point3D(p.x, p.y, z);
    }
}
