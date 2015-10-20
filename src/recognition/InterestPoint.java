package recognition;


import ij.IJ;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * The Class InterestPoint denotes the Interesting points in an image which is obtained by Hessian Matrix in SURF.
 */
public class InterestPoint
{
  
  /** The x Position */
  public float xpos;
  
  /** The y position */
  public float ypos;
  
  /** The strength. */
  public float strength;
  
  /** The trace. */
  public float trace;
  
  /** The sign. */
  public boolean sign;
  
  /** The scale. */
  public float scale;
  
  /** The orientation. */
  public float orientation;
  
  /** The descriptor. */
  public float[] descriptor;
  
  /** The dx. */
  public float dx;
  
  /** The dy. */
  public float dy;

  /**
   * Instantiates a new interest point.
   */
  public InterestPoint() {}

  /**
   * Instantiates a new interest point.
   *
   * @param xpos the x position
   * @param ypos the y position
   * @param strength the strength
   * @param trace the trace
   * @param scale the scale
   */
  public InterestPoint(float xpos, float ypos, float strength, float trace, float scale)
  {
    this.xpos = xpos;
    this.ypos = ypos;
    this.strength = strength;
    this.trace = trace;
    this.scale = scale;
    this.sign = (trace >= 0.0F);
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    Formatter f = new Formatter(sb, Locale.US);

    f.format("%12f %12f %12f %12f %12f %12f ", new Object[] { Float.valueOf(this.xpos), Float.valueOf(this.ypos), Float.valueOf(this.strength), Float.valueOf(this.trace), Float.valueOf(this.scale), Float.valueOf(this.orientation) });
    int descSize = this.descriptor == null ? 0 : this.descriptor.length;
    f.format("%12d ", new Object[] { Integer.valueOf(descSize) });

    if (descSize > 0) {
      for (int i = 0; i < descSize; i++) {
        if (i % 8 == 0) f.format("\n", new Object[0]);
        f.format("%12f ", new Object[] { Float.valueOf(this.descriptor[i]) });
      }
      f.format("\n", new Object[0]);
    }
    return f.toString();
  }

  /**
   * Save to Interesting points to a file. This will help us in reducing the computation time while comparing images.
   *
   * @param ipts the ipts
   * @param fileName the file name
   * @param inclDescriptor the incl descriptor
   */
  public static void saveToFile(List<InterestPoint> ipts, String fileName, boolean inclDescriptor) {
    try {
      PrintWriter out = new PrintWriter(fileName);
      if ((ipts != null) && (ipts.size() > 0))
      {
        for (InterestPoint ipt : ipts) {
          if (inclDescriptor) {
            out.println(ipt);
          }
          else {
            float[] temp = ipt.descriptor;
            ipt.descriptor = null;
            out.println(ipt);
            ipt.descriptor = temp;
          }
        }
      }
      out.close();
    } catch (FileNotFoundException e) {  }
  }

  /**
   * Loads the Interestpoints from file.
   *
   * @param fileName the file name
   * @return the list
   */
  public static List<InterestPoint> loadFromFile(String fileName)
  {
    try {
      Scanner in = new Scanner(new File(fileName));

      int iptsSize = in.nextInt();

      List ipts = new ArrayList(iptsSize);

      for (int i = 0; i < iptsSize; i++)
      {
        float x = in.nextFloat();
        float y = in.nextFloat();
        float strength = in.nextFloat();
        float trace = in.nextFloat();
        float scale = in.nextFloat();
        float ori = in.nextFloat();
        int descSize = in.nextInt();

        InterestPoint ipt = new InterestPoint(x, y, strength, trace, scale);
        ipt.orientation = ori;
        if (descSize > 0) {
          ipt.descriptor = new float[descSize];
          for (int j = 0; j < descSize; j++)
            ipt.descriptor[j] = in.nextFloat();
        }
        ipts.add(ipt);
      }

      in.close();
      return ipts;
    }
    catch (FileNotFoundException e) {    }

    return null;
  }
}