package coupons.Beans;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Customer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String customerName;
	private String password;
	private Set<Coupon> customerCoupons = new HashSet<>();

	public Customer() {
	}

	public long getCustomerId() {
		return id;
	}

	public void setCustomerId(long customerId) {
		this.id = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Coupon> getCustomerCoupons(long id) {
		return customerCoupons;
	}

	public void setCustomerCoupons(Set<Coupon> customerCoupons) {
		this.customerCoupons = customerCoupons;
	}

	@Override
	public String toString() {
		return "Customer: Id=" + id + ", customer name=" + customerName + ", password=" + password;

	}


}
