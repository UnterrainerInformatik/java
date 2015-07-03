package info.unterrainer.java.tools.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

public class StringUtils {

	/**
	 * Private constructor in order to hide constructor of static helper-class.
	 */
	private StringUtils() {
	}

	/**
	 * Returns a string consisting of a specific number of concatenated copies of an input string. For example, repeat("hey", 3) returns the string "heyheyhey".
	 *
	 * @param string
	 *            {@link String} any non-null string
	 * @param count
	 *            {@link int} the number of times to repeat it; a nonnegative integer
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
	 * @param throwable
	 *            {@link Throwable} the throwable
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
	 * @param matches
	 *            {@link List<String>} the matches
	 * @param name
	 *            {@link String} the name
	 * @return true ({@link boolean}), if successful
	 */
	public static synchronized boolean contains(final List<String> matches, final String name) {
		boolean result = false;
		for (int i = 0; (i < matches.size()) && !result; i++) {
			result = name.contains(matches.get(i));
		}
		return result;
	}
}
