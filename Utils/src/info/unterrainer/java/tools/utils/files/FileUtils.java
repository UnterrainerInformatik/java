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

import info.unterrainer.java.tools.utils.NullUtils;
import info.unterrainer.java.tools.utils.StringUtils;
import info.unterrainer.java.tools.utils.streams.CountProperties;
import info.unterrainer.java.tools.utils.streams.StreamUtils;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import lombok.Cleanup;
import lombok.experimental.ExtensionMethod;
import lombok.experimental.UtilityClass;

@UtilityClass
@ExtensionMethod({ StringUtils.class, StreamUtils.class, NullUtils.class })
public final class FileUtils {

	/**
	 * Gets a list of files located in the given directory matching the given file-ending.
	 *
	 * @param dir the directory to search in
	 * @param ending the file-ending to match
	 * @param isRecursive if true, searches all sub-directories as well
	 * @return a list of found files
	 */
	public static List<String> getFileList(File dir, String ending, boolean isRecursive) {
		return getFileList(dir, ending, isRecursive, new ArrayList<>());
	}

	private static List<String> getFileList(File dir, String ending, boolean isRecursive, List<String> result) {
		Arrays.stream(dir.listFiles((f, n) -> !n.startsWith(".") && (f.isDirectory() || n.toLowerCase().endsWith(ending)))).forEach(unchecked((file) -> {
			if (!file.isDirectory()) {
				result.add(file.getCanonicalPath());
			}

			if (file.isDirectory()) {
				getFileList(file, ending, isRecursive, result);
			}
		}));
		return result;
	}

	/**
	 * This utility simply wraps a functional interface that throws a checked exception into a Java 8 Consumer
	 */
	private static <T> Consumer<T> unchecked(CheckedConsumer<T> consumer) {
		return t -> {
			try {
				consumer.accept(t);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		};
	}

	@FunctionalInterface
	private interface CheckedConsumer<T> {
		void accept(T t) throws Exception;
	}

	/**
	 * Gets the number of lines/words/characters contained in a given string by counting the number of occurrence of line-feed characters/spaces/characters. <br/>
	 * It does this by reading the stream step-by-step (in order to prevent holding the whole file in memory) and by counting the number of occurrences of
	 * line-feed characters in that buffer.
	 * <p>
	 * This method is by far the fastest method to do that.<br/>
	 * Info: The word-count is done by counting spaces.
	 *
	 * @param input {@link String} the input string to scan
	 * @return the count properties {@link CountProperties}
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static CountProperties getCountPropertiesOf(final String input) throws IOException {
		return new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)).getCountProperties();
	}

	/**
	 * Gets the number of lines/words/characters contained in a given file by counting the number of occurrence of line-feed characters/spaces/characters.<br/>
	 * It does this by reading the stream step-by-step (in order to prevent holding the whole file in memory) and by counting the number of occurrences of
	 * line-feed characters in that buffer.
	 * <p>
	 * This method is by far the fastest method to do that.<br/>
	 * Info: The word-count is done by counting spaces.
	 *
	 * @param file {@link File} the file to open for scanning
	 * @return the count properties {@link CountProperties}
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static CountProperties getCountPropertiesOf(final File file) throws IOException {
		return new BufferedInputStream(new FileInputStream(file)).getCountProperties();
	}

	/**
	 * Copies a whole directory including all files and sub-directories.
	 *
	 * @param sourcePath path of the directory which should be copied
	 * @param destinationPath path where the directory gets copied to
	 * @return true, if successful
	 */
	public static boolean copyDir(final String sourcePath, final String destinationPath) {
		final File source = new File(sourcePath);
		final File destination = new File(destinationPath);
		return copyDir(source, destination);
	}

	/**
	 * Copies a whole directory including all files and sub-directories.
	 *
	 * @param sourcePath path of the directory which should be copied
	 * @param destinationPath path where the directory gets copied to
	 * @return true, if successful
	 */
	public static boolean copyDir(final File sourcePath, final File destinationPath) {
		if (sourcePath.isDirectory()) {
			if (!destinationPath.exists()) {
				destinationPath.mkdirs();
			}
			final String[] children = sourcePath.list();
			for (final String element : children) {
				final File sourceFile = new File(sourcePath, element);
				final File destination = new File(destinationPath, element);
				if (sourceFile.isDirectory()) {
					if (!copyDir(sourceFile, destination)) {
						return false;
					}
				} else {
					try {
						FileUtils.copyFile(sourceFile, destination);
					} catch (IOException e) {
						return false;
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Copies a given file to a given destination.
	 *
	 * @param source the source file
	 * @param destination the destination file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void copyFile(final String source, final String destination) throws IOException {
		copyFile(new File(source), new File(destination));
	}

	/**
	 * Copies a given file to a given destination.
	 *
	 * @param source {@link File} the source
	 * @param destination {@link File} the destination
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void copyFile(@Nullable File source, @Nullable File destination) throws IOException {
		if (source == null || destination == null) {
			return;
		}

		@Cleanup
		FileInputStream inputStream = new FileInputStream(source);
		@Cleanup
		FileChannel inputChannel = inputStream.getChannel();

		@Cleanup
		FileOutputStream outputStream = new FileOutputStream(destination);
		@Cleanup
		FileChannel outputChannel = outputStream.getChannel();

		outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
	}

	/**
	 * Creates random file content in a given file.
	 *
	 * @param file {@link File} the file to create with random content
	 * @param length the length of the random content in bytes
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void createRandomFileContent(final File file, final long length) throws IOException {
		FileOutputStream fos;
		fos = new FileOutputStream(file);
		final byte[] b = new byte[1];
		final Random rand = new Random();
		for (long i = 0; i < length; i++) {
			rand.nextBytes(b);
			fos.write(b);
		}
		fos.close();
	}

	/**
	 * Delete a directory with all its sub-directories and files.
	 *
	 * @param directory which gets deleted
	 * @return true, if successful
	 * @throws IOException if an I/O error occurs
	 */
	public static boolean deleteDir(final File directory) throws IOException {
		if (!directory.exists() || !directory.isDirectory()) {
			return false;
		}
		final String[] files = directory.list();
		for (final String file : files) {
			final File f = new File(directory, file);
			if (f.isDirectory()) {
				deleteDir(f);
			} else {
				Files.deleteIfExists(Paths.get(f.getAbsolutePath()));
			}
		}
		Files.deleteIfExists(Paths.get(directory.getAbsolutePath()));
		directory.delete();
		return true;
	}

	/**
	 * Delete a directory with all its sub-directories and files.
	 *
	 * @param directory path to the directory which gets deleted
	 * @return true, if successful
	 * @throws IOException if an I/O error occurs
	 */
	public static boolean deleteDir(final String directory) throws IOException {
		final File dir = new File(directory);
		return FileUtils.deleteDir(dir);
	}

	/**
	 * Returns the file extension.
	 *
	 * @param fileName {@link String} the file name
	 * @return String file extension
	 */
	public static String getExtension(final String fileName) {
		return fileName.substring(fileName.lastIndexOf('.') + 1);
	}

	/**
	 * Deletes a file.
	 *
	 * @param fileName the file name
	 * @return true, if the file has been deleted, otherwise false
	 */
	public static boolean delete(final String fileName) {
		boolean success = false;
		final File file = new File(fileName);
		if (file.exists()) {
			success = file.delete();
		}
		return success;
	}

	/**
	 * Returns the file extension.
	 *
	 * @param fullFileName {@link String} the full file name
	 * @return String : file extension
	 */
	@Nullable
	public static String getNameNoExtension(final String fullFileName) {
		String retVal = null;
		// Get file name only
		final String fileName = getName(fullFileName);
		if (!fileName.isEmpty() && fileName.contains(".")) {
			retVal = fileName.substring(0, fileName.lastIndexOf('.'));
		}
		return retVal;
	}

	/**
	 * Returns the short filename for a filename with path.
	 *
	 * @param fullFileName {@link String} the full file name
	 * @return short file name
	 */
	public static String getName(final String fullFileName) {
		String retVal = null;
		// Check separator character
		if (fullFileName.contains(String.valueOf(File.separatorChar))) {
			retVal = fullFileName.substring(fullFileName.lastIndexOf(File.separatorChar) + 1);
		} else {
			// Check separator character
			if (fullFileName.contains(String.valueOf("/"))) {
				// Suspect Unix-style path delimiter
				retVal = fullFileName.substring(fullFileName.lastIndexOf('/') + 1);
			} else {
				retVal = fullFileName;
			}
		}
		return retVal;
	}

	/**
	 * Returns the path (without the file-name) for a filename with path.
	 *
	 * @param fullFileName {@link String} the full file name
	 * @return the path as a string
	 */
	public static String getPath(final String fullFileName) {
		return Paths.get(fullFileName).getParent().toString();
	}

	/**
	 * Reads the contents of a file and returns it as a byteArray taking into account all the exceptions that might occur.
	 *
	 * @param file {@link File} the file
	 * @return the string {@link String}
	 */
	public static byte[] readToByteArray(final File file) throws IOException {
		return java.nio.file.Files.readAllBytes(Paths.get(file.getAbsolutePath()));
	}

	/**
	 * Open a file and read its content to a list of strings.
	 * <p>
	 * Assumes UTF8 as default {@link Charset}.
	 *
	 * @param file the file to read
	 * @return an ArrayList containing all the lines of the read file
	 */
	public static List<String> readToList(@Nullable File file) throws IOException {
		if (file == null) {
			return Collections.emptyList();
		}

		return readToList(file.toPath(), Encoding.UTF8.toCharset());
	}

	/**
	 * Open a file and read its content to a list of strings.
	 *
	 * @param file the file to read
	 * @param cs the {@link Charset} to use (defaults to UTF-8)
	 * @return an ArrayList containing all the lines of the read file
	 */
	public static List<String> readToList(@Nullable File file, @Nullable Charset cs) throws IOException {
		if (file == null) {
			return Collections.emptyList();
		}
		return readToList(file.toPath(), cs);
	}

	/**
	 * Open a file and read its content to a list of strings.
	 * <p>
	 * Assumes UTF8 as default {@link Charset}.
	 *
	 * @param path the path to the file to read
	 * @return an ArrayList containing all the lines of the read file
	 */
	public static List<String> readToList(@Nullable Path path) throws IOException {
		return Files.lines(path).collect(Collectors.toList());
	}

	/**
	 * Open a file and read its content to a list of strings.
	 *
	 * @param path the path to the file to read
	 * @param cs the {@link Charset} to use (defaults to UTF-8)
	 * @return an ArrayList containing all the lines of the read file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static List<String> readToList(@Nullable Path path, @Nullable Charset cs) throws IOException {
		if (path == null) {
			return Collections.emptyList();
		}
		Charset charSet = cs;
		if (cs == null) {
			charSet = Encoding.UTF8.toCharset();
		}
		return Arrays.asList((String[]) Files.lines(path, charSet).toArray());
	}

	/**
	 * Reads the contents of a file and returns it as a string taking into account all the exceptions that might occur.
	 *
	 * @param file {@link File} the file to read
	 * @param encoding {@link Encoding} the encoding to expect when putting the file-content to a string
	 * @return the string {@link String} that consists of the file-content
	 */
	public static String readToString(final File file, final Encoding encoding) throws IOException {
		return new String(readToByteArray(file), encoding.getEncoding());
	}

	/**
	 * Write a list of strings to a file separated by a newline character. Always appends to the target-file.
	 *
	 * @param file path to the file
	 * @param list list of strings which gets written into the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writeListTo(final File file, final List<String> list) throws IOException {
		writeListTo(file, list, true);
	}

	/**
	 * Write a list of strings to a file separated by a newline character.
	 *
	 * @param file path to the file
	 * @param list list of strings which gets written into the file
	 * @param append true = append to file, false = overwrite
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writeListTo(final File file, final List<String> list, final boolean append) throws IOException {
		FileWriter fstream = null;
		fstream = new FileWriter(file, append);
		final BufferedWriter out = new BufferedWriter(fstream);
		for (Integer i = 0; i < list.size(); i++) {
			out.write(list.get(i) + "\n");
		}
		out.close();
	}

	/**
	 * Writes a string to file taking into account the character encoding and all exceptions that might be thrown.
	 * <p>
	 * If the file already exists, the content will be appended (append = true).
	 *
	 * @param file {@link File} the file to write to. May or may not exist.
	 * @param encoding {@link Encoding} the encoding to be used
	 * @param data {@link String} the data to be written
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writeTo(final File file, final Encoding encoding, final String data) throws IOException {
		writeTo(file, encoding, data, true);
	}

	/**
	 * Writes a string to file taking into account the character encoding and all exceptions that might be thrown.
	 *
	 * @param file {@link File} the file to write to. May or may not exist.
	 * @param encoding {@link Encoding} the encoding to be used
	 * @param data {@link String} the data to be written
	 * @param append a flag indicating if the content of the file should be overwritten (replaced) or appended at the end of the existing file
	 */
	public static void writeTo(final File file, final Encoding encoding, final String data, final boolean append) throws IOException {

		final CharsetEncoder charsetEncoder = Charset.forName(encoding.getEncoding()).newEncoder();
		@Cleanup
		OutputStream out = new FileOutputStream(file, append);
		@Cleanup
		Writer writer = new OutputStreamWriter(out, charsetEncoder);

		writer.write(data);
	}

	/**
	 * Gets all sub-directories of given directory.
	 *
	 * @param directory the path of the directory to scan
	 * @return a file array containing all the sub-directories found
	 */
	public File[] getSubDirectories(final String directory) {
		final File source = new File(directory);
		final FileFilter fileFilter = new FileFilter() {
			@Override
			public boolean accept(@Nullable File file) {
				return file.noNull().isDirectory();
			}
		};
		return source.listFiles(fileFilter);
	}
}