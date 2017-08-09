package first_and_last_runs;

public class First_Run {

	public static void main(String[] args) {
		Creation.getinstance().doCreate();
		Creation.getinstance().doInitializefields();
		
		System.out.println("DataBase tables have been created");
		System.out.println("Fields initialized successfully");

	}

}
