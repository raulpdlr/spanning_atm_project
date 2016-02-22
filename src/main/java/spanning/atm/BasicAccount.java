package spanning.atm;


import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation class for Account which represents a rudimentary account.
 * @author Raul
 */
public class BasicAccount implements Account {

	String name;
	BigDecimal accountBalance;
	int pin;
	Locale locale = Locale.US;
	private final NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
	private final static BigDecimal smallestDepositWithdrawAmount = new BigDecimal("0.01");
	public final static String INVALID_AMOUNT_PREPEND = "Invalid amount: ";
	BufferedReader debugReader;
	private static Logger logger = null;
	
	public void setLogger(Logger logger){
		this.logger = logger;
	}
	
	/**
	 * Constructor to create new BasicAccount objects
	 * @param name Name of account holder
	 * @param money Amount of money to seed the account
	 * @param pin PIN access number to configure the account with
	 */
	public BasicAccount(String name, BigDecimal money, int pin){
		super();
		this.name = name;
		this.accountBalance = money;
		this.pin = pin;
	}
	
	public String getName(){
		return name;
	}
	
	/*
	 * (non-Javadoc)
	 * @see spanning.atm.Account#getBalance()
	 */
	public String getBalance(){
		String accountActionResult = "";
		accountActionResult = "Your balance is: " + numberFormat.format(accountBalance);
		return accountActionResult;
	}
	
	/*
	 * (non-Javadoc)
	 * @see spanning.atm.Account#withdrawMoney(java.lang.String)
	 */
	public synchronized String withdrawMoney(String amountString){
		String accountActionResult = "";
		BigDecimal amountBigDecimal = getBigDecimalFromString(amountString);
		
		if(amountBigDecimal == null){
			accountActionResult = INVALID_AMOUNT_PREPEND + amountString;
		} else{
			// Cannot withdraw less than .01
			if(amountBigDecimal.compareTo(smallestDepositWithdrawAmount) == -1){
				accountActionResult = INVALID_AMOUNT_PREPEND + amountBigDecimal;
			} else if(accountBalance.compareTo(amountBigDecimal) == -1){
				accountActionResult = "Insufficient funds, account only has: " + numberFormat.format(accountBalance);
			} else{
				accountBalance = accountBalance.subtract(amountBigDecimal);
				accountActionResult = "Successfully withdrew: " + numberFormat.format(amountBigDecimal) + ", your balance is: " + 
										numberFormat.format(accountBalance);
			}		
		}
		
		return accountActionResult;
	}

	/*
	 * (non-Javadoc)
	 * @see spanning.atm.Account#depositMoney(java.lang.String)
	 */
	public String depositMoney(String amountString){
		String accountActionResult = "";
		BigDecimal amountBigDecimal = getBigDecimalFromString(amountString);

		if(amountBigDecimal == null){
			accountActionResult = INVALID_AMOUNT_PREPEND + amountString;
		} else{
			// Cannot deposit less than .01
			if(amountBigDecimal.compareTo(smallestDepositWithdrawAmount) == -1){
				accountActionResult = INVALID_AMOUNT_PREPEND + amountBigDecimal;
			} else{
				accountBalance = accountBalance.add(amountBigDecimal);
				accountActionResult = "Successfully deposited: " + numberFormat.format(amountBigDecimal) + ", your balance is: " + 
										numberFormat.format(accountBalance);			
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
	public void manageAccount(Console console) throws IOException{
		boolean exit = false;
		do{
			System.out.println("Hello " + this.name);
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
							atmActionResult = this.getBalance();
							System.out.println(atmActionResult);
							break;
					case "2":
							System.out.println("How much to deposit?");
							String depositAmountString = this.readLine(console);
							atmActionResult = this.depositMoney(depositAmountString);
							System.out.println(atmActionResult);							
							break;								
					case "3":
							System.out.println("How much to withdraw?");
							String withdrawalAmountString = this.readLine(console);
							atmActionResult = this.withdrawMoney(withdrawalAmountString);
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
