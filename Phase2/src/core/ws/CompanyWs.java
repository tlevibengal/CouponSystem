package core.ws;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.derby.tools.sysinfo;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import coupons.Beans.Coupon;
import coupons.Beans.CouponType;
import coupons.Exceptions.CouponFacadeException;
import coupons.Facades.CompanyFacade;
import sun.nio.ch.IOUtil;

@Path("companies")
public class CompanyWs {

	@Context
	private HttpServletRequest request;
	@Context
	private HttpServletResponse response;
	
	private CompanyFacade facade;
	private String userName;
	
	@PostConstruct
	public void setFacade() {
		facade = ((CompanyFacade) request.getSession().getAttribute("facade"));
		userName = (String) request.getSession().getAttribute("name");	 

	}
	
	
@GET
@Path("getSessionUser")
@Produces(MediaType.APPLICATION_JSON)
public void getSessionUser(){
	 userName = (String) request.getSession().getAttribute("name");	 
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
	
		try {	couponList = facade.getAllCoupons();
		} catch (CouponFacadeException e) {
			throw new WebApplicationException(HttpServletResponse.SC_BAD_REQUEST);
		}
			 return  couponList;
	 }
	 
	
	@GET
	@Path("findCoupon/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Coupon getCoupon(@PathParam("id") long couponId) {
				 Coupon coupon = new Coupon();
		try {
			coupon = facade.getCoupon(couponId);
		} catch (CouponFacadeException e) {
			throw new WebApplicationException(HttpServletResponse.SC_NOT_FOUND);

			}
		 return coupon;
		 
		 }
	
	@Path("delCoup/{couponId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public void delCoupon(@PathParam("couponId") long couponId) {
		 try {
			facade.deleteCoupon(couponId);
		} catch (CouponFacadeException e) {
			throw new WebApplicationException(HttpServletResponse.SC_BAD_REQUEST);
		}
		}

	
	@GET
	@Path("updateCoupon/{id}/{price}/{endDate}")
	@Produces(MediaType.APPLICATION_JSON)
	public void updateCoupon (@PathParam("id") long couponId,@PathParam("price") double price,@PathParam("endDate")Date endDate){
		 Coupon coupon = new Coupon();
		 coupon.setCouponId(couponId);
		 coupon.setPrice(price);
		 coupon.setEndDate(endDate);
						try {
							facade.updateCoupon(coupon);	
						} catch (CouponFacadeException e) {
							throw new WebApplicationException(HttpServletResponse.SC_NOT_FOUND);

						}
		 }
	
	
	
	


	@GET
	@Path("createCoup/{couponName}/{description}/{startDate}/{endDate}/{amount}/{price}/{type}")
	@Produces(MediaType.APPLICATION_JSON)
	public void createCoupon (@PathParam("couponName") String couponName,@PathParam("description") String description,@PathParam("startDate")Date startDate,@PathParam("endDate")Date endDate,@PathParam("amount")int amount,@PathParam("price")double price,@PathParam("type")CouponType type){
	
		 Coupon coupon = new Coupon();
		coupon.setCouponName(couponName);
		 coupon.setDescription(description);
		coupon.setStartDate(startDate);
		coupon.setEndDate(endDate);
		 coupon.setAmount(amount);
		 coupon.setCouponType(type);
		 coupon.setPrice(price);
		coupon.setImage(couponName);
				 try {
			facade.createCoupon(coupon);
			
		} catch (CouponFacadeException e) {
			throw new WebApplicationException(HttpServletResponse.SC_BAD_REQUEST);
		}
		}
	

}
