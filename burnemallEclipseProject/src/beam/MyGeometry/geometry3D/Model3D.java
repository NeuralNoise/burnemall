package beam.MyGeometry.geometry3D;

import java.util.ArrayList;

public class Model3D {

	public ArrayList<MyMesh> meshes = new ArrayList<MyMesh>();
	public ArrayList<String> textures = new ArrayList<String>();
	public boolean glow = false;
	public double rotationYZ;
	public double rotationXZ;
	public Point3D pos;
}
