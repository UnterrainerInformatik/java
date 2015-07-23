package info.unterrainer.java.tools.utils;

public class ExtensionMethods {

	/**
	 * Checks if the given object is null.<br/>
	 * If not, the object is returned. If so, the given defaultValue is returned.
	 *
	 * @param <T> the generic type of the object to be checked
	 * @param obj the object to be checked
	 * @param ifNull the default value
	 * @return the object or the default-value (ifNull)
	 */
	public static <T> T or(T obj, T ifNull) {
		return obj != null ? obj : ifNull;
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
	public static String toTitleCase(String in) {
		if (in.isEmpty()) {
			return in;
		}
		return "" + Character.toTitleCase(in.charAt(0)) + in.substring(1).toLowerCase();
	}
}
