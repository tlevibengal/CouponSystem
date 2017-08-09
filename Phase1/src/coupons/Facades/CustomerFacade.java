package coupons.Facades;

import java.util.HashSet;
import java.util.Set;

import coupons.Beans.Coupon;
import coupons.Beans.CouponType;
import coupons.DAO.DBDAO.CouponDBDAO;
import coupons.DAO.DBDAO.CustomerDBDAO;
import coupons.Exceptions.CouponFacadeException;
import coupons.Exceptions.DaoCouponException;
import coupons.Exceptions.DuplicateEntryException;
import coupons.Exceptions.IllegalCouponDetailsException;
import coupons.Exceptions.RequestNotFoundException;

public class CustomerFacade implements ClientCouponFacade {

	CouponDBDAO couponDBDAO;
	CustomerDBDAO customerDBDAO;
	private long customerId;

	/**
	 * Constructed during user login. the new CustomerFacade receives a
	 * customerId from the user, and initiates the Coupon DBDAO and the
	 * customerDBDAO.
	 * 
	 */
	public CustomerFacade(long customerId) {
		super();
		this.customerId = customerId;
		customerDBDAO = new CustomerDBDAO();
		couponDBDAO = new CouponDBDAO();
	}

	/**
	 * Purchases a coupon. the method calls the getCoupon method in the coupon
	 * DBDAO class. The received coupon is then checked to make sure that it is
	 * in stock and that it hasn't expired, if all conditions are met the coupon
	 * amount is updated and it is added to the customer_coupon list.
	 * 
	 * @param couponId
	 *            - Given by the user. used to retrieve the coupon id needed in
	 *            the DBDAO method.
	 * @throws CouponFacadeException
	 *             - if the coupon has already been purchased by the customer in
	 *             the past. or if the coupon has expired or if it is out of
	 *             stock. or if the coupon id can't be found in the coupon list.
	 * @throws IllegalCouponDetailsException
	 *             if the requested coupon is out of stock or expired
	 */
	public void purchaseCoupon(long couponId) throws CouponFacadeException, IllegalCouponDetailsException {
		long Today = System.currentTimeMillis();
		try {
			Coupon requestedCoupon = couponDBDAO.getCoupon(couponId);
			boolean isCouponInStock = requestedCoupon.getAmount() > 0;
			boolean isCouponEndDateValid = requestedCoupon.getEndDate().getTime() > Today;

			if (isCouponInStock && isCouponEndDateValid) {
				couponDBDAO.updateCouponAmountAfterPurchise(requestedCoupon);
				customerDBDAO.addPurchesedCoupon(customerId, couponId);
			} else if (!isCouponInStock) {
				throw new IllegalCouponDetailsException(
"The requested coupon " + requestedCoupon.getCouponName()
						+ " is out of stock and cant be purchesed");
			} else if (!isCouponEndDateValid) {
				throw new IllegalCouponDetailsException(
						"The coupons end date" + requestedCoupon.getEndDate() + " ) has expired and cant be purchesed");
			}
		} catch (DuplicateEntryException e) {
			throw new CouponFacadeException(
					"A coupon with id: " + couponId + " has already been purchesed by you and cant be purchesed again");

		} catch (RequestNotFoundException e) {

			throw new CouponFacadeException("A Coupon with id " + couponId + " was not found in the system");

		} catch (DaoCouponException e) {

			throw new CouponFacadeException("Failed to purchese requested coupon ");

		}

	}

	/**
	 * Retrieves a list of all of the coupons purchased by the requesting
	 * customer the method calls the getCustomerCoupons in the customer DBDAO
	 * class.
	 * 
	 * @param customerId
	 *            - given to the constructor on login, its used to identify the
	 *            customer calling the method.
	 * @return a Set of Coupon purchased by the customer.
	 * 
	 * @throws CouponFacadeException
	 *             if the coupon has expired, out of stock ,does not exist or if
	 *             a database error occurred.
	 */
	public Set<Coupon> getAllPurchasedCoupons() throws CouponFacadeException {
		Set<Coupon> couponList = new HashSet<>();
		try {
			couponList = customerDBDAO.getCustomerCoupons(customerId);
			return couponList;
		} catch (RequestNotFoundException e) {
			throw new CouponFacadeException("No coupons were found in your coupon list.");
		} catch (DaoCouponException e) {
			throw new CouponFacadeException("Failed to retreive customer coupon list");
		}

	}

	/**
	 * Retrieves a list of all of the coupons purchased by the requesting
	 * customer that match a specific type. the method calls the
	 * getCustomerCouponsByType in the customer DBDAO class.
	 * 
	 * @param CouponType
	 *            -this is given by the user and is used as a search
	 *            condition/criteria
	 * @param customerId
	 *            - given to the constructor on login, its used to identify the
	 *            customer calling the method.
	 * @return a Set of Coupon purchased by the customer.
	 * 
	 * @throws CouponFacadeException-
	 *             if the coupon id can't be found in the coupon list or if a
	 *             database error has occurred
	 * 
	 */
	public Set<Coupon> getAllPurchasedCouponsByType(CouponType CouponType) throws CouponFacadeException {
		Set<Coupon> couponListByType = new HashSet<>();
		try {

			couponListByType = customerDBDAO.getCustomerCouponsByType(customerId, CouponType);
			return couponListByType;
		} catch (RequestNotFoundException e) {
			throw new CouponFacadeException("You have no Coupons of the type " + CouponType);
		} catch (DaoCouponException e) {
			throw new CouponFacadeException("Failed to retreive coupon list by type " + CouponType);
		}

		}

	/**
	 * Retrieves a list of all of the coupons purchesed by the requesting
	 * customer that cost less than the sent price. the method calls the
	 * getAllPurchasedCouponsByPrice in the customer DBDAO class.
	 * 
	 * @param maxPrice
	 *            - the maximum price search criteria.
	 * @param customerId
	 *            - given to the constructor on login, its used to identify the
	 *            customer calling the method.
	 * @return a Set of Coupon purchased by the customer.
	 * 
	 * @throws CouponFacadeException-
	 *             if the coupon id can't be found in the coupon list or if a
	 *             database error has occurred.
	 */
	public Set<Coupon> getAllPurchasedCouponsByPrice(double maxPrice) throws CouponFacadeException {
		Set<Coupon> couponListByPrice = new HashSet<>();
		try {
			couponListByPrice = customerDBDAO.getCustomerCouponsByPrice(customerId, maxPrice);
			return couponListByPrice;
		} catch (RequestNotFoundException e) {
			throw new CouponFacadeException("You have no Coupons with a price less than:" + maxPrice);

		} catch (DaoCouponException e) {
			throw new CouponFacadeException("Failed to retreive coupon list that cost less than " + maxPrice);
		}

		}

	
	
	public Set<Coupon> getAllExistingCoupons(){
		Set<Coupon> allCoupons = new HashSet<>();
		allCoupons = couponDBDAO.getAllCoupons();
		return allCoupons;
	}
}
