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

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NullUtils {

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
	 * Checks that the specified object reference is not {@code null}.
	 * <p>
	 * This method is designed primarily for doing parameter validation in methods and constructors with multiple parameters.
	 *
	 * @param <T> the generic type of the object to be checked
	 * @param object the object reference to check for null
	 * @return object if not {@code null}
	 * @throws {@link NullPointerException} if object is {@code null}
	 */
	@Nonnull
	public static <T> T noNull(@Nullable T object) {
		return Objects.requireNonNull(object);
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
	 * @throws {@link NullPointerException} if object is {@code null}
	 */
	@Nonnull
	public static <T> T noNull(@Nullable T object, String message) {
		return Objects.requireNonNull(object, message);
	}
}
