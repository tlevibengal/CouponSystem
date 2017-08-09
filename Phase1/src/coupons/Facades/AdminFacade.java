package coupons.Facades;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import coupons.Beans.Company;
import coupons.Beans.Coupon;
import coupons.Beans.Customer;
import coupons.DAO.DBDAO.CompanyDBDAO;
import coupons.DAO.DBDAO.CouponDBDAO;
import coupons.DAO.DBDAO.CustomerDBDAO;
import coupons.Exceptions.CouponFacadeException;
import coupons.Exceptions.DaoCouponException;
import coupons.Exceptions.DuplicateEntryException;
import coupons.Exceptions.IllegalInputException;
import coupons.Exceptions.RequestNotFoundException;

public class AdminFacade implements ClientCouponFacade {

	CouponDBDAO couponDBDAO;
	CompanyDBDAO companyDBDAO;
	CustomerDBDAO customerDBDAO;

	/**
	 * This Constructor is initiated during user login. It has a fixed user and
	 * password. user: "admin", password = "1234" it also initiates the three
	 * DAO's.
	 */
	public AdminFacade() {
		customerDBDAO = new CustomerDBDAO();
		companyDBDAO = new CompanyDBDAO();
		couponDBDAO = new CouponDBDAO();
	}

	/**
	 * Creates a company. first the method makes sure that the given parameters-
	 * name and email are valid. if they are it calls the Create company method
	 * in the Company DBDAO class.
	 * 
	 * @param company
	 *            - defines the company fields given by the user.
	 * @throws CouponFacadeException
	 *             if the user has chosen an illegal name or email ,if the same
	 *             company name already exists in the database or if a database
	 *             error occurred.
	 * 
	 */
	public void createCompany(Company company) throws CouponFacadeException {
		try {
				companyDBDAO.createCompany(company);
					} catch (DuplicateEntryException e) {
			throw new CouponFacadeException(
					"The company " + company.getCompanyName() + " already exists and cant be added");
		} catch (DaoCouponException e) {
			throw new CouponFacadeException("Failed to create company " + company.getCompanyName());

		}

	}

	/**
	 * This method Deletes a company from the database.
	 * 
	 * @param companyId
	 *            -Given by the user. used to identify the company to be
	 *            deleted.
	 * @throws CouponFacadeException
	 *             if the company Id was not found or if a database error
	 *             occurred.
	 * 
	 */

	public void deleteCompany(long companyId) throws CouponFacadeException {
		Set<Coupon> companyCoupons = new HashSet<>();
		try {
			companyDBDAO.deleteCompany(companyId);
		
			companyCoupons = companyDBDAO.getCompanyCoupons(companyId);
			if (companyCoupons.size() != 0) {
				for (Coupon coupon : companyCoupons) {
					couponDBDAO.deleteCouponFromAllLists(coupon.getCouponId());
		} 
			}
		}catch (RequestNotFoundException e) {
			throw new CouponFacadeException(
					"Failed to delete. Company id " + companyId + " was not found in the system");

		} catch (DaoCouponException e) {
			throw new CouponFacadeException("Failed to delete company wih id: " + companyId);
		}

	}

	/**
	 * updates a company. the method calls the update company method in the
	 * Company DBDAO class.
	 * 
	 * @param company
	 *            - Given by the user. used to identify the company and set the
	 *            fields that need to be updated.
	 * 
	 * @throws CouponFacadeException-
	 *             if the user has chosen an illegal end date ( expired), if the
	 *             company already exists in the database or if a database error
	 *             occurred.
	 */
	public void updateCompany(Company company) throws CouponFacadeException {
		try {
						companyDBDAO.updateCompany(company);
			} catch (RequestNotFoundException e) {
			throw new CouponFacadeException(e.getMessage());
		} catch (DaoCouponException e) {
			throw new CouponFacadeException("Failed to update company with id " + company.getCompanyId());
		}
	}

	/**
	 * retrieves a requested company. if the retrieved company is not null., i.e
	 * exists the given company is returned.
	 * 
	 * @param companyId
	 *            -Given by the user. used to identify the company to be
	 *            retrieved.
	 * 
	 * @return a Company built by the result set of the get company request.
	 * 
	 * @throws CouponFacadeException
	 *             if the given company Id was not found or if a database error
	 *             occurred.
	 */
	public Company getCompany(long companyId) throws CouponFacadeException {
		Company company = new Company();
		try {
			company = companyDBDAO.getCompany(companyId);
			return company;
		} catch (RequestNotFoundException e) {
			throw new CouponFacadeException("Company with id " + companyId + " was not found in the system");

		} catch (DaoCouponException e) {
			throw new CouponFacadeException("Failed to find the company with id " + company.getCompanyId());
		}

	}

	/**
	 * retrieves a Set of all existing companies.
	 * 
	 * @return a Set of Companies that exist in the system.
	 * @throws CouponFacadeException
	 *             if the no companies exist in the company list or if a
	 *             database error occurred.
	 */
	public Set<Company> getAllCompanies() throws CouponFacadeException {
		try {
			Set<Company> companyList = companyDBDAO.getAllCompanies();
			return companyList;
		} catch (RequestNotFoundException e) {
			throw new CouponFacadeException("No companies were found in the system");
		} catch (DaoCouponException e) {
			throw new CouponFacadeException("Failed to get company list");
		}

	}

	/**
	 * Creates a customer. the method calls the Create customer method in the
	 * customer DBDAO class.
	 * 
	 * @param customer
	 *            - defines the customer fields given by the user.
	 * @throws CouponFacadeException
	 *             if the user has chosen an illegal name ,if the same customer
	 *             name already exists in the database or if a database error
	 *             occurred.
	 * 
	 */
	public void createCustomer(Customer customer) throws CouponFacadeException {
	try{
				customerDBDAO.createCustomer(customer);
		} catch (DuplicateEntryException e) {
			throw new CouponFacadeException(
					"The company " + customer.getCustomerName() + " already exists and cant be added");
		} catch (DaoCouponException e) {
			throw new CouponFacadeException("Failed to create a new customer");

		}

	}

	/**
	 * This method Deletes a customer from the database.
	 * 
	 * @param customerid
	 *            -Given by the user. used to identify the customer to be
	 *            deleted.
	 * @throws CouponFacadeException
	 *             if the given customer Id was not found or if a database error
	 *             occurred.
	 * 
	 */
	public void deleteCustomer(long customerid) throws CouponFacadeException {
		try {
			customerDBDAO.deleteCustomer(customerid);
		} catch (RequestNotFoundException e) {
			throw new CouponFacadeException(
					"Failed to delete. customer id " + customerid + " was not found in the system");

		} catch (DaoCouponException e) {
			throw new CouponFacadeException("Failed to delete company with id " + customerid);
		}

	}

	/**
	 * updates a customer. the method calls the update customer method in the
	 * customer DBDAO class.
	 * 
	 * @param customer
	 *            - Given by the user. used to identify the customer and set the
	 *            fields that need to be updated.
	 * @throws CouponFacadeException
	 *             if the given customer Id was not found or if a database error
	 *             occurred.
	 */
	public void updateCustomer(Customer customer) throws CouponFacadeException {
		try {
			customerDBDAO.updateCustomer(customer);
		} catch (RequestNotFoundException e) {
			throw new CouponFacadeException(e.getMessage());
		} catch (DaoCouponException e) {
			throw new CouponFacadeException("Failed to update customer with id" + customer.getCustomerId());
		}
	}

	/**
	 * retrieves a requested customer. if the retrieved customer is not null,
	 * i.e exists the given customer is returned.
	 * 
	 * @param customerId
	 *            -Given by the user. used to identify the customer to be
	 *            retrieved.
	 * 
	 * @throws CouponFacadeException
	 *             if the given customer Id was not found or if a database error
	 *             occurred.
	 */
	public Customer getCustomer(long customerId) throws CouponFacadeException {
		Customer customer = new Customer();
		try {
			customer = customerDBDAO.getCustomer(customerId);
			return customer;

		} catch (RequestNotFoundException e) {
			throw new CouponFacadeException("Customer with id " + customerId + " was not found in the system");

		} catch (DaoCouponException e) {
			throw new CouponFacadeException("Failed to find a customer with id " + customerId);
		}

	}

	/**
	 * retrieves a Set of all existing Customers.
	 * 
	 * @return a list of all existing customers.
	 * @throws CouponFacadeException
	 *             if the customer list is empty or if a database error
	 *             occurred.
	 */
	public Set<Customer> getAllCustomers() throws CouponFacadeException {
		try {
			return customerDBDAO.getAllCustomers();
		} catch (RequestNotFoundException e) {
			throw new CouponFacadeException("No customers were found in the system");
		} catch (DaoCouponException e) {
			throw new CouponFacadeException("Failed to retreive customer list");
		}

	}

	/**
	 * Used by the administrator user. to receive a list of all of the coupon
	 * that were deleted by the Daily task thread.
	 * 
	 * @return a Set of expired and deleted coupons.
	 * @throws RequestNotFoundException-
	 *             if the deleted coupon list is empty.
	 * @throws CouponFacadeException
	 *             if a database access error occurred.
	 */
	public Set<Coupon> getTaskDeletedCoupons() throws CouponFacadeException, RequestNotFoundException {
		Set<Coupon> taskDeletedCoupons = new HashSet<>();
		try {
			taskDeletedCoupons = couponDBDAO.getTaskDeletedCoupons();
			if (taskDeletedCoupons.size() == 0) {
				throw new RequestNotFoundException("There are no deleted coupons in the coupon list");
			}
			return taskDeletedCoupons;
		} catch (DaoCouponException e) {
			throw new CouponFacadeException("Failed to retrieve deleted coupons list", e);
		}
	}

	
	public Set<Coupon> getAllExistingCoupons() throws CouponFacadeException {
		Set<Coupon> couponList = new HashSet<>();
					couponList = couponDBDAO.getAllCoupons();
				return couponList;
}

}