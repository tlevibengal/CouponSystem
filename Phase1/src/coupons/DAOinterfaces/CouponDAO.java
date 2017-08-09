package coupons.DAOinterfaces;

import java.util.Set;

import coupons.Beans.Coupon;
import coupons.Beans.CouponType;
import coupons.Exceptions.DaoCouponException;
import coupons.Exceptions.DuplicateEntryException;
import coupons.Exceptions.IllegalInputException;
import coupons.Exceptions.RequestNotFoundException;

public interface CouponDAO {

	public void createCoupon(Coupon coupon) throws IllegalInputException, DaoCouponException, DuplicateEntryException;

	public void deleteCouponFromAllLists(long couponId) throws DaoCouponException, RequestNotFoundException;

	public void updateCoupon(Coupon coupon) throws DaoCouponException, RequestNotFoundException;

	public Coupon getCoupon(long couponId) throws DaoCouponException, RequestNotFoundException;

	public Set<Coupon> getAllCoupons();

	public Set<Coupon> getCouponsByType(CouponType couponType) throws DaoCouponException, RequestNotFoundException;

	public void updateCouponAmountAfterPurchise(Coupon coupon) throws DaoCouponException, RequestNotFoundException;

	Coupon getCouponName(String couponName) throws RequestNotFoundException, DaoCouponException;

	void updateCouponImage(String couponName, String couponImageName)
			throws DaoCouponException, RequestNotFoundException;


}
