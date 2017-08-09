package coupons.Exceptions;

public class IllegalInputException extends CouponsSysException {

	/**
	 * This Exception extends CouponsSysException.The exception is thrown if the
	 * input given by the user is invalid.
	 */
	private static final long serialVersionUID = 1L;


	public IllegalInputException() {
		super();
	}

	public IllegalInputException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public IllegalInputException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalInputException(String message) {
		super(message);
	}

	public IllegalInputException(Throwable cause) {
		super(cause);
	}


}
