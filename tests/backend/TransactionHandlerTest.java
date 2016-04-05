import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import org.junit.*;
import java.util.ArrayList;

public class TransactionHandlerTest {

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

	//attempts to use a unknown transaction
	@Test
	public void testUnknownTrans() {
		Account acc1 = new Account(00000, "acc1", true, 1000.00, 1, false);
		accounts.add(acc1);

		Transaction unknownTrans = new Transaction(999, "acc1", 00000, 0.00, "");
		transactions.add(unknownTrans);

		transHandler = new TransactionHandler(accounts, transactions);

		output = transHandler.HandleTransactions();

		if (outContent.toString().equals("Unknown Transaction Type???")) {
			testResult = false;
		} else {
			testResult = true;
		}

		assertTrue("attempts to use unknown transaction type", testResult);
	}

	//attempts to withdraw $50
	@Test
	public void testWithdraw() {
		Account acc1 = new Account(00000, "acc1", true, 1000.00, 0, false);;
		accounts.add(acc1);

		transactions.add(0, new Transaction(10, "acc1", 00000, 0, ""));
		transactions.add(1, new Transaction(01, "acc1", 00000, 50.00, ""));
		transactions.add(2, new Transaction(00, "acc1", 00000, 0, ""));

		transHandler = new TransactionHandler(accounts, transactions);
		output = transHandler.HandleTransactions();

		ArrayList<Account> expected = new ArrayList();
		expected.add(0, new Account(00000, "acc1", true, 949.90, 0002, false));

		if (outContent.toString().contains("Withdrawl Successful")) {
			testResult = true;
		} else {
			testResult = false;
		}

		assertTrue("fail withdrawing $50 + fee", testResult);
	}

	//attempts to transfer money from the first account to the second account
	@Test
	public void testTransfer() {
		Account acc1 = new Account(00000, "acc1", true, 1000.00, 0, false);
		Account acc2 = new Account(00001, "acc2", true, 1000.00, 0, false);
		accounts.add(acc1);
		accounts.add(acc2);

		transactions.add(0, new Transaction(10, "acc1", 00000, 0, ""));
		transactions.add(1, new Transaction(02, "acc1", 00000, 200.00, ""));
		transactions.add(2, new Transaction(02, "acc2", 00001, 200.00, ""));
		transactions.add(3, new Transaction(00, "acc1", 00000, 0, ""));

		transHandler = new TransactionHandler(accounts, transactions);
		output = transHandler.HandleTransactions();

		// ArrayList<Account> expected = new ArrayList();
		// expected.add(0, new Account(00000, "acc1", true, 949.90, 0002, false));
		// expected.add(1, new Account(00001, "acc2", true, 1050.00, 0002, false));

		if (outContent.toString().contains("Money Transfered")) {
			testResult = true;
		} else {
			testResult = false;
		}

		assertTrue("money not transfered", testResult);
	}

	//attempts to pay $100 to CC
	@Test
	public void testPayBill() {
		Account acc1 = new Account(00000, "acc1", true, 1000.00, 0, false);
		accounts.add(acc1);

		transactions.add(0, new Transaction(10, "acc1", 00000, 0, ""));
		transactions.add(1, new Transaction(03, "acc1", 00000, 100.00, "CC"));
		transactions.add(2, new Transaction(00, "acc1", 00000, 0, ""));

		transHandler = new TransactionHandler(accounts, transactions);
		output = transHandler.HandleTransactions();

		if (outContent.toString().contains("Bills Paid")) {
			testResult = true;
		} else {
			testResult = false;
		}

		assertTrue("unable to pay bills", testResult);
	}

	//attempts to deposit money into account
	@Test
	public void testDeposit() {
		Account acc1 = new Account(00000, "acc1", true, 1000.00, 0, false);
		accounts.add(acc1);

		transactions.add(0, new Transaction(10, "acc1", 00000, 0, ""));
		transactions.add(1, new Transaction(04, "acc1", 00000, 100.00, ""));
		transactions.add(2, new Transaction(00, "acc1", 00000, 0, ""));

		transHandler = new TransactionHandler(accounts, transactions);
		output = transHandler.HandleTransactions();

		if (outContent.toString().contains("Deposit Successful")) {
			testResult = true;
		} else {
			testResult = false;
		}

		assertTrue("unable to deposit money", testResult);
	}

	//attempts to create an account as an admin
	@Test
	public void testAdminCreate() {
		Account acc1 = new Account(00000, "ADMIN", true, 1000.00, 0, false);
		accounts.add(acc1);

		transactions.add(0, new Transaction(10, "ADMIN", 00000, 0, "A"));
		transactions.add(1, new Transaction(05, "new acc", 00001, 100.00, "A"));
		transactions.add(2, new Transaction(00, "ADMIN", 00000, 0, "A"));

		transHandler = new TransactionHandler(accounts, transactions);
		output = transHandler.HandleTransactions();

		if (outContent.toString().contains("Account Created")) {
			testResult = true;
		} else {
			testResult = false;
		}

		assertTrue("unable to create account as admin", testResult);
	}

	//attempts to delete an account as an admin
	@Test
	public void testAdminDelete() {
		Account acc1 = new Account(00000, "ADMIN", true, 1000.00, 0, false);
		Account acc2 = new Account(00001, "temp", true, 1000.00, 0, false);
		accounts.add(acc1);
		accounts.add(acc2);

		transactions.add(0, new Transaction(10, "ADMIN", 00000, 0, "A"));
		transactions.add(1, new Transaction(06, "temp", 00001, 0, "A"));
		transactions.add(2, new Transaction(00, "ADMIN", 00000, 0, "A"));

		transHandler = new TransactionHandler(accounts, transactions);
		output = transHandler.HandleTransactions();

		if (outContent.toString().contains("Account Deleted")) {
			testResult = true;
		} else {
			testResult = false;
		}

		assertTrue("unable to delete account as admin", testResult);
	}

	//attempts to disable an account as an admin
	@Test
	public void testAdminDisable() {
		Account acc1 = new Account(00000, "ADMIN", true, 1000.00, 0, false);
		Account acc2 = new Account(00001, "temp", true, 1000.00, 0, false);
		accounts.add(acc1);
		accounts.add(acc2);

		transactions.add(0, new Transaction(10, "ADMIN", 00000, 0, "A"));
		transactions.add(1, new Transaction(07, "temp", 00001, 0, "A"));
		transactions.add(2, new Transaction(00, "ADMIN", 00000, 0, "A"));

		transHandler = new TransactionHandler(accounts, transactions);
		output = transHandler.HandleTransactions();

		if (outContent.toString().contains("Account Disabled")) {
			testResult = true;
		} else {
			testResult = false;
		}

		assertTrue("unable to disable account as admin", testResult);
	}

	//attempts to change plan on account as an admin
	@Test
	public void testAdminChangePlan() {
		Account acc1 = new Account(00000, "ADMIN", true, 1000.00, 0, false);
		Account acc2 = new Account(00001, "temp", true, 1000.00, 0, false);
		accounts.add(acc1);
		accounts.add(acc2);

		transactions.add(0, new Transaction(10, "ADMIN", 00000, 0, "A"));
		transactions.add(1, new Transaction(8, "temp", 00001, 0, "A"));
		transactions.add(2, new Transaction(00, "ADMIN", 00000, 0, "A"));

		transHandler = new TransactionHandler(accounts, transactions);
		output = transHandler.HandleTransactions();

		if (outContent.toString().contains("Plan Changed")) {
			testResult = true;
		} else {
			testResult = false;
		}

		assertTrue("unable to change plan of account as admin", testResult);
	}

	//attempts to enable an account as an admin
	@Test
	public void testAdminEnable() {
		Account acc1 = new Account(00000, "ADMIN", true, 1000.00, 0, false);
		Account acc2 = new Account(00001, "temp", false, 1000.00, 0, false);
		accounts.add(acc1);
		accounts.add(acc2);

		transactions.add(0, new Transaction(10, "ADMIN", 00000, 0, "A"));
		transactions.add(1, new Transaction(9, "temp", 00001, 0, "A"));
		transactions.add(2, new Transaction(00, "ADMIN", 00000, 0, "A"));

		transHandler = new TransactionHandler(accounts, transactions);
		output = transHandler.HandleTransactions();

		if (outContent.toString().contains("Account Enabled")) {
			testResult = true;
		} else {
			testResult = false;
		}

		assertTrue("unable to enable account as admin", testResult);
	}

	//attempts to log in to an admin account
	@Test
	public void testLoginAdminAccExist() {
		Account acc1 = new Account(00000, "ADMIN", true, 1000.00, 0, false);
		accounts.add(acc1);

		transactions.add(0, new Transaction(10, "ADMIN", 00000, 0, "A"));
		transactions.add(1, new Transaction(00, "ADMIN", 00000, 0, "A"));

		transHandler = new TransactionHandler(accounts, transactions);
		output = transHandler.HandleTransactions();

		if (outContent.toString().contains("Session Started Admin")) {
			testResult = true;
		} else {
			testResult = false;
		}

		assertTrue("unable to find that admin account", testResult);
	}

	//attempts to log in to an standard account
	@Test
	public void testLoginStandardAccExist() {
		Account acc1 = new Account(00000, "acc1", true, 1000.00, 0, false);
		accounts.add(acc1);

		transactions.add(0, new Transaction(10, "acc1", 00000, 0, ""));
		transactions.add(1, new Transaction(00, "acc1", 00000, 0, ""));

		transHandler = new TransactionHandler(accounts, transactions);
		output = transHandler.HandleTransactions();

		if (outContent.toString().contains("Session Started Standard")) {
			testResult = true;
		} else {
			testResult = false;
		}
		assertTrue("unable to find that standard account", testResult);
	}

	//attempts to charge non students rate to a transaction
	@Test
	public void testChargesNonStudent() {
		Account acc1 = new Account(00000, "acc1", true, 1000.00, 0, false);
		accounts.add(acc1);

		transactions.add(0, new Transaction(10, "acc1", 00000, 0, ""));
		transactions.add(1, new Transaction(01, "acc1", 00000, 50.00, ""));
		transactions.add(2, new Transaction(00, "acc1", 00000, 0, ""));

		transHandler = new TransactionHandler(accounts, transactions);
		output = transHandler.HandleTransactions();

		if (acc1.currentBalance == 949.90) {
			testResult = true;
		} else {
			testResult = false;
		}

		assertTrue("can not withdraw transaction charge (non student)", testResult);
	}

	//attempts to charge student rate to a transaction
	@Test
	public void testChargesStudent() {
		Account acc1 = new Account(00000, "acc1", true, 1000.00, 0, true);
		accounts.add(acc1);

		transactions.add(0, new Transaction(10, "acc1", 00000, 0, ""));
		transactions.add(1, new Transaction(01, "acc1", 00000, 50.00, ""));
		transactions.add(2, new Transaction(00, "acc1", 00000, 0, ""));

		transHandler = new TransactionHandler(accounts, transactions);
		output = transHandler.HandleTransactions();

		if (acc1.currentBalance == 949.95) {
			testResult = true;
		} else {
			testResult = false;
		}

		assertTrue("can not withdraw transaction charge (student)", testResult);
	}
}
