import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import org.junit.*;
import java.util.ArrayList;

public class MainTest {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	private ArrayList<Account> accounts;
	private ArrayList<Transaction> transactions;
	private ArrayList<Account> output;
	private BankAccountWriter baw;
	private TransactionHandler transHandler;

	boolean testResult;

	@After
	public void cleanUpStreams() {
		System.setOut(null);
		System.setErr(null);
	}

	@Before
	public void setUp() {
		accounts = new ArrayList<Account>();
		transactions = new ArrayList<Transaction>();
		output = new ArrayList<Account>();
		baw = new BankAccountWriter(accounts);
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
	}

	//tests the entire program
	@Test
	public void testWhole() {
		CurrentData data = new CurrentData();
		//get the accounts from the master accounts file
		data.getCurrentAccounts();
		//get the transactions from the transaction log
		data.getTransactions();


		//Process transactions that have been read in by currentData
		TransactionHandler transHandler = new TransactionHandler(data.accounts, data.transactions);
		//Create a new list to store the updated accounts
		ArrayList<Account> newAccounts;
		//process transactions and return the new accounts information
		newAccounts = transHandler.HandleTransactions();


		//create a BankAccountWriter to generate a new master and current
		//pass in the new accounts information that we created from the transaction handler
		BankAccountWriter bankWriter = new BankAccountWriter(newAccounts);
		//Write new master bank accounts file
		bankWriter.writeAccounts(true);
		//Write new current bank accounts file
		bankWriter.writeAccounts(false);

		if (outContent.toString().contains("Execution Complete")) {
			testResult = false;
		} else	{
			testResult = true;
		}

		assertTrue("fatal error could not complete", testResult);
	}
}
