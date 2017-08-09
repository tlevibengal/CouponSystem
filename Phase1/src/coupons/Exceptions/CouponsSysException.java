package coupons.Exceptions;

public class CouponsSysException extends Exception {

	/**
	 * this Exception is the "super" Exception for all exceptions thrown in this
	 * program
	 */
	private static final long serialVersionUID = 1L;

	public CouponsSysException() {
		super();
	}

	public CouponsSysException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CouponsSysException(String message, Throwable cause) {
		super(message, cause);
	}

	public CouponsSysException(String message) {
		super(message);
	}

	public CouponsSysException(Throwable cause) {
		super(cause);
	}



}
