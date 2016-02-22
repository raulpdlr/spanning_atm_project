

import static org.junit.Assert.*;

import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import spanning.atm.Account;
import spanning.atm.AccountManager;
import spanning.atm.AccountManagerImpl;
import spanning.atm.BasicAccount;
import spanning.atm.AtmController;
import spanning.atm.AtmMachine;

/**
 * Set of tests to exercise the ATM system.
 * @author Raul
 */
public class AtmTest {
	AtmController atmController;
	AtmMachine atmMachine;
	int validPin;
	int invalidPin;
	Locale locale;
	NumberFormat numberFormat;
	BigDecimal startingCash = new BigDecimal(10);
	AccountManager accountManager;
	
	FileInputStream fileInputStream;
	String outputFile;
	PrintStream outputPrintStream;
	private static final Logger logger = Logger.getLogger(AtmTest.class.getName());
	
	private final static String INVALID_AMOUNT_PREPEND = "Invalid amount: ";
	
	@Before
	public void setUp() throws Exception {
		atmController = new AtmController();
		atmMachine = new AtmMachine();
		locale = Locale.US;
		numberFormat = NumberFormat.getCurrencyInstance(locale);
		
		validPin = 1234;
		invalidPin = 9999;
		int pin = validPin;
		BasicAccount account = new BasicAccount("Test Account", startingCash, pin);
		atmMachine.addAccount(pin, account);
		
		accountManager = new AccountManagerImpl();
		
		fileInputStream = new FileInputStream(new File("src" + File.separator + "test" + File.separator + 
							"resources" + File.separator + "input" +  File.separator +"input.txt"));
		
		outputFile = "src" + File.separator + "test" + File.separator + 
				"resources" + File.separator + "output" +  File.separator +"output.txt";
		outputPrintStream = new PrintStream(Paths.get(outputFile).toFile());
	}
	
	@Test
	public final void testGetAccountValidPin() {
		Account account = atmMachine.getAccount(validPin);
		if(account == null){
			fail("Was expecting to find an account");
		}
	}

	@Test
	public final void testGetAccountInvalidPin() {
		Account account = atmMachine.getAccount(invalidPin);
		if(account != null){
			fail("Was not expecting to find an account");
		}
	}

	@Test
	public final void testGetValidBalance() {
		Account account = atmMachine.getAccount(validPin);
			
		String balanceString = accountManager.getBalance(account);
		String expectedString = "Your balance is: " + numberFormat.format(startingCash);

		if(!balanceString.equals(expectedString)){
			fail("Get balance reports invalid amount in account.");
		}
	}	
	
	@Test
	public final void testDepositValidMoneyWholeDollar() {
		Account account = atmMachine.getAccount(validPin);
		String input = "10";

		BigDecimal depositAmount = new BigDecimal(input); 
		String depositResult = accountManager.depositMoney(input, account);
		
		BigDecimal expectedBalance = startingCash.add(depositAmount);		
		String expectedString = "Successfully deposited: " + numberFormat.format(depositAmount) + ", your balance is: " + numberFormat.format(expectedBalance);

		if(!depositResult.equals(expectedString)){
			fail("Failed deposit.");
		}
	}
	
	@Test
	public final void testDepositValidMoneyOnlyCents() {
		Account account = atmMachine.getAccount(validPin);
		String input = "0.05";
		
		BigDecimal depositAmount = new BigDecimal(input); 
		String depositResult = accountManager.depositMoney(input, account);
		
		BigDecimal expectedBalance = startingCash.add(depositAmount);
		String expectedString = "Successfully deposited: " + numberFormat.format(depositAmount) + ", your balance is: " + numberFormat.format(expectedBalance);

		if(!depositResult.equals(expectedString)){
			fail("Failed deposit.");
		}
	}
	
	@Test
	public final void testDepositValidMoneyDollarAndCents() {
		Account account = atmMachine.getAccount(validPin);
		String input = "1.23";
		
		BigDecimal depositAmount = new BigDecimal(input); 
		String depositResult = accountManager.depositMoney(input, account);
		
		BigDecimal expectedBalance = startingCash.add(depositAmount);		
		String expectedString = "Successfully deposited: " + numberFormat.format(depositAmount) + ", your balance is: " + numberFormat.format(expectedBalance);

		if(!depositResult.equals(expectedString)){
			fail("Failed deposit.");
		}
	}
	
	@Test
	public final void testDepositInvalidMoneyZero() {
		Account account = atmMachine.getAccount(validPin);
		String input = "0";
		
		BigDecimal depositAmount = new BigDecimal(input); 
		String depositResult = accountManager.depositMoney(input, account);

		String expectedString = INVALID_AMOUNT_PREPEND + depositAmount;

		if(!depositResult.equals(expectedString)){
			fail("Not expecting to pass.");
		}
	}
	
	@Test
	public final void testDepositInvalidMoneyBlank() {
		Account account = atmMachine.getAccount(validPin);
		String input = "";
		
		String depositResult = accountManager.depositMoney(input, account);

		String expectedString = INVALID_AMOUNT_PREPEND + input;

		if(!depositResult.equals(expectedString)){
			fail("Not expecting to pass.");
		}
	}	
	
	@Test
	public final void testDepositInvalidMoneyLessThanOneCent() {
		Account account = atmMachine.getAccount(validPin);
		String input = "0.001";
		
		BigDecimal depositAmount = new BigDecimal(input); 
		String depositResult = accountManager.depositMoney(input, account);

		String expectedString = INVALID_AMOUNT_PREPEND + depositAmount;

		if(!depositResult.equals(expectedString)){
			fail("Not expecting to pass.");
		}
	}
	
	@Test
	public final void testDepositInvalidMoneyNegativeAmount() {
		Account account = atmMachine.getAccount(validPin);
		String input = "-1";
		
		BigDecimal depositAmount = new BigDecimal(input); 
		String depositResult = accountManager.depositMoney(input, account);

		String expectedString = INVALID_AMOUNT_PREPEND + depositAmount;

		if(!depositResult.equals(expectedString)){
			fail("Wrong error message found.");
		}
	}	

	@Test
	public final void testWithdrawValidMoneyWholeDollar() {
		Account account = atmMachine.getAccount(validPin);
		String input = "1";
		
		BigDecimal withdrawAmount = new BigDecimal(input); 
		String withdrawResult = accountManager.withdrawMoney(input, account);
		
		BigDecimal expectedBalance = startingCash.subtract(withdrawAmount);		
		String expectedString = "Successfully withdrew: " + numberFormat.format(withdrawAmount) + ", your balance is: " + numberFormat.format(expectedBalance);

		if(!withdrawResult.equals(expectedString)){
			fail("Failed withdraw.");
		}
	}	

	@Test
	public final void testWithdrawValidMoneyOnlyCents() {
		Account account = atmMachine.getAccount(validPin);
		String input = ".05";
		
		BigDecimal withdrawAmount = new BigDecimal(input); 
		String withdrawResult = accountManager.withdrawMoney(input, account);
		
		BigDecimal expectedBalance = startingCash.subtract(withdrawAmount);
		String expectedString = "Successfully withdrew: " + numberFormat.format(withdrawAmount) + ", your balance is: " + numberFormat.format(expectedBalance);

		if(!withdrawResult.equals(expectedString)){
			fail("Failed withdraw.");
		}
	}	

	@Test
	public final void testWithdrawValidMoneyDollarAndCents() {
		Account account = atmMachine.getAccount(validPin);
		String input = "9.87";
		
		BigDecimal withdrawAmount = new BigDecimal(input); 
		String withdrawResult = accountManager.withdrawMoney(input, account);
		
		BigDecimal expectedBalance = startingCash.subtract(withdrawAmount);
		String expectedString = "Successfully withdrew: " + numberFormat.format(withdrawAmount) + ", your balance is: " + numberFormat.format(expectedBalance);

		if(!withdrawResult.equals(expectedString)){
			fail("Failed withdraw.");
		}
	}		

	@Test
	public final void testWithdrawInvalidMoneyZero() {
		Account account = atmMachine.getAccount(validPin);
		String input = "0";
		
		BigDecimal withdrawAmount = new BigDecimal(input); 
		String withdrawResult = accountManager.withdrawMoney(input, account);
		
		String expectedString = INVALID_AMOUNT_PREPEND + withdrawAmount;

		if(!withdrawResult.equals(expectedString)){
			fail("Wrong error message found.");
		}
	}

	@Test
	public final void testWithdrawInvalidMoneyBlank() {
		Account account = atmMachine.getAccount(validPin);
		String input = "";
		
		String withdrawResult = accountManager.withdrawMoney(input, account);
		
		String expectedString = INVALID_AMOUNT_PREPEND + input;

		if(!withdrawResult.equals(expectedString)){
			fail("Wrong error message found.");
		}
	}
	
	@Test
	public final void testWithdrawInvalidMoneyLessThanOneCent() {
		Account account = atmMachine.getAccount(validPin);
		String input = "0.001";
		
		BigDecimal withdrawAmount = new BigDecimal(input); 
		String withdrawResult = accountManager.withdrawMoney(input, account);
		
		String expectedString = INVALID_AMOUNT_PREPEND + withdrawAmount;

		if(!withdrawResult.equals(expectedString)){
			fail("Wrong error message found.");
		}
	}

	@Test
	public final void testWithdrawInvalidMoneyNegative() {
		Account account = atmMachine.getAccount(validPin);
		String input = "-1";
		
		BigDecimal withdrawAmount = new BigDecimal(input); 
		String withdrawResult = accountManager.withdrawMoney(input, account);
		
		String expectedString = INVALID_AMOUNT_PREPEND + withdrawAmount;

		if(!withdrawResult.equals(expectedString)){
			fail("Wrong error message found.");
		}
	}	

	@Test
	public final void testWithdrawTooMuchMoney() {
		Account account = atmMachine.getAccount(validPin);
		String input = "100";
		
		new BigDecimal(input); 
		String withdrawResult = accountManager.withdrawMoney(input, account);
		
		String expectedString = "Insufficient funds, account only has: " + numberFormat.format(startingCash);

		if(!withdrawResult.equals(expectedString)){
			fail("Wrong error message found.");
		}
	}
	
	static String readFile(String path, Charset encoding) throws IOException {
		  byte[] encoded = Files.readAllBytes(Paths.get(path));
		  return new String(encoded, encoding);
}

@Test
public final void testBasicConsoleInteraction() throws IOException{
	Account account = atmMachine.getAccount(validPin);
	AccountManager accountManager = new AccountManagerImpl();
	
	Console console = System.console();
	System.setIn(fileInputStream);
	System.setOut(outputPrintStream);
	
	accountManager.manageAccount(console, account);
	
	String outputResult = readFile(outputFile, Charset.forName("utf-8"));
	
	StringBuilder sb = new StringBuilder();
	sb.append("Hello ")
		.append(account.getName())
		.append(System.lineSeparator())
		.append("Actions:")
		.append(System.lineSeparator())
		.append("1: Get Balance")
		.append(System.lineSeparator())
		.append("2: Deposit Money")
		.append(System.lineSeparator())
		.append("3: Withdraw Money")
		.append(System.lineSeparator())
		.append("4: Exit")
		.append(System.lineSeparator())
		
		.append("Your balance is: $10.00")
		.append(System.lineSeparator())
		
		.append("Hello ")
		.append(account.getName())
		.append(System.lineSeparator())
		.append("Actions:")
		.append(System.lineSeparator())
		.append("1: Get Balance")
		.append(System.lineSeparator())
		.append("2: Deposit Money")
		.append(System.lineSeparator())
		.append("3: Withdraw Money")
		.append(System.lineSeparator())
		.append("4: Exit")
		.append(System.lineSeparator())
		
		.append("How much to deposit?")
		.append(System.lineSeparator())
		.append("Successfully deposited: $10.00, your balance is: $20.00")
		.append(System.lineSeparator())
		
		.append("Hello ")
		.append(account.getName())
		.append(System.lineSeparator())
		.append("Actions:")
		.append(System.lineSeparator())
		.append("1: Get Balance")
		.append(System.lineSeparator())
		.append("2: Deposit Money")
		.append(System.lineSeparator())
		.append("3: Withdraw Money")
		.append(System.lineSeparator())
		.append("4: Exit")
		.append(System.lineSeparator())
		
		.append("How much to withdraw?")
		.append(System.lineSeparator())
		.append("Successfully withdrew: $10.00, your balance is: $10.00")
		.append(System.lineSeparator())			
		
		.append("Hello ")
		.append(account.getName())
		.append(System.lineSeparator())
		.append("Actions:")
		.append(System.lineSeparator())
		.append("1: Get Balance")
		.append(System.lineSeparator())
		.append("2: Deposit Money")
		.append(System.lineSeparator())
		.append("3: Withdraw Money")
		.append(System.lineSeparator())
		.append("4: Exit")
		.append(System.lineSeparator())
		
		.append("Goodbye!")
		.append(System.lineSeparator());

	
	String outputExpectedResult = sb.toString();
	
	if(!outputResult.equals(outputExpectedResult)){
		fail("Did not get expected console output");
	}
}		
}
