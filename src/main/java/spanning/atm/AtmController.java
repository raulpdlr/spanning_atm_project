package spanning.atm;


import java.io.Console;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AtmController {

	boolean ideDebug = true;
	private static final Logger log = Logger.getLogger(AtmController.class.getName());
	private static final int shutdownPin = 111222333;
	
	public static void main(String[] args) {
		AtmMachine atmMachine = new AtmMachine();
		atmMachine.init();
		
		Console console = System.console();
		if (console == null) {
		    System.out.println("No console: non-interactive mode!");
		    System.exit(0);
		}
		boolean shutdown = false;
		
		while(!shutdown){
			System.out.println("Enter your PIN to access your account: ");			
			String pinString = new String(console.readPassword());
			int pinInt;
			
			try{
				pinInt = Integer.parseInt(pinString);
				
				if(pinInt == shutdownPin){
					shutdown = true;
					String message = "Shutting down ATM machine";
					log.log(Level.WARNING, message);
					System.out.println(message);
				} else{
					Account account = null;
					account = atmMachine.getAccount(pinInt);
					 
					if(account == null){
						System.out.println("Account not found, try again: ");
					} else{
						account.manageAccount(console);
					}					
				}
			} catch(NumberFormatException nfe){
				String message = "Invalid input for PIN, only numbers allowed, cannot use PIN: " + pinString;
				log.log(Level.WARNING, message);
				System.out.println(message);
			}			
		}
	}
}
