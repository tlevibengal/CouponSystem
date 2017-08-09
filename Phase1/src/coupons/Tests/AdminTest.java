package coupons.Tests;

import java.util.Scanner;

import coupons.Beans.Company;
import coupons.Beans.Coupon;
import coupons.Beans.Customer;
import coupons.CouponSystem.CouponSystem;
import coupons.Exceptions.CouponFacadeException;
import coupons.Exceptions.CouponsSysException;
import coupons.Exceptions.LoginUnsuccessfulException;
import coupons.Exceptions.RequestNotFoundException;
import coupons.Facades.AdminFacade;
import coupons.Facades.ClientType;

public class AdminTest {

	private static CouponSystem system;
	private static AdminFacade facade;
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
				facade = (AdminFacade) system.login(username, password, ClientType.ADMIN);
				System.out.println("\nSuccessfully logged in as administrator");

				loginNotSuccessful = false;
			} catch (CouponsSysException e) {
				System.out.println("Invalid username/password, you have " + (2 - i) + " more tries");

			}
		}
		if (loginNotSuccessful) {
			throw new LoginUnsuccessfulException("Failed to login 3 times, goodbye.");

		}
	}
	// noExceptionsThrown = false;

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
					doCreateCompany();
					break;
				case 2:
					doDeleteCompany();
					break;
				case 3:
					doUpdateCompany();
					break;
				case 4:
					doGetCompany();
					break;
				case 5:
					doGetCompaniesList();
					break;
				case 6:
					doCreateCustomer();
					break;
				case 7:
					doDeleteCustomer();
					break;
				case 8:
					doUpdateCustomer();
					break;
				case 9:
					doGetCustomer();
					break;
				case 10:
					doGetCustomersList();
					break;
				case 11:
					dogetTaskDeletedCoupons();
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
		System.out.println("=======================================");
		System.out.println("\n========== Menu ================\n");
		System.out.println("(1)  Create a new company ");
		System.out.println("(2)  Delete a company ");
		System.out.println("(3)  Update a company ");
		System.out.println("(4)  Find an existing company  ");
		System.out.println("(5)  Get all companies ");
		System.out.println("(6)  Create a new customer ");
		System.out.println("(7)  Delete a customer ");
		System.out.println("(8)  Update a customer ");
		System.out.println("(9)  Find an existing customer  ");
		System.out.println("(10) Get all customers");
		System.out.println("(11) Get all Deleted Coupons");
		System.out.println("......................");
		System.out.println("(0)  Quit ");
		System.out.print("\nPlease enter your choice (0-11): ");
	}

	private static void tryAgainMenu() {
		System.out.print("\nWould you like to try again?");
		System.out.print("  (1) Yes ");
		System.out.println("(2) No ");
	}

	private static void doCreateCompany() {
		noExceptionsThrown = false;
		while (!noExceptionsThrown) {
		System.out.println("Create a new Company:");
		Company company = new Company();
			System.out.print("Please enter the company name: ");
			company.setCompanyName(sc.nextLine());
			System.out.print("Please enter the company password: ");
			company.setPassword(sc.nextLine());
			System.out.print("Please enter the company email: ");
			company.setEmail(sc.nextLine());
			System.out.println("processing request...\n");
			try {
				facade.createCompany(company);
				System.out.println(company + " has been created in the system");
				noExceptionsThrown = true;
			} catch (CouponFacadeException e) {
				System.out.println(e.getMessage());
				tryAgain();
			}

		}
	}

	private static void doUpdateCompany() {
		noExceptionsThrown = false;
		while (!noExceptionsThrown) {
		System.out.println("Company update:");
			System.out.print("Please enter the  company id: ");
			long compId = Long.parseLong(sc.nextLine());
			try {
				Company company = facade.getCompany(compId);
				System.out.println("Current company details: " + company);
				System.out.print("Please enter a new  Email: ");
				String email = (sc.nextLine());
				company.setEmail(email);
				System.out.print("Please enter a new password: ");
				String password = (sc.nextLine());
				company.setPassword(password);
				System.out.println("processing request...\n");
				facade.updateCompany(company);
				System.out.println(company.getCompanyName() + "'s new email and password (" + email + "," + password
						+ ") have been updated in the system");
				noExceptionsThrown = true;
			} catch (CouponFacadeException e) {
				System.out.println(e.getMessage());
				tryAgain();
			}

		}
	}

	private static void doDeleteCompany() {
		noExceptionsThrown = false;
		while (!noExceptionsThrown) {
		System.out.println("Delete a company");
			System.out.print("Please enter the company id: ");
			long companyId = (Long.parseLong(sc.nextLine()));
			System.out.println("processing request...\n");
			try {
				facade.deleteCompany(companyId);
				System.out.println("company with id " + companyId + " has been deleted");
				noExceptionsThrown = true;
			} catch (CouponFacadeException e) {
				System.out.println(e.getMessage());
				tryAgain();
			}

		}
	}

	private static void doGetCompany() {
		noExceptionsThrown = false;
		while (!noExceptionsThrown) {
			System.out.println("Get company detalis");
			System.out.print("Please enter the company id: ");
			long compId = Long.parseLong(sc.nextLine());
			Company company;
			try {
				company = facade.getCompany(compId);
				System.out.println("The company you requested is: " + company);
				noExceptionsThrown = true;
			} catch (CouponFacadeException e) {
				System.out.println(e.getMessage());
				tryAgain();
			}

		}
	}

	private static void doGetCompaniesList() {
			System.out.println("Processing request...\n");
			try {
			for (Company current : facade.getAllCompanies()) {
				System.out.println(current);
				}
			} catch (CouponFacadeException e) {
				System.out.println(e.getMessage());
			}

		}

	private static void dogetTaskDeletedCoupons() {
		System.out.println("Processing request...\n");
		try {
			for (Coupon current : facade.getTaskDeletedCoupons()) {
				System.out.println(current);
			}
		} catch (CouponFacadeException | RequestNotFoundException e) {
			System.out.println(e.getMessage());
		}

	}
	private static void doCreateCustomer() {
		noExceptionsThrown = false;
		while (!noExceptionsThrown) {
			System.out.println("Create a new customer");
			Customer customer = new Customer();
			System.out.print("Please enter the customer's name: ");
			customer.setCustomerName(sc.nextLine());
			System.out.print("Please enter the customer's password: ");
			customer.setPassword(sc.nextLine());
			System.out.println("processing request...\n");
			try {
				facade.createCustomer(customer);
				System.out.println(customer + " has been created in the system");
				noExceptionsThrown = true;
			} catch (CouponFacadeException e) {
				System.out.println(e.getMessage());
				tryAgain();
			}

		}
	}

	private static void doUpdateCustomer() {
		noExceptionsThrown = false;
		while (!noExceptionsThrown) {
			System.out.println("Update customer");
			System.out.print("Please enter the customer's id: ");
			long custId = Long.parseLong(sc.nextLine());
			Customer customer;
			try {
				customer = facade.getCustomer(custId);
				System.out.println("Current customer details: " + customer);
				System.out.print("Please enter a new password: ");
				String password = (sc.nextLine());
				customer.setPassword(password);
				System.out.println("processing request...\n");
				facade.updateCustomer(customer);
				System.out.println(customer.getCustomerName() + "'s new password (" + password
						+ ") has been updated in the system");
				noExceptionsThrown = true;
			} catch (CouponFacadeException e) {
				System.out.println(e.getMessage());
				tryAgain();
			}
		}
	}

	private static void doDeleteCustomer() {
		noExceptionsThrown = false;
		while (!noExceptionsThrown) {
			System.out.println("Delete a customer");
			System.out.print("Please enter the customer's id: ");
			long customerId = (Long.parseLong(sc.nextLine()));
			System.out.println("processing request...\n");
			try {
				facade.deleteCustomer(customerId);
				System.out.println("customer with id " + customerId + " has been deleted");
				noExceptionsThrown = true;
			} catch (CouponFacadeException e) {
				System.out.println(e.getMessage());
				tryAgain();
			}

		}
	}

	private static void doGetCustomer() {
		noExceptionsThrown = false;
		while (!noExceptionsThrown) {
			System.out.println("Get customer detalis");
			System.out.print("Please enter the  customer's id: ");
			long custId = Long.parseLong(sc.nextLine());
			try {
				Customer customer = facade.getCustomer(custId);
				System.out.println("Processing request...\n");
				System.out.println("The customer you requested is: " + customer);
				noExceptionsThrown = true;
			} catch (CouponFacadeException e) {
				System.out.println(e.getMessage());
				tryAgain();
				;
			}
		}
	}

	private static void doGetCustomersList() {
		System.out.println("Processing request...\n");
		System.out.println("All Customers:");
			try {
				for (Customer curr : facade.getAllCustomers()) {
					System.out.println(curr);
				}
			} catch (CouponFacadeException e) {
				System.out.println(e.getMessage());
			}
		}
	}

