package coupons.DAO.DBDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import coupons.Beans.Coupon;
import coupons.Beans.CouponType;
import coupons.Beans.Customer;
import coupons.ConnectionPool.ConnectionPool;
import coupons.DAOinterfaces.CustomerDAO;
import coupons.Exceptions.DaoCouponException;
import coupons.Exceptions.DuplicateEntryException;
import coupons.Exceptions.IllegalLoginException;
import coupons.Exceptions.RequestNotFoundException;

public class CustomerDBDAO implements CustomerDAO {

	private ConnectionPool connectionPool = ConnectionPool.getinstance();

	/**
	 * a constant used to identify if the id was found in the database.
	 */
	private final int ID_NOT_FOUND = 0;

	/**
	 * This constructor initiates the customerDBDAO and loads the couponDBDAO.
	 */
	public CustomerDBDAO() {

	}

	/**
	 * This method is used by the administrator user to create a new customer in
	 * the database customer table. using the given customer, the customer name
	 * and password are added. the id is created automatically using the
	 * getNewCustomerId method.
	 * 
	 * @param customer
	 *            -provides all of the required fields: name and password.
	 * 
	 * @throws DuplicateEntryException
	 *             if the customer (name) already exists in the database.
	 * @throws DaoCouponException
	 *             if a new customer id could not be generated or if a
	 *             SQLException occurred.
	 * 
	 */
	@Override
	public void createCustomer(Customer customer) throws DaoCouponException, DuplicateEntryException {
		String query = "INSERT INTO customer VALUES(?,?,?)";
		Connection conn = connectionPool.getConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			long newCustId = getNewCustomerId();
			customer.setCustomerId(newCustId);
			pstmt.setLong(1, newCustId);
			pstmt.setString(2, customer.getCustomerName());
			pstmt.setString(3, customer.getPassword());
			pstmt.executeUpdate();
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new DuplicateEntryException("Duplicate key value: customer already exists ");
		} catch (SQLException e) {
			throw new DaoCouponException("A database access error occured: " + e.getMessage());
		} finally {
			connectionPool.returnConnection(conn);
		}
	}

	/**
	 * This method is used by the administrator user to delete a customer from
	 * the customer table in the database. if the customer has purchased coupons
	 * in the past he will also be deleted from the customer_coupon list.
	 * 
	 * @param customerId
	 *            -used to identify the customer to be deleted.
	 * @throws RequestNotFoundException-
	 *             if the requested customer does not exist in the database.
	 * 
	 * @throws DaoCouponException
	 *             if a SQLException occurred.
	 */
	@Override
	public void deleteCustomer(long customerId) throws DaoCouponException, RequestNotFoundException {
		String query = "DELETE FROM customer WHERE id=?";
		Connection conn = connectionPool.getConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setLong(1, customerId);
			int status = pstmt.executeUpdate();
			if (status == ID_NOT_FOUND) {
				throw new RequestNotFoundException(customerId);
			} else {
				String query2 = "DELETE FROM customer_coupon WHERE customer_id=?";
				PreparedStatement pstmt2 = conn.prepareStatement(query2);
				pstmt2.setLong(1, customerId);
				pstmt2.executeUpdate();
			}
		} catch (SQLException e) {
			throw new DaoCouponException("A database access error occured: " + e.getMessage());
		} finally {
			connectionPool.returnConnection(conn);
		}
	}

	/**
	 * Used by the administrator to update a customer in the customer table. the
	 * field that is permitted for update is only the customers password.
	 * 
	 * @param customer
	 *            -used to identify the customer in the database ( customer id)
	 *            and supplies the new password.
	 * @throws RequestNotFoundException-
	 *             if the requested customer does not exist in the database.
	 * @throws DaoCouponException
	 *             if a SQLException occurred.
	 */
	@Override
	public void updateCustomer(Customer customer) throws DaoCouponException, RequestNotFoundException {
		String query = "UPDATE customer SET password = ? WHERE id = ?";
		Connection conn = connectionPool.getConnection();
		try {
			Customer daoCustomer = customer;
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, daoCustomer.getPassword());
			pstmt.setLong(2, daoCustomer.getCustomerId());
			int status = pstmt.executeUpdate();
			if (status == ID_NOT_FOUND) {
				throw new RequestNotFoundException(customer.getCustomerId());
			}
		} catch (SQLException e) {
			throw new DaoCouponException("A database access error occured: " + e.getMessage());

		} finally {
			connectionPool.returnConnection(conn);
		}
	}

	/**
	 * This method is used by the administrator user to fetch a specific
	 * Customer from the dataBase.
	 * 
	 * @param customerId
	 *            -provides the customer id used to identify the customer in the
	 *            database. the customer is then built field by field using the
	 *            "Build Customer from RS method"
	 * @return the requested customer;
	 * @throws RequestNotFoundException-
	 *             if the requested customer does not exist in the database.
	 * 
	 * @throws DaoCouponException
	 *             if a SQLException occurred.
	 * 
	 */
	@Override
	public Customer getCustomer(long customerId) throws DaoCouponException, RequestNotFoundException {
		String query = "SELECT * FROM customer WHERE id = ?";
		Customer customer = null;
		Connection conn = connectionPool.getConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setLong(1, customerId);
			ResultSet rs = pstmt.executeQuery();
			boolean requestedCustomerExists = rs.next();
			if (requestedCustomerExists) {
				customer = buildCustomerFromRS(rs);
			} else {
				throw new RequestNotFoundException(customerId);
			}
			return customer;
		} catch (SQLException e) {
			throw new DaoCouponException("A database access error occured: " + e.getMessage());
		}

	}

	/**
	 * This method is used by the administrator user to receive a list of all of
	 * the Customers in the dataBase customer table. each Customer is created
	 * using the "Build Customer from RS" method and added to a new HashSet.
	 * 
	 * @return a set of customers.
	 * @throws RequestNotFoundException-
	 *             if no customers exist in the database.
	 * @throws DaoCouponException
	 *             if a SQLException occurred.
	 */
	@Override
	public Set<Customer> getAllCustomers() throws DaoCouponException, RequestNotFoundException {
		Set<Customer> customerList = new HashSet<>();
		String query = "SELECT * FROM customer";
		Connection conn = connectionPool.getConnection();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				Customer customer = buildCustomerFromRS(rs);
				customerList.add(customer);
			}
			if (customerList.size() < 1) {
				throw new RequestNotFoundException("Empty list-No customers found.");
			}
			return customerList;
		} catch (SQLException e) {
			throw new DaoCouponException("A database access error occured: " + e.getMessage());
		} finally {
			connectionPool.returnConnection(conn);
		}
	}

	/**
	 * This method is used by the customer user to receive a list of all of the
	 * coupons that they have purchased in the past. each found coupon is
	 * created using the "Build coupon from RS" method and added to a new
	 * HashSet.
	 * 
	 * @return a set of coupons.
	 * @param customerId
	 *            -provides this.customer id used to identify the customer in
	 *            the database.
	 * @throws RequestNotFoundException-
	 *             if no coupon exist in the customers coupon list.
	 * @throws DaoCouponException
	 *             if a SQLException occurred.
	 */
	@Override
	public Set<Coupon> getCustomerCoupons(long customerId) throws DaoCouponException, RequestNotFoundException {
		Set<Coupon> couponList = new HashSet<>();
		Connection conn = connectionPool.getConnection();
		try {
			String query = "SELECT COUPON.* FROM CUSTOMER_COUPON INNER JOIN COUPON ON CUSTOMER_COUPON.COUPON_ID = COUPON.ID AND CUSTOMER_COUPON.CUSTOMER_ID = ?";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setLong(1, customerId);
			ResultSet rs = pstmt.executeQuery();
			CouponDBDAO couponDBDAO = new CouponDBDAO();
			while (rs.next()) {
				Coupon coupon = couponDBDAO.buildCouponFromRS(rs);
				couponList.add(coupon);
			}
			if (couponList.size() < 1) {
				throw new RequestNotFoundException("Empty list-No customer coupons found.");
			}
			return couponList;
		} catch (SQLException e) {
			throw new DaoCouponException("A database access error occured:" + e.getMessage());
		} finally {
			connectionPool.returnConnection(conn);
		}
	}

	/**
	 * The boolean Login method is used by the Main Coupon System Login - it is
	 * used to check if the customer exists in the customer table in the
	 * database.
	 * 
	 * @return true- if the login credentials match an existing customer.
	 * @param name
	 *            -provides the customers name.
	 * @param password
	 *            -provides the customers password.
	 * @throws DaoCouponException
	 *             if a SQLException occurred.
	 */
	@Override
	public Boolean login(String name, String password) throws DaoCouponException {
		boolean isLoggedInSuccessfully = false;
		String query = "SELECT * FROM customer WHERE customerName = ? AND password = ?";
		Connection conn = connectionPool.getConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, name);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				isLoggedInSuccessfully = true;
			}
			return isLoggedInSuccessfully;
		} catch (SQLException e) {
			throw new DaoCouponException("A database access error occured:" + e.getMessage());

		} finally {
			connectionPool.returnConnection(conn);
		}
	}

	/**
	 * this method is used to build a new customer using fields that are taken
	 * from a given result set.
	 * 
	 * @param resultSet
	 *            - received from a previous sql query
	 * @return a customer matching the one received in the result set.
	 * @throws DaoCouponException
	 *             if a SQLException occurred.
	 */
	public Customer buildCustomerFromRS(ResultSet resultSet) throws DaoCouponException {
		try {
			Customer customer = new Customer();
			customer.setCustomerId(resultSet.getLong(1));
			customer.setCustomerName(resultSet.getString(2));
			customer.setPassword(resultSet.getString(3));
			return customer;
		} catch (SQLException e) {
			throw new DaoCouponException("A database access error occured:" + e.getMessage());
		}
	}

	/**
	 * This method is used by the create customer method. it fetches a new id
	 * from the last used id table in the DB and returns it. it then forwards
	 * the previous id by 1. this insures a unique id for each new created
	 * customer.
	 * 
	 * @return a unique id.
	 * @throws DaoCouponException
	 *             if a new customer id could not be generated or if a
	 *             SQLException occurred..
	 */
	public long getNewCustomerId() throws DaoCouponException {
		long newCustId = 0;
		String query = "SELECT custId FROM lastUsedId";
		Connection conn = connectionPool.getConnection();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if (rs.next()) {
				newCustId = (rs.getLong(1));

				String query2 = "UPDATE lastUsedId SET custId =" + (newCustId + 1);
				stmt.executeUpdate(query2);
			}
			return newCustId;
		} catch (SQLException e) {
			throw new DaoCouponException("A database access error occured:" + e.getMessage());
		} finally {
			connectionPool.returnConnection(conn);
		}
	}

	/**
	 * This method is used by the Main Coupon System Login - it searches the
	 * customer table for a customer with the requested user and password and
	 * returns there id.
	 * 
	 * @param name
	 *            -sent at login-provides the customers name.
	 * @param password
	 *            -sent at login-provides the customers password.
	 * @return the matching customers id.
	 * @throws DaoCouponException
	 *             if the matching login details ( user and password) are not
	 *             found
	 * @throws IllegalLoginException-
	 *             if a SQLException occurred..
	 */
	public long getLoginId(String name, String password) throws DaoCouponException, IllegalLoginException {
		String query = "SELECT * FROM customer WHERE customerName = ? AND password = ?";
		Connection conn = connectionPool.getConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, name);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				long custId = rs.getLong(1);
				return custId;
			} else {
				throw new IllegalLoginException(
						"Could not get login id with given user & password (" + name + "," + password + ")");
			}
		} catch (SQLException e) {
			throw new DaoCouponException("A database access error occured:" + e.getMessage());
		} finally {
			connectionPool.returnConnection(conn);
		}
	}

	/**
	 * This method is used by the Customer user to receive a list of all of the
	 * coupons of a specific Coupon Type that have been purchased by them. each
	 * corresponding coupon is then built field by field using the
	 * "Build coupon from RS method".
	 * 
	 * @param customerId
	 *            -provides this.customers id used to identify the customer in
	 *            the database.
	 * @param CouponType
	 *            -provides the requested coupon type to search for.
	 * @return a set of the customers coupons of the requested type.
	 * @throws RequestNotFoundException
	 *             if no coupons of the requested type exist in the customer
	 *             coupon list.
	 * @throws DaoCouponException
	 *             if a SQLException occurred.
	 * 
	 */
	public Set<Coupon> getCustomerCouponsByType(long customerId, CouponType CouponType)
			throws DaoCouponException, RequestNotFoundException {
		Set<Coupon> couponListByType = new HashSet<>();
		Connection conn = connectionPool.getConnection();
		try {
			String query = "SELECT COUPON.* FROM CUSTOMER_COUPON INNER JOIN COUPON ON CUSTOMER_COUPON.COUPON_ID = COUPON.ID AND CUSTOMER_COUPON.CUSTOMER_ID= ? AND COUPON.TYPE=? ";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setLong(1, customerId);
			pstmt.setString(2, CouponType.toString());
			ResultSet rs = pstmt.executeQuery();
			CouponDBDAO couponDBDAO = new CouponDBDAO();
			while (rs.next()) {
				Coupon coupon = couponDBDAO.buildCouponFromRS(rs);
				couponListByType.add(coupon);
			}
			if (couponListByType.size() < 1) {
				throw new RequestNotFoundException("You have no Coupons of the type " + CouponType);
			}
			return couponListByType;
		} catch (SQLException e) {
			throw new DaoCouponException("A database access error occured:" + e.getMessage());
		} finally {
			connectionPool.returnConnection(conn);
		}
	}

	/**
	 * This method is used by the Customer user to receive a list of all of the
	 * coupons that have been purchased by them that cost less than the sent
	 * price. each corresponding coupon is then built field by field using the
	 * "Build coupon from RS method".
	 * 
	 * @param customerId
	 *            -provides this.customers id used to identify the customer in
	 *            the database.
	 * @param maxPrice
	 *            -provides the requested maximum price to search by.
	 * @return a set of the customers coupons that cost less than the sent
	 *         price.
	 * @throws RequestNotFoundException
	 *             if no coupons of the requested price range exist in the
	 *             customer coupon list.
	 * @throws DaoCouponException
	 *             if a SQLException occurred.
	 * 
	 */
	public Set<Coupon> getCustomerCouponsByPrice(long customerId, double maxPrice)
			throws DaoCouponException, RequestNotFoundException {
		Set<Coupon> couponListByPrice = new HashSet<>();
		Connection conn = connectionPool.getConnection();
		try {
			String query = "SELECT COUPON.* FROM CUSTOMER_COUPON INNER JOIN COUPON ON CUSTOMER_COUPON.COUPON_ID = COUPON.ID AND CUSTOMER_COUPON.CUSTOMER_ID= ? AND COUPON.PRICE <=? ";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setLong(1, customerId);
			pstmt.setDouble(2, maxPrice);
			ResultSet rs = pstmt.executeQuery();
			CouponDBDAO couponDBDAO = new CouponDBDAO();
			while (rs.next()) {
				Coupon coupon = couponDBDAO.buildCouponFromRS(rs);
				couponListByPrice.add(coupon);
			}
			if (couponListByPrice.size() < 1) {
				throw new RequestNotFoundException("You have no Coupons with a price less than:" + maxPrice);
			}
			return couponListByPrice;
		} catch (SQLException e) {
			throw new DaoCouponException("A database access error occured:" + e.getMessage());
		} finally {
			connectionPool.returnConnection(conn);
		}
	}

	/**
	 * This method is used after a customer has purchased a coupon. it receives
	 * The purchasing customer id and the requested coupon id and adds them to
	 * the customer_coupon list in the database.
	 * 
	 * @param customerId
	 *            -provides this.customer id used to identify the customer in
	 *            the database.
	 * @param couponId
	 *            -provides the coupon id that the customer is wanting to
	 *            purchase.
	 * 
	 * @throws RequestNotFoundException
	 *             - if no customer or coupon with the given id is found.
	 * @throws DuplicateEntryException
	 *             - if this coupon has been purchesed in the past.
	 * @throws DaoCouponException
	 *             if a SQLException occurred. *
	 */
	public void addPurchesedCoupon(long customerId, long couponId)
			throws DaoCouponException, RequestNotFoundException, DuplicateEntryException {
		String query = "INSERT INTO customer_coupon VALUES (?,?)";
		Connection conn = connectionPool.getConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setLong(1, customerId);
			pstmt.setLong(2, couponId);
			int status = pstmt.executeUpdate();
			if (status == ID_NOT_FOUND) {
				throw new RequestNotFoundException(customerId);
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new DuplicateEntryException("This coupon has already been purchesed by you");
		} catch (SQLException e) {
			throw new DaoCouponException("A database access error occured: " + e.getMessage());
		} finally {
			connectionPool.returnConnection(conn);
		}
	}
}
