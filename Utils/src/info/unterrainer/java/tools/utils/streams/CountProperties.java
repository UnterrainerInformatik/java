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

package info.unterrainer.java.tools.utils.streams;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class CountProperties {

	@Getter
	private final long lineCount;
	@Getter
	private final long wordCount;
	@Getter
	private final long characterCount;

	@Override
	public String toString() {
		return "[" + lineCount + "] lines, [" + wordCount + "] words, [" + characterCount + "] characters";
	}
}
