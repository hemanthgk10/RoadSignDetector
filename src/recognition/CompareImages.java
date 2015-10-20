package recognition;


import java.util.HashMap;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class CompareImages.
 */
public class CompareImages {
	
	/**
	 * Intersection.
	 *
	 * @param map1 the map1
	 * @param map2 the map2
	 * @return the map
	 */
	public static Map<InterestPoint, InterestPoint> intersection(Map<InterestPoint, InterestPoint> map1, Map<InterestPoint, InterestPoint> map2)
	  {
	    Map<InterestPoint, InterestPoint> result = new HashMap<InterestPoint, InterestPoint>();
	    for (InterestPoint ipt1 : map1.keySet()) {
	      InterestPoint ipt2 = (InterestPoint)map1.get(ipt1);
	      if (ipt1 == map2.get(ipt2))
	        result.put(ipt1, ipt2);
	    }
	    return result;
	  }

}
