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

package info.unterrainer.java.tools.csvtools;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import info.unterrainer.java.tools.utils.ExtensionMethods;
import lombok.Builder;
import lombok.experimental.ExtensionMethod;

/**
 * This data-structure represents a comma-separated-values file. It helps in dealing with such files and delivers various manipulation routines.
 */
@ExtensionMethod(ExtensionMethods.class)
public class CsvWriter extends CsvBase {

	private final Writer stringWriter;

	/**
	 * The default chunk size (bufferSize / 2 - DEFAULT_ROW_SEPARATOR).
	 */
	private final static int DEFAULT_CHUNK_SIZE = 1;

	/**
	 * The internal buffer that holds the data to be read or to be written. Default value is the chunk size + the length of the default field delimiter.
	 */
	private char[] buffer = new char[DEFAULT_CHUNK_SIZE];

	/**
	 * The definitive chunkSize preset with the default value.
	 */
	private int chunkSize = DEFAULT_CHUNK_SIZE;

	private int bufferCount;
	private int numberOfUnusedBufferCharacters;

	/**
	 * Boolean variable that tells the program if it is necessary to insert a columnSeparator or not.
	 */
	private boolean isFirstFieldInRow = true;

	/**
	 * Tells the program if the fieldDelimiter is initialized (set) or not to execute the initializations only once.
	 */
	private boolean isInitialized = false;

	/**
	 * Is the fieldDelimiter that is really used (string.Empty if it was null).
	 */
	private String usedFieldDelimiter = "";

	/**
	 * Avoids the problem of multiple concatenation of an escaped fieldDelimiter by doing it once.
	 */
	private String doubleFieldDelimiter = "";

	/**
	 * Tells the program to always set quotes (fieldDelimiters) or only if necessary.
	 */
	private QuotingBehavior quotingBehavior = QuotingBehavior.MINIMAL;

	/**
	 * Initializes a new instance of the CsvWriter class. Call close() on it before moving on in order to flush and close the underlying reader. If you don't do
	 * this, the garbage collector will do that for you at an undefined point in time.
	 *
	 * @param stringWriter
	 *            The StringWriter you want the CsvWriter to attach to.
	 */
	public CsvWriter(final StringWriter stringWriter) {
		this.stringWriter = stringWriter;
	}

	/**
	 * Initializes a new instance of the CsvWriter class. Call close() on it before moving on in order to flush and close the underlying reader. If you don't do
	 * this, the garbage collector will do that for you at an undefined point in time.
	 *
	 * @param stringWriter
	 *            The StringWriter you want the CsvWriter to attach to.
	 * @param quotingBehavior
	 *            The quotingBehavior tells the writer how to quote fields in the resulting CSV. Minimal only lets him apply quotes where absolutely necessary.
	 *            All means that he always applies them, necessary or not.
	 */
	public CsvWriter(final StringWriter stringWriter, final QuotingBehavior quotingBehavior) {
		this.stringWriter = stringWriter;
		this.quotingBehavior = quotingBehavior;
	}

	/**
	 * Initializes a new instance of the CsvWriter class. Call close() on it before moving on in order to flush and close the underlying reader. If you don't do
	 * this, the garbage collector will do that for you at an undefined point in time.
	 *
	 * @param stringWriter
	 *            The StringWriter you want the CsvWriter to attach to.
	 * @param columnSeparator
	 *            A delimiter to separate columns (e.g. ';').
	 * @param rowSeparator
	 *            A delimiter to separate rows (e.g. System.getProperty("line.separator")).
	 * @param fieldDelimiter
	 *            A delimiter to enclose special-character-containing strings (e.g. " or just the empty string).
	 */
	public CsvWriter(final StringWriter stringWriter, final char columnSeparator, final String rowSeparator, final String fieldDelimiter) {
		this(stringWriter);
		this.columnSeparator = columnSeparator;
		this.rowSeparator = rowSeparator;
		this.fieldDelimiter = fieldDelimiter;
	}

	/**
	 * Initializes a new instance of the CsvWriter class. Call close() on it before moving on in order to flush and close the underlying reader. If you don't do
	 * this, the garbage collector will do that for you at an undefined point in time.
	 *
	 * @param stringWriter
	 *            The StringWriter you want the CsvWriter to attach to.
	 * @param columnSeparator
	 *            A delimiter to separate columns (e.g. ';').
	 * @param rowSeparator
	 *            A delimiter to separate rows (e.g. System.getProperty("line.separator")).
	 * @param fieldDelimiter
	 *            A delimiter to enclose special-character-containing strings (e.g. " or just the empty string).
	 * @param quotingBehavior
	 *            The quotingBehavior tells the writer how to quote fields in the resulting CSV. Minimal only lets him apply quotes where absolutely necessary.
	 *            All means that he always applies them, necessary or not.
	 */
	public CsvWriter(final StringWriter stringWriter, final char columnSeparator, final String rowSeparator, final String fieldDelimiter,
			final QuotingBehavior quotingBehavior) {
		this(stringWriter, quotingBehavior);
		this.columnSeparator = columnSeparator;
		this.rowSeparator = rowSeparator;
		this.fieldDelimiter = fieldDelimiter;
	}

	/**
	 * Initializes a new instance of the CsvWriter class. Call close() on it before moving on in order to flush and close the underlying reader. If you don't do
	 * this, the garbage collector will do that for you at an undefined point in time.
	 *
	 * @param stringWriter
	 *            The StringWriter you want the CsvWriter to attach to.
	 * @param columnSeparator
	 *            A delimiter to separate columns (e.g. ';').
	 * @param rowSeparator
	 *            A delimiter to separate rows (e.g. System.getProperty("line.separator")).
	 * @param fieldDelimiter
	 *            A delimiter to enclose special-character-containing strings (e.g. " or just the empty string).
	 * @param writeChunkSize
	 *            Size of one chunk (the minimal value is rowSeparator.length() and is automatically assigned if the given value was too small). The bufferSize
	 *            is automatically allocated in any case. It will be readChunkSize + rowSeparator.length() due to the parsing technique used).
	 */
	public CsvWriter(final StringWriter stringWriter, final char columnSeparator, final String rowSeparator, final String fieldDelimiter,
			final int writeChunkSize) {
		this(stringWriter, columnSeparator, rowSeparator, fieldDelimiter);
		setChunkAndBufferSize(writeChunkSize);
	}

	/**
	 * Initializes a new instance of the CsvWriter class. Call close() on it before moving on in order to flush and close the underlying reader. If you don't do
	 * this, the garbage collector will do that for you at an undefined point in time.
	 *
	 * @param stringWriter
	 *            The StringWriter you want the CsvWriter to attach to.
	 * @param columnSeparator
	 *            A delimiter to separate columns (e.g. ';').
	 * @param rowSeparator
	 *            A delimiter to separate rows (e.g. System.getProperty("line.separator")).
	 * @param fieldDelimiter
	 *            A delimiter to enclose special-character-containing strings (e.g. " or just the empty string).
	 * @param writeChunkSize
	 *            Size of one chunk (the minimal value is rowSeparator.length() and is automatically assigned if the given value was too small). The bufferSize
	 *            is automatically allocated in any case. It will be readChunkSize + rowSeparator.length() due to the parsing technique used).
	 * @param quotingBehavior
	 *            The quotingBehavior tells the writer how to quote fields in the resulting CSV. Minimal only lets him apply quotes where absolutely necessary.
	 *            All means that he always applies them, necessary or not.
	 */
	@Builder
	public CsvWriter(final StringWriter stringWriter, final Character columnSeparator, final String rowSeparator, final String fieldDelimiter,
			final Integer writeChunkSize, final QuotingBehavior quotingBehavior) {
		this(stringWriter, columnSeparator.or(DEFAULT_COLUMN_SEPARATOR), rowSeparator.or(DEFAULT_ROW_SEPARATOR),
				fieldDelimiter.or(DEFAULT_FIELD_DELIMITER), writeChunkSize.or(DEFAULT_CHUNK_SIZE));
		this.quotingBehavior = quotingBehavior.or(QuotingBehavior.MINIMAL);
	}

	/**
	 * Initializes the used field delimiter, the double field delimiter and various other things. It does this only once (with the help to the boolean variable
	 * isInitialized.
	 */
	private synchronized void initialize() {
		if (!isInitialized) {
			if (fieldDelimiter != null) {
				usedFieldDelimiter = "" + fieldDelimiter;
				doubleFieldDelimiter = "" + fieldDelimiter + fieldDelimiter;
			}
			numberOfUnusedBufferCharacters = buffer.length;
			isInitialized = true;
		}
	}

	/**
	 * Writes a string to the buffer. Flushes it if necessary.
	 *
	 * @param text
	 *            The text to write.
	 * @throws IOException
	 *             If the underlying stream could not be accessed.
	 */
	private void internalWrite(final String text) throws IOException {
		if (numberOfUnusedBufferCharacters < text.length()) {
			String t = text;
			while (t != null && !t.isEmpty()) {
				String sub;
				if (t.length() > numberOfUnusedBufferCharacters) {
					sub = t.substring(0, numberOfUnusedBufferCharacters);
					t = t.substring(numberOfUnusedBufferCharacters);
				} else {
					sub = t;
					t = "";
				}
				internalWrite(sub);
			}
		} else {
			for (char c : text.toCharArray()) {
				writeToBuffer(c);
			}
			if (numberOfUnusedBufferCharacters == 0) {
				flush();
			}
		}
	}

	/**
	 * Flushes all internal buffers to the underlying writer.
	 *
	 * @throws IOException
	 *             If the underlying stream could not be accessed.
	 */
	public CsvWriter flush() throws IOException {
		stringWriter.write(buffer, 0, bufferCount);
		bufferCount = 0;
		numberOfUnusedBufferCharacters = buffer.length;
		stringWriter.flush();
		return this;
	}

	/**
	 * Calls flush on the underlying stringWriter.
	 *
	 * @throws IOException
	 *             If the underlying stream could not be accessed.
	 */
	public CsvWriter flushUnderlyingWriter() throws IOException {
		if (stringWriter != null) {
			stringWriter.flush();
		}
		return this;
	}

	/**
	 * Writes a character to the buffer.
	 *
	 * @param c
	 *            The character to write.
	 */
	private void writeToBuffer(final char c) {
		buffer[bufferCount] = c;
		bufferCount++;
		numberOfUnusedBufferCharacters--;
	}

	/**
	 * Determines if a field delimiter has to be used in order to produce a well formed CSV format.
	 *
	 * @param csvData
	 *            The data the determination is based on.
	 * @return True, if a field-delimiter has to be used, false otherwise.
	 */
	private boolean isUseFieldDelimiter(final String csvData) {
		boolean isFieldDelimiterNeeded = csvData.contains(usedFieldDelimiter) || csvData.contains(rowSeparator) || csvData.contains("" + columnSeparator);
		if (usedFieldDelimiter != null && usedFieldDelimiter != "" && (isFieldDelimiterNeeded || quotingBehavior == QuotingBehavior.ALL)) {
			return true;
		}
		return false;
	}

	/**
	 * Writes an empty string (followed by a column-separator).
	 *
	 * @throws IOException
	 *             If the underlying stream could not be accessed.
	 */
	public synchronized CsvWriter write() throws IOException {
		write("");
		return this;
	}

	/**
	 * Writes a row-separator, advancing the writer to a new row. Does not write an empty string before advancing the writer.
	 *
	 * @throws IOException
	 *             If the underlying stream could not be accessed.
	 */
	public synchronized CsvWriter writeLine() throws IOException {
		internalWrite(rowSeparator);
		isFirstFieldInRow = true;
		return this;
	}

	/**
	 * Writes a field to the CSV and appends a columnSeparator in front of the new entry if necessary. Appends a rowSeparator at the end, advancing the writer
	 * to a new row.
	 *
	 * @param csvData
	 *            The data that should be written.
	 * @throws IOException
	 *             If the underlying stream could not be accessed.
	 */
	public synchronized CsvWriter writeLine(final String csvData) throws IOException {
		write(csvData);
		writeLine();
		return this;
	}

	/**
	 * Writes a field to the CSV and appends a columnSeparator in front of the new entry if necessary.
	 *
	 * @param csvData
	 *            The data, that should be written to the CSV.
	 * @throws IOException
	 *             If the underlying stream could not be accessed.
	 */
	public synchronized CsvWriter write(final String csvData) throws IOException {
		if (isFirstFieldInRow) {
			isFirstFieldInRow = false;
		} else {
			internalWrite("" + columnSeparator);
		}
		String dataToWrite = csvData;
		if (dataToWrite == null) {
			dataToWrite = "";
		}
		initialize();
		if (isUseFieldDelimiter(dataToWrite)) {
			// Quote the field.
			internalWrite(usedFieldDelimiter);
			// Escape string delimiter.
			internalWrite(dataToWrite.replace(usedFieldDelimiter, doubleFieldDelimiter));
			// Unquote the field.
			internalWrite(usedFieldDelimiter);
		} else {
			internalWrite(dataToWrite);
		}
		return this;
	}

	/**
	 * Writes a row to the CSV ending with a rowSeparator. Treats the target as if it was empty, or at least in a new-row-position (empty row, no field inserted
	 * yet).
	 *
	 * @param csvData
	 *            The data, that should be written to the CSV.
	 * @throws IOException
	 *             If the underlying stream could not be accessed.
	 */
	public synchronized CsvWriter writeLine(final List<String> csvData) throws IOException {
		initialize();
		write(csvData);
		writeLine();
		return this;
	}

	/**
	 * Writes a row to the CSV not ending with a rowSeparator. Treats the target as if it was empty, or at least in a newline-position (empty row, no field
	 * inserted yet).
	 *
	 * @param csvData
	 *            The data, that should be written to the CSV.
	 * @throws IOException
	 *             If the underlying stream could not be accessed.
	 */
	public synchronized CsvWriter write(final List<String> csvData) throws IOException {
		if (csvData == null || csvData.size() == 0) {
			return this;
		}
		initialize();
		for (int i = 0; i < csvData.size(); i++) {
			String fieldData = csvData.get(i);
			write(fieldData);
		}
		return this;
	}

	/**
	 * Writes all rows to a CSV. Treats the target as if it was empty, or at least in a newline-position (empty row, no field inserted yet).
	 *
	 * @param csvData
	 *            The data, that should be written to the CSV.
	 * @throws IOException
	 *             If the underlying stream could not be accessed.
	 */
	public synchronized CsvWriter writeAll(final List<List<String>> csvData) throws IOException {
		initialize();
		for (int i = 0; i < csvData.size(); i++) {
			List<String> rowData = csvData.get(i);
			if (i == csvData.size() - 1) {
				// This is the last one.
				write(rowData);
			} else {
				writeLine(rowData);
			}
		}
		return this;
	}

	/**
	 * Sets the size of the buffer and of the chunk.
	 *
	 * @param chunkSize
	 *            The size of one chunk.
	 */
	private CsvWriter setChunkAndBufferSize(final int chunkSize) {
		this.chunkSize = chunkSize;
		if (this.chunkSize < 1) {
			this.chunkSize = 1;
		}
		buffer = new char[this.chunkSize];
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.Closeable#close()
	 */
	@Override
	public void close() throws IOException {
		if (stringWriter == null) {
			return;
		}
		flush();
		stringWriter.close();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		try {
			close();
		} finally {
			super.finalize();
		}
	}
}
