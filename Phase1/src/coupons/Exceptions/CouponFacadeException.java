package coupons.Exceptions;

public class CouponFacadeException extends CouponsSysException {

	/**
	 * This Exception extends CouponsSysException. it catches any
	 * DaoCouponException that is caught on the facade level.
	 */
	private static final long serialVersionUID = 1L;

	public CouponFacadeException() {
		super();
	}

	public CouponFacadeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CouponFacadeException(String message, Throwable cause) {
		super(message, cause);
	}

	public CouponFacadeException(String message) {
		super(message);
	}

	public CouponFacadeException(Throwable cause) {
		super(cause);
	}



}