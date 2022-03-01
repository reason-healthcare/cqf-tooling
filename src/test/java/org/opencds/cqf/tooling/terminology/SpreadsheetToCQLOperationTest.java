package org.opencds.cqf.tooling.terminology;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.junit.Test;

public class SpreadsheetToCQLOperationTest {

	// replace with your location for cqf-tooling
	String mydir = "/Users/pranav.mechineni/Desktop/final_git/";

	String input_xlsx = mydir
			+ "cqf-tooling/src/test/resources/org/opencds/cqf/tooling/testfiles/SpreadsheetToCQLOperation/CQLv151ChangesApplied.xlsx";
	String output_cql_dir = mydir
			+ "cqf-tooling/src/test/resources/org/opencds/cqf/tooling/testfiles/SpreadsheetToCQLOperation/generated/";
	String expected_output = mydir
			+ "cqf-tooling/src/test/resources/org/opencds/cqf/tooling/testfiles/SpreadsheetToCQLOperation/CQLv151ChangesApplied.cql";

	@Test
	public void generate_CQL_test() {
		SpreadsheetToCQLOperation tester = new SpreadsheetToCQLOperation();

		String[] arguments = new String[3];
		arguments[0] = "-SpreadsheetToCQL";
		arguments[1] = "-pts=" + input_xlsx;
		arguments[2] = "-op=" + output_cql_dir;

		try {
			tester.execute(arguments);
		} catch (Exception e) {
			fail("Should not be throwing exceptions");
		} finally {
			assertTrue(true);
		}

	}

	@Test
	public void compare_output_test() throws IOException {

		BufferedReader reader1 = new BufferedReader(new FileReader(output_cql_dir + "CQLv151ChangesApplied.cql"));
		BufferedReader reader2 = new BufferedReader(new FileReader(expected_output));
		// skip past generated time so we only compare everything in CQL format
		for (int i = 0; i <= 5; i++) {
			reader1.readLine();
			reader2.readLine();
		}

		String line1 = reader1.readLine();
		String line2 = reader2.readLine();

		boolean isEqual = true;

		int curr_line = 1;

		while (line1 != null || line2 != null) {
			if (line1 == null || line2 == null) {
				isEqual = false;

				break;
			} else if (!line1.equals(line2)) {
				isEqual = false;

				break;
			}

			line1 = reader1.readLine();

			line2 = reader2.readLine();

			curr_line++;
		}

		if (isEqual) {
			System.out.println("Both files are the same.");
		} else {
			System.out.println("Both files have different text on line " + curr_line);

			System.out.println("File1 has " + line1 + " and File2 has " + line2 + " at line " + curr_line);
		}

		reader1.close();

		reader2.close();

		assertEquals(true, isEqual);
	}

}
