package spanning.atm;


import java.math.BigDecimal;

/**
 * Implementation class for Account which represents a rudimentary account.
 * @author Raul
 */
public class BasicAccount extends Account {
	
	/**
	 * Constructor to create new BasicAccount objects
	 * @param name Name of account holder
	 * @param money Amount of money to seed the account
	 * @param pin PIN access number to configure the account with
	 */
	public BasicAccount(String name, BigDecimal money, int pin){
		this.name = name;
		this.accountBalance = money;
		this.pin = pin;
	}
	
	public String getName(){
		return name;
	}
	
	public BigDecimal getBalance(){
		return accountBalance;
	}
	
	public void setBalance(BigDecimal newBalance){
		this.accountBalance = newBalance;
	}
}
