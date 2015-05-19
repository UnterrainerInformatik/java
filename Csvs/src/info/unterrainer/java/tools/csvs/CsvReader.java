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
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class CsvReader.
 * <p>
 * Enables you to read CSV-files using various types of delimiters (column and
 * row) and quotes.
 * 
 * @author GEUNT
 * @since 20.09.2013
 */
public class CsvReader extends CsvBase {

	private final int DEFAULT_CHUNK_SIZE = 16384;
	private char[] buffer = new char[DEFAULT_CHUNK_SIZE + 2];
	private int chunkSize = DEFAULT_CHUNK_SIZE;
	private Reader stringReader;
	private Character nextChar;
	private int nextCharBufferIndex;
	private int numberOfUnparsedChars;

	/**
	 * Initializes a new instance of the CsvReader class. Call close() on it
	 * before moving on in order to close the underlying reader. If you don't do
	 * this, the garbage collector will do that for you at an undefined point in
	 * time.
	 * 
	 * @param stringReader
	 *            The StringReader you want the CsvReader to attach to.
	 * @throws IllegalArgumentException
	 *             If the provided stringReader is null.
	 */
	public CsvReader(final StringReader stringReader)
			throws IllegalArgumentException {
		if (stringReader == null) {
			throw new IllegalArgumentException(
					"The StringReader you provided is null.");
		}

		this.stringReader = stringReader;
	}

	/**
	 * Initializes a new instance of the CsvReader class. Call close() on it
	 * before moving on in order to close the underlying reader. If you don't do
	 * this, the garbage collector will do that for you at an undefined point in
	 * time.
	 * 
	 * @param stringReader
	 *            The StringReader you want the CsvReader to attach to.
	 * @param columnSeparator
	 *            A delimiter to separate columns (e.g. ';').
	 * @param rowSeparator
	 *            A delimiter to separate rows (e.g.
	 *            System.getProperty("line.separator")).
	 * @param fieldDelimiter
	 *            A delimiter to enclose special-character-containing strings
	 *            (e.g. " or just null).
	 */
	public CsvReader(final StringReader stringReader,
			final char columnSeparator, final String rowSeparator,
			final Character fieldDelimiter) {
		this(stringReader);
		this.columnSeparator = columnSeparator;
		this.rowSeparator = rowSeparator;
		this.fieldDelimiter = fieldDelimiter;
	}

	/**
	 * Initializes a new instance of the CsvReader class. Call close() on it
	 * before moving on in order to close the underlying reader. If you don't do
	 * this, the garbage collector will do that for you at an undefined point in
	 * time.
	 * 
	 * @param stringReader
	 *            The StringReader you want the CsvReader to attach to.
	 * @param columnSeparator
	 *            A delimiter to separate columns (e.g. ';').
	 * @param rowSeparator
	 *            A delimiter to separate rows (e.g.
	 *            System.getProperty("line.separator")).
	 * @param fieldDelimiter
	 *            A delimiter to enclose special-character-containing strings
	 *            (e.g. " or just null).
	 * @param readChunkSize
	 *            Size of one chunk (the minimal value is rowSeparator.length()
	 *            and is automatically assigned if the given value was too
	 *            small). The bufferSize is automatically allocated in any case.
	 *            It will be readChunkSize + rowSeparator.length() due to the
	 *            parsing technique used).
	 */
	public CsvReader(final StringReader stringReader,
			final char columnSeparator, final String rowSeparator,
			final Character fieldDelimiter, final int readChunkSize) {
		this(stringReader, columnSeparator, rowSeparator, fieldDelimiter);
		setChunkAndBufferSize(readChunkSize);
	}

	/**
	 * Peeks into the buffer where all unhandled characters reside and copies a
	 * lookahead-string consisting out of the next 'length' unhandled
	 * characters. If the buffer ends before the next 'length' characters could
	 * be read, then the string which is available is returned. (Meaning that a
	 * _Peek(8) with the buffer [a23456] returns 'a23456'.)
	 * 
	 * @param length
	 *            The length which should be read ahead.
	 * @return The preview-string consisting of the next 'length' unhandled
	 *         characters.
	 */
	private synchronized String peek(final int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i <= length; i++) {
			// NextChar has index: nextCharBufferIndex.
			int index = nextCharBufferIndex + i;
			if (index < buffer.length && index >= 0) {
				sb.append(buffer[index]);
			}
		}
		return sb.toString();
	}

	/**
	 * Does essentially the same as {@link _Peek} but counts and returns the
	 * current nextChar as well. Example: buffer=[1234] & nextChar=0 then
	 * peekInclusiveNextChar(3) would return [012].
	 * 
	 * @param length
	 *            The length which should be read ahead.
	 * @return The preview-string consisting of the next 'length' unhandled
	 *         characters.
	 */
	private synchronized String peekInclusiveNextChar(final int length) {
		return nextChar + peek(length - 1);
	}

	/**
	 * Reads the next chunk.
	 * 
	 * @throws IOException
	 *             If the underlying stream could not be accessed.
	 */
	private synchronized void readNext() throws IOException {
		nextChar = getNextChar();
		// EOF is not reached
		// AND the rest of the read chunk isn't long enough to contain a
		// rowSeparator.
		// In this case we have to read ahead and keep the existing characters.
		if (nextChar != null
				&& numberOfUnparsedChars + 1 < rowSeparator.length()) {
			// copy the buffer to a temporary array...
			char[] temp = new char[numberOfUnparsedChars + 1];
			System.arraycopy(buffer, nextCharBufferIndex, temp, 0,
					numberOfUnparsedChars + 1);
			// Temp holds the unread chars plus the one just read.
			// Copy the temporary-buffer back over the original buffer
			// thus deleting all the old values except the ones
			// we copied to the temporary-buffer earlier on.
			System.arraycopy(temp, 0, buffer, 0, temp.length);

			readChunk(numberOfUnparsedChars + 1);
			nextCharBufferIndex = 0;
		}
	}

	/**
	 * Omits some of the characters in the buffer by reading ahead for
	 * numberOfCharacters. _ReadNext() is equivalent to _ReadNext(1).
	 * 
	 * @param numberOfCharacters
	 *            The number of characters to read ahead.
	 * @throws IOException
	 *             If the underlying stream could not be accessed.
	 */
	private synchronized void readNext(final int numberOfCharacters)
			throws IOException {
		for (int i = 0; i < numberOfCharacters; i++) {
			readNext();
		}
	}

	/**
	 * Gets the next character in the buffer and reads the next chunk if the
	 * buffer is empty.
	 * 
	 * @return The next character or null if the end of the file is reached.
	 * @throws IOException
	 *             If the underlying stream could not be accessed.
	 */
	private synchronized Character getNextChar() throws IOException {
		if (numberOfUnparsedChars > 0) {
			char c = buffer[nextCharBufferIndex + 1];
			nextCharBufferIndex++;
			numberOfUnparsedChars--;
			return c;
		}
		// If the buffer is empty.
		readChunk(0);
		nextCharBufferIndex = -1;
		if (numberOfUnparsedChars <= 0) {
			return null;
		}
		return getNextChar();
	}

	/**
	 * Reads the next chunk of the input file.
	 * 
	 * @param startIndex
	 *            The start index in the buffer at which the next chunk should
	 *            be appended.
	 * @throws IOException
	 *             If the underlying stream could not be accessed.
	 */
	private synchronized void readChunk(final int startIndex)
			throws IOException {
		int readChars = stringReader.read(buffer, startIndex, chunkSize);
		numberOfUnparsedChars += readChars;
	}

	/**
	 * Reads the next field of the CSV.
	 * 
	 * @return A string containing the next field of the underlying CSV.
	 * @throws IOException
	 *             If the underlying stream could not be accessed.
	 */
	private synchronized String readField() throws IOException {
		StringBuilder content = new StringBuilder();
		boolean isEscaped = false;
		while (nextChar != null) {
			if (nextChar == fieldDelimiter) {
				if (isEscaped && peek(1).equals(fieldDelimiter + "")) {
					// Double fieldDelimiter.
					// Write one of them, omit the other.
					readNext();
				} else {
					isEscaped = !isEscaped;
					readNext();
					continue;
				}
			}
			if (peekInclusiveNextChar(rowSeparator.length()).equals(
					rowSeparator)
					&& !isEscaped) {
				// We are at the last field in a row
				// and therefore this is the end of the field.
				return content.toString();
			}
			if (nextChar == columnSeparator && !isEscaped) {
				// We are at the end of this field.
				return content.toString();
			}
			content.append(nextChar);
			readNext();
		}
		return content.toString();
	}

	/**
	 * Returns the next row, already split into fields. Returns null if the end
	 * of the source is reached.
	 * 
	 * @return An array of string containing the fields of this row or null if
	 *         the end of the source has been reached.
	 * @throws IOException
	 *             If the underlying stream could not be accessed.
	 */
	public synchronized List<String> readRow() throws IOException {
		List<String> fields = new ArrayList<String>();
		if (nextChar == null) {
			readNext();
		}
		while (nextChar != null
				&& !peekInclusiveNextChar(rowSeparator.length()).equals(
						rowSeparator)) {
			fields.add(readField());
			if (nextChar != null && nextChar == columnSeparator) {
				readNext();
				if (nextChar == null
						|| peekInclusiveNextChar(rowSeparator.length()).equals(
								rowSeparator)) {
					fields.add("");
				}
			}
		}
		if (peekInclusiveNextChar(rowSeparator.length()).equals(rowSeparator)) {
			// Reading fields stopped due to line end.
			// Omit the row-separator.
			readNext(rowSeparator.length());
		}
		if (fields.size() == 0 && peekBufferedReader(stringReader) == -1) {
			// This is the EOF and the result is empty.
			return null;
		}
		return fields;
	}

	private int peekBufferedReader(final Reader reader) throws IOException {
		reader.mark(1);
		int result = reader.read();
		reader.reset();
		return result;
	}

	/**
	 * Reads all rows of the CSV and returns them in a List. The resulting
	 * structure is a List-of-List-of-string.
	 * 
	 * @return A List-of-List-of-string representing the read CSV-file.
	 * @throws IOException
	 *             If the underlying stream could not be accessed.
	 */
	public synchronized List<List<String>> readAllRows() throws IOException {
		List<List<String>> data = new ArrayList<List<String>>();
		List<String> row = readRow();
		while (row != null) {
			data.add(row);
			row = readRow();
		}
		if (data.size() == 0) {
			// This is the EOF and the result is empty.
			return null;
		}
		return data;
	}

	/**
	 * Gets the column separator.
	 * 
	 * @return The column separator.
	 */
	public char getColumnSeperator() {
		return columnSeparator;
	}

	/**
	 * Gets the row separator.
	 * 
	 * @return The row separator.
	 */
	public String getRowSeperator() {
		return rowSeparator;
	}

	/**
	 * Gets the field delimiter.
	 * 
	 * @return The field delimiter.
	 */
	public char getFieldDelimiter() {
		return fieldDelimiter;
	}

	/**
	 * Sets the size of the buffer and of the chunk.
	 * 
	 * @param chunkSize
	 *            The size of one chunk.
	 */
	private void setChunkAndBufferSize(final int chunkSize) {
		this.chunkSize = chunkSize;
		if (this.chunkSize < rowSeparator.length()) {
			this.chunkSize = rowSeparator.length();
		}
		buffer = new char[this.chunkSize + rowSeparator.length()];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Closeable#close()
	 */
	@Override
	public void close() throws IOException {
		if (stringReader == null) {
			return;
		}
		stringReader.close();
		stringReader = null;
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
