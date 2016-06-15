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

package info.unterrainer.java.tools.utils.files;

import lombok.Getter;
import lombok.Setter;

import java.nio.charset.Charset;

public enum Encoding {

	UTF8("UTF-8"),
	UTF16("UTF-16"),
	UTF16LE("UTF-16, Little Endian (LE)"),
	UTF16BE("UTF-16, Big Endian (BE)"),
	UTF32("UTF-32"),
	UTF32LE("UTF-32, Little Endian (LE)"),
	UTF32BE("UTF-32, Big Endian (BE)"),
	CP437("Cp437"),
	ANSI("ANSI"),
	ISO88591("ISO-8859-1"),
	CP850("Cp850");

	@Getter
	@Setter
	private String encoding;

	Encoding(final String encoding) {
		setEncoding(encoding);
	}

	public Charset toCharset() {
		return Charset.forName(getEncoding());
	}

	@Override
	public String toString() {
		return getEncoding();
	}
}