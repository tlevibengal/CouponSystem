package coupons.Exceptions;

public class IllegalCouponDetailsException extends CouponsSysException {

	/**
	 * This Exception extends CouponsSysException. The exception is thrown if
	 * the coupon details are invalid.
	 */
	private static final long serialVersionUID = 1L;

	public IllegalCouponDetailsException() {
		super();
	}

	public IllegalCouponDetailsException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public IllegalCouponDetailsException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalCouponDetailsException(String message) {
		super(message);
	}

	public IllegalCouponDetailsException(Throwable cause) {
		super(cause);
	}


}
