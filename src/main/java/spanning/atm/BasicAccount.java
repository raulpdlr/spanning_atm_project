package spanning.atm;


import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation class for Account which represents a rudimentary account.
 * @author Raul
 */
public class BasicAccount implements Account {

	public final static String INVALID_AMOUNT_PREPEND = "Invalid amount: ";
	
	private String name;
	private BigDecimal accountBalance;
	private int pin;
	private Locale locale = Locale.US;
	private final NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
	private final static BigDecimal smallestDepositWithdrawAmount = new BigDecimal("0.01");
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
	

}
