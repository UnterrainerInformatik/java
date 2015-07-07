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
