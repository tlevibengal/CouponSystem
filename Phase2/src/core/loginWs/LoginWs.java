package core.loginWs;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;

import coupons.CouponSystem.CouponSystem;
import coupons.Exceptions.DaoCouponException;
import coupons.Facades.ClientCouponFacade;
import coupons.Facades.ClientType;

@Path("simple")
public class LoginWs {

	@Context
	private HttpServletRequest req;
	@Context
	private HttpServletResponse res;
	// private CouponSystem coupSys = CouponSystem.getInstance();
	Object userName;

	private static final String FACADE = "Facade";



	@GET
	public String myFunction(@QueryParam("username") String username, @QueryParam("password") String password,
			@QueryParam("clientType") String clientTypeString) throws WebApplicationException   {
		HttpSession session = req.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		session = req.getSession(true);
		ClientType clientType;
		String reply = null;
		try {
			session = req.getSession(true);
			switch (clientTypeString) {
			case "ADMIN":
				clientType = ClientType.ADMIN;
				doLogin(username, password, session, CouponSystem.getInstance(), clientType);
				res.sendRedirect("../Admin.html");
				return "";
			case "COMPANY":
				clientType = ClientType.COMPANY;
				doLogin(username, password, session, CouponSystem.getInstance(), clientType);
				res.sendRedirect("../Company.html");
				return "";

			case "CUSTOMER":
				clientType = ClientType.CUSTOMER;
				doLogin(username, password, session, CouponSystem.getInstance(), clientType);
				res.sendRedirect("../Customer.html");
				return "";
			}
		} catch (Exception e) {
			try {
				res.sendRedirect("../errorPage.html");
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}
		return reply;

	}

	private void doLogin(String username, String password, HttpSession session, CouponSystem coupSys,
			ClientType clientType) throws WebApplicationException{
		ClientCouponFacade facade;
		try {
			facade = CouponSystem.getInstance().login(username, password, clientType);
		} catch (DaoCouponException e) {
			throw new WebApplicationException(HttpServletResponse.SC_UNAUTHORIZED);
		}
		session.setAttribute("facade", facade);
		session.setAttribute("name", username);
		session.setAttribute("clientType", clientType.toString());
		userName = username;

	}

}
