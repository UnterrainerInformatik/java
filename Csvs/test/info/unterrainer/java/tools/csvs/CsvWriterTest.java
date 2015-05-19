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

package info.unterrainer.java.tools.csvs;

import java.io.IOException; 
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * The Class CsvWriterTest.
 * <p>
 * The unit-tests for the CsvWriter.
 * 
 * @author GEUNT
 * @since 20.09.2013
 */
public class CsvWriterTest {

    private CsvWriter    csvWriter;
    private StringWriter stringWriter;
    private String       newLine;

    /**
     * Setup method.
     */
    @Before
    public void setupMethod() {
        newLine = System.getProperty("line.separator");
        stringWriter = new StringWriter();
        csvWriter = new CsvWriter(stringWriter, ';', newLine, '"');
    }

    /**
     * Tear down method.
     */
    @After
    public void tearDownMethod() {
        if (csvWriter != null) {
            try {
                csvWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (stringWriter != null) {
            try {
                stringWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Test multiple rows with special characters as data.
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testMultiRowsWithSpecialCharactersAsData() throws IOException {
        List<List<String>> csv = new ArrayList<List<String>>();

        List<String> row = new ArrayList<String>();
        row.add("Great");
        row.add("Totally");
        row.add("This is a" + newLine + "break");
        row.add("");
        csv.add(row);

        row = new ArrayList<String>();
        row.add("");
        row.add("Gr;eat");
        row.add("Totally");
        csv.add(row);

        row = new ArrayList<String>();
        row.add("Great");
        row.add("ÜÖÄ\"ß");
        row.add("");
        row.add("Totally");
        csv.add(row);

        Assert.assertNotNull(csvWriter);
        csvWriter.writeAll(csv);
        csvWriter.close();

        Assert.assertEquals("Great;Totally;\"This is a"
                            + newLine
                            + "break\";"
                            + newLine
                            + ";\"Gr;eat\";Totally"
                            + newLine
                            + "Great;\"ÜÖÄ\"\"ß\";;Totally",
                stringWriter.toString());
    }

    /**
     * Test multi rows with special characters.
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testMultiRowsWithSpecialCharacters() throws IOException {
        Assert.assertNotNull(csvWriter);

        csvWriter.write("Great");
        csvWriter.write("Totally");
        csvWriter.write("This is a" + newLine + "break");
        // The next two lines do the same as calling csvWriter.writeLine("");
        csvWriter.write();
        csvWriter.writeLine();

        csvWriter.write();
        csvWriter.write("Gr;eat");
        csvWriter.writeLine("Totally");

        csvWriter.write("Great");
        csvWriter.write("ÜÖÄ\"ß");
        csvWriter.write("");
        csvWriter.write("Totally");

        csvWriter.close();

        Assert.assertEquals("Great;Totally;\"This is a"
                            + newLine
                            + "break\";"
                            + newLine
                            + ";\"Gr;eat\";Totally"
                            + newLine
                            + "Great;\"ÜÖÄ\"\"ß\";;Totally",
                stringWriter.toString());
    }

    /**
     * Test building methods minimal quoting.
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testBuildingMethodsMinimalQuoting() throws IOException {
        Assert.assertNotNull(csvWriter);

        csvWriter = new CsvWriter(stringWriter, ';', "\r\n", '\"', 10,
                QuotingBehavior.MINIMAL);

        csvWriter.write("Great");
        csvWriter.write("Totally");
        csvWriter.write("This is a\r\nbreak");
        csvWriter.writeLine("");

        csvWriter.write();
        csvWriter.write("Gr;eat");
        csvWriter.writeLine("Totally");

        csvWriter.write("Great");
        csvWriter.write("ÜÖÄ\"ß");
        csvWriter.write();
        csvWriter.write("Totally");

        csvWriter.close();

        Assert.assertEquals("Great;Totally;\"This is a\r\nbreak\";"
                            + "\r\n;\"Gr;eat\";Totally\r\n"
                            + "Great;\"ÜÖÄ\"\"ß\";;Totally",
                stringWriter.toString());
    }

    /**
     * Test building methods maximal quoting.
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testBuildingMethodsMaximalQuoting() throws IOException {
        csvWriter = new CsvWriter(stringWriter, ';', "\r\n", '\"', 10,
                QuotingBehavior.ALL);
        Assert.assertNotNull(csvWriter);

        csvWriter.write("Great");
        csvWriter.write("Totally");
        csvWriter.write("This is a\r\nbreak");
        csvWriter.writeLine("");

        csvWriter.write();
        csvWriter.write("Gr;eat");
        csvWriter.writeLine("Totally");

        csvWriter.write("Great");
        csvWriter.write("ÜÖÄ\"ß");
        csvWriter.write();
        csvWriter.write("Totally");

        csvWriter.close();

        Assert.assertEquals("\"Great\";\"Totally\";\"This is a\r\nbreak"
                            + "\";\"\"\r\n\"\";\"Gr;eat\";\"Totally\"\r\n\""
                            + "Great\";\"ÜÖÄ\"\"ß\";\"\";\"Totally\"",
                stringWriter.toString());
    }

    /**
     * Test appending null with write string.
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testAppendingNullWithWriteString() throws IOException {
        csvWriter = new CsvWriter(stringWriter, ';', "\r\n", '\"', 10,
                QuotingBehavior.ALL);
        Assert.assertNotNull(csvWriter);

        csvWriter.write("");
        csvWriter.write();
        csvWriter.write("3");
        csvWriter.writeLine();

        csvWriter.write("1");
        csvWriter.write((String) null);
        csvWriter.write("3");

        csvWriter.close();

        Assert.assertEquals("\"\";\"\";\"3\"\r\n\"1\";\"\";\"3\"",
                stringWriter.toString());
    }

    /**
     * Test appending null with write all.
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testAppendingNullWithWriteAll() throws IOException {
        List<List<String>> csv = new ArrayList<List<String>>();

        List<String> row = new ArrayList<String>();
        row.add("");
        row.add("");
        row.add("3");
        csv.add(row);

        row = new ArrayList<String>();
        row.add("1");
        row.add(null);
        row.add("3");
        csv.add(row);

        csvWriter = new CsvWriter(stringWriter, ';', "\r\n", '\"', 200,
                QuotingBehavior.ALL);
        Assert.assertNotNull(csvWriter);

        csvWriter.writeAll(csv);
        csvWriter.close();

        Assert.assertEquals("\"\";\"\";\"3\"\r\n\"1\";\"\";\"3\"",
                stringWriter.toString());
    }

    /**
     * Test write empty string.
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testWriteEmptyString() throws IOException {
        Assert.assertNotNull(csvWriter);

        List<String> ls = new ArrayList<String>();
        ls.add("test");
        ls.add("");
        ls.add("test2");
        ls.add("");
        ls.add("test3");

        csvWriter.write("test");
        csvWriter.write();
        csvWriter.write("test2");
        csvWriter.write("");
        csvWriter.write("test3");

        csvWriter.close();

        Assert.assertEquals(convertToSimpleCsvString(ls),
                stringWriter.toString());
    }

    /**
     * Test write single string.
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testWriteSingleString() throws IOException {
        Assert.assertNotNull(csvWriter);

        String str = "test";

        csvWriter.write(str);
        csvWriter.close();

        Assert.assertEquals(str, stringWriter.toString());
    }

    /**
     * Test write list of string.
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testWriteListOfString() throws IOException {
        Assert.assertNotNull(csvWriter);

        List<String> ls = new ArrayList<String>();
        ls.add("test");
        ls.add("test1");
        ls.add("test2");

        csvWriter.write(ls);
        csvWriter.close();

        Assert.assertEquals(convertToSimpleCsvString(ls),
                stringWriter.toString());
    }

    /**
     * Test write all.
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testWriteAll() throws IOException {
        Assert.assertNotNull(csvWriter);

        List<List<String>> data = new ArrayList<List<String>>();

        List<String> row = new ArrayList<String>();
        row.add("test");
        row.add("test1");
        row.add("test2");
        data.add(row);

        row = new ArrayList<String>();
        row.add("test3");
        row.add("üäöß!§$%&/()==?`´");
        row.add("test5");
        data.add(row);

        csvWriter.writeAll(data);
        csvWriter.close();

        Assert.assertEquals(convertAllToSimpleCsvString(data),
                stringWriter.toString());
    }

    /**
     * Test write line single string.
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testWriteLineSingleString() throws IOException {
        Assert.assertNotNull(csvWriter);

        String str = "test";

        csvWriter.writeLine(str);
        csvWriter.close();

        Assert.assertEquals(str + newLine, stringWriter.toString());
    }

    /**
     * Test write line list string.
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testWriteLineListString() throws IOException {
        Assert.assertNotNull(csvWriter);

        List<String> ls = new ArrayList<String>();
        ls.add("test");
        ls.add("test1");
        ls.add("test2");

        csvWriter.writeLine(ls);
        csvWriter.close();

        Assert.assertEquals(convertToSimpleCsvString(ls) + newLine,
                stringWriter.toString());
    }

    private String convertToSimpleCsvString(final List<String> ls) {
        String ret = "";
        for (int i = 0; i < ls.size(); i++) {
            ret += ls.get(i);
            if (i != ls.size() - 1) {
                ret += ";";
            }
        }
        return ret;
    }

    private String convertAllToSimpleCsvString(final List<List<String>> data) {
        boolean isFirst = true;
        String result = "";
        for (List<String> row : data) {
            if (isFirst) {
                isFirst = false;
            } else {
                result += newLine;
            }
            result += convertToSimpleCsvString(row);
        }
        return result;
    }
}
