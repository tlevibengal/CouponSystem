package coupons.DAOinterfaces;

import java.util.Set;

import coupons.Beans.Coupon;
import coupons.Beans.CouponType;
import coupons.Beans.Customer;
import coupons.Exceptions.DaoCouponException;
import coupons.Exceptions.DuplicateEntryException;
import coupons.Exceptions.IllegalInputException;
import coupons.Exceptions.RequestNotFoundException;

public interface CustomerDAO {

	public void createCustomer(Customer customer)
			throws IllegalInputException, DaoCouponException, DuplicateEntryException;

	public void deleteCustomer(long customerId) throws DaoCouponException, RequestNotFoundException;

	public void updateCustomer(Customer customer) throws DaoCouponException, RequestNotFoundException;

	public Customer getCustomer(long customerId) throws DaoCouponException, RequestNotFoundException;

	public Set<Customer> getAllCustomers() throws DaoCouponException, RequestNotFoundException;

	public void addPurchesedCoupon(long customerId, long couponId)
			throws DaoCouponException, RequestNotFoundException, DuplicateEntryException;

	public Set<Coupon> getCustomerCoupons(long customerId) throws DaoCouponException, RequestNotFoundException;

	public Boolean login(String customerName, String password) throws DaoCouponException;

	public Set<Coupon> getCustomerCouponsByType(long customerId, CouponType CouponType)
			throws DaoCouponException, RequestNotFoundException;

	public Set<Coupon> getCustomerCouponsByPrice(long customerId, double maxPrice)
			throws DaoCouponException, RequestNotFoundException;


}
