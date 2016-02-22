package spanning.atm;

import java.io.Console;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.logging.Level;

/**
 * Interface to define an ATM account and all the actions that can be done to it.
 * @author Raul
 *
 */
public interface Account {

	/**
	 * Function that returns a String reporting the account balance.
	 * @return
	 */
	public String getBalance();
	
	/**
	 * Function which will remove money from an account
	 * @param amount Amount of money to withdraw
	 * @return A String reporting the outcome of the request
	 */
	public String withdrawMoney(String amount);

	/**
	 * Function which will add money to the account
	 * @param amount Amount of money to deposit
	 * @return A String reporting the outcome of the request
	 */
	public String depositMoney(String amount);
	
	/**
	 * Function to enter the interactive management console.
	 * @param console Console handler which the user can interact with for input
	 * @throws IOException
	 */
	public void manageAccount(Console console) throws IOException;
	
	public String getName();
}
