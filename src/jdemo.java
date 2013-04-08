import java.sql.*;

public class jdemo {

	public static void main(String[] argv) {

		Connection connection = null;

		try { 

			connection = DriverManager.getConnection( "jdbc:sap://server:port/?autocommit=false",Account,Password); 

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