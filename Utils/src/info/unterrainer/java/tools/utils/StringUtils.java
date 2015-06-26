package info.unterrainer.java.tools.utils;

public class StringUtils {

	/**
	 * Private constructor in order to hide it because this is a collection of static methods.
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
}
