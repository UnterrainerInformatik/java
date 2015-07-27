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

import java.lang.reflect.Array;

import lombok.experimental.UtilityClass;

/**
 * This static utility-class contains methods concerned with arrays.
 */
@UtilityClass
public final class ArrayUtils {

	/**
	 * Concatenates two arrays of the same generic type to a new one.
	 *
	 * @param <T> the generic type of both arrays
	 * @param first the first part of the new array
	 * @param second the second part of the new array
	 * @return a new array containing the result of the concatenation of the first and second array.
	 */
	public static <T> T[] concatenate(T[] first, T[] second) {
		int aLen = first.length;
		int bLen = second.length;

		@SuppressWarnings("unchecked")
		T[] concatenateArray = (T[]) Array.newInstance(first.getClass().getComponentType(), aLen + bLen);
		System.arraycopy(first, 0, concatenateArray, 0, aLen);
		System.arraycopy(second, 0, concatenateArray, aLen, bLen);

		return concatenateArray;
	}
}