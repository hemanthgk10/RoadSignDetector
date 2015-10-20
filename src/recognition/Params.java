package recognition;

import java.awt.Color;


// TODO: Auto-generated Javadoc
/**
 * The Class Params.
 */
public class Params
{
  
  /** The filter sizes. */
  private int[][] filterSizes = { { 9, 15, 21, 27 }, { 15, 27, 39, 51 }, { 27, 51, 75, 99 }, { 51, 99, 147, 195 } };
  
  /** The max filter sizes. */
  private int[] maxFilterSizes = { 27, 51, 99, 195 };

  /** The octaves. */
  private int octaves = 4;
  
  /** The layers. */
  private int layers = 4;

  /** The double image size. */
  boolean doubleImageSize = false;

  /** The threshold. */
  private float threshold = 0.001F;

  /** The init step. */
  private int initStep = 2;

  /** The step inc factor. */
  private int stepIncFactor = 2;
  
  /** The upright. */
  private boolean upright;
  
  /** The desc size. */
  private int descSize = 64;

  /** The display orientation vectors. */
  private boolean displayOrientationVectors = true;

  /** The display descriptor windows. */
  private boolean displayDescriptorWindows = false;

  /** The line width. */
  private int lineWidth = 1;

  /**
   * Instantiates a new params with the default values.
   */
  public Params()
  {
    this.octaves = 4;
    this.layers = 4;
    this.threshold = 0.00100f;
    this.initStep = 2;
    this.upright = false;
    this.displayOrientationVectors = true;
    this.displayDescriptorWindows = false;
    this.lineWidth = 1;
  }

  /**
   * Instantiates a new params.
   *
   * @param p the p
   */
  public Params(Params p) {
    this.octaves = p.octaves;
    this.layers = p.layers;
    this.threshold = p.threshold;
    this.initStep = p.initStep;
    this.upright = p.upright;
    this.displayOrientationVectors = p.displayOrientationVectors;
    this.displayDescriptorWindows = p.displayDescriptorWindows;
    this.lineWidth = p.lineWidth;
  }

  /**
   * Gets the octaves.
   *
   * @return the octaves
   */
  public int getOctaves()
  {
    return this.octaves; 
  } 
  
  /**
   * Gets the layers.
   *
   * @return the layers
   */
  public int getLayers() { return this.layers; }

  /**
   * Gets the filter size.
   *
   * @param octave the octave
   * @param layer the layer
   * @return the filter size
   */
  public int getFilterSize(int octave, int layer) {
    return this.filterSizes[octave][layer];
  }

  /**
   * Gets the max filter size.
   *
   * @param octave the octave
   * @return the max filter size
   */
  public int getMaxFilterSize(int octave)
  {
    return this.maxFilterSizes[octave];
  }

  /**
   * Gets the threshold.
   *
   * @return the threshold
   */
  public float getThreshold()
  {
    return this.threshold;
  }

  /**
   * Gets the inits the step.
   *
   * @return the inits the step
   */
  public int getInitStep()
  {
    return this.initStep;
  }
  
  /**
   * Gets the step inc factor.
   *
   * @return the step inc factor
   */
  public int getStepIncFactor() {
    return this.stepIncFactor;
  }

  /**
   * Checks if is upright.
   *
   * @return true, if is upright
   */
  public boolean isUpright()
  {
    return this.upright;
  }
  
  /**
   * Gets the desc size.
   *
   * @return the desc size
   */
  public int getDescSize() {
    return this.descSize;
  }

  /**
   * Checks if is display orientation vectors.
   *
   * @return true, if is display orientation vectors
   */
  public boolean isDisplayOrientationVectors()
  {
    return this.displayOrientationVectors;
  }
  
  /**
   * Checks if is display descriptor windows.
   *
   * @return true, if is display descriptor windows
   */
  public boolean isDisplayDescriptorWindows() {
    return this.displayDescriptorWindows;
  }
  
  /**
   * Gets the line width.
   *
   * @return the line width
   */
  public int getLineWidth() {
    return this.lineWidth;
  }
  
  /**
   * Gets the descriptor window color.
   *
   * @return the descriptor window color
   */
  public Color getDescriptorWindowColor() {
	    return Color.PINK; } 
  
  /**
   * Gets the orientation vector color.
   *
   * @return the orientation vector color
   */
  public Color getOrientationVectorColor() { return Color.YELLOW; }

  /**
   * Gets the dark point color.
   *
   * @return the dark point color
   */
  public Color getDarkPointColor() {
	    return Color.BLUE;
	  }
  
  /**
   * Gets the light point color.
   *
   * @return the light point color
   */
  public Color getLightPointColor() {
	    return Color.RED;
	  }

}