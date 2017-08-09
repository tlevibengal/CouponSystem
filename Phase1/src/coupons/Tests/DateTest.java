package coupons.Tests;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTest {

	public static void main(String[] args) throws ParseException {

		
		  //create object of SimpleDateFormat class with custom format
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	   

	      //parse string containing specified format into date object
		Date date = sdf.parse("25/02/2017");
		System.out.println(date);
		System.out.println(date.getTime());
		java.sql.Date d1 = new java.sql.Date(date.getTime());
		System.out.println(d1);
		System.out.println(new Date());

		System.out.println("\n=========== Coupon Types ================");
		System.out.print("(1) Food & Gourmet(FOOD)");
		System.out.print("     (2) House & Home(HOME)");
		System.out.print("   (3) Sporting Goods(SPORTS)");
		System.out.println("  (4) Travel(TRAVEL)");
		System.out.print("(5) Beauty products(BEAUTY)");
		System.out.print("  (6) Toys & Games(GAMES)");
		System.out.print("  (7) Clothing(FASHION)");
		System.out.print("       (8) Computers,Laptops & Tablets(ELECTRONICS)");
		System.out.println();

		System.out.print("\nWould you like to try again?");
		System.out.print("  (1) Yes ");
		System.out.println("(2) No ");

}
		
}
