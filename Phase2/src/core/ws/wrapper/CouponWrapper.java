package core.ws.wrapper;

import java.util.Date;

import coupons.Beans.Coupon;
import coupons.Beans.CouponType;

public class CouponWrapper {

	private long id;
	private String name;
	private Date startDate;
	private Date endDate;
	private int amount;
	private CouponType CouponType;
	private String description;
	private double price;
	private String image;
	
	
	
	
	
	public CouponWrapper(Coupon coupon) {
		super();
	}
	public long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public Date getStartDate() {
		return startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public int getAmount() {
		return amount;
	}
	public CouponType getCouponType() {
		return CouponType;
	}
	public String getDescription() {
		return description;
	}
	public double getPrice() {
		return price;
	}
	public String getImage() {
		return image;
	}
	
	
	
	
}
