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

import java.util.Calendar;
import java.util.Date;

/**
 * This static utility-class contains methods that help when dealing with date-time conversions. Most notably it contains methods to convert date-time to
 * ISO8601 strings and vice versa. It does so by utilizing the JAXB-parser directly.
 */
public final class DateUtils {

	/**
	 * Private constructor in order to hide constructor of static helper-class.
	 */
	private DateUtils() {
	}

	/**
	 * Converts a ISO-8601 string to a {@link Calendar}-object.
	 *
	 * @param date
	 *            the date in the form of a ISO-8601 string (for example: '2009-06-30T18:30:00+02:00')
	 * @return the calendar
	 */
	public static Calendar fromIso8601(String date) {
		if (date == null) {
			return null;
		}
		return javax.xml.bind.DatatypeConverter.parseDateTime(date);
	}

	/**
	 * Converts a {@link Calendar}-object to a ISO-8601 string.
	 *
	 * @param calendar
	 *            the calendar
	 * @return the ISO-8601 string (for example: '2009-06-30T18:30:00+02:00')
	 */
	public static String toIso8601(Calendar calendar) {
		if (calendar == null) {
			return null;
		}
		return javax.xml.bind.DatatypeConverter.printDateTime(calendar);
	}

	/**
	 * Converts a {@link Date}-object to a ISO-8601 string.
	 *
	 * @param date
	 *            the date
	 * @return the ISO-8601 string (for example: '2009-06-30T18:30:00+02:00')
	 */
	public static String toIso8601(Date date) {
		if (date == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return toIso8601(c);
	}

	/**
	 * Converts a (Unix)-time-stamp of type long to a ISO-8601 string.
	 *
	 * @param timeStamp
	 *            the time stamp
	 * @return the ISO-8601 string (for example: '2009-06-30T18:30:00+02:00')
	 */
	public static String toIso8601(Long timeStamp) {
		if (timeStamp == null) {
			return null;
		}
		Date date = new Date(timeStamp);
		return toIso8601(date);
	}
}
