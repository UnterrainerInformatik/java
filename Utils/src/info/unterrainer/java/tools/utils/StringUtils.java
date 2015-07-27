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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
	@Nonnull
	public static String repeat(@Nullable String string, int count) {
		String result = "";
		if (string != null) {

			for (int i = 0; i < count; i++) {
				result += string;
			}
		}
		return result;
	}

	/**
	 * Checks whether a given string is null or empty.
	 *
	 * @param in a {@link String} to be checked
	 * @return true, or false
	 */
	public static boolean isNullOrEmpty(@Nullable String string) {
		return string == null || string.equals("");
	}

	/**
	 * Gets the stack trace as a formatted String.
	 *
	 * @param throwable {@link Throwable} the throwable
	 * @return the stack trace {@link String}
	 */
	@Nonnull
	public static String getStackTrace(@Nonnull Throwable throwable) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);

		throwable.printStackTrace(printWriter);
		return result.toString();
	}

	/**
	 * Converts the first character of the in-argument to titlecase using case mapping information from the UnicodeData file. If a character has no explicit
	 * titlecase mapping and is not itself a titlecase char according to UnicodeData, then the uppercase mapping is returned as an equivalent titlecase mapping.
	 * If the first {@code char} of the in-argument is already a titlecase {@code char}, the same in-value will be returned.
	 * <p>
	 * Note that {@code Character.isTitleCase(Character.toTitleCase(ch))} does not always return {@code true} for some ranges of characters.
	 * <p>
	 * <b>Note:</b> This method cannot handle <a href="#supplementary"> supplementary characters</a>. To support all Unicode characters, including supplementary
	 * characters, use the {@link #toTitleCase(int)} method.
	 *
	 * @param in the string to convert the first character of.
	 * @return the string starting with the titlecase equivalent of the first character, if any; otherwise, with the character itself.
	 */
	@Nullable
	public static String toTitleCase(@Nullable String in) {
		if (in == null || in.isEmpty()) {
			return in;
		}
		return "" + Character.toTitleCase(in.charAt(0)) + in.substring(1).toLowerCase();
	}

	/**
	 * Evaluates if a given string contains one or more of the strings within a given list.
	 *
	 * @param inputString the input string to check
	 * @param items the items to check for
	 * @return true, if successful, false otherwise
	 */
	public static boolean contains(@Nullable String inputString, String... items) {
		return contains(inputString, items);
	}

	/**
	 * Evaluates if a given string contains one or more of the strings within a given list.
	 *
	 * @param inputString the input string to check
	 * @param items the items to check for
	 * @return true, if successful, false otherwise
	 */
	public static boolean contains(@Nullable String inputString, @Nullable List<String> items) {
		if (inputString == null || items == null) {
			return false;
		}

		for (String s : items) {
			if (inputString.contains(s)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Strips the leading and trailing quotes {@code (")} from a string if it starts with one and ends with one. Does not trim the string first; That's up to
	 * you.
	 *
	 * @param text {@link String} the text to strip the quotes from
	 * @return the string {@link String} the text without the quotes or the original text, if the condition was not satisfied
	 */
	@Nullable
	public static String stripQuotes(@Nullable String text) {
		if (text == null) {
			return null;
		}

		if (text.length() > 1 && text.startsWith("\"") && text.endsWith("\"")) {
			return text.substring(1, text.length() - 1);
		}
		return text;
	}
}
