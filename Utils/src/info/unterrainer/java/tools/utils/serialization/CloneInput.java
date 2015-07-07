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

package info.unterrainer.java.tools.utils.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

public class CloneInput extends ObjectInputStream {
	private final CloneOutput output;

	CloneInput(final InputStream in, final CloneOutput output) throws IOException {
		super(in);
		this.output = output;
	}

	@Override
	protected Class<?> resolveClass(final ObjectStreamClass osc) throws IOException, ClassNotFoundException {
		Class<?> c = output.getClassQueue().poll();
		String expected = osc.getName();
		String found = (c == null) ? null : c.getName();
		if (!expected.equals(found)) {
			throw new InvalidClassException("Classes desynchronized: " + "found " + found + " when expecting " + expected);
		}
		return c;
	}

	@Override
	protected Class<?> resolveProxyClass(final String[] interfaceNames) throws IOException, ClassNotFoundException {
		return output.getClassQueue().poll();
	}
}