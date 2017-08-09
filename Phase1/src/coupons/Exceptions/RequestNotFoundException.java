package coupons.Exceptions;

public class RequestNotFoundException extends CouponsSysException {

	/**
	 * This Exception extends CouponsSysException.The exception is thrown if the
	 * requested argument was not found in the database.
	 */
	private static final long serialVersionUID = 1L;

	public RequestNotFoundException() {
		super();
	}

	public RequestNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RequestNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public RequestNotFoundException(String message) {
		super(message);
	}

	public RequestNotFoundException(Throwable cause) {
		super(cause);
	}

	public RequestNotFoundException(long companyId) {
	}



}
