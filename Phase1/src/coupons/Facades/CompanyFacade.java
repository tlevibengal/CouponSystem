package coupons.Facades;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import coupons.Beans.Coupon;
import coupons.Beans.CouponType;
import coupons.DAO.DBDAO.CompanyDBDAO;
import coupons.DAO.DBDAO.CouponDBDAO;
import coupons.Exceptions.CouponFacadeException;
import coupons.Exceptions.DaoCouponException;
import coupons.Exceptions.DuplicateEntryException;
import coupons.Exceptions.IllegalCouponDetailsException;
import coupons.Exceptions.IllegalInputException;
import coupons.Exceptions.RequestNotFoundException;

public class CompanyFacade implements ClientCouponFacade {

	private CouponDBDAO couponDBDAO;
	private CompanyDBDAO companyDBDAO;
	private long companyId;

	/**
	 * Constructed during user login. the new CompanyFacade receives a company
	 * id from the user. it also initiates the CouponDBDAO and the companyDBDAO.
	 * 
	 * @param compId-
	 *            this is sent to the constructor during the user login. this id
	 *            will identify the company that is calling the method. and in
	 *            some cases will be used as an argument.
	 */
	public CompanyFacade(long compId) {
		super();
		this.companyId = compId;
		companyDBDAO = new CompanyDBDAO();
		couponDBDAO = new CouponDBDAO();
	}

	/**
	 * Creates a coupon. the method calls the Create coupon method in the
	 * Company DBDAO class. after the coupon is created it is added to the
	 * company_coupon list with the addCreatedCoupon method.
	 * 
	 * @param coupon
	 *            -this coupon is given by the user and sent to the
	 *            corresponding DBDAO method.
	 * @param companyId
	 *            -provides this.company id used to identify the company that is
	 *            creating the coupon.
	 * @throws CouponFacadeException
	 *             if the user has chosen an illegal name or end date ,if the
	 *             same coupon name already exists in the database or if a
	 *             database error occurred.
	 * 
	 */
	public void createCoupon(Coupon coupon) throws CouponFacadeException {
		try {
				try {
					couponDBDAO.createCoupon(coupon);
					companyDBDAO.addCreatedCoupon(companyId, coupon.getCouponId());
				} catch (DuplicateEntryException e) {
					throw new CouponFacadeException("The coupon " + coupon.getCouponName() + " already exists and cant be added");
				}
				
		} catch (DaoCouponException e) {
			throw new CouponFacadeException("Failed to create new coupon " + coupon.getCouponName());

		}
	}

	//

	/**
	 * Deletes a coupon. the method calls the Delete coupon method in the
	 * customer DBDAO class. after the coupon has been deleted from the coupon
	 * list and from the company list a check is made to see if it has been
	 * Purchased in the past- if it has it is deleted from the customer_coupon
	 * list also.
	 * 
	 * @param couponId
	 *            -Given by the user. used to identify the coupon to be deleted.
	 * @throws CouponFacadeException
	 *             -when no coupon with the given id is found in the DB or if a
	 *             database error occurred.
	 */
	public void deleteCoupon(long couponId) throws CouponFacadeException {
		try {
			Coupon couponToDelete = companyDBDAO.getCompanyCoupon(companyId, couponId);
			couponDBDAO.deleteCouponFromAllLists(couponToDelete.getCouponId());
		} catch (RequestNotFoundException e) {
			throw new CouponFacadeException(
					"Failed to delete. coupon id " + couponId + " was not found your coupon list");
		} catch (DaoCouponException e) {
			throw new CouponFacadeException("Failed to delete coupon with id " + couponId);
		}
	}

	/**
	 * Used by the company user to update a coupon.
	 * 
	 * @param coupon
	 *            -given by the user. used to set the fields to be updated (end
	 *            date and price).
	 * @throws CouponFacadeException
	 *             when the end date is illegal, if no coupon with the given id
	 *             is found in the DB or if a database error occurred.
	 * 
	 */
	public void updateCoupon(Coupon coupon) throws CouponFacadeException {
		boolean validEndDate;
		try {
			// validEndDate = isEndDateValid(coupon.getEndDate().getTime());
			// if (validEndDate) {
				couponDBDAO.updateCoupon(coupon);
			// }
			// } catch (IllegalCouponDetailsException e) {
			// throw new CouponFacadeException(e.getMessage());
		} catch (RequestNotFoundException e) {
			throw new CouponFacadeException(
					"The requested coupon id " + coupon.getCouponId() + " was not found in your coupon list.");
		} catch (DaoCouponException e) {
			throw new CouponFacadeException(
					"Failed to update coupon end date & price " + coupon.getEndDate() + "," + coupon.getPrice());
		}
	}
	
	public void updateCouponImage(Coupon coupon) throws CouponFacadeException {
		try {
				couponDBDAO.updateCouponImage(coupon.getCouponName(), coupon.getImage());
		} catch (RequestNotFoundException e) {
			throw new CouponFacadeException(
					"The requested coupon name " + coupon.getCouponName() + " was not found in your coupon list.");
		} catch (DaoCouponException e) {
			throw new CouponFacadeException("A dataBase system error occurred");
		}
	}


	/**
	 * Retrieves a specific coupon created by the company. the method calls the
	 * get coupon method in the coupon DBDAO class.
	 * 
	 * @param couponId
	 *            -provides the coupon id used to identify the requested coupon
	 *            in the database.
	 * @return the Coupon that was requested.
	 * @throws CouponFacadeException
	 *             -when no coupon with the given id is found in the DB or if a
	 *             database error occurred.
	 */
	public Coupon getCoupon(long couponId) throws CouponFacadeException {
		try {
			Coupon requestedCoupon = companyDBDAO.getCompanyCoupon(companyId, couponId);
			return requestedCoupon;
		} catch (RequestNotFoundException e) {
			throw new CouponFacadeException(
					"The requested coupon id " + couponId + " was not found in your coupon list.");
		} catch (DaoCouponException e) {
			throw new CouponFacadeException("Failed to retreive requested coupon");

		}
	}
	
	
	 
	public Coupon getCouponByName(String couponName) throws CouponFacadeException {
		try {
			Coupon requestedCoupon = couponDBDAO.getCouponName(couponName);
			return requestedCoupon;
		} catch (RequestNotFoundException e) {
			throw new CouponFacadeException(
					"The requested coupon name " + couponName + " was not found in your coupon list.");
		} catch (DaoCouponException e) {
			throw new CouponFacadeException("Failed to retreive requested coupon");

		}
	}

	/**
	 * Retrieves a list of all of the coupons created by the requesting company
	 * the method calls the get getCompanyCoupons in the company DBDAO class.
	 * 
	 * @param companyId
	 *            - given to the constructor on login, its used to identify the
	 *            company calling the method.
	 *
	 * @return a set of Coupons created by this company.
	 * 
	 * @throws CouponFacadeException
	 *             if the company has no coupons or if a database error
	 *             occurred.
	 */
	public Set<Coupon> getAllCoupons() throws CouponFacadeException {
		Set<Coupon> couponList = new HashSet<>();
		try {
			couponList = companyDBDAO.getCompanyCoupons(companyId);
			return couponList;
		} catch (DaoCouponException e) {
			throw new CouponFacadeException("Failed to retreive company coupon list ");
		}

	}
	
	public Set<Coupon> getcouponPicList() throws CouponFacadeException {
		Set<Coupon> couponPicList = new HashSet<>();
		try {
			try {
				couponPicList = companyDBDAO.getCouponPicList();
				
			} catch (RequestNotFoundException e) {
				throw new CouponFacadeException("empty couponPic list ");
			}
			
			return couponPicList;
		} catch (DaoCouponException e) {
			throw new CouponFacadeException("Failed to retreive company coupon list ");
		}

	}


	/**
	 * Retrieves a list of all of the coupons created by the requesting company
	 * that match a specific coupon type. the method calls the get
	 * getCompanyCouponsByType in the company DBDAO class.
	 * 
	 * @param companyId
	 *            - given to the constructor on login, its used to identify the
	 *            company calling the method.
	 * @param CouponType
	 *            according to this type the list will be checked.
	 *
	 * @return a set of Coupons created by this company that match the requested
	 *         type.
	 * 
	 * @throws CouponFacadeException
	 *             if the companyId was not found , if no coupons of the
	 *             requested type exist or if a database error occurred. .
	 */
	public Set<Coupon> getCouponsByType(CouponType CouponType) throws CouponFacadeException {
		Set<Coupon> couponListByType = new HashSet<>();
		try {
			couponListByType = companyDBDAO.getCompanyCouponsByType(companyId, CouponType);
			return couponListByType;
		} catch (RequestNotFoundException e) {
			throw new CouponFacadeException("No coupons of this type were found in your coupon list.");
		} catch (DaoCouponException e) {
			throw new CouponFacadeException("Failed to retreive company coupon list by type " + CouponType);
		}

	}

	/**
	 * Retrieves a list of all of the coupons created by the requesting company
	 * that cost less than the sent price. the method calls the
	 * getAllPurchasedCouponsByPrice in the company DBDAO class.
	 * 
	 * @param maxPrice
	 *            - the maximum price search criteria.
	 * @param companyId
	 *            - given to the constructor on login, its used to identify the
	 *            company calling the method.
	 * @return a Set of Coupon created by the company of the requested price.
	 * 
	 * @throws CouponFacadeException-
	 *             if the coupon id can't be found, if the coupon list is empty
	 *             or if a database error has occurred.
	 */
	public Set<Coupon> getAllCouponsByPrice(double maxPrice) throws CouponFacadeException {
		Set<Coupon> couponListByPrice = new HashSet<>();
		try {
			couponListByPrice = companyDBDAO.getCompanyCouponsByPrice(companyId, maxPrice);
			return couponListByPrice;
		} catch (RequestNotFoundException e) {
			throw new CouponFacadeException("You have no Coupons with a price less than:" + maxPrice);

		} catch (DaoCouponException e) {
			throw new CouponFacadeException("Failed to retreive coupon list that cost less than " + maxPrice);
		}

	}

	

	/**
	 * Retrieves a list of all of the coupons created by the requesting company
	 * that match a specific coupon type. the method calls the get
	 * getCompanyCouponsByType in the company DBDAO class.
	 * 
	 * @param companyId
	 *            - given to the constructor on login, its used to identify the
	 *            company calling the method.
	 * @param endDate
	 *            according to this endDate the list will be checked.
	 * @return a set of Coupons created by this company that expire before the
	 *         requested endDate.
	 * 
	 * @throws CouponFacadeException
	 *             if the companyId was not found , if no coupons of the
	 *             requested expiration Date exist or if a database error
	 *             occurred.
	 */
	public Set<Coupon> getCouponsByDate(Date endDate) throws CouponFacadeException {
		Set<Coupon> couponListByEndDate = new HashSet<>();
		try {
				couponListByEndDate = companyDBDAO.getCouponsByDate(companyId, endDate.getTime());
		} catch (RequestNotFoundException e) {
			throw new CouponFacadeException("You have no Coupons expiring before:" + endDate);
		} catch (DaoCouponException e) {
			throw new CouponFacadeException("Failed to retreive coupon list expiring before: " + endDate);
		}
		return couponListByEndDate;

	}

	/**
	 * This method changes the given string date to SQL Date format.
	 * 
	 * @param dateString
	 *            - the end date given by the user
	 * @return a formated Date.
	 * @throws IllegalInputException
	 *             - if the date doesn't match the legal pattern (dd/mm/yyyy).
	 */
	public Date convertStringToDate(String dateString) throws IllegalInputException {
		java.sql.Date sqlDateFormat = new java.sql.Date(0);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date stringToJavaDate = null;
		try {
			stringToJavaDate = sdf.parse(dateString);
			sqlDateFormat = new java.sql.Date(stringToJavaDate.getTime());
		} catch (ParseException e) {
			throw new IllegalInputException("Invalid date format, date must be dd/mm/yyyy");
		}
		return sqlDateFormat;

	}
	
	public void updatePics(String couponName,String picName) throws CouponFacadeException{
		try {
			companyDBDAO.uploadCouponPicsToServer(couponName, picName);
		} catch (DaoCouponException e) {
			e.printStackTrace();
			//throw new CouponFacadeException("Failed to retreive coupon Pic list ");

		}
	}
	
	public String getACouponPic(String couponName) throws CouponFacadeException{
		String gottonCouponPicName;
		try {
			gottonCouponPicName = companyDBDAO.getCouponPicName(couponName);
		} catch (DaoCouponException | RequestNotFoundException e) {
			throw new CouponFacadeException("Failed to retreive coupon Pic list ");
		}
		return gottonCouponPicName;
	}
	
}
