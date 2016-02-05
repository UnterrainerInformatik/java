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

import info.unterrainer.java.tools.utils.NullUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.Serializable;
import java.util.Map.Entry;

/**
 * This class is a tuple containing two variables of possibly two different types. This is a convenience class that should ease the burden when requiring
 * multiple return values.
 * <p>
 * It implements {@link Serializable}, so it is serializable as long as the parameters are serializable.
 * <p>
 * This class implements a legacy interface ({@link Entry}) that is not annotated with null-annotations.<br>
 * In order to do that we reset the {@link ParametersAreNonnullByDefault} to <code>none</code>.
 *
 * @param <A> the type of the first parameter.
 * @param <B> the type of the second parameter.
 */
@ParametersAreNonnullByDefault({})
@AllArgsConstructor
@NoArgsConstructor
public final class Tuple2<A, B> implements Serializable, Entry<A, B> {

	private static final long serialVersionUID = 907647270469007515L;

	@Getter
	@Setter
	private A a;

	@Getter
	@Setter
	private B b;

	@Override
	public String toString() {
		String aString = a != null ? a.toString() : null;
		String bString = b != null ? b.toString() : null;
		return "Tuple2: [a=" + aString + ", b=" + bString + "]";
	}

	@Override
	public A getKey() {
		return a;
	}

	@Override
	public B getValue() {
		return b;
	}

	@Override
	public B setValue(B value) {
		B old = this.getValue();
		this.setB(NullUtils.noNull(value));
		return old;
	}
}