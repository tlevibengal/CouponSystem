package first_and_last_runs;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Destroy {
	private static Destroy instance = new Destroy();
	
	
	private Destroy(){
		
	}
	
	public static Destroy getinstance() {
		return instance;
	}
	

	String driverName = "org.apache.derby.jdbc.ClientDriver";
	String url =  "jdbc:derby://localhost:1527/coupon_system_db;create=true";
	
	String query;

		 
		public void removeTables(){

			String  url =  "jdbc:derby://localhost:1527/coupon_system_db;create=true";
			String query;

			try (Connection con = DriverManager.getConnection(url); java.sql.Statement stmt = con.createStatement();) {
				query = "DROP TABLE coupon";
				stmt.executeUpdate(query);
				query = "DROP TABLE company";
				stmt.executeUpdate(query);
				query = "DROP TABLE customer";
				stmt.executeUpdate(query);
				query = "DROP TABLE company_coupon";
				stmt.executeUpdate(query);
				query = "DROP TABLE customer_coupon";
				stmt.executeUpdate(query);
				query = "DROP TABLE lastUsedId";
				stmt.executeUpdate(query);
				query = "DROP TABLE deleted_coupons";
				stmt.executeUpdate(query);
				query = "DROP TABLE coupon_pics";
				stmt.executeUpdate(query);
				System.out.println("All tables removed from dataBase");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
			 
	 
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


