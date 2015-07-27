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

import info.unterrainer.java.tools.utils.StreamUtils;
import info.unterrainer.java.tools.utils.StringUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.function.Consumer;

import lombok.Cleanup;
import lombok.experimental.ExtensionMethod;
import lombok.experimental.UtilityClass;

@UtilityClass
@ExtensionMethod(StringUtils.class)
public final class FileUtils {

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
		return StreamUtils.getCountPropertiesOf(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
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
		return StreamUtils.getCountPropertiesOf(new BufferedInputStream(new FileInputStream(file)));
	}

	/**
	 * Copy directory.
	 *
	 * @param sourcePath path of the directory which should be copied
	 * @param destinationPath path where the directory gets copied to
	 * @return true, if successful
	 */
	public static boolean copyDirectory(final File sourcePath, final File destinationPath) {
		if (sourcePath.isDirectory()) {
			if (!destinationPath.exists()) {
				destinationPath.mkdirs();
			}
			final String[] children = sourcePath.list();
			for (final String element : children) {
				final File sourceFile = new File(sourcePath, element);
				final File destination = new File(destinationPath, element);
				if (sourceFile.isDirectory()) {
					if (!copyDirectory(sourceFile, destination)) {
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
	 * Copies the given file to the given destination.
	 *
	 * @param source {@link File} the source
	 * @param destination {@link File} the destination
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void copyFile(final File source, final File destination) throws IOException {
		@Cleanup
		FileChannel inputChannel = null;
		@Cleanup
		FileChannel outputChannel = null;

		inputChannel = new FileInputStream(source).getChannel();
		outputChannel = new FileOutputStream(destination).getChannel();
		outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
	}

	/**
	 * Copy directory.
	 *
	 * @param sourcePath path of the directory which should be copied
	 * @param destinationPath path where the directory gets copied to
	 * @return true, if successful
	 */
	public static boolean copyDirectory(final String sourcePath, final String destinationPath) {
		final File source = new File(sourcePath);
		final File destination = new File(destinationPath);
		return copyDirectory(source, destination);
	}

	/**
	 * Copy a file.
	 *
	 * @param srcFile the source file
	 * @param dstFile the destination file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void copyFile(final String srcFile, final String dstFile) throws IOException {
		final File src = new File(srcFile);
		final File dest = new File(dstFile);
		@Cleanup
		InputStream in = null;
		@Cleanup
		OutputStream out = null;

		in = new FileInputStream(src);
		out = new FileOutputStream(dest);
		final int buffersize = 1024;
		final byte[] buf = new byte[buffersize];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
	}

	/**
	 * Creates the directory/sub-directory-structure as a sibling of the given parent directory (if it doesn't already exist in the first place).
	 *
	 * @param parent {@link File} the parent directory to create the new structure in
	 * @param directory {@link String} the directory or directory with sub-directories to create, if it doesn't already exist
	 */
	public static void createDirectory(final File parent, final String directory) {
		final File dir = new File(parent, directory);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	/**
	 * creates a file with known name.
	 *
	 * @param fileName file name and path
	 * @return file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static File createFile(final String fileName) throws IOException {
		final File file = new File(fileName);
		file.createNewFile();
		return file;
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
	 */
	public static boolean deleteDirectory(final File directory) {
		if (!directory.exists() || !directory.isDirectory()) {
			return false;
		}
		final String[] files = directory.list();
		for (final String file : files) {
			final File f = new File(directory, file);
			if (f.isDirectory()) {
				deleteDirectory(f);
			} else {
				FileUtils.deleteFile(f.getAbsolutePath());
			}
		}
		FileUtils.deleteFile(directory.getAbsolutePath());
		directory.delete();
		return true;
	}

	/**
	 * Delete a directory with all its sub-directories and files.
	 *
	 * @param directory path to the directory which gets deleted
	 * @return true, if successful
	 */
	public static boolean deleteDirectory(final String directory) {
		final File dir = new File(directory);
		return FileUtils.deleteDirectory(dir);
	}

	/**
	 * Delete file.
	 *
	 * @param fileName the file name
	 * @return true, if delete file
	 */
	public static boolean deleteFile(final String fileName) {
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
	 * @param fileName {@link String} the file name
	 * @return String file extension
	 */
	public static String getFileExtension(final String fileName) {
		return fileName.substring(fileName.lastIndexOf('.') + 1);
	}

	/**
	 * Returns the file extension.
	 *
	 * @param fullFileName {@link String} the full file name
	 * @return String : file extension
	 */
	public static String getFileNameNoExt(final String fullFileName) {
		String retVal = null;
		// Get file name only
		final String fileName = getFileNameOnly(fullFileName);
		if (!StringUtils.isNullOrEmpty(fileName) && fileName.contains(".")) {
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
	public static String getFileNameOnly(final String fullFileName) {
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
	 * Returns the directory the file is located in.
	 *
	 * @param fullFileName {@link String} the full file name
	 * @return file directory
	 */
	public static String getFilePath(final String fullFileName) {
		String retVal = null;
		// Check separator character
		if (fullFileName.contains(String.valueOf(File.separatorChar))) {
			retVal = fullFileName.substring(0, fullFileName.lastIndexOf(File.separatorChar));
		} else {
			// Check separator character
			if (fullFileName.contains(String.valueOf(File.separatorChar))) {
				// Suspect Unix-style path delimiter
				retVal = fullFileName.substring(0, fullFileName.lastIndexOf('/'));
			}
		}
		return retVal;
	}

	/**
	 * Gets the file-name from a string containing a path and file-name.
	 *
	 * @param fileNameAndPath {@link String} the file name and path to get the file-name from
	 * @return the name {@link String} of the file
	 */
	public static String getName(final String fileNameAndPath) {
		final int lastIndex = fileNameAndPath.lastIndexOf('/');
		if (lastIndex == -1) {
			return fileNameAndPath;
		}
		return fileNameAndPath.substring(lastIndex + 1);
	}

	/**
	 * Gets the path from a string containing a file-name.
	 *
	 * @param fileNameAndPath {@link String} the file name and path to get the path from
	 * @return the path {@link String} or an empty string, if the given fileNameAndPath only contained a file-name.
	 */
	public static String getPath(final String fileNameAndPath) {
		final int lastIndex = fileNameAndPath.lastIndexOf('/');
		if (lastIndex == -1) {
			return "";
		}
		return fileNameAndPath.substring(0, lastIndex + 1);
	}

	/**
	 * Checks if is file existing.
	 *
	 * @param fileName {@link String} the file name
	 * @return true, if is file existing
	 */
	public static boolean isFileExisting(final String fileName) {
		final File file = new File(fileName);
		return file.exists();
	}

	/**
	 * Load an external component if and only if the given component is null. Returns the found external component's content or the given content, if it wasn't
	 * null in the first place.
	 *
	 * @param content {@link String} the component's content
	 * @param file {@link File} the file
	 * @param encoding {@link Encoding} the encoding
	 * @return the string {@link String}
	 */
	public static String loadComponentIfNull(final String content, final File file, final Encoding encoding) {
		if (content == null) {
			try {
				return readFileToString(file, encoding);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return content;
	}

	/**
	 * Loads a properties-file from disk and returns a properties-object.
	 *
	 * @param pathAndNameAndExtension {@link String} the path and name and extension
	 * @return the properties {@link Properties} that where read from file
	 */
	public static Properties loadProperties(final String pathAndNameAndExtension) throws IOException {
		final Properties properties = new Properties();
		final BufferedInputStream stream = new BufferedInputStream(new FileInputStream(pathAndNameAndExtension));
		properties.load(stream);
		stream.close();
		return properties;
	}

	/**
	 * Opens a file either to be replaced by a new content or to append.
	 *
	 * @param file {@link File} the file
	 * @param encoding {@link Encoding} the encoding
	 * @param append the append
	 * @return the prints the writer {@link PrintWriter}
	 */
	public static PrintWriter openFile(final File file, final Encoding encoding, final boolean append) throws IOException {
		return new PrintWriter(new OutputStreamWriter(new FileOutputStream(file.getAbsoluteFile(), append), encoding.getEncoding()));
	}

	/**
	 * Reads the contents of a file and returns it as a byteArray taking into account all the exceptions that might occur.
	 *
	 * @param file {@link File} the file
	 * @return the string {@link String}
	 */
	public static byte[] readFileToByteArray(final File file) throws IOException {
		return java.nio.file.Files.readAllBytes(Paths.get(file.getAbsolutePath()));
	}

	/**
	 * Open a file and read its content to a list of strings.
	 *
	 * @param file the path to the file to read
	 * @return an ArrayList containing all the lines of the read file
	 */
	public static List<String> readFileToList(final File file) throws IOException {
		String line;
		final ArrayList<String> data = new ArrayList<String>();
		final FileReader fr = new FileReader(file);
		final BufferedReader br = new BufferedReader(fr);
		while ((line = br.readLine()) != null) {
			data.add(line);
		}
		br.close();
		return data;
	}

	/**
	 * Reads the contents of a file and returns it as a string taking into account all the exceptions that might occur.
	 *
	 * @param file {@link File} the file to read
	 * @param encoding {@link Encoding} the encoding to expect when putting the file-content to a string
	 * @return the string {@link String} that consists of the file-content
	 */
	public static String readFileToString(final File file, final Encoding encoding) throws IOException {
		return new String(readFileToByteArray(file), encoding.getEncoding());
	}

	/**
	 * Reads the contents of a file and returns it as a List<String> taking into account all the exceptions that might occur.
	 *
	 * @param file {@link File} the file to read
	 * @param encoding {@link Encoding} the encoding to expect when putting the file-content to a string
	 * @return the string {@link String} that consists of the file-content
	 */
	public static List<String> readFileToList(final File file, final Encoding encoding) throws IOException {
		List<String> result = new ArrayList<>();
		List<String> lines = java.nio.file.Files.readAllLines(Paths.get(file.getAbsolutePath()), encoding.toCharset());

		for (String line : lines) {
			Collections.addAll(result, line.split(";"));
		}
		return result;
	}

	/**
	 * Write a list of strings to a file separated by a newline character. Always appends to the target-file.
	 *
	 * @param file path to the file
	 * @param list list of strings which gets written into the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writeListToFile(final File file, final List<String> list) throws IOException {
		writeListToFile(file, list, true);
	}

	/**
	 * Write a list of strings to a file separated by a newline character.
	 *
	 * @param file path to the file
	 * @param list list of strings which gets written into the file
	 * @param append true = append to file, false = overwrite
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writeListToFile(final File file, final List<String> list, final boolean append) throws IOException {
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
	public static void writeToFile(final File file, final Encoding encoding, final String data) throws IOException {
		writeToFile(file, encoding, data, true);
	}

	/**
	 * Writes a string to file taking into account the character encoding and all exceptions that might be thrown.
	 *
	 * @param file {@link File} the file to write to. May or may not exist.
	 * @param encoding {@link Encoding} the encoding to be used
	 * @param data {@link String} the data to be written
	 * @param append a flag indicating if the content of the file should be overwritten (replaced) or appended at the end of the existing file
	 */
	public static void writeToFile(final File file, final Encoding encoding, final String data, final boolean append) throws IOException {
		@Cleanup
		OutputStream out = null;
		@Cleanup
		Writer writer = null;
		final CharsetEncoder charsetEncoder = Charset.forName(encoding.getEncoding()).newEncoder();

		out = new FileOutputStream(file, append);
		writer = new OutputStreamWriter(out, charsetEncoder);
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
			public boolean accept(final File file) {
				return file.isDirectory();
			}
		};
		return source.listFiles(fileFilter);
	}

	/**
	 * Creates the contains file name filter.
	 *
	 * @param matches the matches
	 * @return the filename filter {@link FilenameFilter}
	 */
	public static FilenameFilter createContainsFileNameFilter(final List<String> matches) {
		return new FilenameFilter() {
			@Override
			public boolean accept(final File dir, final String name) {
				return name.contains(matches);
			}
		};
	}

	/**
	 * Creates the replaced contains file name filter.
	 *
	 * @param matches the matches
	 * @return the filename filter {@link FilenameFilter}
	 */
	public static FilenameFilter createSubStringContainsFileNameFilter(final List<String> matches, final String importInterFacePkg) {
		return new FilenameFilter() {
			@Override
			public boolean accept(final File dir, final String name) {
				return name.substring(importInterFacePkg.length()).contains(matches);
			}
		};
	}

	/**
	 * Creates the contains file filter.
	 *
	 * @param matches the matches
	 * @return the file filter {@link FileFilter}
	 */
	public static FileFilter createContainsFileFilter(final List<String> matches) {
		return new FileFilter() {
			@Override
			public boolean accept(final File pathname) {
				return pathname.getName().contains(matches);
			}
		};
	}

	/**
	 * Creates the file list.
	 *
	 * @param path {@link String} the path
	 * @return the list
	 */
	public static List<File> createRecursiveFileList(final String path) {
		List<File> result = new ArrayList<File>();
		File directory = new File(path);
		File[] fList = directory.listFiles();
		result.add(directory);
		result.addAll(Arrays.asList(fList));
		for (File file : fList) {
			if (file.isDirectory()) {
				result.addAll(createRecursiveFileList(file.getAbsolutePath()));
			}
		}
		return result;
	}

	/**
	 * Assure that the given directory exists.
	 *
	 * @param targetFolder {@link String} the target folder
	 * @return true, if successful
	 */
	public static boolean assureDirExists(final String targetFolder) {
		File f = new File(targetFolder);
		boolean result = true;
		if (!f.exists()) {
			result = f.mkdirs();
		}
		return result;
	}
}