import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import org.junit.*;
import java.util.ArrayList;

public class TransactionTest {

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

	//tests the .toString method in transactionHandler
	@Test
	public void testToString() {
		Account acc1 = new Account(00000, "acc1", true, 0, 0, false);
		accounts.add(acc1);

		Transaction login = new Transaction(10, "acc1", 00000, 0.00, "");
		transactions.add(login);

		transHandler = new TransactionHandler(accounts, transactions);

		output = transHandler.HandleTransactions();

		if (login.toString().contains("00000_acc1_________________A_00000.00_0000_N")) {
			testResult = false;
		} else	{
			testResult = true;
		}

		assertTrue("did not write transaction properly", testResult);
	}
}
