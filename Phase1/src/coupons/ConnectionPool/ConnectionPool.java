package coupons.ConnectionPool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ConnectionPool {

	private static ConnectionPool instance = new ConnectionPool();
	private static String url;
	private Set<Connection> connectionSet;

	/**
	 * This constructor connects to the database and creates a set of 10
	 * connections, to be used to perform actions in the database. each DAO
	 * method takes a connection at the beginning of the method and returns it
	 * at the end. There are a maximum of 10 connections at one time, if a
	 * method tries to get a connection while there are non available, wait()
	 * method is called.
	 * 
	 * @throws SQLException
	 *             if The connection to the DB was unsuccessful.
	 */
	private ConnectionPool() {

		// FOR USE AT HOME- TAMAR//
			// url = "jdbc:derby://localhost:1527/C:/Users/user/Desktop/java/db/bin/coupon_system_db;create=true";

		// FOR USE AT abba//
		// url ="jdbc:derby://localhost:1527/C:/Users/Windows/Desktop/Java/db/bin/coupon_system_db;create=true";
			 url =  "jdbc:derby://localhost:1527/coupon_system_db;create=true";
			 
		connectionSet = new HashSet<Connection>();

		try {
			Class.forName("org.apache.derby.jdbc.ClientDriver");
		} catch (ClassNotFoundException e1) {
			System.out.println("ERROR LOADING APACHE DRIVER");
			e1.printStackTrace();
		}
		
		for (int i = 0; i < 10; i++) {
			try {
				Connection conn = DriverManager.getConnection(url);
				connectionSet.add(conn);
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("There are no free connections at this time");
			}
		}
	}

	/**
	 * This method creates an instance of the connection pool, calling the
	 * constructor and creating a "pool" of connections to be used.
	 * 
	 * @return An instance of the connection pool
	 */
	public static ConnectionPool getinstance() {
		return instance;
	}


	/**
	 * This method is used at the beginning of every method that tries to
	 * connect to the dataBase. A connection is taken from the connection pool
	 * Set.
	 * 
	 * @return A connection.
	 * @throws InterruptedException
	 *             if there are no available connections in the connection pool
	 *             Set.
	 */
	public synchronized Connection getConnection() {
		while (connectionSet.size() == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println("The thread is currently inactive");
				System.out.println(e.getMessage());
			}
		}

		Connection current = connectionSet.iterator().next();
		connectionSet.remove(current);
		return current;
	}

	/**
	 * This method is used at the end of every method that connects to the
	 * dataBase. it takes the current connection that is being used and puts it
	 * back into the connection pool set.
	 * 
	 * @param conn
	 *            -The connection that is to be returned.
	 */
	public synchronized void returnConnection(Connection conn) {
		connectionSet.add(conn);
		notify();
	}

	/**
	 * This method is used by the System's final shut down operations. It closes
	 * all current active connections.
	 */
	public void closeAllConnections() {
		Connection current = connectionSet.iterator().next();
		try {
			current.close();
			} catch (SQLException e) {
			System.out.println(e.getMessage());
			}

		}
	}


