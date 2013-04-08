import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class jdemo {

	public static void main(String[] argv) {

		Connection connection = null;
		//load properties
		Properties prop = new Properties();
		String serverIP = "";
		String serverPort = "";
		String dbUser= "";
		String dbPassword = "";
    	try {
               //load a properties file
    			prop.load(new FileInputStream("conf/database.properties"));
               
    			//get the property value and print it out
    			serverIP = prop.getProperty("serverip");
    			serverPort = prop.getProperty("serverport");
    			dbUser = prop.getProperty("dbuser");
    			dbPassword = prop.getProperty("dbpassword");
 
    	} catch (IOException ex) {
    		ex.printStackTrace();
    		return ;
        }
		try { 
			connection = DriverManager.getConnection( "jdbc:sap://" + serverIP + ":" + serverPort + "/?autocommit=false",dbUser,dbPassword); 

		} catch (SQLException e) {

			System.err.println("Connection Failed. User/Passwd Error?");

			return;

		}

		if (connection != null) {

			try {

				System.out.println("Connection to HANA successful!");

				Statement stmt = connection.createStatement();

				ResultSet resultSet = stmt.executeQuery("Select 'hello world' from dummy");

				resultSet.next();

				String hello = resultSet.getString(1);

				System.out.println(hello);

			} catch (SQLException e) {

				System.err.println("Query failed!");

			}

		}

	}
}