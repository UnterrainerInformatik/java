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

import java.io.Closeable;

/**
 * The Class CsvBase.
 * <p>
 * This is the base-class for both the reader and the writer. It's mainly used to share default-values.
 *
 * @author GEUNT
 * @since 20.09.2013
 */
public abstract class CsvBase implements Closeable {

	/**
	 * The default value for the column separator.
	 */
	protected final static Character DEFAULT_COLUMN_SEPARATOR = ';';

	/**
	 * The default value for the row separator.
	 */
	protected final static String DEFAULT_ROW_SEPARATOR = "\r\n";

	/**
	 * The default value for the field delimiter.
	 */
	protected final static Character DEFAULT_FIELD_DELIMITER = '"';

	/**
	 * This is the lock-object for this class.
	 */
	protected Object lockObject = new Object();

	/**
	 * The value of the column separator that is used by the program.
	 */
	protected Character columnSeparator = DEFAULT_COLUMN_SEPARATOR;

	/**
	 * The value of the field delimiter that is used by the program.
	 */
	protected Character fieldDelimiter = DEFAULT_FIELD_DELIMITER;

	/**
	 * The value of the row separator that is used by the program.
	 */
	protected String rowSeparator = DEFAULT_ROW_SEPARATOR;
}