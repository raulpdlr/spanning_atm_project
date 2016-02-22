package spanning.atm;


import java.io.Console;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Object which holds input to the ATM system via static void main()
 * @author Raul
 */
public class AtmController {

	private static final Logger logger = Logger.getLogger(AtmController.class.getName());
	static private FileHandler loggerFile;

	private static final int shutdownPin = 111222333;
	
	/**
	 * Main entry into the ATM system.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			loggerFile = new FileHandler("local_logger.txt");
			logger.addHandler(loggerFile);
			logger.setUseParentHandlers(false); // Suppress logging to console.
		} catch (SecurityException e) {
			throw new RuntimeException("Problems configuring logger : " + e.getMessage());
		} catch (IOException e) {
			throw new RuntimeException("Problems configuring logger : " + e.getMessage());
		}

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
					logger.log(Level.WARNING, message);
					System.out.println(message);
				} else{
					Account account = null;
					account = atmMachine.getAccount(pinInt);
					 
					if(account == null){
						System.out.println("Account not found, try again: ");
					} else{
						try {
							account.manageAccount(console);
						} catch (IOException e) {
							logger.log(Level.SEVERE, "Unable to manage account with console");
						}
					}					
				}
			} catch(NumberFormatException nfe){
				String message = "Invalid input for PIN, only numbers allowed, cannot use PIN: " + pinString;
				logger.log(Level.WARNING, message);
				System.out.println(message);
			}		
		}
	}
}
