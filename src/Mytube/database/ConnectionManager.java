package Mytube.database;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
public class ConnectionManager {
	
	private static String driver="oracle.jdbc.driver.OracleDriver";
	private static String url="jdbc:oracle:thin:@localhost:1521:XE";
	private static String user="hr";
	private static String password="hr";
	private static ConnectionManager cm=new ConnectionManager();
	static {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static ConnectionManager getInstance(){
		return cm;
	}
	private ConnectionManager() {}
	
	public Connection getConnection()
	{
		Connection con=null;
		
		try {
			con=DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
	}
	public static void close(Connection con)
	{
		try {
			if(con!=null)
			{
				con.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
