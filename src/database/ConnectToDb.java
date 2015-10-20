package database;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSetMetaData;


/**
 * The Class ConnectToDb class contains method to retrieve all the images from DB, retrieve the information 
 * of a matched image and method for saving an road sign to db.
 */
public class ConnectToDb
{
    
    /** The db url. */
    private static String dbURL = "jdbc:derby:MyDbTest;create=true;";
    private static String tableName = "roadSignDb";
    /** The table name. */
  // private static String tableName = "testingDb";
    
    /** The fin. */
    private static FileInputStream fin = null;
    
    /** The pst. */
    private static PreparedStatement pst = null;
    
    /** The stmt. */
    private static Statement stmt = null;

    /** The conn. */
    private static Connection conn = null;
    
    /** The fos. */
    private static FileOutputStream fos = null;
    
    /** The rs. */
    private ResultSet rs = null;



    /**
     * Creates the connection to database
     */
    private  void createConnection()
    {
        try
        {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            
            //Get a connection
            conn = DriverManager.getConnection(dbURL); 
        }
        catch (Exception except)
        {
            except.printStackTrace();
        }
    }
    
    /**
     * Insert a road sign in to the database.
     *
     * @param description the description
     * @param path the path of the image.
     * @param type the type of image
     */
    public void insertToDB( String description, String path, int type )
    {
        createConnection();
 
        try
        {
            File img = new File(path);
            fin = new FileInputStream(img);
            pst = conn.prepareStatement("INSERT INTO roadSignDb (description, imagedata,type) VALUES ('" +  description + "' , ?," + type + " )");
            pst.setBinaryStream(1, fin, (int) img.length());
            pst.executeUpdate();
            
            
        }catch (FileNotFoundException ex){
        	ex.printStackTrace();
        }
      
        catch (SQLException sqlExcept){
        	sqlExcept.printStackTrace();
        }
        
        shutdown();

    }
    
    /**
     * Retrieves all the images stored in Database.
     */
    public  void selectFromDB()
    {
        createConnection();

        try
        {
            String query = "SELECT * FROM roadSignDb";
            pst = conn.prepareStatement(query);
            File imagedir = new File(".");
            String path = null, tempPath;
            path = imagedir.getCanonicalPath()+"/imagedir";
            
            ResultSet result = pst.executeQuery();
            ResultSetMetaData rsmd = result.getMetaData();
            int numberCols = rsmd.getColumnCount();
            /*
            for (int i=1; i<=numberCols; i++)
            {
                //print Column Names
                System.out.print(rsmd.getColumnLabel(i)+"\t\t");  
            }*/

            System.out.println();
            while(result.next()){
                tempPath = path;
                int id = result.getInt(1);
                String desc = result.getString(2);
                String imageName = Integer.toString(id)+".jpg";
                tempPath = tempPath + "/" + imageName;
                File file = new File(tempPath);
	            fos = new FileOutputStream(file);
	            Blob blob = result.getBlob("IMAGEDATA");
	            int len = (int) blob.length();
	            byte[] buf = blob.getBytes(1, len);
	            fos.write(buf, 0, len);
	            int type = result.getInt(4);
            }
        }catch (IOException ex) {
            ex.printStackTrace();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
        shutdown();

    }
    
    /**
     * Gets the info of the roadsign matched with the input image.
     *
     * @param imageNo the Image Number
     * @return the info
     */
    public String getInfo(int imageNo){
	
        createConnection();
    	String desc = null;
        try
        {
            String query = "SELECT * FROM roadSignDb where id ="+imageNo+" ORDER BY ID ASC";
            //System.out.println("query:"+query);
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();
            while(rs.next()){
            	desc = rs.getString(2);
            }

        }catch (Exception ex) {
            ex.printStackTrace();
        }
   	
        shutdown();
    	return desc;
    
    }
    
    /**
     * Shutdown the connection to Database.
     */
    private static void shutdown()
    {
        try
        {
            if (pst != null)
            {
                pst.close();
            }
            if (stmt != null)
            {
                stmt.close();
            }
            
            if (conn != null)
            {
                DriverManager.getConnection(dbURL + ";shutdown=true");
                conn.close();
            }           
        }
        catch (SQLException sqlExcept)
        {
              }

    }
}

