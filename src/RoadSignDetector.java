import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;

import database.ConnectToDb;

import recognition.CompareImages;
import recognition.FindPoints;
import recognition.InterestPoint;
import recognition.Matcher;

// TODO: Auto-generated Javadoc
/**
 * The Class RoadSignDetector.
 */
public class RoadSignDetector extends JFrame {

   /** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

  /** The tool bar. */
  protected JToolBar toolBar;
  
  /** The image. */
  private BufferedImage image;
  
  /** The path. */
  private String path = null;
  
  /** The connect. */
  private ConnectToDb connect;
  

  /**
   * Instantiates a new road sign detector.
   */
  public RoadSignDetector() {
    super("Road Sign Detector");
    setSize(500, 500);
    add(new JComponent()
    {
       
			private static final long serialVersionUID = 1L;

			public void paintComponent(Graphics g)
       {
				
          if (image != null) g.drawImage(image, 0, 0, null);

       }
    });

    JMenuBar menuBar = createMenuBar();
    setJMenuBar(menuBar);

    WindowListener wndCloser = new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    };
    addWindowListener(wndCloser);
        setVisible(true);
  }

  /**
   * Creates the menu bar.
   *
   * @return the j menu bar
   */
  protected JMenuBar createMenuBar() {
    final JMenuBar menuBar = new JMenuBar();

    JMenu mFile = new JMenu("File");
    mFile.setMnemonic('f');


    ImageIcon iconOpen = new ImageIcon("/Users/hemanth/Documents/workspace/RoadSignDetector/icons/open.jpeg");
    Action actionOpen = new AbstractAction("Open...", iconOpen) {
      public void actionPerformed(ActionEvent e) {
    	  openFile();
      }
    };
    JMenuItem item = mFile.add(actionOpen);
    mFile.add(item);

    ImageIcon iconSave = new ImageIcon("/Users/hemanth/Documents/workspace/RoadSignDetector/icons/images.jpeg");
    Action actionSave = new AbstractAction("Save To DB...", iconSave) {
      public void actionPerformed(ActionEvent e) {
        
    	  //showmessage("save action");
          //openFile();
         if(path != null){
    	  String str = JOptionPane.showInputDialog(null, "Enter Description of the Road Sign : ", "Road Sign", 1);
      			  if(str != null)
      			  JOptionPane.showMessageDialog(null, "You entered the text : " + str, "0.45", 1);
      			  else
      			  JOptionPane.showMessageDialog(null, "You pressed cancel button.", "Road Sign", 1);
      			  
          	//String description = str;
          	int type = 0;
          	connect = new ConnectToDb();
          	connect.insertToDB(str, path, type );
      	}

      }
    };
    item = mFile.add(actionSave);
    mFile.add(item);

    mFile.addSeparator();
    
    ImageIcon exitIcon = new ImageIcon("/Users/hemanth/Documents/workspace/RoadSignDetector/icons/exit.jpeg");
    Action actionExit = new AbstractAction("Exit", exitIcon) {
      public void actionPerformed(ActionEvent e) {
        System.exit(0);
      }
    };
    item = mFile.add(actionExit);
    item.setMnemonic('x');
    menuBar.add(mFile);

    toolBar = new JToolBar();
    JButton btn2 = toolBar.add(actionOpen);
    btn2.setToolTipText("Open text file");
    JButton btn3 = toolBar.add(actionSave);
    btn3.setToolTipText("Save text file");

    JMenu imageEditor = new JMenu("Detect Road Sign");
    imageEditor.setMnemonic('d');
    ImageIcon processIcon = new ImageIcon("/Users/hemanth/Documents/workspace/RoadSignDetector/icons/process.jpeg");
    Action processSign = new AbstractAction("Process Road Sign", processIcon) {
      public void actionPerformed(ActionEvent e) {
        
    	  openDirectory();
      }
    };
    JMenuItem surf = imageEditor.add(processSign);   
    imageEditor.add(surf);
    menuBar.add(imageEditor);
    JButton btn1 = toolBar.add(processSign);
    btn1.setToolTipText("Process Image");

    getContentPane().add(toolBar, BorderLayout.NORTH);

    return menuBar;
  }
  
  /**
   * Open a file and load the image.
   */
	
  public void openFile()
  {
     JFileChooser chooser = new JFileChooser();
     chooser.setCurrentDirectory(new File("."));
     String[] extensions = ImageIO.getReaderFileSuffixes();
     chooser.setFileFilter(new FileNameExtensionFilter("Image files", extensions));
     int r = chooser.showOpenDialog(this);
     if (r != JFileChooser.APPROVE_OPTION) return;
     path = chooser.getSelectedFile().toString();
     try
     {
        Image img = ImageIO.read(chooser.getSelectedFile());
        image = new BufferedImage(img.getWidth(null), img.getHeight(null),
              BufferedImage.TYPE_INT_RGB);
        image.getGraphics().drawImage(img, 0, 0, null);
     }
     catch (IOException e)
     {
        JOptionPane.showMessageDialog(this, e);
     }
     repaint();
  }

  /**
   * Open directory to open the director containing images.
   */
  public void openDirectory(){
	   
	    BufferedImage directoryImage, tempImage = null;
	    boolean doReverseComparisonToo = true;
      File imagedir = new File(".");
      String directoryPath ;
      File myDirectory = null;
      String fileName = null;
      int fileNumber =0;
      
      /* connect to Database */
    	connect = new ConnectToDb();
        connect.selectFromDB();
      try {
			directoryPath = imagedir.getCanonicalPath()+"/imagedir";
			myDirectory = new File(directoryPath);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
 	    Vector <BufferedImage> images = new Vector<BufferedImage>();
        Vector <String> names = new Vector<String>();
 	     BufferedImage img1 = null;
	     String[] directoryFiles = myDirectory.list();
	     for(int i=0; i <directoryFiles.length; i++){
	    	 try
	         {
	    		  File file = new File(myDirectory,directoryFiles[i] );
	    		  String ext = getExtension(file.getName());
	    		  if(ext.equalsIgnoreCase("gif") ||ext.equalsIgnoreCase("jpeg") || ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("png") ){
	              img1 = ImageIO.read(file);
	              names.add(file.getName());
	  	    	  images.add(img1);
	    		  }

	         }catch (IOException e)
	         {
	             JOptionPane.showMessageDialog(this, e);
	         }
	    	
	     }
	     

	     Map<InterestPoint, InterestPoint> matchedPoints, tempMatchingPoints = new HashMap<InterestPoint, InterestPoint>();
	     FindPoints findPoints = new FindPoints();
	     List <InterestPoint> sourceInterestPoints;
	     findPoints.setImage(image);
	     findPoints.run();
	     sourceInterestPoints = findPoints.getInterestPoints();

	     List <InterestPoint> destInterestPoints;
	     for(int i=0; i< images.size(); i++){
	     	findPoints.setImage(images.get(i));

	     	findPoints.run();
	     	destInterestPoints =findPoints.getInterestPoints();
	     	
	   	     matchedPoints = Matcher.findMathes(sourceInterestPoints, destInterestPoints);
	   	     
	   	     if (doReverseComparisonToo) {
			       Map<InterestPoint, InterestPoint> matchedPointsReverse = Matcher.findMathes(destInterestPoints, sourceInterestPoints);
			       matchedPoints = CompareImages.intersection(matchedPoints, matchedPointsReverse);
			  }
	   	     
	   	     directoryImage = findPoints.getFinalImage();
	    	// createFrame("Matched Image",directoryImage);

	   	     if(matchedPoints.size() > tempMatchingPoints.size()){
	   	        tempMatchingPoints = matchedPoints;	
		   	     tempImage = directoryImage;
		   	     fileName = names.get(i);
	   	     }		    	 
	     }
	    // splash.showSplashAndExit();

	     if (tempMatchingPoints.size() == 0) {
	        // System.out.println("No Match found");
	    	 showmessage("No Matching Found");
	         
	     }else{
	    	 /* show matching points */
	    	 //tempImage = findPoints.drawInterestPoints(tempMatchingPoints,tempImage);
	    	 
	    	 /* Show matched Image of DB */
	    	 createFrame("Matched Image",tempImage);
	    	 //showmessage("No.of Matching Points Found:"+tempMatchingPoints.size());
	         connect.getInfo(fileNumber);
	         String contentDescription = connect.getInfo(Integer.parseInt(getFileName(fileName)));

             showmessage(contentDescription);
	     }

}

/**
 * Creates the frame.
 *
 * @param str the str
 * @param image the image
 */
public void createFrame(String str, BufferedImage image){
	   
	   ShowFrame showFrame = new ShowFrame(str, image);
	   showFrame.callPaint();
}

/**
 * Gets the extension.
 *
 * @param fileName the file name
 * @return the extension
 */
public String  getExtension(String fileName) {
	  String ext="";
	  int mid= fileName.lastIndexOf(".");
	  ext=fileName.substring(mid+1,fileName.length());  
	  return ext;  
	}

/**
 * Gets the file name.
 *
 * @param fileName the file name
 * @return the file name
 */
public String  getFileName(String fileName) {
	  String imgName="";
	  int mid= fileName.lastIndexOf(".");
	  imgName=fileName.substring(0,mid);  
	  return imgName;  
	}

  /**
   * The main method.
   *
   * @param argv the arguments
   */
  public static void main(String argv[]) {
    new RoadSignDetector();
  }
  
  /**
   * Showmessage.
   *
   * @param str the str
   */
  public void showmessage(String str){
	  
      JOptionPane.showMessageDialog(null, str);

  }
}
 