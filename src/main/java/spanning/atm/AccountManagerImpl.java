package spanning.atm;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.InputMismatchException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class which is able to interact with and manage individual ATM accounts
 * @author Raul
 */
public class AccountManagerImpl implements AccountManager{
	private static final Logger logger = Logger.getLogger(AtmController.class.getName());
	static private FileHandler loggerFile;
	BufferedReader debugReader;
	
	public AccountManagerImpl(){
		try {
			loggerFile = new FileHandler("local_logger.txt");
			logger.addHandler(loggerFile);
			logger.setUseParentHandlers(false); // Suppress logging to console.
		} catch (SecurityException e) {
			throw new RuntimeException("Problems configuring logger : " + e.getMessage());
		} catch (IOException e) {
			throw new RuntimeException("Problems configuring logger : " + e.getMessage());
		}		
	}
	
	/*
	 * (non-Javadoc)
	 * @see spanning.atm.Account#manageAccount(java.io.Console)
	 */
	public void manageAccount(Console console, Account account) throws IOException{
		boolean exit = false;
		do{
			System.out.println("Hello " + account.getName());
			System.out.println("Actions:");
			System.out.println("1: Get Balance");
			System.out.println("2: Deposit Money");
			System.out.println("3: Withdraw Money");
			System.out.println("4: Exit");
			
			String input = null;
			try{
				
				input = this.readLine(console);
				
				String atmActionResult = "";
				
				switch(input){
					case "1":	
							atmActionResult = account.getBalance();
							System.out.println(atmActionResult);
							break;
					case "2":
							System.out.println("How much to deposit?");
							String depositAmountString = this.readLine(console);
							atmActionResult = account.depositMoney(depositAmountString);
							System.out.println(atmActionResult);							
							break;								
					case "3":
							System.out.println("How much to withdraw?");
							String withdrawalAmountString = this.readLine(console);
							atmActionResult = account.withdrawMoney(withdrawalAmountString);
							System.out.println(atmActionResult);
							break;							
					case "4":
							System.out.println("Goodbye!");
							exit = true;
							break;
					default:
							System.out.println("Not a valid number option, try again.");			
				}
			} catch(InputMismatchException e){
				String message = "Input must be a number 1-4";
				logger.log(Level.INFO, message);
				System.out.println(message);
			}
		} while(!exit);
	}
	
	/**
	 * Helper function to read input from the console
	 * @param console Console handler for input
	 * @return Next line read from console
	 * @throws IOException Exception thrown in the case that the debug BufferedRead cannot be configured.
	 */
	private String readLine(Console console) throws IOException {
	    if (console != null) {
	        return console.readLine();
	    } else{
	    	if(debugReader == null){
			    debugReader = new BufferedReader(new InputStreamReader(System.in));	    		
	    	} 

		    return debugReader.readLine();	    	
	    }
	}
}
