package core.ws.wrapper;

import java.util.Set;

import coupons.Beans.Company;
import coupons.Beans.Coupon;
import coupons.Beans.Customer;

public class CustomerWrapper extends Object {
	private long id;
	private String customerName;
	private String password;

	public CustomerWrapper(Customer customer) {
		this.id=customer.getCustomerId();
		this.customerName = customer.getCustomerName();
		this.password = customer.getPassword();
	}

	public long getId() {
		return id;
	}


	public String getCompanyName() {
		return customerName;
	}



	public String getEmail() {
		return password;
	}


}
