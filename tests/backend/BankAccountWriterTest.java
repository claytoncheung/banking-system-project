import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import org.junit.Before;
import org.junit.After;
import java.util.ArrayList;

public class BankAccountWriterTest {


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
	public void BankAccountWriterErrors() {
		assertEquals("", errContent.toString());
	}

}
