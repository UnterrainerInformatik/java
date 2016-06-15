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

import lombok.experimental.UtilityClass;

import javax.annotation.Nullable;

@UtilityClass
public class NullUtils {

	/**
	 * Checks if the given object is null.<br>
	 * If not, the object is returned. If so, the given default-value is returned.
	 * <p>
	 * If the given default-value is null as well, null is returned.
	 *
	 * @param <T> the generic type of the object to be checked
	 * @param object the object to be checked
	 * @param ifNull the default value
	 * @return the object or the default-value (ifNull)
	 */
	public static <T> @Nullable T or(@Nullable T object, @Nullable T ifNull) {
		return object != null ? object : ifNull;
	}

	/**
	 * Returns the first of two given parameters that is not null, if either is, or otherwise throws a NullPointerException.
	 *
	 * @param <T> the generic type of the objects to be checked
	 * @param object the first object to be checked and to be returned if it's not equal null
	 * @param ifNull the second object to be checked and to be returned if the first object is null and the second isn't
	 * @return first if it is non-null; otherwise second if it is non-null
	 * @throws NullPointerException if both objects are {@code null}
	 */
	public static <T> T orNoNull(@Nullable T object, @Nullable T ifNull) {
		return noNull(or(object, ifNull));
	}

	/**
	 * Returns the first of two given parameters that is not null. The second parameter HAS to be nonnull.
	 * Helper method that does unboxing as well since most of the IDEs have problems resolving over that barrier.
	 *
	 * @param object the first object to be checked and to be returned if it's not equal null
	 * @param defaultValue the second object to be checked and to be returned if the first object is null and the second isn't
	 * @return first if it is non-null; otherwise second if it is non-null
	 */
	public static double orNoNullUnbox(@Nullable Double object, double defaultValue) {
		return noNull(or(object, defaultValue));
	}

	/**
	 * Returns the first of two given parameters that is not null. The second parameter HAS to be nonnull.
	 * Helper method that does unboxing as well since most of the IDEs have problems resolving over that barrier.
	 *
	 * @param object the first object to be checked and to be returned if it's not equal null
	 * @param defaultValue the second object to be checked and to be returned if the first object is null and the second isn't
	 * @return first if it is non-null; otherwise second if it is non-null
	 */
	public static int orNoNullUnbox(@Nullable Integer object, int defaultValue) {
		return noNull(or(object, defaultValue));
	}

	/**
	 * Returns the first of two given parameters that is not null. The second parameter HAS to be nonnull.
	 * Helper method that does unboxing as well since most of the IDEs have problems resolving over that barrier.
	 *
	 * @param object the first object to be checked and to be returned if it's not equal null
	 * @param defaultValue the second object to be checked and to be returned if the first object is null and the second isn't
	 * @return first if it is non-null; otherwise second if it is non-null
	 */
	public static short orNoNullUnbox(@Nullable Short object, short defaultValue) {
		return noNull(or(object, defaultValue));
	}

	/**
	 * Returns the first of two given parameters that is not null. The second parameter HAS to be nonnull.
	 * Helper method that does unboxing as well since most of the IDEs have problems resolving over that barrier.
	 *
	 * @param object the first object to be checked and to be returned if it's not equal null
	 * @param defaultValue the second object to be checked and to be returned if the first object is null and the second isn't
	 * @return first if it is non-null; otherwise second if it is non-null
	 */
	public static long orNoNullUnbox(@Nullable Long object, long defaultValue) {
		return noNull(or(object, defaultValue));
	}

	/**
	 * Returns the first of two given parameters that is not null. The second parameter HAS to be nonnull.
	 * Helper method that does unboxing as well since most of the IDEs have problems resolving over that barrier.
	 *
	 * @param object the first object to be checked and to be returned if it's not equal null
	 * @param defaultValue the second object to be checked and to be returned if the first object is null and the second isn't
	 * @return first if it is non-null; otherwise second if it is non-null
	 */
	public static char orNoNullUnbox(@Nullable Character object, char defaultValue) {
		return noNull(or(object, defaultValue));
	}

	/**
	 * Returns the first of two given parameters that is not null. The second parameter HAS to be nonnull.
	 * Helper method that does unboxing as well since most of the IDEs have problems resolving over that barrier.
	 *
	 * @param object the first object to be checked and to be returned if it's not equal null
	 * @param defaultValue the second object to be checked and to be returned if the first object is null and the second isn't
	 * @return first if it is non-null; otherwise second if it is non-null
	 */
	public static byte orNoNullUnbox(@Nullable Byte object, byte defaultValue) {
		return noNull(or(object, defaultValue));
	}

	/**
	 * Returns the first of two given parameters that is not null. The second parameter HAS to be nonnull.
	 * Helper method that does unboxing as well since most of the IDEs have problems resolving over that barrier.
	 *
	 * @param object the first object to be checked and to be returned if it's not equal null
	 * @param defaultValue the second object to be checked and to be returned if the first object is null and the second isn't
	 * @return first if it is non-null; otherwise second if it is non-null
	 */
	public static float orNoNullUnbox(@Nullable Float object, float defaultValue) {
		return noNull(or(object, defaultValue));
	}

	/**
	 * Returns the first of two given parameters that is not null. The second parameter HAS to be nonnull.
	 * Helper method that does unboxing as well since most of the IDEs have problems resolving over that barrier.
	 *
	 * @param object the first object to be checked and to be returned if it's not equal null
	 * @param defaultValue the second object to be checked and to be returned if the first object is null and the second isn't
	 * @return first if it is non-null; otherwise second if it is non-null
	 */
	public static boolean orNoNullUnbox(@Nullable Boolean object, boolean defaultValue) {
		return noNull(or(object, defaultValue));
	}

	/**
	 * Checks that the specified object reference is not {@code null}.
	 * <p>
	 * This method is designed primarily for doing parameter validation in methods and constructors with multiple parameters.
	 *
	 * @param <T> the generic type of the object to be checked
	 * @param object the object reference to check for null
	 * @return object if not {@code null}
	 * @throws NullPointerException if object is {@code null}
	 */
	public static <T> T noNull(@Nullable T object) {
		if (object == null){
			throw new NullPointerException();
		}
		return object;
	}

	/**
	 * Checks that the specified object reference is not {@code null} and throws a customized {@link NullPointerException} if it is.
	 * <p>
	 * This method is designed primarily for doing parameter validation in methods and constructors with multiple parameters.
	 *
	 * @param <T> the generic type of the object to be checked
	 * @param object the object reference to check for null
	 * @param message message to be used in the eventually thrown {@link NullPointerException}
	 * @return object if not {@code null}
	 * @throws NullPointerException if object is {@code null}
	 */
	public static <T> T noNull(@Nullable T object, @Nullable String message) {
		if (object == null)
			throw new NullPointerException(message);
		return object;
	}
}
