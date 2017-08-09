package coupons.Exceptions;

public class LoginUnsuccessfulException extends CouponsSysException {

	/**
	 * This Exception extends CouponsSysException.The exception is thrown if the
	 * System login has failed. after 3 attempts.
	 */
	private static final long serialVersionUID = 1L;

	public LoginUnsuccessfulException() {
		super();
	}

	public LoginUnsuccessfulException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public LoginUnsuccessfulException(String message, Throwable cause) {
		super(message, cause);
	}

	public LoginUnsuccessfulException(String message) {
		super(message);
	}

	public LoginUnsuccessfulException(Throwable cause) {
		super(cause);
	}

}
