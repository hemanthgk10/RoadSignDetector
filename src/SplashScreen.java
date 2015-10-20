import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

// TODO: Auto-generated Javadoc
/**
 * The Class SplashScreen.
 */
public class SplashScreen extends JWindow {
  
  /** The duration. */
  private int duration;
  
  /** The directory path. */
  private String directoryPath = null ;
  
  /** The imagedir. */
  private File imagedir = new File(".");

  /**
   * Instantiates a new splash screen.
   *
   * @param d the d
   */
  public SplashScreen(int d) {
    duration = d;
	//try {
		//directoryPath = imagedir.getCanonicalPath()+"/icons/animated_progress.gif";
		 directoryPath = "/Users/hemanth/Documents/workspace/RoadSignDetector/icons/animated_progress.gif";
//	} catch (IOException e1) {
	//	e1.printStackTrace();
	//}

  }

  // A simple little method to show a title screen in the center
  // of the screen for the amount of time given in the constructor
  /**
   * Show splash.
   */
  public void showSplash() {
    JPanel content = (JPanel) getContentPane();
    content.setBackground(Color.white);

    // Set the window's bounds, centering the window
    int width = 450;
    int height = 115;
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (screen.width - width) / 2;
    int y = (screen.height - height) / 2;
    setBounds(x, y, width, height);

    // Build the splash screen
    JLabel label = new JLabel(new ImageIcon(directoryPath));
    JLabel copyrt = new JLabel("Processing Images.....",JLabel.CENTER);
    copyrt.setFont(new Font("Sans-Serif", Font.BOLD, 12));
    content.add(label, BorderLayout.CENTER);
    content.add(copyrt, BorderLayout.SOUTH);
    Color oraRed = new Color(156, 20, 20, 255);
    content.setBorder(BorderFactory.createLineBorder(oraRed, 10));
    // Display it
    
    setFocusableWindowState(true);
    setFocusable(true);
    setVisible(true);

    // Wait a little while, maybe while loading resources
    try {
      Thread.sleep(duration);
    } catch (Exception e) {
    }
     setVisible(false);
  }

  /**
   * Show splash and exit.
   */
  public void showSplashAndExit() {
    showSplash();
    //System.exit(0);
  }

}
