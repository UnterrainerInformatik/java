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

package info.unterrainer.java.tools.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {

	/**
	 * Returns a string consisting of a specific number of concatenated copies of an input string. For example, repeat("hey", 3) returns the string "heyheyhey".
	 *
	 * @param string {@link String} any non-null string
	 * @param count the number of times to repeat it; a nonnegative integer
	 * @return the string {@link String} containing string repeated count times (the empty string if count is zero)
	 */
	public static String repeat(final String string, final int count) {
		String result = "";
		for (int i = 0; i < count; i++) {
			result += string;
		}
		return result;
	}

	public static boolean isNullOrEmpty(String string) {
		return string == null || string.equals("");
	}

	/**
	 * Gets the stack trace as a formatted String.
	 *
	 * @param throwable {@link Throwable} the throwable
	 * @return the stack trace {@link String}
	 */
	public static String getStackTrace(final Throwable throwable) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);

		throwable.printStackTrace(printWriter);
		return result.toString();
	}

	/**
	 * Contains check if the name contains any of the items in matches.
	 *
	 * @param matches the matches
	 * @param name {@link String} the name
	 * @return true, if successful
	 */
	public static synchronized boolean contains(final List<String> matches, final String name) {
		boolean result = false;
		for (int i = 0; (i < matches.size()) && !result; i++) {
			result = name.contains(matches.get(i));
		}
		return result;
	}

	/**
	 * Strips the leading and trailing quotes {@code (")} from a string if it starts with one and ends with one. Does not trim the string first; That's up to
	 * you.
	 *
	 * @param text {@link String} the text to strip the quotes from
	 * @return the string {@link String} the text without the quotes or the original text, if the condition was not satisfied
	 */
	public static String stripQuotes(final String text) {
		if (text == null) {
			return null;
		}

		if ((text.length() > 1) && text.startsWith("\"") && text.endsWith("\"")) {
			return text.substring(1, text.length() - 1);
		}
		return text;
	}
}
