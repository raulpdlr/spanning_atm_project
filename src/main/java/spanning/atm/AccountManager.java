package spanning.atm;

import java.io.Console;
import java.io.IOException;

/**
 * Interface which will allow account management
 * @author Raul
 */
public interface AccountManager {
	
	/**
	 * Function that returns a String reporting the account balance.
	 * @return
	 */
	public String getBalance(Account account);
	
	/**
	 * Function which will remove money from an account
	 * @param amount Amount of money to withdraw
	 * @return A String reporting the outcome of the request
	 */
	public String withdrawMoney(String amount, Account account);

	/**
	 * Function which will add money to the account
	 * @param amount Amount of money to deposit
	 * @return A String reporting the outcome of the request
	 */
	public String depositMoney(String amount, Account account);
	
	/**
	 * Function to enter the interactive management console for an account;
	 * @param console Console handler which the user can interact with for input
	 * @throws IOException
	 */
	public void manageAccount(Console console, Account account) throws IOException;
}
