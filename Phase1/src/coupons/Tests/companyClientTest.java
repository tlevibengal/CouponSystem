package coupons.Tests;

import java.util.Date;
import java.util.Scanner;
import java.util.Set;

import coupons.Beans.Coupon;
import coupons.Beans.CouponType;
import coupons.CouponSystem.CouponSystem;
import coupons.Exceptions.CouponFacadeException;
import coupons.Exceptions.CouponsSysException;
import coupons.Exceptions.IllegalInputException;
import coupons.Exceptions.LoginUnsuccessfulException;
import coupons.Facades.ClientType;
import coupons.Facades.CompanyFacade;

public class companyClientTest {

	private static CouponSystem system;
	private static CompanyFacade facade;
	private static CouponType CouponType;
	private static boolean noExceptionsThrown;

	private static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) throws Exception {
		System.out.println("===================================================");
		System.out.println("***** WELCOME TO THE COUPON MANAGEMENT SYSTEM *****");
		System.out.println("===================================================");

		start();
		try {
			login();
			go();
			stop();
		} catch (LoginUnsuccessfulException e) {
			System.out.println(e.getMessage());
			stop();

		}

	}

	private static void start() {

		system = CouponSystem.getInstance();

	}

	private static void login() throws LoginUnsuccessfulException {
		System.out.println("\nPlease enter your user credentials:");
		boolean loginNotSuccessful = true;
		for (int i = 0; i < 3 && loginNotSuccessful; i++) {
			System.out.print("User Name: ");
			String username = sc.nextLine();
			System.out.print("Password: ");
			String password = sc.nextLine();

			try {
				facade = (CompanyFacade) system.login(username, password, ClientType.COMPANY);
				System.out.println("\nSuccessfully logged in as company");
				loginNotSuccessful = false;
			} catch (CouponsSysException e) {
				System.out.println("Invalid username/password, you have " + (2 - i) + " more tries");

			}
		}
		if (loginNotSuccessful) {
			throw new LoginUnsuccessfulException("Failed to login 3 times, goodbye.");

		}
	}

	private static void go() {

		String input;
		boolean keepGoing = true;
		while (keepGoing) {
			showMenu();
			input = sc.nextLine();
			try {
				int choice = Integer.parseInt(input);
				System.out.println();
				switch (choice) {
				case 1:
					doCreateCoupon();
					break;
				case 2:
					doDeleteCoupon();
					break;
				case 3:
					doUpdateCoupon();
					break;
				case 4:
					doGetCoupon();

					break;
				case 5:
					doGetCompanyCouponList();

					break;
				case 6:
					doGetCouponByType();
					break;
				case 7:
					doGetCouponsByPrice();
					break;
				case 8:
					dogetCouponsByDate();
					break;

				case 0:
					System.out.println("GoodBye");
					keepGoing = false;
					break;

				default:
					System.out.println(choice + " is not a legal choice, please try again");
					break;
				}

			} catch (NumberFormatException e) {
				System.out.println(input + " is not a legal choice, please try again");
			}
		}
	}

	private static CouponType UserCouponType(String userInput) {
		CouponType = null;
		while (true) {
			try {
				int choice = Integer.parseInt(userInput);
				System.out.println();
				switch (choice) {
				case 1:
					CouponType = (coupons.Beans.CouponType.FOOD);
					break;
				case 2:
					CouponType = (coupons.Beans.CouponType.HOME);
					break;

				case 3:
					CouponType = (coupons.Beans.CouponType.SPORTS);

					break;
				case 4:
					CouponType = (coupons.Beans.CouponType.TRAVEL);

					break;
				case 5:
					CouponType = (coupons.Beans.CouponType.BEAUTY);
					break;
				case 6:
					CouponType = (coupons.Beans.CouponType.GAMES);

					break;
				case 7:
					CouponType = (coupons.Beans.CouponType.FASHION);
					break;
				case 8:
					CouponType = (coupons.Beans.CouponType.ELECTRONICS);
					break;

				default:
					System.out.println(choice + " is not a legal choice, please try again");

					break;
				}
				return CouponType;
			} catch (NumberFormatException e) {
				System.out.println(userInput + " is not a legal choice, please try again");
			}
		}
	}

	private static boolean tryAgain() {
		tryAgainMenu();
		String input;
		while (true) {
			input = sc.nextLine();
			try {
				int choice = Integer.parseInt(input);
				System.out.println();
				switch (choice) {
				case 1:
					return noExceptionsThrown = false;
				case 2:
					System.out.println("Reloading option menu");
					return noExceptionsThrown = true;

				default:
					System.out.println(choice + " is not a legal choice, please try again");
					break;
				}

			} catch (NumberFormatException e) {
				System.out.println(input + " is not a legal choice, please try again");
			}
		}
	}

	private static void stop() {
		sc.close();
		system.shutDownCouponSystem();

	}

	private static void showMenu() {
		System.out.println("==================================");
		System.out.println("\n========== Menu ===========\n");
		System.out.println("(1) Create a new coupon ");
		System.out.println("(2) Delete a coupon  ");
		System.out.println("(3) Update a coupon  ");
		System.out.println("(4) Find an existing coupon   ");
		System.out.println("(5) Get all created coupons  ");
		System.out.println("(6) Get all coupons by type  ");
		System.out.println("(7) Get all coupons by price ");
		System.out.println("(8) Get all coupons by exparation date ");

		System.out.println("..........................");
		System.out.println("(0) Quit  ");
		System.out.print("Please enter your choice (0-6):  ");
		System.out.println();
	}

	private static void showMenuCouponType() {
		System.out.println();
		System.out.println("=========== Coupon Types ================\n");
		System.out.print("(1) Food & Gourmet(FOOD)");
		System.out.print("     (2) House & Home(HOME)");
		System.out.print("   (3) Sporting Goods(SPORTS)");
		System.out.println("  (4) Travel(TRAVEL)");
		System.out.print("(5) Beauty products(BEAUTY)");
		System.out.print("  (6) Toys & Games(GAMES)");
		System.out.print("  (7) Clothing(FASHION)");
		System.out.println("       (8) Computers,Laptops & Tablets(ELECTRONICS)");
		System.out.print("Please choose the coupon type: ");

	}

	private static void tryAgainMenu() {
		System.out.print("\nWould you like to try again?");
		System.out.print("  (1) Yes ");
		System.out.println("(2) No ");
	}

	private static void doGetCoupon() {
		noExceptionsThrown = false;
		while (!noExceptionsThrown) {
			System.out.println("Get coupon details:");
			System.out.print("Enter coupon id: ");
			try {
				long couponId = Long.parseLong(sc.nextLine());

				Coupon coupon = facade.getCoupon(couponId);
				System.out.println("processing request...\n");
				System.out.println("The coupon you requested is: " + coupon);
				noExceptionsThrown = true;
			} catch (CouponFacadeException e) {
				System.out.println(e.getMessage());
				tryAgain();
			}

		}
	}

	private static void doGetCouponByType() {
		noExceptionsThrown = false;
		while (!noExceptionsThrown) {
			showMenuCouponType();
			String UserCouponType = (sc.nextLine());
			CouponType couponType = UserCouponType(UserCouponType);
			System.out.println("Processing request...\n");
			System.out.println("Your coupons of the type " + couponType);
			try {
				for (Coupon curr : facade.getCouponsByType(couponType)) {
					System.out.println(curr);
					noExceptionsThrown = true;
				}
			} catch (CouponFacadeException e) {
				System.out.println(e.getMessage());
				tryAgain();
			}

		}
	}

	private static void doGetCouponsByPrice() {
		noExceptionsThrown = false;
		while (!noExceptionsThrown) {
			System.out.print("Please enter the max price: ");
			Double maxPrice = Double.parseDouble(sc.nextLine());
			System.out.println("Processing request...\n");
			try {
				for (Coupon current : facade.getAllCouponsByPrice(maxPrice)) {
					System.out.println(current);
					noExceptionsThrown = true;
				}
			} catch (CouponFacadeException e) {
				System.out.println(e.getMessage());
				tryAgain();
			}
		}
	}

	private static void dogetCouponsByDate() {
		noExceptionsThrown = false;
		while (!noExceptionsThrown) {
			System.out.print("Please enter expiration date (dd/mm/yyyy):");
			try {
				Date endDate = (facade.convertStringToDate(sc.nextLine()));
			System.out.println("Processing request...\n");
				Set<Coupon> couponListByEndDate = facade.getCouponsByDate(endDate);
				for (Coupon current : couponListByEndDate) {
					System.out.println(current);
					noExceptionsThrown = true;
				}
			} catch (IllegalInputException e) {
				System.out.println(e.getMessage());
			} catch (CouponFacadeException e) {
				System.out.println(e.getMessage());
				tryAgain();
			}
		}
	}

	private static void doGetCompanyCouponList() {
		System.out.println("Processing request...\n");
		try {
			for (Coupon curr : facade.getAllCoupons()) {
				System.out.println(curr);
			}
		} catch (CouponFacadeException e) {
			System.out.println(e.getMessage());
		}

	}

	private static void doUpdateCoupon() {
		noExceptionsThrown = false;
		while (!noExceptionsThrown) {
			System.out.println("Coupon Update");
			System.out.print("Enter coupon id: ");
			long couponId = Long.parseLong(sc.nextLine());
			try {
				Coupon coupon = facade.getCoupon(couponId);
				System.out.println("Your current coupon details are: " + coupon);
				System.out.print("Enter a new Coupon end date: (mm/dd/yyyy) ");
				Date endDate = (facade.convertStringToDate(sc.nextLine()));
				coupon.setEndDate(endDate);
				System.out.print("Enter the new Coupon price: ");
				double price = (Double.parseDouble(sc.nextLine()));
				coupon.setPrice(price);
				System.out.println("processing request...\n");
				facade.updateCoupon(coupon);
				System.out.println(coupon.getCouponName() + "'s new end date and price (" + endDate + "," + price
						+ ") have been updated in the system");
				noExceptionsThrown = true;
			} catch (CouponFacadeException | IllegalInputException e) {
				System.out.println(e.getMessage());
				tryAgain();
			}
		}
	}

	private static void doDeleteCoupon() {
		noExceptionsThrown = false;
		while (!noExceptionsThrown) {
			System.out.println("Delete coupon:");
			System.out.print("Enter coupon id: ");
			long couponId = (Long.parseLong(sc.nextLine()));
			System.out.println("processing request...\n");
			try {
				facade.deleteCoupon(couponId);
				System.out.println("Coupon with id " + couponId + " has been deleted");
				noExceptionsThrown = true;
			} catch (CouponFacadeException e) {
				System.out.println(e.getMessage());
				tryAgain();
			}

		}
	}

	private static void doCreateCoupon() {
		noExceptionsThrown = false;
		while (!noExceptionsThrown) {
			try {
				System.out.println("create Coupon");
				Coupon coupon = new Coupon();
				System.out.print("Enter Coupon name: ");
				coupon.setCouponName(sc.nextLine());
				System.out.print("Enter a short Coupon description: ");
				coupon.setDescription(sc.nextLine());
				System.out.print("Enter Coupon start Date: (dd/mm/yyyy) ");
				coupon.setStartDate(facade.convertStringToDate(sc.nextLine()));
				System.out.print("Enter Coupon End Date: (dd/mm/yyyy) ");
				coupon.setEndDate(facade.convertStringToDate(sc.nextLine()));
				System.out.print("Enter Coupon price: ");
				coupon.setPrice(Double.parseDouble(sc.nextLine()));
				System.out.print("Enter the number of Coupons you want to create: ");
				coupon.setAmount((Integer.parseInt(sc.nextLine())));
				showMenuCouponType();
				String UserCouponType = (sc.nextLine());
				CouponType couponType = UserCouponType(UserCouponType);
				coupon.setCouponType(couponType);
				System.out.print("Enter the Coupon image: ");
				coupon.setImage(sc.nextLine());
				System.out.println("processing request...\n");
				facade.createCoupon(coupon);
				System.out.println(coupon + " was created Successfully");
				noExceptionsThrown = true;
			} catch (CouponFacadeException | IllegalInputException e) {
				System.out.println(e.getMessage());
				tryAgain();
			}

		}
	}



}
