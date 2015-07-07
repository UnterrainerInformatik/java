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
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Queue;

import lombok.Getter;

public class CloneOutput extends ObjectOutputStream {
	@Getter
	private final Queue<Class<?>> classQueue = new LinkedList<Class<?>>();

	CloneOutput(final OutputStream out) throws IOException {
		super(out);
	}

	@Override
	protected void annotateClass(final Class<?> c) {
		classQueue.add(c);
	}

	@Override
	protected void annotateProxyClass(final Class<?> c) {
		classQueue.add(c);
	}
}
