package beam.MyGeometry.collections;

import java.util.ArrayList;

public class Map2D<E> {
	
	private ArrayList<ArrayList<E>> values;
	
	private int xSize;
	private int ySize;
	private int resolution;
	
	
	// BEN j'ai essayer de creer une collection pour gere des cartes en 2D, en internalisant au maximum la resolution,
	// histoire que �a soit transparent dans les codes ou on l'utilise, mais je n'y suis pas parvenu. j'ai toujours besoin de 
	// me referer a la resolution � l'exterieur du code pour appeller les bonne valeur de la map, donc c'est useless. Si t'as une
	// id�e, je suis preneur.
	public Map2D(int x, int y, int resolution) {
		this.resolution = resolution;
		xSize = x / resolution;
		ySize = y / resolution;
		values = new ArrayList<ArrayList<E>>(xSize);
		for (int i = 0; i < xSize; i++) {
			ArrayList<E> row = new ArrayList<E>();
			for (int j = 0; j < ySize; j++) {
				row.add(null);
			}
			values.add(row);
		}
	}
	
	public void set(int x, int y, E value) {
		values.get(x/resolution).set(y/resolution, value);
	}
	
	public E get(int x, int y) {
		return values.get(x/resolution).get(y/resolution);
	}
	
	public ArrayList<E> get(int x) {
		return values.get(x);
	}
	
	public int xSize() {
		return xSize;
	}

	public int ySize() {
		return ySize;
	}
}
