package coupons.Exceptions;

public class DaoCouponException extends CouponsSysException {


	/**
	 * This Exception extends CouponsSysException. it catches any SQLException
	 * thrown from the DataBase.
	 */
	private static final long serialVersionUID = 1L;

	public DaoCouponException() {
		super();
	}

	public DaoCouponException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DaoCouponException(String message, Throwable cause) {
		super(message, cause);
	}

	public DaoCouponException(String message) {
		super(message);
	}

	public DaoCouponException(Throwable cause) {
		super(cause);
	}


}
