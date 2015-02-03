package beam;

public class Util {

	public static String debug(double[] array) {
		String res = "[";
		for(double d : array)
			res+=d+", ";
		res+="]";
		System.out.println(res);
		return res.toString();
	}

}
