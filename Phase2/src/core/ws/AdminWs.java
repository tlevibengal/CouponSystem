package core.ws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import core.ws.wrapper.CompanyWrapper;
import core.ws.wrapper.CustomerWrapper;
import coupons.Beans.Company;
import coupons.Beans.Customer;
import coupons.Exceptions.CouponFacadeException;
import coupons.Exceptions.CouponsSysException;
import coupons.Facades.AdminFacade;

@Path("adminWs")
public class AdminWs {

	@Context
	private HttpServletRequest request;
	@Context
	private HttpServletResponse response;

	private AdminFacade facade;
	private String userName;

	@PostConstruct
	public void setFacade() {
		facade = ((AdminFacade) request.getSession().getAttribute("facade"));
		userName = (String) request.getSession().getAttribute("name");

	}

	@GET
	@Path("getSessionUser")
	@Produces(MediaType.APPLICATION_JSON)
	public void getSessionUser() {
		userName = (String) request.getSession().getAttribute("name");
		if(userName.equals(null)){
			throw new WebApplicationException(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
		}
		response.setHeader("user", userName);
	}

	@GET
	@Path("quit")
	@Produces(MediaType.APPLICATION_JSON)
	public void logOut() {
		request.getSession().invalidate();
		response.setHeader("quit", "quit");
	


	}

	@GET
	@Path("getAllCompanies")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<Company> getAllCompanies() {
		Set<Company> companyList = new HashSet<>();
		try {
			companyList = facade.getAllCompanies();
		} catch (CouponFacadeException e) {
			throw new WebApplicationException(HttpServletResponse.SC_NOT_FOUND);
		}
		return companyList;
	}

	@GET
	@Path("getAllCustomers")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<Customer> getAllCustomers() {
		Set<Customer> customerList = new HashSet<>();
		try {
			customerList = facade.getAllCustomers();
		} catch (CouponFacadeException e) {
			throw new WebApplicationException(HttpServletResponse.SC_NOT_FOUND);
		}
		return customerList;
	}

	@GET
	@Path("findCompany/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public CompanyWrapper getCompany(@PathParam("id") long companyId) {
		Company company = new Company();
		try {
			company = facade.getCompany(companyId);
		} catch (CouponFacadeException e) {
			throw new WebApplicationException(HttpServletResponse.SC_NOT_FOUND);
		}
		return new CompanyWrapper(company);
	}

	@GET
	@Path("findCustomer/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public CustomerWrapper getCustomer(@PathParam("id") long customerId) {
		Customer customer = new Customer();
		try {
			customer = facade.getCustomer(customerId);
		} catch (CouponFacadeException e) {
			throw new WebApplicationException(HttpServletResponse.SC_NOT_FOUND);

		}
		return new CustomerWrapper(customer);
	}

	@Path("delComp/{companyId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public void delComp(@PathParam("companyId") long companyId) {
		try {
			facade.deleteCompany(companyId);
			} catch (CouponFacadeException e) {
			throw new WebApplicationException(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	@Path("delCust/{customerId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public void delCustomer(@PathParam("customerId") long customerId) {
		try {
			facade.deleteCustomer(customerId);
		} catch (CouponFacadeException e) {
			throw new WebApplicationException(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	@GET
	@Path("updateComp/{companyId}/{companyEmail}/{companyPassword}")
	@Produces(MediaType.APPLICATION_JSON)
	public void updateComp(@PathParam("companyId") long companyId, @PathParam("companyEmail") String email,
			@PathParam("companyPassword") String password) {
		Company company = new Company();
		company.setCompanyId(companyId);
		company.setEmail(email);
		company.setPassword(password);
		try {
			facade.updateCompany(company);
		} catch (CouponFacadeException e) {
			throw new WebApplicationException(HttpServletResponse.SC_NOT_FOUND);

		}
	}

	@GET
	@Path("updateCust/{customerId}/{customerPassword}")
	@Produces(MediaType.APPLICATION_JSON)
	public void updateCust(@PathParam("customerId") long customerId, @PathParam("customerPassword") String password) {
		Customer customer = new Customer();
		customer.setCustomerId(customerId);
		customer.setPassword(password);
		try {
			facade.updateCustomer(customer);
		} catch (CouponFacadeException e) {
			throw new WebApplicationException(HttpServletResponse.SC_NOT_FOUND);

		}
	}

	@GET
	@Path("createComp/{name}/{email}/{password}")
	@Produces(MediaType.APPLICATION_JSON)
	public void createCompany(@PathParam("name") String companyName, @PathParam("email") String email,
			@PathParam("password") String password) {
		Company company = new Company();
		company.setCompanyName(companyName);
		company.setEmail(email);
		company.setPassword(password);
		try {
			facade.createCompany(company);
		} catch (CouponFacadeException e) {
			throw new WebApplicationException(HttpServletResponse.SC_NOT_FOUND);

		}
	}

	@GET
	@Path("createCust/{name}/{password}")
	@Produces(MediaType.APPLICATION_JSON)
	public void createCustomer(@PathParam("name") String customerName, @PathParam("password") String password) {
		Customer customer = new Customer();
		customer.setCustomerName(customerName);
		customer.setPassword(password);
		try {
			facade.createCustomer(customer);
			response.setHeader("success", "The customer you requested has been created successfully");
		} catch (CouponFacadeException e) {
			throw new WebApplicationException(HttpServletResponse.SC_NOT_FOUND);

		}
	}

}
