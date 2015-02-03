package beam.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Util {

	public static String debug(double[] array) {
		String res = "[";
		for(double d : array)
			res+=d+", ";
		res+="]";
		System.out.println(res);
		return res.toString();
	}

	public static <T> Collection<T> makeCollection(T item) {
		List<T> res = new ArrayList<>(1);
		res.add(item);
		return res;
	}

}
