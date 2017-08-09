package coupons.DailyTaskThread;

import coupons.DAO.DBDAO.CouponDBDAO;
import coupons.Exceptions.DaoCouponException;

public class DailyCouponExperationTask implements Runnable {

	private CouponDBDAO couponDBDAO;

	/**
	 * This constructor loads the CouponDBDAO and initiates the Daily coupon
	 * expiration Thread.
	 */
	public DailyCouponExperationTask() {
		couponDBDAO = new CouponDBDAO();
	}

	/**
	 * This Thread runs every 24 hours, deleting all of the expired coupon from
	 * all lists. It then add the deleted coupons to the deleted coupon table in
	 * the database.
	 */
	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			try {
				couponDBDAO.removeExpiredCoupons();
				Thread.sleep(25000);
				// Thread.sleep(1000 * 60 * 60 * 24);

			} catch (DaoCouponException e) {
				System.out.println(e.getMessage());
			} catch (InterruptedException e) {
				System.out.println("Daily coupon expiration task stopped");
				return;
			}
		}
	}
}
