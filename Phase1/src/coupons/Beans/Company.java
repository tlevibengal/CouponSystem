package coupons.Beans;

import java.io.Serializable;
import java.util.Set;

public class Company implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String companyName;
	private String email;
	private String password;
	private Set<Coupon> CompanyCoupons;

	public Company() {

	}

	public long getCompanyId() {
		return id;
	}

	public void setCompanyId(long companyId) {
		this.id = companyId;
	}


	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setCompanyCoupons(Set<Coupon> companyCoupons) {
		CompanyCoupons = companyCoupons;
	}

	public Set<Coupon> getCompanyCoupons() {
		return CompanyCoupons;
	}

	@Override
	public String toString() {
		return "Company: Id=" + id + ", company name=" + companyName + ", email=" + email + ", password=" + password
				+ ",";
	}



}
