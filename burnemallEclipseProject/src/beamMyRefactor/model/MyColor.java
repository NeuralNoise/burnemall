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
	
	public static MyColor getSpectralColor(double rate){
		int i = (int)(1275*rate);
		int r = 0;
		int g = 0;
		int b = 0;
		if(i>=0 && i<255){
			r = 255;
			g = i;
			b = 0;
		}
		if(i>=255 && i<510){
			r = 510-i;
			g = 255;
			b = 0;
		}
		if(i>=510 && i<765){
			r = 0;
			g = 255;
			b = i-510;
		}
		if(i>=765 && i<1020){
			r = 0;
			g = 1020-i;
			b = 255;
		}
		if(i>=1020 && i<=1275){
			r = i-1020;
			g = 0;
			b = 255;
		}
		return new MyColor(r, g, b);
	}
}
