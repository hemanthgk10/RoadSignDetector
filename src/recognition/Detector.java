
package recognition;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class Detector.
 */
public class Detector
{
  
  /** The Constant EPSILON. */
  static final float EPSILON = 1.401299E-43F;

  /**
   * Fast hessian.
   *
   * @param img the img
   * @param p the p
   * @return the list
   */
  public static List<InterestPoint> fastHessian(IntegralImage img, Params p)
  {
    float[][][] det = new float[p.getLayers()][img.getWidth()][img.getHeight()];

    float[][][] trace = new float[p.getLayers()][img.getWidth()][img.getHeight()];

    List res = new ArrayList(2000);

    int octave = 0; for (int step = p.getInitStep(); octave < p.getOctaves(); step *= p.getStepIncFactor())
    {
      int margin = p.getMaxFilterSize(octave) / 2;
      int xBound = img.getWidth() - margin;
      int yBound = img.getHeight() - margin;

      for (int layer = 0; layer < p.getLayers(); layer++) {
        int w = p.getFilterSize(octave, layer);
        int L = w / 3;
        int L2 = 2 * L - 1;
        int wHalf = w / 2;
        int LHalf = L / 2;
        int Lminus1 = L - 1;
        float filterArea = w * w;

        for (int y = margin; y < yBound; y += step) {
          for (int x = margin; x < xBound; x += step)
          {
            float Dxx = img.area(x - wHalf, y - Lminus1, w, L2) - 
              img.area(x - LHalf, y - Lminus1, L, L2) * 3.0F;
            float Dyy = img.area(x - Lminus1, y - wHalf, L2, w) - 
              img.area(x - Lminus1, y - LHalf, L2, L) * 3.0F;
            float Dxy = img.area(x - L, y - L, L, L) - 
              img.area(x + 1, y - L, L, L) + 
              img.area(x + 1, y + 1, L, L) - 
              img.area(x - L, y + 1, L, L);

            Dxx /= filterArea;
            Dyy /= filterArea;
            Dxy /= filterArea;

            det[layer][x][y] = (Dxx * Dyy - 0.81F * Dxy * Dxy);
            trace[layer][x][y] = (Dxx + Dyy);
          }

        }

      }

      margin += step; xBound -= step; yBound -= step;

      for (int layer = 1; layer < p.getLayers() - 1; layer++) {
        int filterSize = p.getFilterSize(octave, layer);
        int filterSizeIncrement = filterSize - p.getFilterSize(octave, layer - 1);

        int countIPCandidates = 0;
        int countThresholded = 0;
        int countSuppressed = 0;
        int countInterpolationNotSucceed = 0;
        int countBadInterpolationResult = 0;
        int countIP = 0;

        for (int y = margin; y < yBound; y += step) {
          for (int x = margin; x < xBound; x += step) {
            countIPCandidates++;

            float v = det[layer][x][y];
            if (v < p.getThreshold()) {
              countThresholded++;
            }
            else if (!isLocalMaximum(v, det, layer, x, y, step)) {
              countSuppressed++;
            }
            else
            {
              float[] X = interpolatePoint(det, layer, x, y, step);
              if (X == null) {
                countInterpolationNotSucceed++;
              }
              else
              {
                float xInterp = x + X[0] * step;
                float yInterp = y + X[1] * step;
                float scale = (filterSize + X[2] * filterSizeIncrement) * 0.1333333F;

                if ((scale >= 1.0F) && (xInterp >= 0.0F) && (xInterp < img.getWidth()) && (yInterp >= 0.0F) && (yInterp < img.getHeight()))
                {
                  countIP++;
                  res.add(new InterestPoint(xInterp, yInterp, det[layer][x][y], trace[layer][x][y], scale));
                } else {
                  countBadInterpolationResult++;
                }
              }
            }
          }
        }
       // p.getStatistics().add(octave, layer, countIPCandidates, countThresholded, countSuppressed, countInterpolationNotSucceed, countBadInterpolationResult, countIP);
      }
      octave++;
    }

    return res;
  }

  /**
   * Checks if is local maximum.
   *
   * @param v the v
   * @param det the det
   * @param s the s
   * @param x the x
   * @param y the y
   * @param step the step
   * @return true, if is local maximum
   */
  private static boolean isLocalMaximum(float v, float[][][] det, int s, int x, int y, int step)
  {
    float[][] l = det[(s - 1)]; float[][] m = det[s]; float[][] u = det[(s + 1)];
    int px = x - step; int nx = x + step; int py = y - step; int ny = y + step;

    if ((v >= l[px][py]) && (v >= l[px][y]) && (v >= l[px][ny]) && 
      (v >= l[x][py]) && (v >= l[x][y]) && (v >= l[x][ny]) && 
      (v >= l[nx][py]) && (v >= l[nx][y]) && (v >= l[nx][ny]))
    {
      if ((v >= m[px][py]) && (v >= m[px][y]) && (v >= m[px][ny]) && 
        (v >= m[x][py]) && (v >= m[x][ny]) && 
        (v >= m[nx][py]) && (v >= m[nx][y]) && (v >= m[nx][ny]))
      {
        if ((v >= u[px][py]) && (v >= u[px][y]) && (v >= u[px][ny]) && 
          (v >= u[x][py]) && (v >= u[x][y]) && (v >= u[x][ny]) && 
          (v >= u[nx][py]) && (v >= u[nx][y]) && (v >= u[nx][ny])) return true;
      }
    }
    return 
      false;
  }

  /**
   * Interpolate point.
   *
   * @param det the det
   * @param i the i
   * @param x the x
   * @param y the y
   * @param step the step
   * @return the float[]
   */
  static float[] interpolatePoint(float[][][] det, int i, int x, int y, int step)
  {
    float[][] l = det[(i - 1)]; float[][] m = det[i]; float[][] u = det[(i + 1)];
    int px = x - step; int nx = x + step; int py = y - step; int ny = y + step;

    float dx = -(m[nx][y] - m[px][y]) / 2.0F;
    float dy = -(m[x][ny] - m[x][py]) / 2.0F;
    float ds = -(u[x][y] - l[x][y]) / 2.0F;
    float[] b = { dx, dy, ds };

    float v = m[x][y];

    float dxx = m[px][y] - 2.0F * v + m[nx][y];
    float dxy = (m[nx][ny] - m[px][ny] - m[nx][py] + m[px][py]) / 4.0F;
    float dxs = (u[nx][y] - u[px][y] - l[nx][y] + l[px][y]) / 4.0F;

    float dyx = dxy;
    float dyy = m[x][py] - 2.0F * v + m[x][ny];
    float dys = (u[x][ny] - u[x][py] - l[x][ny] + l[x][py]) / 4.0F;

    float dsx = dxs;
    float dsy = dys;
    float dss = l[x][y] - 2.0F * v + u[x][y];

    float[][] A = { { dxx, dxy, dxs }, { dyx, dyy, dys }, { dsx, dsy, dss } };

    float[] X = new float[3];
    if (solve(A, b, X)) {
      return X;
    }
    return null;
  }

  /**
   * Solve.
   *
   * @param A the a
   * @param B the b
   * @param X the x
   * @return true, if successful
   */
  public static boolean solve(float[][] A, float[] B, float[] X)
  {
    float a = A[0][0]; float b = A[0][1]; float c = A[0][2];
    float d = A[1][0]; float e = A[1][1]; float f = A[1][2];
    float g = A[2][0]; float h = A[2][1]; float i = A[2][2];

    float r = B[0];
    float s = B[1];
    float t = B[2];

    float detA = det(a, b, c, 
      d, e, f, 
      g, h, i);

    if (equal(detA, 0.0F)) {
      return false;
    }
    float detX1 = det(r, b, c, 
      s, e, f, 
      t, h, i);

    float detX2 = det(a, r, c, 
      d, s, f, 
      g, t, i);

    float detX3 = det(a, b, r, 
      d, e, s, 
      g, h, t);

    X[0] = (detX1 / detA);
    X[1] = (detX2 / detA);
    X[2] = (detX3 / detA);

    return true;
  }

  /**
   * Det.
   *
   * @param a the a
   * @param b the b
   * @param c the c
   * @param d the d
   * @param e the e
   * @param f the f
   * @param g the g
   * @param h the h
   * @param i the i
   * @return the float
   */
  static float det(float a, float b, float c, float d, float e, float f, float g, float h, float i)
  {
    return a * (e * i - f * h) + b * (f * g - d * i) + c * (d * h - e * g);
  }

  /**
   * Equal.
   *
   * @param f1 the f1
   * @param f2 the f2
   * @return true, if successful
   */
  public static boolean equal(float f1, float f2)
  {
    return Math.abs(f1 - f2) < 1.401299E-43F;
  }
}