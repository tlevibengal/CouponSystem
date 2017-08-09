package core.ws;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import coupons.Beans.Coupon;
import coupons.Beans.CouponType;
import coupons.Exceptions.CouponFacadeException;
import coupons.Exceptions.IllegalCouponDetailsException;
import coupons.Facades.CustomerFacade;

@Path("customers")
public class CustomerWs {

	@Context
	private HttpServletRequest request;
	@Context
	private HttpServletResponse response;
	
	private CustomerFacade facade;
	private String userName;

	@PostConstruct
	public void setFacade() {
		facade = ((CustomerFacade) request.getSession().getAttribute("facade"));
		 userName = (String) request.getSession().getAttribute("name");	 

	}
	
	
@GET
@Path("getSessionUser")
@Produces(MediaType.APPLICATION_JSON)
public void getSessionUser(){	
		response.setHeader("user", userName);		
	}



@GET
@Path("quit")
@Produces(MediaType.APPLICATION_JSON)
public void logOut(){
	request.getSession().invalidate();
	response.setHeader("quit", "quit");


	}

@GET
@Path("loadTypes")
@Produces(MediaType.APPLICATION_JSON)
public static String[] names() {
    CouponType[] coupTypes =CouponType.values();
    String[] names = new String[coupTypes.length];
    for (int i = 0; i < coupTypes.length; i++) {
        names[i] = coupTypes[i].toString();
    }
   // System.out.println(names);
    return names;
}

	 @GET
	 @Path("getAllCoupons")
	 @Produces(MediaType.APPLICATION_JSON)
	 public Set<Coupon> getAllCoupons() {
		 Set<Coupon> couponList = new HashSet<>();
		 couponList = facade.getAllExistingCoupons();
				 	 return  couponList;
	 }
	 
	 
	 
	 @GET
	 @Path("getAllpurchasedCoupons")
	 @Produces(MediaType.APPLICATION_JSON)
	 public Set<Coupon> getAllpurchasedCoupons() {
		 Set<Coupon> purchasedCouponList = new HashSet<>();
		 try {
			purchasedCouponList = facade.getAllPurchasedCoupons();
		} catch (CouponFacadeException e) {
			throw new WebApplicationException(HttpServletResponse.SC_NOT_FOUND);
		}
				 	 return  purchasedCouponList;
	 }
	 
	 
	 
	
	@GET
	@Path("buy/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public void buyCoupon(@PathParam("id") long couponId) {
		try {
			facade.purchaseCoupon(couponId);
		} catch (CouponFacadeException e) {
			throw new WebApplicationException(HttpServletResponse.SC_NOT_FOUND);
		} catch (IllegalCouponDetailsException e) {
			throw new WebApplicationException(HttpServletResponse.SC_CONFLICT);
		}
	

		
	
}

}

	
