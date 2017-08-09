package coupons.Exceptions;

public class DuplicateEntryException extends CouponsSysException {

	/**
	 * This Exception extends CouponsSysException. it catches any
	 * SQLIntegrityConstraintViolationException thrown from the DataBase.
	 */
	private static final long serialVersionUID = 1L;

	public DuplicateEntryException() {
		super();
	}

	public DuplicateEntryException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DuplicateEntryException(String message, Throwable cause) {
		super(message, cause);
	}

	public DuplicateEntryException(String message) {
		super(message);
	}

	public DuplicateEntryException(Throwable cause) {
		super(cause);
	}

}
