package coupons.DAO.DBDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import coupons.Beans.Company;
import coupons.Beans.Coupon;
import coupons.Beans.CouponType;
import coupons.ConnectionPool.ConnectionPool;
import coupons.DAOinterfaces.CompanyDAO;
import coupons.Exceptions.DaoCouponException;
import coupons.Exceptions.DuplicateEntryException;
import coupons.Exceptions.IllegalLoginException;
import coupons.Exceptions.RequestNotFoundException;

public class CompanyDBDAO implements CompanyDAO {

	private ConnectionPool connectionPool = ConnectionPool.getinstance();
	CouponDBDAO couponDBDAO;

	/**
	 * a constant used to identify if the id was found in the database.
	 */
	private final int ID_NOT_FOUND = 0;

	/**
	 * This constructor initiates the companyDBDAO and loads an instance of the
	 * CouponDBDAO.
	 */
	public CompanyDBDAO() {
		couponDBDAO = new CouponDBDAO();
	}

	/**
	 * This method is used by the create company method. it fetches a new id
	 * from the last used id table in the DB and returns it. it then forwards
	 * the previous id by 1. this insures a unique id for each new created
	 * company.
	 * 
	 * @return a unique id.
	 * @throws DaoCouponException
	 *             if a new company id could not be generated or if a
	 *             SQLException occurred.
	 */
	public long getNewCompanyId() throws DaoCouponException {
		long newCompId = 0;
		String query = "SELECT compId FROM lastUsedId";
		Connection conn = connectionPool.getConnection();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if (rs.next()) {
				newCompId = (rs.getLong(1));

				String query2 = "UPDATE lastUsedId SET compId =" + (newCompId + 1);
				stmt.executeUpdate(query2);
			}
			return newCompId;
		} catch (SQLException e) {
			throw new DaoCouponException("A database access error occured:" + e.getMessage());
		} finally {
			connectionPool.returnConnection(conn);
		}
	}

	/**
	 * This method is used by the administrator user to create a new company in
	 * the database company table. using the given argument the company
	 * name,email and password are added. the id is created automatically using
	 * the getNewCompanyId method.
	 * 
	 * @param company
	 *            -provides all of the required fields.( name, email and
	 *            password).
	 * @throws DuplicateEntryException
	 *             if the company (name) already exists in the database.
	 * @throws DaoCouponException
	 *             if a new company id could not be generated or if a
	 *             SQLException occurred.
	 * 
	 */
	@Override
	public void createCompany(Company company) throws DaoCouponException, DuplicateEntryException {
		String query = "INSERT INTO company VALUES(?,?,?,?)";
		Connection conn = connectionPool.getConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			long newCompId = getNewCompanyId();
			company.setCompanyId(newCompId);
			pstmt.setLong(1, newCompId);
			pstmt.setString(2, company.getCompanyName());
			pstmt.setString(3, company.getEmail());
			pstmt.setString(4, company.getPassword());
			pstmt.executeUpdate();
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new DuplicateEntryException("Duplicate key value: company already exists ");
		} catch (SQLException e) {
			throw new DaoCouponException("A database access error occured: " + e.getMessage());
		} finally {
			connectionPool.returnConnection(conn);
		}
	}

	/**
	 * This method is used by the administrator user to delete a company from
	 * the company table in the database.
	 * 
	 * @param companyId
	 *            -used to identify the company to be deleted.
	 * @throws RequestNotFoundException-
	 *             if the requested company does not exist in the database.
	 * @throws DaoCouponException
	 *             if a SQLException occurred.
	 * 
	 */
	@Override
	public void deleteCompany(long companyId) throws DaoCouponException, RequestNotFoundException {
		String query = "DELETE FROM Company WHERE id=?";
		Connection conn = connectionPool.getConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setLong(1, companyId);
			int status = pstmt.executeUpdate();
			if (status == ID_NOT_FOUND) {
				throw new RequestNotFoundException(companyId);
			}
		} catch (SQLException e) {
			throw new DaoCouponException("A database access error occured: " + e.getMessage());
		} finally {
			connectionPool.returnConnection(conn);
		}
	}

	/**
	 * Used by the administrator user to update a company in the company table.
	 * the fields that are permitted for update are only the company email and
	 * password.
	 * 
	 * @param company
	 *            -Given by the user. used to identify the company and set the
	 *            fields that need to be updated.( email and password)
	 * @throws RequestNotFoundException
	 *             if the requested company does not exist in the database.
	 * @throws DaoCouponException
	 *             if a SQLException occurred.
	 */
	@Override
	public void updateCompany(Company company) throws DaoCouponException, RequestNotFoundException {
		String query = "UPDATE company SET email = ?, password = ? WHERE id = ?";
		Connection conn = connectionPool.getConnection();
		try {
			Company daoCompany = company;
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, daoCompany.getEmail());
			pstmt.setString(2, daoCompany.getPassword());
			pstmt.setLong(3, daoCompany.getCompanyId());
			int status = pstmt.executeUpdate();
			if (status == ID_NOT_FOUND) {
				throw new RequestNotFoundException(company.getCompanyId());
			}
		} catch (SQLException e) {
			throw new DaoCouponException("A database access error occured: " + e.getMessage());

		} finally {
			connectionPool.returnConnection(conn);
		}
	}

	/**
	 * This method is used by the administrator user to fetch a specific company
	 * from the dataBase.
	 * 
	 * @param companyId
	 *            - Given by the user. used to identify the requested company
	 * @throws RequestNotFoundException
	 *             if the requested company does not exist in the database.
	 * @throws DaoCouponException
	 *             if a SQLException occurred.
	 * 
	 */
	@Override
	public Company getCompany(long companyId) throws DaoCouponException, RequestNotFoundException {
		String query = "SELECT * FROM company WHERE id = ?";
		Company company = null;
		try {
			Connection conn = connectionPool.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setLong(1, companyId);
			ResultSet rs = pstmt.executeQuery();
			connectionPool.returnConnection(conn);
			boolean requestedCompany = rs.next();
			if (requestedCompany) {
				company = buildCompanyFromRS(rs);
			} else {
				throw new RequestNotFoundException(companyId);
			}
			return company;
		} catch (SQLException e) {
			throw new DaoCouponException("A database access error occured: " + e.getMessage());
		}

	}

	/**
	 * This method is used by the administrator user to receive a list of all of
	 * the companies in the dataBase company table. each company is created
	 * using the "Build company from RS" method and added to a new HashSet.
	 * 
	 * @return a set of companies.
	 * @throws RequestNotFoundException-
	 *             if no companies exist in the database.
	 * @throws DaoCouponException
	 *             if a SQLException occurred.
	 */
	@Override
	public Set<Company> getAllCompanies() throws DaoCouponException, RequestNotFoundException {
		Set<Company> companyList = new HashSet<>();
		String query = "SELECT * FROM company";
		Connection conn = connectionPool.getConnection();
		try {
			java.sql.Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			connectionPool.returnConnection(conn);
			while (rs.next()) {
				Company company = buildCompanyFromRS(rs);
				companyList.add(company);
			}
			if (companyList.size() < 1) {
				throw new RequestNotFoundException("Empty list-No companies found.");
			}
			return companyList;
		} catch (SQLException e) {
			throw new DaoCouponException("A database access error occured: " + e.getMessage());
		} finally {
			connectionPool.returnConnection(conn);
		}
	}

	/**
	 * This method is used by the company user to receive a list of all of the
	 * coupons that they have created in the past. each found coupon is created
	 * using the "Build coupon from RS" method and added to a new HashSet.
	 * 
	 * @return a set of coupons.
	 * @param compnayId
	 *            -provides this.company id used to identify the company in the
	 *            database.
	 * @throws RequestNotFoundException-
	 *             if company has yet to create coupons.
	 * @throws DaoCouponException
	 *             if a SQLException occurred.
	 */
	@Override
	public Set<Coupon> getCompanyCoupons(long compnayId) throws DaoCouponException {
		Set<Coupon> couponList = new HashSet<>();
		Coupon coupon;
		Connection conn = connectionPool.getConnection();

		try {
			String query = "SELECT COUPON.* FROM company_coupon INNER JOIN COUPON ON company_coupon.coupon_Id = coupon.ID AND company_coupon.company_Id = ?";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setLong(1, compnayId);
			ResultSet rs = pstmt.executeQuery();
			connectionPool.returnConnection(conn);
			while (rs.next()) {
				coupon = couponDBDAO.buildCouponFromRS(rs);
				couponList.add(coupon);
			}
		
			return couponList;
		} catch (SQLException e) {
			throw new DaoCouponException("A database access error occured: " + e.getMessage());
		} finally {
			connectionPool.returnConnection(conn);
		}
	}

	/**
	 * This method is used by the company user to receive a requested coupon
	 * from there coupon list. The found coupon is created using the
	 * "Build coupon from RS" method and returned.
	 * 
	 * @return the requested coupon
	 * @param companyId
	 *            -provides this.company id used to identify the company in the
	 *            database.
	 * @param couponId
	 *            provides the requested coupon id used to identify the coupon
	 *            in the database.
	 * @throws RequestNotFoundException
	 *             if the requested coupon id doesn't exist in the companies
	 *             coupon list.
	 * @throws DaoCouponException
	 *             if a SQLException occurred.
	 */

	public Coupon getCompanyCoupon(long companyId, long couponId) throws DaoCouponException, RequestNotFoundException {
		Connection conn = connectionPool.getConnection();
		try {
			String query = "SELECT COUPON.* FROM COMPANY_COUPON INNER JOIN COUPON ON COMPANY_COUPON.COUPON_ID = COUPON.ID AND COMPANY_COUPON.COMPANY_ID= ? AND COUPON_ID=? ";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setLong(1, companyId);
			pstmt.setLong(2, couponId);
			ResultSet rs = pstmt.executeQuery();
			connectionPool.returnConnection(conn);
			boolean foundCoupon = rs.next();
			if (foundCoupon) {
				Coupon coupon = couponDBDAO.buildCouponFromRS(rs);
				return coupon;
			} else {
				throw new RequestNotFoundException(
						"The coupon with id: " + couponId + " doesnt exist in your created coupon list");
			}
		} catch (SQLException e) {
			throw new DaoCouponException("A database access error occured:" + e.getMessage());
		} finally {
			connectionPool.returnConnection(conn);
		}
	}

	/**
	 * The boolean Login method is used by the Main Coupon System Login - it is
	 * used to check if the company exists in the company table in the database.
	 * 
	 * @return true- if the login credentials match an existing company.
	 * @param name
	 *            -provides the company name.
	 * @param password
	 *            -provides the company password.
	 * @throws DaoCouponException
	 *             if the login details are not found or if a SQLException
	 *             occurred.
	 * 
	 */
	@Override
	public Boolean login(String name, String password) throws DaoCouponException {
		boolean isLoggedInSuccessfully = false;
		String query = "SELECT * FROM company WHERE companyName = ? AND password = ?";
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
	 * this method is used to build a new company using fields that are taken
	 * from a given result set.
	 * 
	 * @param resultSet
	 *            - received from a previous SQL query
	 * @return a company matching the one received in the result set.
	 * @throws DaoCouponException
	 *             if a SQLException occurred.
	 */
	public Company buildCompanyFromRS(ResultSet resultSet) throws DaoCouponException {
		Company company = new Company();
		try {
			company.setCompanyId(resultSet.getLong(1));
			company.setCompanyName(resultSet.getString(2));
			company.setEmail(resultSet.getString(3));
			company.setPassword(resultSet.getString(4));
			return company;
		} catch (SQLException e) {
			throw new DaoCouponException("A database access error occured: " + e.getMessage());
		}
	}

	/**
	 * This method is used after a company has created a coupon. it receives The
	 * purchasing company id and the requested coupon id and adds them to the
	 * company_coupon list in the database.
	 * 
	 * @param companyId
	 *            -provides this.company id used to identify the company that is
	 *            creating the coupon.
	 * @param couponId
	 *            -provides the coupon id that the company has created.
	 * @throws DaoCouponException
	 *             if an SQLException occurred.
	 */
	public void addCreatedCoupon(long companyId, long couponId) throws DaoCouponException {
		String query = "INSERT INTO Company_coupon VALUES (?,?)";
		Connection conn = connectionPool.getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setLong(1, companyId);
			pstmt.setLong(2, couponId);
			pstmt.executeUpdate();
			connectionPool.returnConnection(conn);
		} catch (SQLException e) {
			throw new DaoCouponException("A database access error occured:" + e.getMessage());
		} finally {
			connectionPool.returnConnection(conn);
		}
	}

	/**
	 * This method is used by the Main Coupon System Login - it searches the
	 * company table for a company with the requested user and password and
	 * returns there id.
	 * 
	 * @param name
	 *            -sent at login-provides the customers name.
	 * @param password
	 *            -sent at login-provides the customers password.
	 * @return the matching company id.
	 * @throws IllegalLoginException
	 *             if the users id could not be pulled from the database.
	 * @throws DaoCouponException
	 *             if an SQLException occurred.
	 */
	public long getLoginId(String name, String password) throws DaoCouponException, IllegalLoginException {
		long companyId = 0;
		String query = "SELECT * FROM company WHERE companyName = ? AND password = ?";
		Connection conn = connectionPool.getConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, name);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				companyId = rs.getLong(1);
				return companyId;
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
	 * This method is used by the company user to receive a list of all of the
	 * created coupons of a specific Coupon Type. each corresponding coupon is
	 * then built field by field using the "Build coupon from RS method".
	 * 
	 * @param companyId
	 *            -provides this.company id used to identify the company in the
	 *            database.
	 * @param CouponType
	 *            -provides the requested coupon type to search for.
	 * @return a set of the company coupons of the requested type.
	 * @throws RequestNotFoundException
	 *             if no coupons of the requested type exist in the company
	 *             coupon list.
	 * @throws DaoCouponException
	 *             if the company id is not found in the company_coupon list or
	 *             if a SQLException occurred.
	 * 
	 * 
	 */
	public Set<Coupon> getCompanyCouponsByType(long companyId, CouponType CouponType)
			throws DaoCouponException, RequestNotFoundException {
		Set<Coupon> couponListByType = new HashSet<>();
		Connection conn = connectionPool.getConnection();
		try {
			String query = "SELECT COUPON.* FROM COMPANY_COUPON INNER JOIN COUPON ON COMPANY_COUPON.COUPON_ID = COUPON.ID AND COMPANY_COUPON.COMPANY_ID= ? AND COUPON.TYPE=? ";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setLong(1, companyId);
			pstmt.setString(2, CouponType.toString());
			ResultSet rs = pstmt.executeQuery();
			connectionPool.returnConnection(conn);
			while (rs.next()) {
				Coupon coupon = couponDBDAO.buildCouponFromRS(rs);
				couponListByType.add(coupon);
			}
			if (couponListByType.size() < 1) {
				throw new RequestNotFoundException("Empty list-No company coupons of this type were found.");
			}
			return couponListByType;
		} catch (SQLException e) {
			throw new DaoCouponException("A database access error occured:" + e.getMessage());
		} finally {
			connectionPool.returnConnection(conn);
		}
	}

	/**
	 * This method is used by the company user to receive a list of all of the
	 * coupons that have been created by them that cost less than the sent
	 * price. each corresponding coupon is then built field by field using the
	 * "Build coupon from RS method".
	 * 
	 * @param companyId
	 *            -provides this.company id used to identify the customer in the
	 *            database.
	 * @param maxPrice
	 *            -provides the requested maximum price to search by.
	 * @return a set of the companies coupons that cost less than the sent
	 *         price.
	 * @throws RequestNotFoundException
	 *             if no coupons of the requested price range exist in the
	 *             company coupon list.
	 * @throws DaoCouponException
	 *             if the company id is not found in the company_coupon list or
	 *             if a SQLException has occurred.
	 * 
	 */
	public Set<Coupon> getCompanyCouponsByPrice(long companyId, double maxPrice)
			throws DaoCouponException, RequestNotFoundException {
		Set<Coupon> couponListByPrice = new HashSet<>();
		Connection conn = connectionPool.getConnection();
		String query = "SELECT COUPON.* FROM COMPANY_COUPON INNER JOIN COUPON ON COMPANY_COUPON.COUPON_ID = COUPON.ID AND COMPANY_COUPON.COMPANY_ID= ? AND COUPON.PRICE <=? ";
		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setLong(1, companyId);
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
	 * This method is used by the company user to receive a list of all of the
	 * coupons that expire before a given date. Each corresponding coupon is
	 * then built field by field using the "Build coupon from RS method".
	 * 
	 * @param companyId
	 *            -provides this.company id used to identify the customer in the
	 *            database.
	 * @param endDate
	 *            -provides the requested date to search by.
	 * @return a set of the companies coupons that expire before the given date.
	 * @throws RequestNotFoundException
	 *             if no coupons of the requested date range exist in the
	 *             company coupon list.
	 * @throws DaoCouponException
	 *             if the company id is not found in the company_coupon list or
	 *             if a SQLException has occurred.
	 * 
	 */
	public Set<Coupon> getCouponsByDate(long companyId, long endDate)
			throws DaoCouponException, RequestNotFoundException {
		Set<Coupon> couponListByDate = new HashSet<>();
		Connection conn = connectionPool.getConnection();
		String query = "SELECT COUPON.* FROM COMPANY_COUPON INNER JOIN COUPON ON COMPANY_COUPON.COUPON_ID = COUPON.ID AND COMPANY_COUPON.COMPANY_ID= ? AND COUPON.end_date <=? ";
		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setLong(1, companyId);
			pstmt.setDate(2, new java.sql.Date(endDate));
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Coupon coupon = couponDBDAO.buildCouponFromRS(rs);
				couponListByDate.add(coupon);
			}
			if (couponListByDate.size() < 1) {
				throw new RequestNotFoundException("Empty coupon list- no coupons expiring before:" + endDate);
			}
			return couponListByDate;
		} catch (SQLException e) {
			throw new DaoCouponException("A database access error occured:" + e.getMessage());
		} finally {
			connectionPool.returnConnection(conn);
		}
	}
	
	
	public void uploadCouponPicsToServer(String couponName,String picName) throws DaoCouponException{
		String query = "INSERT INTO coupon_Pics VALUES (?,?)";
		Connection conn = connectionPool.getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, couponName);
			pstmt.setString(2,picName);
			pstmt.executeUpdate();
			connectionPool.returnConnection(conn);
		} catch (SQLException e) {
			throw new DaoCouponException("A database access error occured: " + e.getMessage());
	
		}
}
	
	public Set<Coupon> getCouponPicList() throws DaoCouponException, RequestNotFoundException{
		Set<Coupon>coupPicList = new HashSet<>();
		String query = "SELECT * FROM coupon_Pics";
		Connection conn = connectionPool.getConnection();
		try {
			java.sql.Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			connectionPool.returnConnection(conn);
			while (rs.next()) {
				Coupon coupon = new Coupon();
				coupon.setCouponName(rs.getString(1));
				coupon.setImage(rs.getString(2));
				coupPicList.add(coupon);
			}
			if (coupPicList.size() < 1) {
				throw new RequestNotFoundException("Empty list-No couponPics found.");
			}
			return coupPicList;
		} catch (SQLException e) {
			throw new DaoCouponException("A database access error occured: " + e.getMessage());
		} finally {
			connectionPool.returnConnection(conn);
		}
		}
	
	
	public String getCouponPicName(String couponName) throws DaoCouponException, RequestNotFoundException {
		Connection conn = connectionPool.getConnection();
		try {
			String query = "SELECT * FROM coupon_Pics WHERE coupon_name =?";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, couponName);
			ResultSet rs = pstmt.executeQuery();
			connectionPool.returnConnection(conn);
			boolean foundCoupon = rs.next();
			if (foundCoupon) {
				Coupon coupon = new Coupon();
				coupon.setCouponName(rs.getString(1));
				coupon.setImage(rs.getString(2));
				return coupon.getImage();
			} else {
				throw new RequestNotFoundException(
						"The coupon with name: " + couponName + " doesnt exist in your  coupon list");
			}
		} catch (SQLException e) {
			throw new DaoCouponException("A database access error occured:" + e.getMessage());
		} finally {
			connectionPool.returnConnection(conn);
		}
	}
}