package recognition;


import ij.IJ;
import ij.process.ImageProcessor;
import java.util.Arrays;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class IJFacade.
 */
public class IJFacade
{
  
  /** The last result. */
  private static List<InterestPoint> lastResult = null;

  /**
   * Sets the last result.
   *
   * @param ipts the new last result
   */
  public static synchronized void setLastResult(List<InterestPoint> ipts) { lastResult = ipts; } 
  
  /**
   * Gets the last result.
   *
   * @return the last result
   */
  public static synchronized List<InterestPoint> getLastResult() { return lastResult;
  }

  /**
   * Detect and describe interest points.
   *
   * @param intImg the int img
   * @return the list
   */
  public static List<InterestPoint> detectAndDescribeInterestPoints(IntegralImage intImg)
  {
    return detectAndDescribeInterestPoints(intImg, new Params());
  }

  /**
   * Detect and describe interest points.
   *
   * @param intImg the int img
   * @param p the p
   * @return the list
   */
  public static List<InterestPoint> detectAndDescribeInterestPoints(IntegralImage intImg, Params p)
  {
    List<InterestPoint> ipts = Detector.fastHessian(intImg, p);

  //  p.getStatistics().detectedIPs = ipts.size();
    float[] strengthOfIPs = new float[ipts.size()];
    for (int i = 0; i < ipts.size(); i++) {
      strengthOfIPs[i] = ((InterestPoint)ipts.get(i)).strength;
    }
    Arrays.sort(strengthOfIPs);
   // p.getStatistics().strengthOfIPs = strengthOfIPs;

    if (!p.isUpright())
      for (InterestPoint ipt : ipts)
        Descriptor.computeAndSetOrientation(ipt, intImg);
      for (InterestPoint ipt : ipts)
        Descriptor.computeAndSetDescriptor(ipt, intImg, p);

    setLastResult(ipts);
    return ipts;
  }

  /**
   * Draw interest points.
   *
   * @param img the img
   * @param ipts the ipts
   * @param params the params
   */
  public static void drawInterestPoints(ImageProcessor img, List<InterestPoint> ipts, Params params)
  {
    for (InterestPoint ipt : ipts)
      drawSingleInterestPoint(img, params, ipt);
  }

  /**
   * Draw single interest point.
   *
   * @param img the img
   * @param p the p
   * @param ipt the ipt
   */
  public static void drawSingleInterestPoint(ImageProcessor img, Params p, InterestPoint ipt)
  {
    int x = Math.round(ipt.xpos);
    int y = Math.round(ipt.ypos);
    float w = ipt.scale * 10.0F;
    float ori = ipt.orientation;
    float co = (float)Math.cos(ori);
    float si = (float)Math.sin(ori);
    float s = ipt.strength * 10000.0F;

    if (p.isDisplayDescriptorWindows()) {
      img.setLineWidth(p.getLineWidth());
      img.setColor(p.getDescriptorWindowColor());

      float x0 = w * (si + co) + ipt.xpos; float y0 = w * (-co + si) + ipt.ypos;
      float x1 = w * (si - co) + ipt.xpos; float y1 = w * (-co - si) + ipt.ypos;
      float x2 = w * (-si - co) + ipt.xpos; float y2 = w * (co - si) + ipt.ypos;
      float x3 = w * (-si + co) + ipt.xpos; float y3 = w * (co + si) + ipt.ypos;

      img.moveTo(x, y);
      img.lineTo(Math.round(x0), Math.round(y0));
      img.lineTo(Math.round(x1), Math.round(y1));
      img.lineTo(Math.round(x2), Math.round(y2));
      img.lineTo(Math.round(x3), Math.round(y3));
      img.lineTo(x, y);
    }

    if (p.isDisplayOrientationVectors()) {
      img.setLineWidth(p.getLineWidth());
      img.setColor(p.getOrientationVectorColor());
      img.drawLine(x, y, Math.round(s * co + x), Math.round(s * si + y));
    }

    img.setLineWidth(p.getLineWidth() * 4);
    if (ipt.sign)
      img.setColor(p.getDarkPointColor());
    else
      img.setColor(p.getLightPointColor());
    img.drawDot(x, y);
  }

}
