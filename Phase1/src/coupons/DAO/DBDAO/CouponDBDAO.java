package coupons.DAO.DBDAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import coupons.Beans.Coupon;
import coupons.Beans.CouponType;
import coupons.ConnectionPool.ConnectionPool;
import coupons.DAOinterfaces.CouponDAO;
import coupons.Exceptions.DaoCouponException;
import coupons.Exceptions.DuplicateEntryException;
import coupons.Exceptions.RequestNotFoundException;

public class CouponDBDAO implements CouponDAO {

	ConnectionPool connectionPool = ConnectionPool.getinstance();

	/**
	 * a constant used to identify if the id was found in the database.
	 */
	private final int ID_NOT_FOUND = 0;

	/**
	 * This constructor initiates the CouponDBDAO.
	 */
	public CouponDBDAO() {

	}

	/**
	 * Used by the company user to create a new coupon in the DB. using the
	 * given coupon, coupon parameters are set in the prepared statement. the id
	 * is created automatically using the getNewCouponId method.
	 * 
	 * @param coupon
	 *            -used later to provide all of the required fields.
	 * 
	 * @throws DuplicateEntryException
	 *             if the coupon already exists in the coupon list.
	 * @throws DaoCouponException
	 *             if a new coupon id could not be generated or if a
	 *             SQLException occurred.
	 */
	@Override
	public void createCoupon(Coupon coupon) throws DaoCouponException, DuplicateEntryException {
		String query = "INSERT INTO COUPON VALUES(?,?,?,?,?,?,?,?,?)";
		Connection conn = connectionPool.getConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			long newCoupId = getNewCouponId();
			coupon.setCouponId(newCoupId);
			pstmt.setLong(1, newCoupId);
			pstmt.setString(2, coupon.getCouponName());
			pstmt.setDate(3, new java.sql.Date(coupon.getStartDate().getTime()));
			pstmt.setDate(4, new java.sql.Date(coupon.getEndDate().getTime()));
			pstmt.setInt(5, coupon.getAmount());
			pstmt.setString(6, coupon.getCouponType().toString());
			pstmt.setString(7, coupon.getDescription());
			pstmt.setDouble(8, coupon.getPrice());
			pstmt.setString(9, coupon.getImage());
			pstmt.executeUpdate();
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new DuplicateEntryException("Duplicate key value: coupon already exists ");
		} catch (SQLException e) {
			throw new DaoCouponException("A database access error occured:" + e.getMessage());
		} finally {
			connectionPool.returnConnection(conn);
		}
	}

	/**
	 * Used by the company user to update a coupon. The fields that are
	 * permitted for update are the coupons end date and price.
	 * 
	 * @param coupon
	 *            -provides the coupon id used to identify the coupon in the
	 *            database and supplies the new end date and price.
	 * @throws RequestNotFoundException
	 *             when no coupon with the given id is found in the DB.
	 * @throws DaoCouponException
	 *             if a SQLException occurred.
	 * 
	 */
	@Override
	public void updateCoupon(Coupon coupon) throws DaoCouponException, RequestNotFoundException {
		String query = "UPDATE coupon SET end_date = ?, price = ? WHERE id= ?";
		Connection conn = connectionPool.getConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setDate(1, new java.sql.Date(coupon.getEndDate().getTime()));
			pstmt.setDouble(2, coupon.getPrice());
			pstmt.setDouble(3, coupon.getCouponId());
			int status = pstmt.executeUpdate();
			if (status == ID_NOT_FOUND) {
				throw new RequestNotFoundException(coupon.getCouponId());
			}
		} catch (SQLException e) {
			throw new DaoCouponException("A database access error occured: " + e.getMessage());
		} finally {
			connectionPool.returnConnection(conn);
		}
	}
	
	@Override
	public void updateCouponImage(String couponName , String couponImageName) throws DaoCouponException, RequestNotFoundException {
		String query = "UPDATE coupon SET image= ? WHERE name= ?";
		Connection conn = connectionPool.getConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1,couponImageName);
			int status = pstmt.executeUpdate();
			if (status == 0) {
				throw new RequestNotFoundException("coupon name: "+couponName+ " was not found");
			}
		} catch (SQLException e) {
			throw new DaoCouponException("A database access error occured: " + e.getMessage());
		} finally {
			connectionPool.returnConnection(conn);
		}
	}

	/**
	 * Used by the customer Facade class. after a coupon has been purchased this
	 * method updates the amount in the coupon list.
	 * 
	 * @param coupon
	 *            -provides the coupon id used to identify the coupon in the
	 *            database and supplies the coupon amount to be updated.
	 * @throws RequestNotFoundException
	 *             -when no coupon with the given id is found in the DB.
	 * @throws DaoCouponException
	 *             if a SQLException occurred.
	 * 
	 */
	@Override
	public void updateCouponAmountAfterPurchise(Coupon coupon) throws DaoCouponException, RequestNotFoundException {
		double oldAmount = coupon.getAmount();
			String query = "UPDATE COUPON SET amount =? WHERE ID= ?";
		Connection conn = connectionPool.getConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setDouble(1, (oldAmount - 1));
			pstmt.setDouble(2, coupon.getCouponId());
			int status = pstmt.executeUpdate();
			if (status == ID_NOT_FOUND) {
				throw new RequestNotFoundException(coupon.getCouponId());
			}
		} catch (SQLException e) {
			throw new DaoCouponException("A database access error occured: " + e.getMessage());
		} finally {
			connectionPool.returnConnection(conn);
		}
	}

	/**
	 * Used by the company user to receive a specific Coupon that they have
	 * created. if found it returns the coupon, that is then built field by
	 * field using the "Build coupon from RS method"
	 * 
	 * @param couponId
	 *            -provides the coupon id used to identify the requested coupon
	 *            in the database.
	 * @return the Coupon that was requested.
	 * @throws RequestNotFoundException
	 *             -when no coupon with the given id is found in the DB.
	 * @throws DaoCouponException-
	 *             when an SQLException occurred.
	 */
	@Override
	public Coupon getCoupon(long couponId) throws DaoCouponException, RequestNotFoundException {
		String query = "SELECT * FROM COUPON WHERE ID = ?";
		Connection conn = connectionPool.getConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setLong(1, couponId);
			ResultSet rs = pstmt.executeQuery();
			boolean foundCoupon = rs.next();
			if (foundCoupon) {
				Coupon coupon = buildCouponFromRS(rs);
				return coupon;
			} else {
				throw new RequestNotFoundException("A Coupon with id: " + couponId + " was not found");
			}
		} catch (SQLException e) {
			throw new DaoCouponException("Connection error- unable to connect to the database ");
		} finally {
			connectionPool.returnConnection(conn);
		}
	}
	
	@Override
	public Coupon getCouponName(String couponName) throws RequestNotFoundException, DaoCouponException {
		String query = "SELECT * FROM COUPON WHERE NAME = ?";
		Connection conn = connectionPool.getConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, couponName);
			ResultSet rs = pstmt.executeQuery();
			boolean foundCoupon = rs.next();
			if (foundCoupon) {
				Coupon coupon = buildCouponFromRS(rs);
				return coupon;
			} else {
				throw new RequestNotFoundException("A Coupon with name: " + couponName + " was not found");
			}
		} catch (SQLException e) {
			throw new DaoCouponException("Connection error- unable to connect to the database ");
		} finally {
			connectionPool.returnConnection(conn);
		}
	}

	/**
	 * Used by the Daily Thread, returns a list of a all of the Coupons in the
	 * coupon list. each found coupon is built field by field using the
	 * "Build coupon from RS method"
	 * 
	 * @return a Set of coupons.
	 * @throws DaoCouponException
	 *             if a SQLException occurred.
	 * @throws RequestNotFoundException
	 *             if no coupons exist in the database.
	 */
	@Override
	public Set<Coupon> getAllCoupons() {
		Set<Coupon> couponList = new HashSet<>();
		String query = "SELECT * FROM COUPON";
		Connection conn = connectionPool.getConnection();
		try{
				PreparedStatement pstmt = conn.prepareStatement(query);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			Coupon coupon = buildCouponFromRS(rs);
			couponList.add(coupon);
		}}catch(SQLException | DaoCouponException e){
			e.printStackTrace();
		}
		connectionPool.returnConnection(conn);
		return couponList;
	}

	/**
	 * This method is used to receive a list of all of the coupons of a specific
	 * Coupon Type. each corresponding coupon is then built field by field using
	 * the "Build coupon from RS method".
	 * 
	 * @param CouponType
	 *            -provides the requested coupon type to search for.
	 * @return a set of the coupons of the requested type.
	 * @throws RequestNotFoundException
	 *             if no coupons of the requested price range exist in the
	 *             customer coupon list.
	 * @throws DaoCouponException
	 *             if a SQLException occurred. *
	 */
	@Override
	public Set<Coupon> getCouponsByType(CouponType couponType) throws DaoCouponException, RequestNotFoundException {
		Set<Coupon> couponListByType = new HashSet<>();
		String query = "SELECT * FROM COUPON WHERE COUPONTYPE = ?";
		Connection conn = connectionPool.getConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, couponType.toString());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				Coupon coupon = buildCouponFromRS(rs);
				couponListByType.add(coupon);
			}
			if (couponListByType.size() < 1) {
				throw new RequestNotFoundException("No Coupons of this type " + couponType + " found in the system");
			}
			return couponListByType;
		} catch (SQLException e) {
			throw new DaoCouponException("A database access error occured:" + e.getMessage());
		} finally {
			connectionPool.returnConnection(conn);
		}
	}

	/**
	 * Used to build a new coupon using fields that are taken from a given
	 * result set.
	 *
	 * @param resultSet
	 *            - received from a previous SQL query
	 * @return a coupon matching the one received in the result set.
	 * @throws DaoCouponException
	 *             if a SQLException occurred.
	 */
	public Coupon buildCouponFromRS(ResultSet resultSet) throws DaoCouponException {
		Coupon coupon = new Coupon();
		try {
			coupon.setCouponId(resultSet.getLong(1));
			coupon.setCouponName(resultSet.getString(2));
			coupon.setStartDate(resultSet.getDate(3));
			coupon.setEndDate(resultSet.getDate(4));
			coupon.setAmount(resultSet.getInt(5));
			coupon.setCouponTypeString((resultSet.getString(6)));
			coupon.setDescription(resultSet.getString(7));
			coupon.setPrice(resultSet.getDouble(8));
			coupon.setImage(resultSet.getString(9));
			return coupon;
		} catch (SQLException e) {
			throw new DaoCouponException("A database access error occured:" + e.getMessage());
		}
	}

	/**
	 * This method is used by the create coupon method. it fetches a new id from
	 * the last used id table in the DB and returns it. it then forwards the
	 * previous id by 1. this insures a unique id for each new created coupon.
	 * 
	 * @return a unique id.
	 * @throws DaoCouponException
	 *             if a new coupon id could not be generated or if a
	 *             SQLException occurred.
	 */
	public long getNewCouponId() throws DaoCouponException {
		long newCoupId = 0;
		String query = "SELECT coupid FROM lastUsedId";
		Connection conn = connectionPool.getConnection();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if (rs.next()) {
				newCoupId = (rs.getLong(1));
				String query2 = "UPDATE lastUsedId SET coupId =" + (newCoupId + 1);
				stmt.executeUpdate(query2);
			}
			return newCoupId;
		} catch (SQLException e) {
			throw new DaoCouponException("A database access error occured:" + e.getMessage());
		} finally {
			connectionPool.returnConnection(conn);
		}
	}

	/**
	 * This method is used to delete a coupon from all of the coupon lists in
	 * the database. (coupon list, company_coupon list and the customer_coupon
	 * list)
	 * 
	 * @param coupon
	 *            -provides the coupon id used to identify the coupon in the
	 *            database.
	 * @throws RequestNotFoundException
	 *             if the requested coupon id doesn't exist in the companies
	 *             coupon list.
	 * @throws DaoCouponException-
	 *             if a SQLException occurred.
	 */
	@Override
	public void deleteCouponFromAllLists(long couponId) throws DaoCouponException, RequestNotFoundException {
		Connection conn = connectionPool.getConnection();
		String query = "DELETE FROM COUPON WHERE ID=?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setLong(1, couponId);
			int status = pstmt.executeUpdate();
			if (status == ID_NOT_FOUND) {
				throw new RequestNotFoundException(couponId);
			}

			String query2 = "DELETE FROM company_coupon WHERE coupon_ID=?";
			PreparedStatement pstmt2 = conn.prepareStatement(query2);
			pstmt2.setLong(1, couponId);
			pstmt2.executeUpdate();

			String query3 = "delete FROM CUSTOMER_COUPON WHERE coupon_ID = ?";
			PreparedStatement pstmt3 = conn.prepareStatement(query3);
			pstmt3.setLong(1, couponId);
			pstmt3.executeUpdate();
		} catch (SQLException e) {
			throw new DaoCouponException("A database access error occured:" + e.getMessage());
		} finally {
			connectionPool.returnConnection(conn);
		}

	}

	/**
	 * This method is used by the daily task thread. it creates a list of all of
	 * the expired coupons and add them to the deleted coupon list.
	 * 
	 * @throws RequestNotFoundException
	 *             if the requested coupon id doesn't exist in the companies
	 *             coupon list.
	 * @return a set of expired coupons.
	 * @throws DaoCouponException-
	 *             if a SQLException occurred.
	 */
	public Set<Coupon> removeExpiredCoupons() throws DaoCouponException {
		Set<Coupon> expiredCoupons = new HashSet<>();
		String query = "SELECT * FROM COUPON WHERE end_date < ?";
		Date today = new java.sql.Date(System.currentTimeMillis());
		Connection conn = connectionPool.getConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setDate(1, today);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Coupon coupon = buildCouponFromRS(rs);

				String query2 = "INSERT INTO deleted_coupons VALUES(?,?,?,?,?,?,?,?,?)";
				PreparedStatement pstmt2 = conn.prepareStatement(query2);
				pstmt2.setLong(1, coupon.getCouponId());
				pstmt2.setString(2, coupon.getCouponName());
				pstmt2.setDate(3, new java.sql.Date(coupon.getStartDate().getTime()));
				pstmt2.setDate(4, new java.sql.Date(coupon.getEndDate().getTime()));
				pstmt2.setInt(5, coupon.getAmount());
				pstmt2.setString(6, coupon.getCouponType().toString());
				pstmt2.setString(7, coupon.getDescription());
				pstmt2.setDouble(8, coupon.getPrice());
				pstmt2.setString(9, coupon.getImage());
				pstmt2.executeUpdate();
				expiredCoupons.add(coupon);
				for (Coupon expiredCoupon : expiredCoupons) {
					deleteCouponFromAllLists(expiredCoupon.getCouponId());
					System.out.println("expired coupon deleted"+expiredCoupon);
				}
			}
			return expiredCoupons;

		} catch (SQLException | DaoCouponException | RequestNotFoundException e) {
			throw new DaoCouponException("A database access error occured:" + e.getMessage());
		} finally {
			connectionPool.returnConnection(conn);
		}
	}

	/**
	 * Used by the administrator, this method is used to retrieve a list of all
	 * of the task deleted coupons.
	 * 
	 * @return a set of coupons.
	 * @throws DaoCouponException
	 *             if a SQLException occurred.
	 */
	public Set<Coupon> getTaskDeletedCoupons() throws DaoCouponException {
		Set<Coupon> deletedCoupons = new HashSet<>();
		String query = "SELECT * FROM deleted_coupons";
		Connection conn = connectionPool.getConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Coupon coupon = buildCouponFromRS(rs);
				deletedCoupons.add(coupon);
			}
			return deletedCoupons;
		} catch (SQLException e) {
			throw new DaoCouponException("A database access error occured:" + e.getMessage());
		} finally {
			connectionPool.returnConnection(conn);
		}
	}
}
