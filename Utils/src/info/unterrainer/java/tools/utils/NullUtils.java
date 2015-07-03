package info.unterrainer.java.tools.utils;

public class NullUtils {

	/**
	 * Private constructor in order to hide constructor of static helper-class.
	 */
	private NullUtils() {
	}

	/**
	 * Checks if the given object is null.<br/>
	 * If not, the object is returned. If so, the given defaultValue is returned.
	 *
	 * @param <T>
	 *            the generic type of the object to be checked
	 * @param obj
	 *            the object to be checked
	 * @param defaultValue
	 *            the default value
	 * @return the object or the defaultValue
	 */
	public static <T> T defaultIfNull(T obj, T defaultValue) {
		return obj == null ? defaultValue : obj;
	}
}
