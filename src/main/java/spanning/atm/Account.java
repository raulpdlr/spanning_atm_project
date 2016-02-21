package spanning.atm;


import java.io.Console;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Account {

	String name;
	BigDecimal accountBalance;
	int pin;
	Locale locale = Locale.US;
	private final NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
	private static final Logger log = Logger.getLogger(Account.class.getName());
	private final BigDecimal smallestDepositWithdrawAmount = new BigDecimal("0.01");
	
	public Account(String name, BigDecimal money, int pin){
		this.name = name;
		this.accountBalance = money;
		this.pin = pin;
	}
	
	public String getBalance(){
		String accountActionResult = "";
		accountActionResult ="Your balance is: " + numberFormat.format(accountBalance);
		return accountActionResult;
	}
	
	public synchronized String withdrawMoney(BigDecimal amount){
		String accountActionResult = "";
		
		// Cannot withdraw less than .01
		if(amount.compareTo(smallestDepositWithdrawAmount) == -1){
			accountActionResult = "Invalid amount to withdraw: $" + amount;
		} else if(accountBalance.compareTo(amount) == -1){
			accountActionResult = "Insufficient funds, account only has: " + numberFormat.format(accountBalance);
		} else{
			accountBalance = accountBalance.subtract(amount);
			accountActionResult = "Successfully withdrew: " + numberFormat.format(amount) + ", your balance is: " + numberFormat.format(accountBalance);
		}
		
		return accountActionResult;
	}

	public String depositMoney(BigDecimal amount){
		String accountActionResult = "";

		// Cannot deposit less than .01
		if(amount.compareTo(smallestDepositWithdrawAmount) == -1){
			accountActionResult = "Invalid amount to deposit: " + amount;
		} else{
			accountBalance = accountBalance.add(amount);
			accountActionResult = "Successfully deposited: " + numberFormat.format(amount) + ", your balance is: " + numberFormat.format(accountBalance);			
		}
		
		return accountActionResult;
	}
	
	public void manageAccount(Console console){
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
				input = console.readLine();
				
				String atmActionResult = "";
				
				switch(input){
					case "1":	
							atmActionResult = this.getBalance();
							System.out.println(atmActionResult);
							break;
					case "2":
							System.out.println("How much to deposit?");
							String depositAmountString = console.readLine();
							BigDecimal depositAmountBigDecimal = this.getBigDecimal(depositAmountString);
							if(depositAmountBigDecimal != null){
								atmActionResult = this.depositMoney(depositAmountBigDecimal);
								System.out.println(atmActionResult);
							}
							break;								
					case "3":
							System.out.println("How much to withdraw?");
							String withdrawalAmountString = console.readLine();
							BigDecimal withdrawalAmountBigDecimal = this.getBigDecimal(withdrawalAmountString);
							if(withdrawalAmountBigDecimal != null){
								atmActionResult = this.withdrawMoney(withdrawalAmountBigDecimal);
								System.out.println(atmActionResult);
							}
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
				log.log(Level.INFO, message);
				System.out.println(message);
			}
		} while(!exit);
	}
	
	/**
	 * 
	 * @param input
	 * @return
	 */
	private BigDecimal getBigDecimal(String input){
		BigDecimal result = null;
		try{
			result = new BigDecimal(input);
			// Dis-allow account interaction with money less than 1 cent.
			// IE, cannot deposit 1.001
			if(getNumberOfDecimalPlaces(result) > 2){
				String message = "Invalid amount: $" + input;
				System.out.println(message);
				result = null;
			}
		} catch(NumberFormatException e){
			String message = "Invalid amount: " + input;
			log.log(Level.WARNING, message);
			System.out.println(message);			
		}
		return result;
	}
	
	private int getNumberOfDecimalPlaces(BigDecimal bigDecimal) {
	    String string = bigDecimal.stripTrailingZeros().toPlainString();
	    int index = string.indexOf(".");
	    return index < 0 ? 0 : string.length() - index - 1;
	}
}
