package recognition;


import ij.ImagePlus;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class Matcher.
 */
public class Matcher
{
  
  /**
   * Find mathes.
   *
   * @param ipts1 the ipts1
   * @param ipts2 the ipts2
   * @param doReverseComparisonToo the do reverse comparison too
   * @return the map
   */
  public static Map<InterestPoint, InterestPoint> findMathes(List<InterestPoint> ipts1, List<InterestPoint> ipts2, boolean doReverseComparisonToo)
  {
    Map<InterestPoint, InterestPoint> matchedPoints = findMathes(ipts1, ipts2);

    if (doReverseComparisonToo) {
      Map<InterestPoint, InterestPoint> matchedPointsReverse = findMathes(ipts2, ipts1);

      Map<InterestPoint, InterestPoint> matchedPointsBoth = new HashMap<InterestPoint, InterestPoint>();
      for (InterestPoint ipt1 : matchedPoints.keySet()) {
        InterestPoint ipt2 = (InterestPoint)matchedPoints.get(ipt1);
        if (ipt1 == matchedPointsReverse.get(ipt2))
          matchedPointsBoth.put(ipt1, ipt2);
      }
      matchedPoints = matchedPointsBoth;
    }
    return matchedPoints;
  }

  /**
   * Find mathes.
   *
   * @param ipts1 the ipts1
   * @param ipts2 the ipts2
   * @return the map
   */
  public static Map<InterestPoint, InterestPoint> findMathes(List<InterestPoint> ipts1, List<InterestPoint> ipts2)
  {
    Map<InterestPoint, InterestPoint> res = new HashMap<InterestPoint, InterestPoint>();

    int descSize = 64;

    for (InterestPoint p1 : ipts1)
    {
      float secondBest;
      float bestDistance = secondBest = 3.4028235E+38F;
      InterestPoint bestMatch = null;

      for (InterestPoint p2 : ipts2)
      {
        if (p1.sign != p2.sign)
        {
          continue;
        }
        float distance = 0.0F;
        float[] v1 = p1.descriptor;
        float[] v2 = p2.descriptor;
        float delta;
        int i = 0;
        while (i < v1.length && i < v2.length) { 
          delta = v1[i] - v2[i];
          distance += delta * delta;
          if (distance >= secondBest)
            break;
          i++; 
          if (i < descSize)
          {
            continue;
          }

          if (distance < bestDistance) {
            secondBest = bestDistance;
            bestDistance = distance;
            bestMatch = p2;
          } else {
            secondBest = distance;
          }
        }
      }

      if (bestDistance >= 0.5F * secondBest) {
        continue;
      }
      res.put(p1, bestMatch);

      bestMatch.dx = (bestMatch.xpos - p1.xpos);
      bestMatch.dy = (bestMatch.ypos - p1.ypos);
    }

    return res;
  }

  /**
   * Gets the target point by homography.
   *
   * @param p1 the p1
   * @param h the h
   * @return the target point by homography
   */
  public static Point2Df getTargetPointByHomography(Point2Df p1, float[][] h)
  {
    float p1_z = 1.0F;
    float Z = h[2][0] * p1.x + h[2][1] * p1.y + h[2][2] * p1_z;
    float X = (h[0][0] * p1.x + h[0][1] * p1.y + h[0][2] * p1_z) / Z;
    float Y = (h[1][0] * p1.x + h[1][1] * p1.y + h[1][2] * p1_z) / Z;
    return new Point2Df(X, Y);
  }

  /**
   * Count matches using homography.
   *
   * @param matches the matches
   * @param imp1 the imp1
   * @param margin the margin
   * @param h the h
   * @param imp2 the imp2
   * @param tolerance the tolerance
   * @return the int
   */
  public static int countMatchesUsingHomography(Map<InterestPoint, InterestPoint> matches, ImagePlus imp1, int margin, float[][] h, ImagePlus imp2, float tolerance) {
    int count = 0;

    for (Map.Entry pair : matches.entrySet())
    {
      float x1 = ((InterestPoint)pair.getKey()).xpos;
      float y1 = ((InterestPoint)pair.getKey()).ypos;

      float x2 = ((InterestPoint)pair.getValue()).xpos;
      float y2 = ((InterestPoint)pair.getValue()).ypos;

      float z2H = h[2][0] * x1 + h[2][1] * y1 + h[2][2];
      float x2H = (h[0][0] * x1 + h[0][1] * y1 + h[0][2]) / z2H;
      float y2H = (h[1][0] * x1 + h[1][1] * y1 + h[1][2]) / z2H;

      float dx = Math.abs(x2H - x2);
      float dy = Math.abs(y2H - y2);

      if ((dx <= tolerance) && (dy <= tolerance))
        count++;
    }
    return count;
  }

  /**
   * Translate corners.
   *
   * @param matches the matches
   * @param src_corners the src_corners
   * @param dst_corners the dst_corners
   * @return the int
   */
  static int translateCorners(Map<InterestPoint, InterestPoint> matches, Point2D[] src_corners, Point2D[] dst_corners)
  {
    int n = matches.size();
    if (n < 4) {
      return 0;
    }
    Point2Df[] pt1 = new Point2Df[n];

    Point2Df[] pt2 = new Point2Df[n];

    int i = 0;
    for (Map.Entry pair : matches.entrySet()) {
      pt1[i] = new Point2Df(((InterestPoint)pair.getKey()).xpos, ((InterestPoint)pair.getKey()).ypos);
      pt2[i] = new Point2Df(((InterestPoint)pair.getValue()).xpos, ((InterestPoint)pair.getValue()).ypos);
      i++;
    }

    int CV_RANSAC = 8;
    double[] h = cvFindHomography(pt1, pt2, 8, 5.0D);

    if (h == null) {
      return 0;
    }

    for (i = 0; i < 4; i++) {
      double x = src_corners[i].x; double y = src_corners[i].y;
      double Z = 1.0D / (h[6] * x + h[7] * y + h[8]);
      double X = (h[0] * x + h[1] * y + h[2]) * Z;
      double Y = (h[3] * x + h[4] * y + h[5]) * Z;
      dst_corners[i] = new Point2D((int)Math.round(X), (int)Math.round(Y));
    }
    return 1;
  }

  /**
   * Cv find homography.
   *
   * @param objectPoints the object points
   * @param imagePoints the image points
   * @param method the method
   * @param ransacReprojThreshold the ransac reproj threshold
   * @return the double[]
   */
  static double[] cvFindHomography(Point2Df[] objectPoints, Point2Df[] imagePoints, int method, double ransacReprojThreshold)
  {
    double[] homography = (double[])null;

    return homography;
  }

  /**
   * The Class Point2D.
   */
  public static class Point2D
  {
    
    /** The x. */
    public int x;
    
    /** The y. */
    public int y;

    /**
     * Instantiates a new point2 d.
     */
    public Point2D()
    {
    }

    /**
     * Instantiates a new point2 d.
     *
     * @param x the x
     * @param y the y
     */
    public Point2D(int x, int y)
    {
      this.x = x;
      this.y = y;
    }
  }
  
  /**
   * The Class Point2Df.
   */
  public static class Point2Df {
    
    /** The x. */
    public float x;
    
    /** The y. */
    public float y;

    /**
     * Instantiates a new point2 df.
     */
    public Point2Df() {
    }
    
    /**
     * Instantiates a new point2 df.
     *
     * @param x the x
     * @param y the y
     */
    public Point2Df(float x, float y) { this.x = x;
      this.y = y;
    }
  }
  
}
