package coupons.CouponSystem;

import coupons.ConnectionPool.ConnectionPool;
import coupons.DAO.DBDAO.CompanyDBDAO;
import coupons.DAO.DBDAO.CustomerDBDAO;
import coupons.DailyTaskThread.DailyCouponExperationTask;
import coupons.Exceptions.DaoCouponException;
import coupons.Exceptions.IllegalLoginException;
import coupons.Facades.AdminFacade;
import coupons.Facades.ClientCouponFacade;
import coupons.Facades.ClientType;
import coupons.Facades.CompanyFacade;
import coupons.Facades.CustomerFacade;

public class CouponSystem implements ClientCouponFacade {

	private static CouponSystem instance = new CouponSystem();
	private Thread dailyThread;
	/**
	 * This constructor initiates the ConnectionPool and starts the
	 * dailyCouponExperationTask
	 */
	private CouponSystem() {
		ConnectionPool.getinstance();
		dailyThread = new Thread(new DailyCouponExperationTask());
		dailyThread.start();
	}

	/**
	 * This method creates an instance of the coupon system.
	 * 
	 * @return An instance of the coupon system.
	 */
	public static CouponSystem getInstance() {
		return instance;
	}

	/**
	 * This method is used in order to login to the coupon system. according to
	 * the login details the specific client facade is loaded.
	 * 
	 * @param name
	 *            - the user name given by the user.
	 * @param password
	 *            -the password given by the user.
	 * @param clientType
	 *            -the users client type- can be one of 3- Administrator,
	 *            company and customer.
	 * @param customerId
	 *            -this customerId- retrieved from the dataBase using the user &
	 *            password.
	 * @param companyId
	 *            -this customerId- retrieved from the dataBase using the user &
	 *            password.
	 * @return A Client Coupon Facade-a general Facade that according to the
	 *         login details ( user,password,client type) loads the correct
	 *         facade- Administrator ,company or customer facade.
	 * @throws DaoCouponException
	 *             if the user credentials are not recognized.
	 */
	public ClientCouponFacade login(String name, String password, ClientType clientType) throws DaoCouponException {
		ClientCouponFacade clientCouponFacade;

		switch (clientType) {
		case ADMIN:
			clientCouponFacade = new AdminFacade();
			boolean areAdminCredentialsValid = name.equals("admin") && password.equals("1234");
			if (!areAdminCredentialsValid) {
				throw new DaoCouponException("Incorrect admin credentials, please try again");
			}
			break;
		case CUSTOMER:
			CustomerDBDAO customerDBDAO = new CustomerDBDAO();
			long customerId;
			try {
				customerId = customerDBDAO.getLoginId(name, password);
				clientCouponFacade = new CustomerFacade(customerId);
				if (!customerDBDAO.login(name, password)) {
				}
			} catch (IllegalLoginException e) {
				throw new DaoCouponException("Incorrect customer username or password.");
			}
			break;
		case COMPANY:
			CompanyDBDAO companyDBDAO = new CompanyDBDAO();
			try {
			long companyId = companyDBDAO.getLoginId(name, password);
			clientCouponFacade = new CompanyFacade(companyId);
			if (!companyDBDAO.login(name, password)) {
				}
			} catch (IllegalLoginException e) {
			throw new DaoCouponException("Incorrect company username or password.");
		}
			break;
		default:
			throw new DaoCouponException("Unknown client type.");
		}
		return clientCouponFacade;

	}

	/**
	 * This method is used to shut down the coupon system. All of the
	 * connections are returned to the connection pool set and the daily task
	 * thread is stopped.
	 */
	public void shutDownCouponSystem() {
		dailyThread.interrupt();
		ConnectionPool connectionPool = ConnectionPool.getinstance();
		connectionPool.closeAllConnections();

	}

}
