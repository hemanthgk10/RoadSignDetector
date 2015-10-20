package recognition;

// TODO: Auto-generated Javadoc
/**
 * The Class Descriptor.
 */
public class Descriptor
{
  
  /** The Constant pi. */
  static final float pi = 3.14159F;
  
  /** The Constant gauss25. */
  static final double[][] gauss25 = { 
    { 0.02350693969273D, 0.01849121369071D, 0.01239503121241D, 0.00708015417522D, 0.00344628101733D, 0.00142945847484D, 0.0005052487906D }, 
    { 0.02169964028389D, 0.01706954162243D, 0.01144205592615D, 0.00653580605408D, 0.00318131834134D, 0.00131955648461D, 0.00046640341759D }, 
    { 0.01706954162243D, 0.01342737701584D, 0.009000639979389999D, 0.00514124713667D, 0.00250251364222D, 0.00103799989504D, 0.00036688592278D }, 
    { 0.01144205592615D, 0.009000639979389999D, 0.00603330940534D, 0.00344628101733D, 0.00167748505986D, 0.00069579213743D, 0.00024593098864D }, 
    { 0.00653580605408D, 0.00514124713667D, 0.00344628101733D, 0.00196854695367D, 0.00095819467066D, 0.00039744277546D, 0.0001404780098D }, 
    { 0.00318131834134D, 0.00250251364222D, 0.00167748505986D, 0.00095819467066D, 0.00046640341759D, 0.00019345616757D, 6.837798818E-05D }, 
    { 0.00131955648461D, 0.00103799989504D, 0.00069579213743D, 0.00039744277546D, 0.00019345616757D, 8.024231247E-05D, 2.836202103E-05D } };

  /** The Constant gauss33. */
  static final double[][] gauss33 = { 
    { 0.014614763D, 0.013958917D, 0.012162744D, 0.00966788D, 0.00701053D, 0.004637568D, 0.002798657D, 0.001540738D, 0.000773799D, 0.000354525D, 0.000148179D }, 
    { 0.013958917D, 0.013332502D, 0.011616933D, 0.009234028D, 0.006695928D, 0.004429455D, 0.002673066D, 0.001471597D, 0.0007390740000000001D, 0.000338616D, 0.000141529D }, 
    { 0.012162744D, 0.011616933D, 0.010122116D, 0.008045833000000001D, 0.005834325D, 0.003859491D, 0.002329107D, 0.001282238D, 0.0006439730000000001D, 0.000295044D, 0.000123318D }, 
    { 0.00966788D, 0.009234028D, 0.008045833000000001D, 0.006395444D, 0.004637568D, 0.003067819D, 0.001851353D, 0.001019221D, 0.000511879D, 0.000234524D, 9.802240000000001E-05D }, 
    { 0.00701053D, 0.006695928D, 0.005834325D, 0.004637568D, 0.003362869D, 0.002224587D, 0.001342483D, 0.0007390740000000001D, 0.000371182D, 0.000170062D, 7.10796E-05D }, 
    { 0.004637568D, 0.004429455D, 0.003859491D, 0.003067819D, 0.002224587D, 0.001471597D, 0.000888072D, 0.000488908D, 0.000245542D, 0.000112498D, 4.70202E-05D }, 
    { 0.002798657D, 0.002673066D, 0.002329107D, 0.001851353D, 0.001342483D, 0.000888072D, 0.000535929D, 0.000295044D, 0.000148179D, 6.78899E-05D, 2.83755E-05D }, 
    { 0.001540738D, 0.001471597D, 0.001282238D, 0.001019221D, 0.0007390740000000001D, 0.000488908D, 0.000295044D, 0.00016243D, 8.15765E-05D, 3.73753E-05D, 1.56215E-05D }, 
    { 0.000773799D, 0.0007390740000000001D, 0.0006439730000000001D, 0.000511879D, 0.000371182D, 0.000245542D, 0.000148179D, 8.15765E-05D, 4.09698E-05D, 1.87708E-05D, 7.84553E-06D }, 
    { 0.000354525D, 0.000338616D, 0.000295044D, 0.000234524D, 0.000170062D, 0.000112498D, 6.78899E-05D, 3.73753E-05D, 1.87708E-05D, 8.60008E-06D, 3.59452E-06D }, 
    { 0.000148179D, 0.000141529D, 0.000123318D, 9.802240000000001E-05D, 7.10796E-05D, 4.70202E-05D, 2.83755E-05D, 1.56215E-05D, 7.84553E-06D, 3.59452E-06D, 1.50238E-06D } };

  /**
   * Compute and set descriptor.
   *
   * @param ipt the ipt
   * @param intImg the int img
   * @param p the p
   */
  public static void computeAndSetDescriptor(InterestPoint ipt, IntegralImage intImg, Params p)
  {
    float si;
    float co = 1.0F;
    if (p.isUpright()) {
      si = 0.0F;
    } else {
      co = (float)Math.cos(ipt.orientation);
      si = (float)Math.sin(ipt.orientation);
    }

    float[] desc = new float[p.getDescSize()];

    int count = 0;
    int ix = 0; int jx = 0; int xs = 0; int ys = 0;
    float gauss_s1 = 0.0F; float gauss_s2 = 0.0F;
    float rx = 0.0F; float ry = 0.0F; float rrx = 0.0F; float rry = 0.0F; float len = 0.0F;

    float scale = ipt.scale;
    int doubledScale = 2 * Math.round(scale);
    int x = Math.round(ipt.xpos);
    int y = Math.round(ipt.ypos);

    int i = -8; int j = 0;
    float cx = -0.5F; float cy = 0.0F;

    while (i < 12) {
      j = -8;
      i -= 4;
      cx += 1.0F;
      cy = -0.5F;

      while (j < 12)
      {
        float mdy;
        float mdx;
        float dy;
        float dx = dy = mdx = mdy = 0.0F;
        cy += 1.0F;
        j -= 4;
        ix = i + 5;
        jx = j + 5;
        xs = Math.round(x + (-jx * scale * si + ix * scale * co));
        ys = Math.round(y + (jx * scale * co + ix * scale * si));

        for (int k = i; k < i + 9; k++) {
          for (int l = j; l < j + 9; l++)
          {
            int sample_x = Math.round(x + scale * (-l * si + k * co));
            int sample_y = Math.round(y + scale * (l * co + k * si));

            gauss_s1 = gaussian(xs - sample_x, ys - sample_y, 2.5F * scale);
            rx = haarX(intImg, sample_x, sample_y, doubledScale);
            ry = haarY(intImg, sample_x, sample_y, doubledScale);

            rrx = gauss_s1 * (-rx * si + ry * co);
            rry = gauss_s1 * (rx * co + ry * si);

            dx += rrx;
            dy += rry;
            mdx += Math.abs(rrx);
            mdy += Math.abs(rry);
          }

        }

        gauss_s2 = gaussian(cx - 2.0F, cy - 2.0F, 1.5F);

        desc[(count++)] = (dx * gauss_s2);
        desc[(count++)] = (dy * gauss_s2);
        desc[(count++)] = (mdx * gauss_s2);
        desc[(count++)] = (mdy * gauss_s2);

        len += (dx * dx + dy * dy + mdx * mdx + mdy * mdy) * gauss_s2 * gauss_s2;

        j += 9;
      }
      i += 9;
    }

    len = (float)Math.sqrt(len);
    for (int idx = 0; idx < p.getDescSize(); idx++) {
      desc[idx] /= len;
    }

    ipt.descriptor = desc;
  }

  /**
   * Compute and set orientation.
   *
   * @param ipt the ipt
   * @param intImg the int img
   */
  public static void computeAndSetOrientation(InterestPoint ipt, IntegralImage intImg)
  {
    float[] resX = new float[109];
    float[] resY = new float[109];
    float[] Ang = new float[109];
    int[] id = { 6, 5, 4, 3, 2, 1, 0, 1, 2, 3, 4, 5, 6 };

    int x = Math.round(ipt.xpos); int y = Math.round(ipt.ypos); int s = Math.round(ipt.scale);
    int waveletSize = 4 * s;
    int i = 0;
    float gauss = 0.0F;
    for (int dx = -6; dx <= 6; dx++) {
      for (int dy = -6; dy <= 6; dy++) {
        if (dx * dx + dy * dy < 36) {
          gauss = (float)gauss25[id[(dx + 6)]][id[(dy + 6)]];
          resX[i] = (gauss * haarX(intImg, x + dx * s, y + dy * s, waveletSize));
          resY[i] = (gauss * haarY(intImg, x + dx * s, y + dy * s, waveletSize));
          Ang[i] = getAngle(resX[i], resY[i]);
          i++;
        }

      }

    }

    float windowSize = 1.047197F;
    float step = 0.15F;

    float twoPi = 6.28318F;
    float sumX = 0.0F; float sumY = 0.0F;
    float current = 0.0F; float max = 0.0F; float orientation = 0.0F;
    float ang1 = 0.0F; for (float ang2 = ang1 + 1.047197F; ang1 < twoPi; ang2 += 0.15F) {
      sumX = sumY = 0.0F;
      for (int k = 0; k < Ang.length; k++)
      {
        if (((ang1 > Ang[k]) || (Ang[k] >= ang2)) && (
          (ang1 > Ang[k] + twoPi) || (Ang[k] + twoPi >= ang2))) continue;
        sumX += resX[k];
        sumY += resY[k];
      }

      current = sumX * sumX + sumY * sumY;
      if (current > max) {
        max = current;
        orientation = getAngle(sumX, sumY);
      }
      ang1 += 0.15F;
    }

    ipt.orientation = orientation;
  }

  /**
   * Gaussian.
   *
   * @param x the x
   * @param y the y
   * @param sig the sig
   * @return the float
   */
  static float gaussian(float x, float y, float sig)
  {
    return (float)(1.0F / (6.28318F * sig * sig) * Math.exp(-(x * x + y * y) / (2.0F * sig * sig)));
  }

  /**
   * Haar x.
   *
   * @param img the img
   * @param x the x
   * @param y the y
   * @param w the w
   * @return the float
   */
  static float haarX(IntegralImage img, int x, int y, int w)
  {
    int wHalf = w / 2;
    return img.area(x, y - wHalf, wHalf, w) - 
      img.area(x - wHalf, y - wHalf, wHalf, w);
  }

  /**
   * Haar y.
   *
   * @param img the img
   * @param x the x
   * @param y the y
   * @param w the w
   * @return the float
   */
  static float haarY(IntegralImage img, int x, int y, int w)
  {
    int wHalf = w / 2;
    return img.area(x - wHalf, y, w, wHalf) - 
      img.area(x - wHalf, y - wHalf, w, wHalf);
  }

  /**
   * Gets the angle.
   *
   * @param x the x
   * @param y the y
   * @return the angle
   */
  static float getAngle(float x, float y)
  {
    if ((x >= 0.0F) && (y >= 0.0F)) return (float)Math.atan(y / x);
    if ((x < 0.0F) && (y >= 0.0F)) return (float)(3.141590118408203D - Math.atan(-y / x));
    if ((x < 0.0F) && (y < 0.0F)) return (float)(3.141590118408203D + Math.atan(y / x));
    if ((x >= 0.0F) && (y < 0.0F)) return (float)(6.283180236816406D - Math.atan(-y / x));
    return 0.0F;
  }
}

