package first_and_last_runs;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import coupons.Beans.Coupon;
import coupons.Beans.CouponType;
import coupons.Exceptions.CouponFacadeException;
import coupons.Exceptions.IllegalInputException;
import coupons.Facades.CompanyFacade;

public  class Creation {
	
	private static Creation instance = new Creation();
	
	
	private Creation(){
		
	}
	
	public static Creation getinstance() {
		return instance;
	}
	

	String driverName = "org.apache.derby.jdbc.ClientDriver";
	String url =  "jdbc:derby://localhost:1527/coupon_system_db;create=true";
	
	String query;

	
			 
	 public void doCreate(){
			try{
				Class.forName(driverName);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			try (Connection con = DriverManager.getConnection(url); java.sql.Statement stmt = con.createStatement();) {
				query = "CREATE TABLE coupon(id BIGINT PRIMARY KEY NOT NULL, name VARCHAR(30) UNIQUE, start_date DATE, end_date DATE,amount INT, type VARCHAR(30), description VARCHAR(60),price DOUBLE, image VARCHAR(60))";
				stmt.executeUpdate(query);
				query = "CREATE TABLE company(id BIGINT PRIMARY KEY NOT NULL,companyName VARCHAR(30) UNIQUE, email VARCHAR(30), password VARCHAR(30))";
				stmt.executeUpdate(query);
				query = "CREATE TABLE customer(id BIGINT PRIMARY KEY NOT NULL, customerName VARCHAR(30) UNIQUE, password VARCHAR(30))";
				stmt.executeUpdate(query);
				query = "CREATE TABLE company_coupon(company_id BIGINT NOT NULL, coupon_id BIGINT ,PRIMARY KEY (company_id, coupon_id))";
				stmt.executeUpdate(query);
				query = "CREATE TABLE customer_coupon(customer_id BIGINT NOT NULL, coupon_id BIGINT, PRIMARY KEY (customer_id,coupon_id))";
				stmt.executeUpdate(query);
				query = "CREATE TABLE lastUsedId (custid BIGINT, compid BIGINT, coupid BIGINT)";
				stmt.executeUpdate(query);
				query = "INSERT INTO lastUsedId VALUES(2,2,2)";
				stmt.executeUpdate(query);
				query = "CREATE TABLE deleted_Coupons (id BIGINT, name VARCHAR(30) , start_date DATE, end_date DATE,amount INT, type VARCHAR(30), description VARCHAR(30),price DOUBLE, image VARCHAR(30))";
				stmt.executeUpdate(query);
				query = "CREATE TABLE coupon_pics(coupon_name VARCHAR(30) UNIQUE, pics_name VARCHAR(30) UNIQUE )";
				stmt.executeUpdate(query);

			} catch (SQLException e) {
				e.printStackTrace();
			}

	 }
	 
	 

	 public void doInitializefields(){
		 CompanyFacade facade = new CompanyFacade(1);
			try{
				Class.forName(driverName);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			try (Connection con = DriverManager.getConnection(url);
					java.sql.Statement stmt = con.createStatement();) {
		
				query = "INSERT INTO company VALUES(1,'CompLtd','CompLtd@gmail.com','CompLtd')";
				stmt.executeUpdate(query);

				query = "INSERT INTO customer VALUES(1, 'CustBase', 'CustBase')";
				stmt.executeUpdate(query);

				Coupon coupon = new Coupon();
				coupon.setCouponName("Basic Coupon Done");
				coupon.setDescription("20% Off Dinner");
				coupon.setStartDate(facade.convertStringToDate("01/02/2016"));
				coupon.setEndDate(facade.convertStringToDate("01/02/2025"));
				coupon.setPrice(45);
				coupon.setAmount(30);
				coupon.setCouponType(CouponType.FOOD);
				coupon.setImage("basicDinner.jpg");
				facade.createCoupon(coupon);	
				
				query = "INSERT INTO company_coupon VALUES(1,1)";
				stmt.executeUpdate(query);
			

			} catch (SQLException | CouponFacadeException | IllegalInputException e) {
				e.printStackTrace();
			}

	 }
 
		
	
	
	 }
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


