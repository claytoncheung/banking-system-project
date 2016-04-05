package tests.backend;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import org.junit.Before;
import org.junit.After;
import java.util.ArrayList;
import backend.Account;

public class AccountTest {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	@Before
	public void setUpStreams() {
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
	}

	@After
	public void cleanUpStreams() {
		System.setOut(null);
		System.setErr(null);
	}

	@Test
	public void testAccountCreation() {
		Account testAcc = new Account(0, "test", true, 1000.00, 0, false);
		assertEquals("", errContent.toString());
	}

	// @Test
	// public void testWillAlwaysFail() {
	//     fail("An error message");
	// }
	
	@Test
	//Tests the toString() function
	public void testToString() {
		
		//Condition coverage: master, isActive, isStudent == true
		String expected = "00000 Test Account         A 01000.00 0000 S";
		Account testAcc1 = new Account(0, "Test Account", true, 1000.00, 0, true);
		String out = testAcc1.toString(true);
		assertEquals("Not Equal", expected, out);
		
		//Condition coverage: isActive == false
		expected = "00000 Test Account         D 01000.00 0000 S";
		testAcc1 = new Account(0, "Test Account", false, 1000.00, 0, true);
		out = testAcc1.toString(true);
		assertEquals("Not Disabled", expected, out);
		
		//Condition coverage: isStudent == false
		expected = "00000 Test Account         A 01000.00 0000 N";
		testAcc1 = new Account(0, "Test Account", true, 1000.00, 0, false);
		out = testAcc1.toString(true);
		assertEquals("Is Student", expected, out);
		
		//Condition coverage: master == false
		expected = "00000 Test Account         A 01000.00 N";
		testAcc1 = new Account(0, "Test Account", true, 1000.00, 0, false);
		out = testAcc1.toString(false);
		assertEquals("Not Master", expected, out);
	}
}
