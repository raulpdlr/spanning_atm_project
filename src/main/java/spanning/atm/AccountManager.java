package spanning.atm;

import java.io.Console;
import java.io.IOException;

public interface AccountManager {
	/**
	 * Function to enter the interactive management console for an account;
	 * @param console Console handler which the user can interact with for input
	 * @throws IOException
	 */
	public void manageAccount(Console console, Account account) throws IOException;
}
