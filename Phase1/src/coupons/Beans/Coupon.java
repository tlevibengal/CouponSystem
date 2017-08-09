package coupons.Beans;

import java.io.Serializable;
import java.util.Date;

public class Coupon implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String name;
	private Date startDate;
	private Date endDate;
	private int amount;
	private CouponType CouponType;
	private String description;
	private double price;
	private String image;

	public Coupon() {
		super();
	}

	public long getCouponId() {
		return id;
	}

	public void setCouponId(long couponId) {
		this.id = couponId;
	}

	public String getCouponName() {
		return name;
	}

	public void setCouponName(String name) {
		this.name = name;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public CouponType getCouponType() {
		return CouponType;
	}

	public void setCouponType(CouponType couponType) {
		CouponType = couponType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String couponDescription) {
		this.description = couponDescription;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "Coupon: Id=" + id + ", name=" + name + ", start date=" + startDate + ", end date=" + endDate
				+ ", amount="
				+ amount + ", CouponType=" + CouponType + ", description=" + description + ", price=" + price
 + ", image=" + image;
	}

		public void setCouponTypeString(String couponType) {
		CouponType = coupons.Beans.CouponType.valueOf(couponType);
	}




}
