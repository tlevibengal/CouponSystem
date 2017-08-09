package coupons.DAOinterfaces;

import java.util.Set;

import coupons.Beans.Company;
import coupons.Beans.Coupon;
import coupons.Beans.CouponType;
import coupons.Exceptions.DaoCouponException;
import coupons.Exceptions.DuplicateEntryException;
import coupons.Exceptions.IllegalInputException;
import coupons.Exceptions.RequestNotFoundException;

public interface CompanyDAO {

	public void createCompany(Company company)
			throws DaoCouponException, IllegalInputException, DuplicateEntryException;

	public void deleteCompany(long companyId) throws DaoCouponException, RequestNotFoundException;

	public void updateCompany(Company company) throws DaoCouponException, RequestNotFoundException;

	public Company getCompany(long companyId) throws DaoCouponException, RequestNotFoundException;

	public Set<Company> getAllCompanies() throws DaoCouponException, RequestNotFoundException;

	public Set<Coupon> getCompanyCoupons(long companyId) throws DaoCouponException, RequestNotFoundException;

	public Boolean login(String name, String password) throws DaoCouponException;

	public long getNewCompanyId() throws DaoCouponException;

	public void addCreatedCoupon(long compId, long couponId) throws DaoCouponException;

	public Set<Coupon> getCompanyCouponsByType(long companyId, CouponType CouponType)
			throws DaoCouponException, RequestNotFoundException;


}
