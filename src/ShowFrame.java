

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

// TODO: Auto-generated Javadoc
/**
 * The Class ShowFrame.
 */
public class ShowFrame extends JFrame {
  
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
    
    /** The image. */
    private BufferedImage  image;
  
  /**
   * Instantiates a new show frame.
   *
   * @param frameName the frame name
   * @param imageInput the image input
   */
  public ShowFrame(String frameName, BufferedImage imageInput) {
  
	  setTitle(frameName);
	  setSize(200, 200);
	  setVisible(true);
	  this.image = imageInput;
	  
  }
  
  /**
   * Call paint.
   */
  public void callPaint(){
	  repaint();
  }

  /* (non-Javadoc)
   * @see java.awt.Window#paint(java.awt.Graphics)
   */
  public void paint(Graphics g) {
	  
     g.drawImage( image, 0, 0, null);
  }
  
}