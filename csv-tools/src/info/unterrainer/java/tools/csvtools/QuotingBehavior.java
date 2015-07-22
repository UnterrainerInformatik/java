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

/**
 * The Enumeration QuotingBehavior.
 * <p>
 * Contains all the possible quoting-behaviors a CSV-writer may have.
 */
public enum QuotingBehavior {
	MINIMAL(0),
	ALL(1);

	private int code;

	/**
	 * Instantiates a new quoting behavior.
	 *
	 * @param code the code
	 */
	private QuotingBehavior(final int code) {
		this.code = code;
	}

	/**
	 * Gets the unique code that lies behind this particular enumeration.
	 *
	 * @return A unique code.
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Gets the enumeration value given the proper unique code.
	 *
	 * @param code The code the enumeration value should be found for.
	 * @return The enumeration value that was found for the given unique code.
	 */
	public static QuotingBehavior fromCode(final int code) {
		for (QuotingBehavior b : QuotingBehavior.values()) {
			if (code == b.getCode()) {
				return b;
			}
		}
		return null;
	}

	/**
	 * Displays the name of the enumeration in lower case with the first letter capitalized.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		// Capitalize the first letter.
		String s = super.toString();
		return s.substring(0, 1) + s.substring(1).toLowerCase();
	}
}