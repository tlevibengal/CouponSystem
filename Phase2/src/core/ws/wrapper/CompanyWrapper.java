package core.ws.wrapper;

import java.util.Set;

import coupons.Beans.Company;
import coupons.Beans.Coupon;

public class CompanyWrapper extends Object {
	private long id;
	private String companyName;
	private String email;

	public CompanyWrapper(Company company) {
		this.id=company.getCompanyId();
		this.companyName = company.getCompanyName();
		this.email = company.getEmail();
	}

	public long getId() {
		return id;
	}


	public String getCompanyName() {
		return companyName;
	}



	public String getEmail() {
		return email;
	}


}
