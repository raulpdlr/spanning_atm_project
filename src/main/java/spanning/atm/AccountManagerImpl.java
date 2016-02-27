package spanning.atm;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class which is able to interact with and manage individual ATM accounts
 * @author Raul
 */
public class AccountManagerImpl implements AccountManager{
	private static final Logger logger = Logger.getLogger(AtmController.class.getName());
	private final static BigDecimal smallestDepositWithdrawAmount = new BigDecimal("0.01");
	private final static String INVALID_AMOUNT_PREPEND = "Invalid amount: ";
	private	BufferedReader debugReader;
	
	/*
	 * (non-Javadoc)
	 * @see spanning.atm.AccountManager#getBalance(spanning.atm.Account)
	 */
	public String getBalance(Account account){
		String accountActionResult = "";
		accountActionResult = "Your balance is: " + account.getNumberFormat().format(account.accountBalance);
		return accountActionResult;
	}
	
	/*
	 * (non-Javadoc)
	 * @see spanning.atm.AccountManager#withdrawMoney(java.lang.String, spanning.atm.Account)
	 */
	public synchronized String withdrawMoney(String amountString, Account account){
		String accountActionResult = "";
		BigDecimal amountBigDecimal = getBigDecimalFromString(amountString);
		
		if(amountBigDecimal == null){
			accountActionResult = INVALID_AMOUNT_PREPEND + amountString;
		} else{
			// Cannot withdraw less than .01
			if(amountBigDecimal.compareTo(smallestDepositWithdrawAmount) == -1){
				accountActionResult = INVALID_AMOUNT_PREPEND + amountBigDecimal;
			} else if(account.getBalance().compareTo(amountBigDecimal) == -1){
				accountActionResult = "Insufficient funds, account only has: " + account.getNumberFormat().format(account.getBalance());
			} else{
				BigDecimal newBalance = account.getBalance().subtract(amountBigDecimal);
				account.setBalance(newBalance);
				accountActionResult = "Successfully withdrew: " + account.getNumberFormat().format(amountBigDecimal) + ", your balance is: " + 
						account.getNumberFormat().format(account.getBalance());
			}		
		}
		
		return accountActionResult;
	}

	/*
	 * (non-Javadoc)
	 * @see spanning.atm.AccountManager#depositMoney(java.lang.String, spanning.atm.Account)
	 */
	public String depositMoney(String amountString, Account account){
		String accountActionResult = "";
		BigDecimal amountBigDecimal = getBigDecimalFromString(amountString);

		if(amountBigDecimal == null){
			accountActionResult = INVALID_AMOUNT_PREPEND + amountString;
		} else{
			// Cannot deposit less than .01
			if(amountBigDecimal.compareTo(smallestDepositWithdrawAmount) == -1){
				accountActionResult = INVALID_AMOUNT_PREPEND + amountBigDecimal;
			} else{
				BigDecimal newBalance = account.getBalance().add(amountBigDecimal);
				account.setBalance(newBalance);
				accountActionResult = "Successfully deposited: " + account.getNumberFormat().format(amountBigDecimal) + ", your balance is: " + 
						account.getNumberFormat().format(account.getBalance());			
			}			
		}
		
		return accountActionResult;
	}
	
	/**
	 * Helper function to transform the user entered String into a BigDecimal
	 * @param input String which represents a number
	 * @return The converted BigDecimal number of the input or null if invalid.
	 */
	private static BigDecimal getBigDecimalFromString(String input){
		BigDecimal result = null;
		try{
			result = new BigDecimal(input);
		} catch(NumberFormatException e){
			String message = INVALID_AMOUNT_PREPEND + input;
			logger.log(Level.WARNING, message);
		}
		return result;
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
							atmActionResult = this.getBalance(account);
							System.out.println(atmActionResult);
							break;
					case "2":
							System.out.println("How much to deposit?");
							String depositAmountString = this.readLine(console);
							atmActionResult = this.depositMoney(depositAmountString, account);
							System.out.println(atmActionResult);							
							break;								
					case "3":
							System.out.println("How much to withdraw?");
							String withdrawalAmountString = this.readLine(console);
							atmActionResult = this.withdrawMoney(withdrawalAmountString, account);
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
