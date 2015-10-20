package recognition;

import ij.ImagePlus;
import ij.process.ImageProcessor;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

/**
 * The Class FindPoints is used to find the Interesting points in an image.
 */
public class FindPoints {

	/** The final image. */
	private BufferedImage image, finalImage;
	
	/** The ipts. */
	private List<InterestPoint> ipts;
    
    /** The image plus. */
    private ImagePlus imagePlus;
    
    /** The params. */
    private Params params;
    
	/**
	 * Instantiates a new find points.
	 */
	public FindPoints(){
		 params = new Params();
	}
	
	/**
	 * Instantiates a new find points.
	 *
	 * @param image the image
	 */
	public FindPoints(BufferedImage image){		
		this.image = image;
		imagePlus = new ImagePlus("finalImage", image);
		params = new Params();
	}
	
	/**
	 * Gets the final image.
	 *
	 * @return the final image
	 */
	public BufferedImage getFinalImage() {
		return finalImage;
	}
	
	/**
	 * Sets the image.
	 *
	 * @param image the new image
	 */
	public void setImage(BufferedImage image) {
		this.image = image;
		imagePlus = new ImagePlus("finalImage", image);

	}
	
	/**
	 * Run.
	 */
	public void run(){
		
		IntegralImage integralImage = new IntegralImage(imagePlus.getProcessor(), true);
	    ipts = IJFacade.detectAndDescribeInterestPoints(integralImage, params);

	    if (ipts.size() == 0) {
	    	System.out.println("No Interest Points found");
	        
	      }
	    
	    ImageProcessor ip2 = imagePlus.getProcessor().duplicate().convertToRGB();
	    
	    /* unncomment the below line to see the interesting points in an image. */
	    //IJFacade.drawInterestPoints(ip2, ipts, params);
	    ImagePlus outputImage = new ImagePlus("finalImage", ip2);
        finalImage = outputImage.getBufferedImage();
       
	}
	
	/**
	 * Gets the interest points.
	 *
	 * @return the interest points
	 */
	public List<InterestPoint> getInterestPoints(){
		return ipts;	
	}
	
	/**
	 * Draw interest points.
	 *
	 * @param matchedPoints the matched points
	 * @param finalImage the final image
	 * @return the buffered image
	 */
	public BufferedImage drawInterestPoints(Map<InterestPoint, InterestPoint> matchedPoints, BufferedImage finalImage){
		ImagePlus ip = new ImagePlus("finalImage", finalImage);
	    ImageProcessor ip2 = ip.getProcessor().duplicate().convertToRGB();
	    
	    for (Map.Entry pair : matchedPoints.entrySet()) {
	      IJFacade.drawSingleInterestPoint(ip2, params, (InterestPoint)pair.getValue());
	    }
	    ImagePlus outputImage = new ImagePlus("finalImage", ip2);
        finalImage = outputImage.getBufferedImage();

		return finalImage;
		
	}
	
}
