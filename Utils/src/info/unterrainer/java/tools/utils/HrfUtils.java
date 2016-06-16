package info.unterrainer.java.tools.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.util.Locale;

import lombok.experimental.UtilityClass;

@UtilityClass
/**
 * This class is concerned with the conversion to human readable forms (HRF).
 */
public class HrfUtils {
	public final long SECOND = 1000;
	public final long MINUTE = SECOND * 60;
	public final long HOUR = MINUTE * 60;
	public final long DAY = HOUR * 24;
	public final long WEEK = DAY * 7;
	public final long MONTH = DAY * 30;
	public final long YEAR = DAY * 360;

	/**
	 * Converts a long into a human readable byte format with units 'KB', 'MB', 'GB', 'TB', 'PB', or 'EB'. Uses non - SI-units (meaning 1024 byte is a KB).
	 *
	 * @param bytes the number to convert
	 * @return a sting in human readable form (like '3.4KB')
	 */
	public static String toHumanReadableByteCount(long bytes) {
		return humanReadableByteCount(bytes, false);
	}

	/**
	 * Converts a long into a human readable byte format with units 'kB', 'MB', 'GB', 'TB', 'PB', or 'EB'. Uses SI-units (meaning 1000 byte is a kB).
	 *
	 * @param bytes the number to convert
	 * @return a sting in human readable form (like '3.4kB')
	 */
	public static String toHumanReadableSiByteCount(long bytes) {
		return humanReadableByteCount(bytes, true);
	}

	private static String humanReadableByteCount(long bytes, boolean si) {
		int unit = si ? 1000 : 1024;
		if (bytes < unit) {
			return bytes + " B";
		}
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}

	/**
	 * Converts a long into a human readable format for a duration with units 'ms', 's', 'm', 'h', 'd', 'W', 'M' and 'Y'.
	 *
	 * @param millis the number to convert
	 * @return a string in human readable form (like '3.4ms')
	 */
	public static String toHumanReadableDuration(long millis) {
		return toHumanReadable(Duration.ofMillis(millis));
	}

	/**
	 * Converts a long into a human readable format for a duration with units 'milliseconds', 'seconds', 'minutes', 'hours', 'days', 'weeks', 'months' or
	 * 'years'.
	 *
	 * @param millis the number to convert
	 * @return a string in human readable form (like '3.4 milliseconds')
	 */
	public static String toHumanReadableDurationLongUnits(long millis) {
		return toHumanReadableLongUnits(Duration.ofMillis(millis));
	}

	/**
	 * Converts a duration into a human readable format for a duration with units 'ms', 's', 'm', 'h', 'd', 'W', 'M' and 'Y'.
	 *
	 * @param duration the duration to convert
	 * @return a string in human readable form (like '3.4ms')
	 */
	public static String toHumanReadable(Duration duration) {
		return toHumanReadableDuration(duration, new String[] { "ms", "s", "m", "h", "d", "W", "M", "Y" });
	}

	/**
	 * Converts a duration into a human readable format for a duration with units 'milliseconds', 'seconds', 'minutes', 'hours', 'days', 'weeks', 'months' or
	 * 'years'.
	 *
	 * @param duration the duration to convert
	 * @return a string in human readable form (like '3.4 milliseconds')
	 */
	public static String toHumanReadableLongUnits(Duration duration) {
		return toHumanReadableDuration(duration, new String[] { " milliseconds", " seconds", " minutes", " hours", " days", " weeks", " months", " years" });
	}

	/**
	 * Converts a long into a human readable format for a duration with units of your liking.<br>
	 * You may specify your own unit-strings by passing an appropriate string-array containing all units in the following order.
	 * <p>
	 *
	 * <pre>
	 * <code>
	 * index | default-unit | long
	 * ---------------------------
	 * 0     |    'ms'      | milliseconds
	 * 1     |    's'       | seconds
	 * 2     |    'm'       | minutes
	 * 3     |    'h'       | hours
	 * 4     |    'd'       | days
	 * 5     |    'W'       | weeks
	 * 6     |    'M'       | months
	 * 7     |    'Y'       | years
	 * </code>
	 * </pre>
	 *
	 * @param long the number to convert
	 * @param units a string-array containing the unit-strings in the right order with length 8
	 * @return a string in human readable form (like '3.4ms')
	 */
	public static String toHumanReadableDuration(long millis, String[] units) {
		return toHumanReadableDuration(Duration.ofMillis(millis), units);
	}

	/**
	 * Converts a duration into a human readable format for a duration with units of your liking.<br>
	 * You may specify your own unit-strings by passing an appropriate string-array containing all units in the following order.
	 * <p>
	 *
	 * <pre>
	 * <code>
	 * index | default-unit | long
	 * ---------------------------
	 * 0     |    'ms'      | milliseconds
	 * 1     |    's'       | seconds
	 * 2     |    'm'       | minutes
	 * 3     |    'h'       | hours
	 * 4     |    'd'       | days
	 * 5     |    'W'       | weeks
	 * 6     |    'M'       | months
	 * 7     |    'Y'       | years
	 * </code>
	 * </pre>
	 *
	 * @param duration the duration to convert
	 * @param units a string-array containing the unit-strings in the right order with length 8
	 * @return a string in human readable form (like '3.4ms')
	 */
	public static String toHumanReadableDuration(Duration duration, String[] units) {
		String[] u = new String[] { "ms", "s", "m", "h", "d", "W", "M", "Y" };
		if (units.length == 8) {
			u = units;
		}

		NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
		DecimalFormat df = (DecimalFormat) nf;
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(0);

		float[] r = calculateHumanReadableDuration(duration);
		String s = df.format(r[0]);
		s += u[(int) r[1]];
		return s;
	}

	private static float[] calculateHumanReadableDuration(Duration duration) {
		Duration d = duration;
		if (d.isNegative()) {
			d = d.multipliedBy(-1);
		}
		final Long[] boundaries = new Long[] { SECOND, MINUTE, HOUR, DAY, WEEK, MONTH, YEAR };

		float value = 0;
		float boundaryIndex = 0;
		long m = d.toMillis();
		for (int i = 0; i < boundaries.length; i++) {
			long l = boundaries[i];
			if (m < l) {
				if (i == 0) {
					value = m;
				} else {
					value = (float) m / (float) boundaries[i - 1];
				}
				boundaryIndex = i;
				break;
			} else {
				if (i == boundaries.length - 1) {
					value = (float) m / (float) boundaries[i];
					boundaryIndex = i + 1;
				}
			}
		}
		return new float[] { duration.isNegative() ? -value : value, boundaryIndex };
	}
}