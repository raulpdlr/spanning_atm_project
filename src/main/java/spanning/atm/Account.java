package spanning.atm;

import java.math.BigDecimal;
import java.util.Locale;

/**
 * Interface to define an ATM account and all the actions that can be done to it.
 * @author Raul
 *
 */
public abstract class Account {
	protected String name;
	protected BigDecimal accountBalance;
	protected int pin;
	protected Locale locale = Locale.US; // Defaulting to US for now
	
	public abstract String getName();
	public abstract BigDecimal getBalance();
	public abstract void setBalance(BigDecimal newBalance);
}