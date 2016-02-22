package spanning.atm;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Object which contains all the ATM user accounts
 * @author Raul
 */
public class AtmMachine {

	private Map<Integer, Account> accounts = new HashMap<Integer, Account>();
	static private FileHandler loggerFile;
	private static final Logger logger = Logger.getLogger(AtmMachine.class.getName());
	
	public void setupLogger(){
		try {
			loggerFile = new FileHandler("local_logger.txt");
			logger.addHandler(loggerFile);
			logger.setUseParentHandlers(false);
		} catch (SecurityException e) {
			throw new RuntimeException("Problems configuring logger : " + e.getMessage());
		} catch (IOException e) {
			throw new RuntimeException("Problems configuring logger : " + e.getMessage());
		}		
	}	
	
	public void init(){
		setupLogger();
		
		int pin = 1234;
		BigDecimal startingCash = new BigDecimal(10);
		BasicAccount account = new BasicAccount("John", startingCash, pin);
		account.setLogger(logger);
		accounts.put(pin, account);
		
		pin = 5678;
		startingCash = new BigDecimal(100);
		account = new BasicAccount("Tim", startingCash, pin);
		account.setLogger(logger);
		accounts.put(pin, account);
		
		pin = 9999;
		startingCash = new BigDecimal(1000);
		account = new BasicAccount("Sarah", startingCash, pin);
		account.setLogger(logger);
		accounts.put(pin, account);
	}
	
	public void addAccount(Integer pin, Account account){
		accounts.put(pin, account);
	}
	
	public Account getAccount(Integer pin){
		Account account = null;
		account = accounts.get(pin);				
		return account;
	}
}
