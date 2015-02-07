package beamMyRefactor.model;

import java.awt.Color;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class MyColor {

	public static final MyColor GREEN = MyColor.rgb(0,255,0);

	@Element
	private int red, green, blue;
	private Color awtColor;
	
	public MyColor(@Element(name="red") int red, @Element(name="green") int green, @Element(name="blue")int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		awtColor = new Color(red, green, blue);
	}

	private static MyColor rgb(int r, int g, int b) {
		return new MyColor(r, g, b);
	}

	public Color getAwt() {
		return awtColor;
	}

	// convert an hex encoded color
	public static MyColor get(String col) {
		if (col.startsWith("#"))
			col=col.substring(1);
		assert col.length()==6;
		int red = Integer.parseInt(col.substring(0,2),16);
		int green = Integer.parseInt(col.substring(2,4),16);
		int blue = Integer.parseInt(col.substring(4,6),16);
		return new MyColor(red, green, blue);
	}
	
	public MyColor getScaled(double f){
		return new MyColor((int)(red*f), (int)(green*f), (int)(blue*f));
	}

}
