package spanning.atm;


import java.io.Console;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AtmMachine {

	Map<Integer, Account> accounts = new HashMap<Integer, Account>();
	
	public void init(){
		int pin = 1234;
		BigDecimal startingCash = new BigDecimal(10);
		Account account = new Account("John", startingCash, pin);
		accounts.put(pin, account);
		
		pin = 5678;
		startingCash = new BigDecimal(100);
		account = new Account("Tim", startingCash, pin);
		accounts.put(pin, account);
		
		pin = 9999;
		startingCash = new BigDecimal(1000);
		account = new Account("Sarah", startingCash, pin);
		accounts.put(pin, account);
	}
	
	public Account getAccount(Integer pin){
		Account account = null;
		account = accounts.get(pin);				
		return account;
	}
}
