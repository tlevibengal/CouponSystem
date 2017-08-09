package coupons.Tests;

import java.util.Scanner;

import coupons.Beans.Coupon;
import coupons.Beans.CouponType;
import coupons.CouponSystem.CouponSystem;
import coupons.Exceptions.CouponFacadeException;
import coupons.Exceptions.CouponsSysException;
import coupons.Exceptions.IllegalCouponDetailsException;
import coupons.Exceptions.LoginUnsuccessfulException;
import coupons.Facades.ClientType;
import coupons.Facades.CustomerFacade;

public class CustomerClientTest {

	private static CouponSystem system;
	private static CustomerFacade facade;
	private static CouponType CouponType;
	private static boolean noExceptionsThrown;

	private static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) throws CouponFacadeException {
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
				facade = (CustomerFacade) system.login(username, password, ClientType.CUSTOMER);
				System.out.println("\nSuccessfully logged in as customer");
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
					doPurchaseCoupon();
					break;
				case 2:
					doAllCoupons();
					break;
				case 3:
					doAllCouponsByType();
					break;
				case 4:
					doAllCouponsByPrice();
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

	// } catch (CouponsSysException e) {
	// System.out.println("ERROR !");}
	// Throwable t = e;
	// do {
	// System.out.println(t.getMessage());
	// t = t.getCause();
	// } while (t instanceof CouponsSysException);
	//
	// // USE THIS LINE FOR DEBUG
	// // e.printStackTrace(System.out);
	// } catch (RuntimeException e) {
	// System.out.println("ERROR !");
	// System.out.println(e);
	// }
	// }
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
		System.out.println("\n============= Menu ================\n");
		System.out.println("(1) Purchese a coupon   ");
		System.out.println("(2) All purchesed coupons  ");
		System.out.println("(3) All purchesed coupons by type  ");
		System.out.println("(4) All purchesed coupons by price  ");
		System.out.println(".................................");
		System.out.println("(0) Quit  ");
		System.out.print("Please enter your choice (0-4):  ");
	}

	private static void showMenuCouponType() {
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

	private static void doPurchaseCoupon() {
		noExceptionsThrown = false;
		while (!noExceptionsThrown) {
			System.out.println("Coupon purchese:");
			System.out.print("Please enter the coupon id: ");
			long coupId = Long.parseLong(sc.nextLine());
			try {
				facade.purchaseCoupon(coupId);
				System.out.println("Processing request...\n");
				System.out.println(" The Coupon with id: " + coupId + " has been added to your account");
				noExceptionsThrown = true;
			} catch (CouponFacadeException | IllegalCouponDetailsException e) {
				System.out.println(e.getMessage());
				tryAgain();
			}

		}
	}

	private static void doAllCoupons() {
		System.out.println("Purchese history:...\n");
		System.out.println("Processing request...\n");
		try {
			for (Coupon current : facade.getAllPurchasedCoupons()) {
				System.out.println(current);
			}
		} catch (CouponFacadeException e) {
			System.out.println(e.getMessage());
		}
	}

	private static void doAllCouponsByType() {
		noExceptionsThrown = false;
		while (!noExceptionsThrown) {
			showMenuCouponType();
			String UserCouponType = (sc.nextLine());
			CouponType couponType = UserCouponType(UserCouponType);
			System.out.println("Processing request...\n");
			System.out.println("Your coupons of the type " + couponType);
			try {
				for (Coupon current : facade.getAllPurchasedCouponsByType(couponType)) {
					System.out.println(current);
					noExceptionsThrown = true;
				}
			} catch (CouponFacadeException e) {
				System.out.println(e.getMessage());
				tryAgain();
			}
		}
	}

	private static void doAllCouponsByPrice() {
		noExceptionsThrown = false;
		while (!noExceptionsThrown) {
		System.out.print("Please enter the max price: ");
		Double maxPrice = Double.parseDouble(sc.nextLine());
		System.out.println("Processing request...\n");
		try {
				for (Coupon current : facade.getAllPurchasedCouponsByPrice(maxPrice)) {
					System.out.println(current);
					noExceptionsThrown = true;
			}
		} catch (CouponFacadeException e) {
			System.out.println(e.getMessage());
				tryAgain();
		}
	}
	}

}
