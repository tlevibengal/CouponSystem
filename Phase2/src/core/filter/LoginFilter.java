package core.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import coupons.CouponSystem.CouponSystem;


@WebFilter("/LoginFiltr")
public class LoginFilter implements Filter {


    public LoginFilter() {
    }


	public void destroy() {
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if(((HttpServletRequest)servletRequest).getPathTranslated().indexOf("simple")==-1){
			HttpSession session = ((HttpServletRequest)servletRequest).getSession(false);
			if (session == null) {
				//in case there is no session we do not forward req to ws
				//TODO - throw exception?
				((HttpServletResponse)response).sendRedirect("index.html");
				return;
			}
			
		}
		//session was found so it is an authenticated user
		chain.doFilter(servletRequest, response);
		
	}


	public void init(FilterConfig fConfig) throws ServletException {
	}
}
