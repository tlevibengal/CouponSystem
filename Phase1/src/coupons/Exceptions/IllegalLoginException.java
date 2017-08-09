package coupons.Exceptions;

public class IllegalLoginException extends CouponsSysException {

	/**
	 * This Exception extends CouponsSysException.The exception is thrown if the
	 * System login has failed.(Company or customer user and password not found
	 * in the database.
	 */
	private static final long serialVersionUID = 1L;

	public IllegalLoginException() {
		super();
	}

	public IllegalLoginException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public IllegalLoginException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalLoginException(String message) {
		super(message);
	}

	public IllegalLoginException(Throwable cause) {
		super(cause);
	}

}
