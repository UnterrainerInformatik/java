/**************************************************************************
 * <pre>
 * 
 * Copyright (c) Unterrainer Informatik OG.
 * This source is subject to the Microsoft Public License.
 * 
 * See http://www.microsoft.com/opensource/licenses.mspx#Ms-PL.
 * All other rights reserved.
 * 
 * (In other words you may copy, use, change and redistribute it without
 * any restrictions except for not suing me because it broke something.)
 * 
 * THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY
 * KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A PARTICULAR
 * PURPOSE.
 * 
 * </pre>
 ***************************************************************************/

package at.prosigma.csv;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * The Class CsvReaderTest.
 * <p>
 * The unit-tests for the CsvReader.
 *
 * @author GEUNT
 * @since 20.09.2013
 */
public class CsvReaderTest {

	private CsvReader csvReader;
	private StringReader stringReader;
	private String newLine;

	/**
	 * Setup class.
	 */
	@BeforeClass
	public void setupClass() {
		newLine = System.getProperty("line.separator");
	}

	/**
	 * Setup method.
	 */
	@BeforeMethod
	public void setupMethod() {
	}

	/**
	 * Tear down method.
	 */
	@AfterMethod
	public void tearDownMethod() {
		if (csvReader != null) {
			try {
				csvReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (stringReader != null) {
			stringReader.close();
		}
	}

	/**
	 * Test empty fields.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testEmptyFields() throws IOException {
		stringReader = new StringReader("\"test\";test1;A 01;t;;");

		csvReader = new CsvReader(stringReader, ';', newLine, '"');
		Assert.assertNotNull(csvReader);

		List<String> row = csvReader.readRow();
		Assert.assertNotNull(row);

		Assert.assertNotNull(row);
		Assert.assertEquals(row, new ArrayList<String>(Arrays.asList("test", "test1", "A 01", "t", "", "")));
	}

	/**
	 * Test empty fields variant.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testEmptyFieldsVariant() throws IOException {
		stringReader = new StringReader("A;;A;T;;\r\nGreat");

		csvReader = new CsvReader(stringReader);
		Assert.assertNotNull(csvReader);

		List<String> row = csvReader.readRow();
		Assert.assertNotNull(row);
		Assert.assertEquals(row, new ArrayList<String>(Arrays.asList("A", "", "A", "T", "", "")));
	}

	/**
	 * Test special characters.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testSpecialCharacters() throws IOException {
		stringReader = new StringReader("����;\"!\"\"�$%&/()=?\"\r\n\"_:;'*\";<>.-,,#+");

		csvReader = new CsvReader(stringReader, ';', newLine, '"');
		Assert.assertNotNull(csvReader);

		List<String> row = csvReader.readRow();
		Assert.assertNotNull(row);
		Assert.assertEquals(row, new ArrayList<String>(Arrays.asList("����", "!\"�$%&/()=?")));

		row = csvReader.readRow();
		Assert.assertNotNull(row);
		Assert.assertEquals(row, new ArrayList<String>(Arrays.asList("_:;'*", "<>.-,,#+")));
	}

	/**
	 * Test escaped field delimiters.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testEscapedFieldDelimiters() throws IOException {
		stringReader = new StringReader("\"A\",01,\"A\"\" \"\"01\",\"t\"\"\",,\"\"");

		csvReader = new CsvReader(stringReader, ',', newLine, '"');
		Assert.assertNotNull(csvReader);

		List<String> row = csvReader.readRow();
		Assert.assertNotNull(row);

		Assert.assertEquals(row, new ArrayList<String>(Arrays.asList("A", "01", "A\" \"01", "t\"", "", "")));

		row = csvReader.readRow();
		Assert.assertNull(row);
	}

	/**
	 * Test quote special.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testQuoteSpecial() throws IOException {
		stringReader = new StringReader("9008390101544,���,\"Normal\" Test a string.,,");

		csvReader = new CsvReader(stringReader, ',', newLine, null);
		Assert.assertNotNull(csvReader);

		List<String> row = csvReader.readRow();
		Assert.assertNotNull(row);

		Assert.assertEquals(row,
				new ArrayList<String>(Arrays.asList("9008390101544", "���", "\"Normal\" Test a string.", "", "")));

		row = csvReader.readRow();
		Assert.assertNull(row);
	}

	/**
	 * Test read all rows.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testReadAllRows() throws IOException {
		stringReader = new StringReader("test;test1;test5\r\n\"test2\";test3\r\ntest6;;\"test8\"");

		csvReader = new CsvReader(stringReader);
		Assert.assertNotNull(csvReader);

		List<List<String>> data = csvReader.readAllRows();
		Assert.assertNotNull(data);

		ArrayList<ArrayList<String>> d = new ArrayList<ArrayList<String>>();

		d.add(new ArrayList<String>(Arrays.asList("test", "test1", "test5")));
		d.add(new ArrayList<String>(Arrays.asList("test2", "test3")));
		d.add(new ArrayList<String>(Arrays.asList("test6", "", "test8")));

		Assert.assertEquals(data, d);
	}

	/**
	 * Test three line file with line breaks and evil delimiters.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testThreeLineFileWithLineBreaksAndEvilDelimiters() throws IOException {
		stringReader = new StringReader("\"Great\";\"Totally\";\"Cool\"" + newLine + "\"Gr" + newLine
				+ "eat\";\"Totally\";Cool" + newLine + "Great;Totally;Cool");

		csvReader = new CsvReader(stringReader, ';', newLine, '"');
		Assert.assertNotNull(csvReader);

		List<String> row = csvReader.readRow();
		Assert.assertNotNull(row);
		Assert.assertEquals(row, new ArrayList<String>(Arrays.asList("Great", "Totally", "Cool")));

		row = csvReader.readRow();
		Assert.assertNotNull(row);
		Assert.assertEquals(row, new ArrayList<String>(Arrays.asList("Gr" + newLine + "eat", "Totally", "Cool")));

		row = csvReader.readRow();
		Assert.assertNotNull(row);
		Assert.assertEquals(row, new ArrayList<String>(Arrays.asList("Great", "Totally", "Cool")));

		row = csvReader.readRow();
		Assert.assertNull(row);
	}

	/**
	 * Test two lines with empty last line and previous field delimiter.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testTwoLinesWithEmptyLastLineAndPreviousFieldDelimiter() throws IOException {
		stringReader = new StringReader("Great;Totally;");
		csvReader = new CsvReader(stringReader, ';', newLine, null);

		List<String> row = csvReader.readRow();
		Assert.assertNotNull(row);
		Assert.assertEquals(row, new ArrayList<String>(Arrays.asList("Great", "Totally", "")));

		row = csvReader.readRow();
		Assert.assertNull(row);
	}

	/**
	 * Test buffer normal.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testBufferNormal() throws IOException {
		// 10 rows with 5 fields each containing 100 characters each.
		// = 5000 characters.
		final String FIELD_DATA = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789";
		String[] rowData = (String[]) (Arrays.asList(FIELD_DATA, FIELD_DATA, FIELD_DATA, FIELD_DATA, FIELD_DATA))
				.toArray();
		String[][] csvData = (String[][]) (Arrays.asList(rowData, rowData, rowData, rowData, rowData, rowData, rowData,
				rowData, rowData, rowData)).toArray();
		String csvToWrite = writeCsv(csvData, ';', newLine);
		stringReader = new StringReader(csvToWrite);

		csvReader = new CsvReader(stringReader, ';', newLine, '\"');
		// ReadAllRows calls ReadRow consecutively...
		List<List<String>> csv = csvReader.readAllRows();
		Assert.assertNotNull(csv);

		checkCsvValues(csv, csvData);

		// all following read-attempts should return null...
		List<String> row = csvReader.readRow();
		Assert.assertNull(row);

		List<List<String>> rows = csvReader.readAllRows();
		Assert.assertNull(rows);
	}

	/**
	 * Test buffer small.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testBufferSmall() throws IOException {
		// 10 rows with 5 fields each containing 100 characters each.
		// = 5000 characters.
		final String FIELD_DATA = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789";
		String[] rowData = (String[]) (Arrays.asList(FIELD_DATA, FIELD_DATA, FIELD_DATA, FIELD_DATA, FIELD_DATA))
				.toArray();
		String[][] csvData = (String[][]) (Arrays.asList(rowData, rowData, rowData, rowData, rowData, rowData, rowData,
				rowData, rowData, rowData)).toArray();
		String csvToWrite = writeCsv(csvData, ';', "tooLong" + newLine);
		stringReader = new StringReader(csvToWrite);

		csvReader = new CsvReader(stringReader, ';', "tooLong" + newLine, '\"', 1);
		// ChunkSize should be RowSeparator.length after this constructor call ("tooLong\r\n" = 9 characters).

		// ReadAllRows calls ReadRow consecutively...
		List<List<String>> csv = csvReader.readAllRows();
		Assert.assertNotNull(csv);

		checkCsvValues(csv, csvData);

		// all following read-attempts should return null...
		List<String> row = csvReader.readRow();
		Assert.assertNull(row);

		List<List<String>> rows = csvReader.readAllRows();
		Assert.assertNull(rows);
	}

	/**
	 * Test read row.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testReadRow() throws IOException {
		stringReader = new StringReader("test;test1;test2\r\ntest4;test5;test6");

		csvReader = new CsvReader(stringReader);
		Assert.assertNotNull(csvReader);

		List<String> row = csvReader.readRow();
		Assert.assertNotNull(row);
		Assert.assertEquals(row, new ArrayList<String>(Arrays.asList("test", "test1", "test2")));

		row = csvReader.readRow();
		Assert.assertNotNull(row);
		Assert.assertEquals(row, new ArrayList<String>(Arrays.asList("test4", "test5", "test6")));
	}

	/**
	 * Test breaks between delimiters.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testBreaksBetweenDelimiters() throws IOException {
		stringReader = new StringReader("test;\"test\r\nnewlineTest\";test2\r\ntest4;test5;test6");

		csvReader = new CsvReader(stringReader);
		Assert.assertNotNull(csvReader);

		List<String> row = csvReader.readRow();
		Assert.assertNotNull(row);
		Assert.assertEquals(row, new ArrayList<String>(Arrays.asList("test", "test\r\nnewlineTest", "test2")));

		row = csvReader.readRow();
		Assert.assertNotNull(row);
		Assert.assertEquals(row, new ArrayList<String>(Arrays.asList("test4", "test5", "test6")));
	}

	private static String writeCsv(String[][] csv, char columnSeparator, String rowSeparator) {
		String result = "";
		boolean isFirst = true;
		for (String[] row : csv) {
			if (isFirst) {
				isFirst = false;
			} else {
				result += rowSeparator;
			}
			List<String> stringList = Arrays.asList(row);
			result += writeRow(stringList, columnSeparator);
		}
		return result;
	}

	private static String writeRow(List<String> row, char columnSeparator) {
		String result = "";
		boolean isFirst = true;
		for (String s : row) {
			if (isFirst) {
				isFirst = false;
			} else {
				result += columnSeparator;
			}
			result += s;
		}
		return result;
	}

	private void checkCsvValues(List<List<String>> csvToCheck, String[][] rightCsvValues) {
		Assert.assertEquals(csvToCheck.size(), rightCsvValues.length);
		for (int i = 0; i < csvToCheck.size(); i++) {
			checkRowValues(csvToCheck.get(i), rightCsvValues[i]);
		}
	}

	private void checkRowValues(List<String> rowToCheck, String[] rightFieldValues) {
		Assert.assertEquals(rowToCheck.size(), rightFieldValues.length);
		for (int i = 0; i < rowToCheck.size(); i++) {
			Assert.assertEquals(rowToCheck.get(i), rightFieldValues[i]);
		}
	}
}
