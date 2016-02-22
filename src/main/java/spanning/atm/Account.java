package spanning.atm;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Interface to define an ATM account
 * @author Raul
 *
 */
public abstract class Account {
	protected String name;
	protected BigDecimal accountBalance;
	protected int pin;
	protected Locale locale = Locale.US; // Defaulting to US for now
	protected final NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
	
	public abstract String getName();
	public abstract BigDecimal getBalance();
	public abstract void setBalance(BigDecimal newBalance);
	public NumberFormat getNumberFormat(){
		return numberFormat;
	}
}