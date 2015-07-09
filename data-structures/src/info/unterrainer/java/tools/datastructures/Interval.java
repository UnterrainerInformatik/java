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

package info.unterrainer.java.tools.datastructures;

public class Interval<T extends Comparable<T>> {

	private T maximalValue;
	private T minimalValue;

	/**
	 * Initializes a new instance of the {@link Interface} structure.
	 *
	 * @param min
	 *            the minimal value
	 * @param max
	 *            the maximal value
	 * @throws IllegalArgumentException
	 *             if the given maximal-value is smaller than the given minimal-value
	 */
	public Interval(T min, T max) {

		if (min.compareTo(max) > 0 || max.compareTo(min) < 0) {
			throw new IllegalArgumentException("Min has to be smaller than or equal to max.");
		}
		minimalValue = min;
		maximalValue = max;
	}

	/**
	 * Gets or sets the minimal value. Adjusts the maximal-value as well if it should currently be smaller than the given value.
	 *
	 * @return the minimal value of this interval
	 */
	public T getMin() {
		return minimalValue;
	}

	public void setMin(T value) {
		minimalValue = value;
		if (value.compareTo(maximalValue) > 0) {
			maximalValue = value;
		}
	}

	/**
	 * Gets or sets the maximal value. Adjusts the minimal-value as well if it should currently be smaller than the given value.
	 *
	 * @return the maximal value of this interval
	 */
	public T getMax() {
		return maximalValue;
	}

	public void setMax(T value) {
		maximalValue = value;
		if (value.compareTo(minimalValue) < 0) {
			minimalValue = value;
		}
	}

	/**
	 * Clamps the given value to the {@link Interval}. If the value is greater than the interval's maximal-value, the interval's maximal-value is returned. If
	 * the value is smaller than the interval's minimal-value, the interval's minimal-value is returned.
	 *
	 * @param value
	 * @return the clamped value
	 */
	public T clamp(T value) {
		if (value.compareTo(maximalValue) > 0) {
			return maximalValue;
		}
		if (value.compareTo(minimalValue) < 0) {
			return minimalValue;
		}
		return value;
	}

	/**
	 * Determines whether a specified value is in between the intervals boundaries.
	 *
	 * @param value
	 *            the value
	 * @return true if the given value is in between the specified boundaries
	 */
	public boolean isInBetween(T value) {
		if (value.compareTo(getMin()) < 0) {
			return false;
		}

		return value.compareTo(getMax()) <= 0;
	}

	@Override
	public String toString() {
		return "[" + minimalValue + ":" + maximalValue + "]";
	}
}
