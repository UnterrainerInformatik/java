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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import javax.annotation.Nullable;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StreamUtils {

	/**
	 * Converts an input-stream to a {@link String}.
	 * <p>
	 * The caller is responsible for closing the {@link InputStream}.
	 *
	 * @param inputStream the {@link InputStream}
	 * @return the content of the {@link InputStream} or {@code null} in case an {@link IOException} occurred
	 * @throws IOException if the {@link InputStream} could not be read
	 */
	@Nullable
	public static String toString(@Nullable InputStream inputStream) throws IOException {
		if (inputStream == null) {
			return null;
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
		StringBuilder out = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			out.append(line);
		}
		return out.toString();
	}

	/**
	 * Gets a part of a stream given by a start-index and end-index by lines.
	 *
	 * @param inputStream {@link InputStream} the input stream to read from
	 * @param outputStream {@link OutputStream} the output stream to write to
	 * @param startLineIndex the start line index
	 * @param endLineIndex the end line index. If you want all until the file ends, just specify -1 here
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void partByLines(final InputStream inputStream, final OutputStream outputStream, final long startLineIndex, final long endLineIndex)
			throws IOException {
		part(inputStream, outputStream, startLineIndex, endLineIndex, true, false, false);
	}

	/**
	 * Gets a part of a stream given by a start-index and end-index by words.
	 *
	 * @param inputStream {@link InputStream} the input stream to read from
	 * @param outputStream {@link OutputStream} the output stream to write to
	 * @param startWordIndex the start word index
	 * @param endWordIndex the end word index. If you want all until the file ends, just specify -1 here
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void partByWords(final InputStream inputStream, final OutputStream outputStream, final long startWordIndex, final long endWordIndex)
			throws IOException {
		part(inputStream, outputStream, startWordIndex, endWordIndex, false, true, false);
	}

	/**
	 * Gets a part of a stream given by a start-index and end-index by characters.
	 *
	 * @param inputStream {@link InputStream} the input stream to read from
	 * @param outputStream {@link OutputStream} the output stream to write to
	 * @param startCharacterIndex the start character index
	 * @param endCharacterIndex the end character index. If you want all until the file ends, just specify -1 here
	 */
	public static void partByCharacters(final InputStream inputStream, final OutputStream outputStream, final long startCharacterIndex,
			final long endCharacterIndex) throws IOException {
		part(inputStream, outputStream, startCharacterIndex, endCharacterIndex, false, false, true);
	}

	/**
	 * Gets a part of a stream given by a start-index and end-index.
	 *
	 * @param inputStream {@link InputStream} the input stream to read from
	 * @param outputStream {@link OutputStream} the output stream to write to
	 * @param startIndex the start index
	 * @param endIndex the end index. If you want all until the file ends, just specify -1 here
	 * @param indexIsLine the index is line-controlled
	 * @param indexIsWord the index is word-controlled
	 * @param indexIsCharacter the index is character-controlled
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void part(final InputStream inputStream, final OutputStream outputStream, final long startIndex, final long endIndex,
			final boolean indexIsLine, final boolean indexIsWord, final boolean indexIsCharacter) throws IOException {
		try {
			final int bufferSize = 1024;
			byte[] c = new byte[bufferSize];
			long count = 0;
			int readChars = 0;
			while ((readChars = inputStream.read(c)) != -1) {
				for (int i = 0; i < readChars; i++) {
					byte b = c[i];
					if (indexIsCharacter) {
						count++;
					} else if (indexIsWord && b == ' ') {
						count++;
					} else if (indexIsLine && b == '\n') {
						count++;
					}
					if (count >= startIndex && (endIndex == -1 || count <= endIndex)) {
						outputStream.write(b);
					}
				}
			}
		} finally {
			inputStream.close();
		}
	}

	/**
	 * Gets a part of a stream given by a start-index and end-index by characters.
	 *
	 * @param inputStream {@link InputStream} the input stream to read from
	 * @param outputStream {@link OutputStream} the output stream to write to
	 * @param startCharacterIndex the start character index
	 * @param endCharacterIndex the end character index
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void getPartByCharacters(final InputStream inputStream, final OutputStream outputStream, final long startCharacterIndex,
			final long endCharacterIndex) throws IOException {
		try {
			final int bufferSize = 1024;
			byte[] c = new byte[bufferSize];
			long count = 0;
			int readChars = 0;
			while ((readChars = inputStream.read(c)) != -1) {
				for (int i = 0; i < readChars; i++) {
					count++;
					if (count >= startCharacterIndex && count <= endCharacterIndex) {
						outputStream.write(c[i]);
					}
				}
			}
		} finally {
			inputStream.close();
		}
	}

	/**
	 * Gets the number of lines/words/characters contained in a given input-stream by counting the number of occurrence of line-feed
	 * characters/spaces/characters.<br/>
	 * It does this by reading the stream step-by-step (in order to prevent holding the whole file in memory) and by counting the number of occurrences of
	 * line-feed characters in that buffer.
	 * <p>
	 * This method is by far the fastest method to do that.<br/>
	 * Info: The word-count is done by counting spaces.
	 *
	 * @param inputStream {@link InputStream} the input stream to scan
	 * @return the count properties {@link CountProperties}
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static CountProperties getCountProperties(final InputStream inputStream) throws IOException {
		try {
			final int bufferSize = 1024;
			byte[] c = new byte[bufferSize];
			long lineCount = 0;
			long wordCount = 0;
			long characterCount = 0;
			int readChars = 0;
			while ((readChars = inputStream.read(c)) != -1) {
				for (int i = 0; i < readChars; i++) {
					characterCount++;
					if (c[i] == '\n') {
						lineCount++;
						wordCount++;
					} else if (c[i] == ' ') {
						wordCount++;
					}
				}
			}
			lineCount++;
			wordCount++;
			return new CountProperties(lineCount, wordCount, characterCount);
		} finally {
			inputStream.close();
		}
	}
}
