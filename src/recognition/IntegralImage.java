package recognition;


import ij.process.ByteProcessor;
import ij.process.ColorProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import ij.process.ShortProcessor;

// TODO: Auto-generated Javadoc
/**
 * The Class IntegralImage.
 */
public class IntegralImage
{
  
  /** The data. */
  private float[][] data;
  
  /** The width. */
  private int width;
  
  /** The height. */
  private int height;
  
  /** The max x. */
  private int maxX;
  
  /** The max y. */
  private int maxY;

  /**
   * Sets the data.
   *
   * @param a the new data
   */
  private void setData(float[][] a)
  {
    this.data = a;
    this.width = this.data.length;
    this.height = this.data[0].length;
    this.maxX = (this.width - 1);
    this.maxY = (this.height - 1);
  }

  /**
   * Gets the.
   *
   * @param x the x
   * @param y the y
   * @return the float
   */
  public float get(int x, int y)
  {
    return this.data[x][y];
  }

  /**
   * Gets the width.
   *
   * @return the width
   */
  public int getWidth()
  {
    return this.width;
  }

  /**
   * Gets the height.
   *
   * @return the height
   */
  public int getHeight() {
    return this.height;
  }

  /**
   * Gets the max x.
   *
   * @return the max x
   */
  public int getMaxX() {
    return this.maxX;
  }

  /**
   * Gets the max y.
   *
   * @return the max y
   */
  public int getMaxY() {
    return this.maxY;
  }

  /**
   * Instantiates a new integral image.
   *
   * @param src the src
   */
  public IntegralImage(ImageProcessor src)
  {
    setData(src.convertToByte(false).getFloatArray());

    convertInternalBufferToIntegralImage();
  }

  /**
   * Convert internal buffer to integral image.
   */
  private void convertInternalBufferToIntegralImage()
  {
    float rowSum = 0.0F;

    for (int x = 0; x < this.width; x++) {
      rowSum += this.data[x][0];
      this.data[x][0] = rowSum;
    }

    for (int y = 1; y < this.height; y++) {
      rowSum = 0.0F;
      for (int x = 0; x < this.width; x++) {
        rowSum += this.data[x][y];
        this.data[x][y] = (rowSum + this.data[x][(y - 1)]);
      }
    }
  }

  /**
   * Instantiates a new integral image.
   *
   * @param src the src
   * @param weightedAndNormalizedConversion the weighted and normalized conversion
   */
  public IntegralImage(ImageProcessor src, boolean weightedAndNormalizedConversion)
  {
    int width = src.getWidth();
    int height = src.getHeight();
    float[][] a = new float[width][height];
    float min = 3.4028235E+38F; float max = 1.4E-45F;

    if (((src instanceof ByteProcessor)) || ((src instanceof ShortProcessor)) || ((src instanceof FloatProcessor))) {
      int i = 0; for (int y = 0; y < height; y++)
        for (int x = 0; x < width; x++) {
          float val = src.getf(i++);
          a[x][y] = val;
          if (val < min) { min = val; } else {
            if (val <= max) continue; max = val;
          }
        }
    }
    else if ((src instanceof ColorProcessor))
    {
      float rw = 0.299F; float gw = 0.587F; float bw = 0.114F;

      int i = 0; for (int y = 0; y < height; y++)
        for (int x = 0; x < width; x++)
        {
          int intVal = src.get(i++);
          int r = (intVal & 0xFF0000) >> 16;
          int g = (intVal & 0xFF00) >> 8;
          int b = intVal & 0xFF;
          float val = r * rw + g * gw + b * bw;

          a[x][y] = val;
          if (val < min) { min = val; } else {
            if (val <= max) continue; max = val;
          }
        }
    }
    else {
      System.out.println("Unknown image type for Integral Image.");
      return;
    }
    int y;
    float scale = 1.0F / (max - min);
    for (int x = 0; x < width; x++) {
      float[] col = a[x];
      for (y = 0; y < height; y++) {
        float val = col[y] - min;
        if (val < 0.0F) val = 0.0F;
        val *= scale;
        if (val > 1.0F) val = 1.0F;
        col[y] = val;
      }
    }

    setData(a);

    convertInternalBufferToIntegralImage();
  }

  /**
   * Area.
   *
   * @param x1 the x1
   * @param y1 the y1
   * @param rectWidth the rect width
   * @param rectHeight the rect height
   * @return the float
   */
  float area(int x1, int y1, int rectWidth, int rectHeight)
  {
    x1--; y1--;
    int x2 = x1 + rectWidth;
    int y2 = y1 + rectHeight;

    if (x1 > this.maxX) x1 = this.maxX;
    if (y1 > this.maxY) y1 = this.maxY;
    if (x2 > this.maxX) x2 = this.maxX;
    if (y2 > this.maxY) y2 = this.maxY;

    float A = (x1 < 0) || (y1 < 0) ? 0.0F : this.data[x1][y1];
    float B = (x2 < 0) || (y1 < 0) ? 0.0F : this.data[x2][y1];
    float C = (x1 < 0) || (y2 < 0) ? 0.0F : this.data[x1][y2];
    float D = (x2 < 0) || (y2 < 0) ? 0.0F : this.data[x2][y2];

    return D - B - C + A;
  }

  /**
   * Area2.
   *
   * @param x1 the x1
   * @param y1 the y1
   * @param rectWidth the rect width
   * @param rectHeight the rect height
   * @return the float
   */
  float area2(int x1, int y1, int rectWidth, int rectHeight)
  {
    x1--; y1--;
    int x2 = x1 + rectWidth;
    int y2 = y1 + rectHeight;
    return this.data[x2][y2] - this.data[x2][y1] - this.data[x1][y2] + this.data[x1][y1];
  }
}
