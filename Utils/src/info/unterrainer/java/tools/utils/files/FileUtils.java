/*
 * Copyright 2012 NTS New Technology Systems GmbH. All Rights reserved. NTS PROPRIETARY/CONFIDENTIAL. Use is subject to
 * NTS License Agreement. Address: Doernbacher Strasse 126, A-4073 Wilhering, Austria Homepage: www.ntswincash.com
 */
package info.unterrainer.java.tools.utils.files;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import info.unterrainer.java.tools.utils.StringUtils;

/**
 * The Class Files.
 * <p>
 * This class contains a collection of static helper-methods used in conjunction with files. This is the newest version of such a class. Please use it.
 * <p>
 * If you find that it doesn't contain a method that it should contain, please add it or transfer it from one of the older tool-classes.
 * <p>
 * The base construct of this class is the java.io.File. It is used to build paths and path-filename-combinations and to return most of the values. One thing
 * you have to know before you're starting to use methods of this class is, that a file doesn't have to exist (therefore the exists()-method). You may specify a
 * file and create it later on. Also know that file is a synonym for 'file or folder'. You may concatenate directories with the File(File parent, String
 * current) constructor or files to directories in the same way. Don't bother to care about the right directory-separators since every file-constructor converts
 * them automatically to the system-directory-separator per default.
 * <p>
 * To get the right path (remember: this may be a directory or directory with a filename) from a file, use one of the following:
 * <ul>
 * <li><b>file.getPath()</b><br>
 * gets the path exactly in the same way, as you specified it (relative or not)
 * <li><b>file.getAbsolutePath()</b><br>
 * gets the path you specified and roots it, if it was relative (depending on the system you're using the root may be the user's directory or the currently used
 * directory... see the documentation of this method for further information).
 * <li><b>file.getCanonicalPath()</b><br>
 * returns the same path as file.getAbsolutePath() but solves constructs as 'foo/../bar/../foo/text.txt' as well. Therefore it maybe needs to access the
 * file-system so be prepared to catch the even or odd exception (you won't need this method very often).
 * </ul>
 * <p>
 * Directories never end with a directory-separator, but that's not a problem since you'd concatenate them with the File-constructor and that would add the
 * right one.
 * <p>
 * If you want to get a filename or extension from a File then use FilenameUtils.getName(), getBaseName() or getPath().
 *
 * @author GEUNT
 */
public final class FileUtils {
	private static final Logger logger = LogManager.getLogger(FileUtils.class);

	private static final String END = "'].\n";

	/**
	 * Instantiates a new Files-class. Here in order to hide the public constructor since this is a static utility class.
	 */
	private FileUtils() {
	}

	/**
	 * Gets a part of a stream given by a start-index and end-index by lines.
	 *
	 * @param inputStream
	 *            {@link InputStream} the input stream to read from
	 * @param outputStream
	 *            {@link OutputStream} the output stream to write to
	 * @param startLineIndex
	 *            {@link long} the start line index
	 * @param endLineIndex
	 *            {@link long} the end line index. If you want all until the file ends, just specify -1 here
	 */
	public static void partOfByLines(final InputStream inputStream, final OutputStream outputStream, final long startLineIndex, final long endLineIndex) {
		partOf(inputStream, outputStream, startLineIndex, endLineIndex, true, false, false);
	}

	/**
	 * Gets a part of a stream given by a start-index and end-index by words.
	 *
	 * @param inputStream
	 *            {@link InputStream} the input stream to read from
	 * @param outputStream
	 *            {@link OutputStream} the output stream to write to
	 * @param startWordIndex
	 *            {@link long} the start word index
	 * @param endWordIndex
	 *            {@link long} the end word index. If you want all until the file ends, just specify -1 here
	 */
	public static void partOfByWords(final InputStream inputStream, final OutputStream outputStream, final long startWordIndex, final long endWordIndex) {
		partOf(inputStream, outputStream, startWordIndex, endWordIndex, false, true, false);
	}

	public static List<String> getFileList(File dir, String ending, boolean isRecursive) {
		List<String> result = new ArrayList<String>();
		Arrays.stream(dir.listFiles((f, n) -> !n.startsWith(".") && (f.isDirectory() || n.toLowerCase().endsWith(ending)))).forEach(unchecked((file) -> {
			if (!file.isDirectory()) {
				result.add(file.getCanonicalPath());
			}

			if (file.isDirectory()) {
				getFileList(file, ending, isRecursive);
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
	 * Gets a part of a stream given by a start-index and end-index by characters.
	 *
	 * @param inputStream
	 *            {@link InputStream} the input stream to read from
	 * @param outputStream
	 *            {@link OutputStream} the output stream to write to
	 * @param startCharacterIndex
	 *            {@link long} the start character index
	 * @param endCharacterIndex
	 *            {@link long} the end character index. If you want all until the file ends, just specify -1 here
	 */
	public static void partOfByCharacters(final InputStream inputStream, final OutputStream outputStream, final long startCharacterIndex,
			final long endCharacterIndex) {
		partOf(inputStream, outputStream, startCharacterIndex, endCharacterIndex, false, false, true);
	}

	/**
	 * Gets a part of a stream given by a start-index and end-index.
	 *
	 * @param inputStream
	 *            {@link InputStream} the input stream to read from
	 * @param outputStream
	 *            {@link OutputStream} the output stream to write to
	 * @param startIndex
	 *            {@link long} the start index
	 * @param endIndex
	 *            {@link long} the end index. If you want all until the file ends, just specify -1 here
	 * @param indexIsLine
	 *            {@link boolean} the index is line-controlled
	 * @param indexIsWord
	 *            {@link boolean} the index is word-controlled
	 * @param indexIsCharacter
	 *            {@link boolean} the index is character-controlled
	 */
	public static void partOf(final InputStream inputStream, final OutputStream outputStream, final long startIndex, final long endIndex,
			final boolean indexIsLine, final boolean indexIsWord, final boolean indexIsCharacter) {
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
					} else if (indexIsWord && (b == ' ')) {
						count++;
					} else if (indexIsLine && (b == '\n')) {
						count++;
					}
					if ((count >= startIndex) && ((endIndex == -1) || (count <= endIndex))) {
						outputStream.write(b);
					}
				}
			}
		} catch (IOException e) {
			logger.fatal("IO Exception occurred while reading input-stream:\n" + StringUtils.getStackTrace(e));
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				logger.fatal("IO Exception occurred while closing input-stream:\n" + StringUtils.getStackTrace(e));
			}
		}
	}

	/**
	 * Gets a part of a stream given by a start-index and end-index by characters.
	 *
	 * @param inputStream
	 *            {@link InputStream} the input stream to read from
	 * @param outputStream
	 *            {@link OutputStream} the output stream to write to
	 * @param startCharacterIndex
	 *            {@link long} the start character index
	 * @param endCharacterIndex
	 *            {@link long} the end character index
	 */
	public static void getPartOfByCharacters(final InputStream inputStream, final OutputStream outputStream, final long startCharacterIndex,
			final long endCharacterIndex) {
		try {
			final int bufferSize = 1024;
			byte[] c = new byte[bufferSize];
			long count = 0;
			int readChars = 0;
			while ((readChars = inputStream.read(c)) != -1) {
				for (int i = 0; i < readChars; i++) {
					count++;
					if ((count >= startCharacterIndex) && (count <= endCharacterIndex)) {
						outputStream.write(c[i]);
					}
				}
			}
		} catch (IOException e) {
			logger.fatal("IO Exception occurred while reading input-stream:\n" + StringUtils.getStackTrace(e));
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				logger.fatal("IO Exception occurred while closing input-stream:\n" + StringUtils.getStackTrace(e));
			}
		}
	}

	/**
	 * Gets the number of lines/words/characters contained in a given string by counting the number of occurrence of line-feed characters/spaces/characters.
	 * <br/>
	 * It does this by reading the stream step-by-step (in order to prevent holding the whole file in memory) and by counting the number of occurrences of
	 * line-feed characters in that buffer.
	 * <p>
	 * This method is by far the fastest method to do that.<br/>
	 * Info: The word-count is done by counting spaces.
	 *
	 * @param input
	 *            {@link String} the input string to scan
	 * @return the count properties {@link CountProperties}
	 */
	public static CountProperties getCountPropertiesOf(final String input) {
		return getCountPropertiesOf(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
	}

	/**
	 * Gets the number of lines/words/characters contained in a given file by counting the number of occurrence of line-feed characters/spaces/characters.<br/>
	 * It does this by reading the stream step-by-step (in order to prevent holding the whole file in memory) and by counting the number of occurrences of
	 * line-feed characters in that buffer.
	 * <p>
	 * This method is by far the fastest method to do that.<br/>
	 * Info: The word-count is done by counting spaces.
	 *
	 * @param file
	 *            {@link File} the file to open for scanning
	 * @return the count properties {@link CountProperties}
	 */
	public static CountProperties getCountPropertiesOf(final File file) {
		try {
			return getCountPropertiesOf(new BufferedInputStream(new FileInputStream(file)));
		} catch (FileNotFoundException e) {
			logger.fatal("File not found:\n" + StringUtils.getStackTrace(e));
		}
		return null;
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
	 * @param inputStream
	 *            {@link InputStream} the input stream to scan
	 * @return the count properties {@link CountProperties}
	 */
	public static CountProperties getCountPropertiesOf(final InputStream inputStream) {
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
		} catch (IOException e) {
			logger.fatal("IO Exception occurred while reading input-stream:\n" + StringUtils.getStackTrace(e));
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				logger.fatal("IO Exception occurred while closing input-stream:\n" + StringUtils.getStackTrace(e));
			}
		}
		return null;
	}

	/**
	 * Clears file and creates a new empty one.
	 *
	 * @param fileName
	 *            AS path and name
	 * @author DBR
	 */
	public static void clearFile(final String fileName) {
		final File file = new File(fileName);
		logger.info("Deleting file: " + fileName);
		file.delete();
		createFile(fileName);
		logger.info("Creating file: " + fileName);
	}

	/**
	 * Copy directory.
	 *
	 * @param sourcePath
	 *            path of the directory which should be copied
	 * @param destinationPath
	 *            path where the directory gets copied to
	 * @return true ({@link boolean}), if successful
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
					if (!FileUtils.copyFile(sourceFile, destination)) {
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
	 * @param source
	 *            {@link File} the source
	 * @param destination
	 *            {@link File} the destination
	 * @return true is file was copied successfully
	 */
	@SuppressWarnings("resource")
	public static boolean copyFile(final File source, final File destination) {
		try {
			logger.info("Copy file " + source.getAbsolutePath() + " to " + destination.getAbsolutePath());
			FileChannel inputChannel = null;
			FileChannel outputChannel = null;
			try {
				inputChannel = new FileInputStream(source).getChannel();
				outputChannel = new FileOutputStream(destination).getChannel();
				outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
				return true;
			} finally {
				inputChannel.close();
				outputChannel.close();
			}
		} catch (final IOException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	/**
	 * Copy directory.
	 *
	 * @param sourcePath
	 *            path of the directory which should be copied
	 * @param destinationPath
	 *            path where the directory gets copied to
	 * @return true ({@link boolean}), if successful
	 */
	public static boolean copyDirectory(final String sourcePath, final String destinationPath) {
		logger.info("Copy files from " + sourcePath + " to " + destinationPath);
		final File source = new File(sourcePath);
		final File destination = new File(destinationPath);
		return copyDirectory(source, destination);
	}

	/**
	 * Copy file.
	 *
	 * @param srcFile
	 *            the src file
	 * @param dstFile
	 *            the dst file
	 */
	public static void copyFile(final String srcFile, final String dstFile) {
		final File src = new File(srcFile);
		final File dest = new File(dstFile);
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(src);
			out = new FileOutputStream(dest);
			final int buffersize = 1024;
			logger.info("Copy file " + srcFile + " to " + dstFile);
			final byte[] buf = new byte[buffersize];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
		} catch (final FileNotFoundException e) {
			System.out.println("File not found " + e.getMessage());
		} catch (final IOException e) {
			System.out.println("IO Exception occurred " + e.getMessage());
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Creates the directory/sub-directory-structure as a sibling of the given parent directory (if it doesn't already exist in the first place).
	 *
	 * @param parent
	 *            {@link File} the parent directory to create the new structure in
	 * @param directory
	 *            {@link String} the directory or directory with sub-directories to create, if it doesn't already exist
	 */
	public static void createDirectory(final File parent, final String directory) {
		final File dir = new File(parent, directory);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	/**
	 * Creates directory and returns success.
	 *
	 * @param directory
	 *            AS path and name
	 * @author DBR
	 */
	public static void createDirectory(final String directory) {
		final File file = new File(directory);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	/**
	 * creates a file with known name.
	 *
	 * @param fileName
	 *            file name and path
	 * @return file
	 * @author DBR
	 */
	public static File createFile(final String fileName) {
		try {
			final File file = new File(fileName);
			final boolean exist = file.createNewFile();
			if (exist) {
				logger.info("File " + fileName + " created.");
			}
			return file;
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * Creates random file content in a given file.
	 *
	 * @param file
	 *            {@link File} the file to create with random content
	 * @param length
	 *            {@link long} the length of the random content in bytes
	 */
	public static void createRandomFileContent(final File file, final long length) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			final byte[] b = new byte[1];
			final Random rand = new Random();
			for (long i = 0; i < length; i++) {
				rand.nextBytes(b);
				fos.write(b);
			}
			fos.close();
		} catch (final IOException e) {
			logger.fatal("Fatal IO-error when working with file ['" + file.getAbsolutePath() + END + StringUtils.getStackTrace(e));
		}
	}

	/**
	 * Delete a directory with all its sub-directories and files.
	 *
	 * @param directory
	 *            which gets deleted
	 * @return true ({@link boolean}), if successful
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
	 * @param directory
	 *            path to the directory which gets deleted
	 * @return true ({@link boolean}), if successful
	 */
	public static boolean deleteDirectory(final String directory) {
		final File dir = new File(directory);
		return FileUtils.deleteDirectory(dir);
	}

	/**
	 * Delete file.
	 *
	 * @param fileName
	 *            the file name
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
	 * @param fileName
	 *            {@link String} the file name
	 * @return String file extension
	 */
	public static String getFileExtension(final String fileName) {
		return fileName.substring(fileName.lastIndexOf('.') + 1);
	}

	/**
	 * Returns the file extension.
	 *
	 * @param fullFileName
	 *            {@link String} the full file name
	 * @return String : file extension
	 */
	public static String getFileNameNoExt(final String fullFileName) {
		String retVal = null;
		// Get file name only
		final String fileName = getFileNameOnly(fullFileName);
		if (!(StringUtils.isNullOrEmpty(fileName)) && fileName.contains(".")) {
			retVal = fileName.substring(0, fileName.lastIndexOf('.'));
		}
		return retVal;
	}

	/**
	 * Returns the short filename for a filename with path.
	 *
	 * @param fullFileName
	 *            {@link String} the full file name
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
	 * @param fullFileName
	 *            {@link String} the full file name
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
			} else {
				logger.info("Recieved filename '" + fullFileName + "' contains neither Windows nor Unix-style directory delimiter.");
			}
		}
		return retVal;
	}

	/**
	 * Gets the file-name from a string containing a path and file-name.
	 *
	 * @param fileNameAndPath
	 *            {@link String} the file name and path to get the file-name from
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
	 * @param fileNameAndPath
	 *            {@link String} the file name and path to get the path from
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
	 * Calculates the path to the SCTM result directory (which is temporary by the way). If that path doesn't exist, null is returned, so you can choose your
	 * actions accordingly.
	 *
	 * @return path to SCTM result directory.
	 */
	public static File getSctmTemporaryDirectoryPath() {
		final File dirPath = new File(System.getenv("TMP") + "\\SCC_ExecServer_19124_19125\\PerfProjects");
		long datelastmodified = 0;
		File sctmdir = new File("");
		if (dirPath.exists()) {
			final File[] dircontent = dirPath.listFiles();
			for (final File f : dircontent) {
				if (datelastmodified < f.lastModified()) {
					datelastmodified = f.lastModified();
					sctmdir = f;
				}
			}
			return sctmdir;
		} else {
			return null;
		}
	}

	/**
	 * Checks if is file existing.
	 *
	 * @param fileName
	 *            {@link String} the file name
	 * @return true {@link Boolean} , if is file existing
	 */
	public static boolean isFileExisting(final String fileName) {
		boolean result = false;
		try {
			final File file = new File(fileName);
			result = file.exists();
		} catch (final Exception e) {
			logger.fatal("Exception occurred: " + e.getMessage());
			result = false;
		}
		return result;
	}

	/**
	 * Load an external component if and only if the given component is null. Returns the found external component's content or the given content, if it wasn't
	 * null in the first place.
	 *
	 * @param content
	 *            {@link String} the component's content
	 * @param file
	 *            {@link File} the file
	 * @param encoding
	 *            {@link Encoding} the encoding
	 * @return the string {@link String}
	 */
	public static String loadComponentIfNull(final String content, final File file, final Encoding encoding) {
		if (content == null) {
			return readFileToString(file, encoding);
		}
		return content;
	}

	/**
	 * Loads a properties-file from disk and returns a properties-object.
	 *
	 * @param pathAndNameAndExtension
	 *            {@link String} the path and name and extension
	 * @return the properties {@link Properties} that where read from file
	 */
	public static Properties loadProperties(final String pathAndNameAndExtension) {
		try {
			final Properties properties = new Properties();
			final BufferedInputStream stream = new BufferedInputStream(new FileInputStream(pathAndNameAndExtension));
			properties.load(stream);
			stream.close();
			return properties;
		} catch (final IOException e) {
			logger.fatal("Problem opening resource bundle of application under test ['"
					+ pathAndNameAndExtension
					+ "'] could not be found. Exception:\n"
					+ StringUtils.getStackTrace(e));
		} catch (final IllegalArgumentException e) {
			logger.fatal("Problem opening resource bundle of application under test ['"
					+ pathAndNameAndExtension
					+ "'] could not be found. Exception:\n"
					+ StringUtils.getStackTrace(e));
		} catch (final SecurityException e) {
			logger.fatal("Problem opening resource bundle of application under test ['"
					+ pathAndNameAndExtension
					+ "'] could not be found. Exception:\n"
					+ StringUtils.getStackTrace(e));
		}
		return null;
	}

	/**
	 * Opens a file either to be replaced by a new content or to append.
	 *
	 * @param file
	 *            {@link File} the file
	 * @param encoding
	 *            {@link Encoding} the encoding
	 * @param append
	 *            {@link Boolean} the append
	 * @return the prints the writer {@link PrintWriter}
	 */
	public static PrintWriter openFile(final File file, final Encoding encoding, final boolean append) {
		PrintWriter out = null;
		try {
			out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file.getAbsoluteFile(), append), encoding.getEncoding()));
		} catch (final UnsupportedEncodingException e) {
			logger.fatal("Error opening file ['" + file.getAbsolutePath() + END + StringUtils.getStackTrace(e));
		} catch (final FileNotFoundException e) {
			logger.fatal("Error opening file ['" + file.getAbsolutePath() + END + StringUtils.getStackTrace(e));
		}
		return out;
	}

	/**
	 * Read DataBase Name from File.
	 *
	 * @param fDatabaseFile
	 *            Path to File
	 * @return String
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @author thl
	 */
	public static String readDatabasefromFile(final File fDatabaseFile) throws IOException {
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		BufferedReader dis = null;
		StringBuffer sFileContent = new StringBuffer("");
		try {
			if (fDatabaseFile.exists()) {
				fis = new FileInputStream(fDatabaseFile);
				// Here BufferedInputStream is added for fast reading.
				bis = new BufferedInputStream(fis);
				dis = new BufferedReader(new InputStreamReader(fis));
				// dis.ready() true stream is not empty
				while (dis.ready()) {
					// this statement reads the line from the file and print it
					// to
					// the console.
					sFileContent.append(dis.readLine());
				}
				fis.close();
				bis.close();
				dis.close();
				return sFileContent.toString();
			} else {
				return sFileContent.toString();
			}
		} catch (final FileNotFoundException e) {
			logger.fatal(e.getMessage());
			return sFileContent.toString();
		} catch (final IOException e) {
			logger.fatal(e.getMessage());
			return sFileContent.toString();
		}
	}

	/**
	 * Reads the contents of a file and returns it as a byteArray taking into account all the exceptions that might occur.
	 *
	 * @param file
	 *            {@link File} the file
	 * @return the string {@link String}
	 */
	public static byte[] readFileToByteArray(final File file) {
		try {
			return java.nio.file.Files.readAllBytes(Paths.get(file.getAbsolutePath()));
		} catch (final IOException e) {
			logger.fatal("Error reading file ['" + file.getAbsolutePath() + END + StringUtils.getStackTrace(e));
		}
		return null;
	}

	/**
	 * Open a file and read content to a List.
	 *
	 * @param fFileName
	 *            Path to the File
	 * @return ArrayList
	 * @author thl
	 */
	public static List<String> readFileToList(final File fFileName) {
		String line = "";
		final ArrayList<String> data = new ArrayList<String>();
		try {
			final FileReader fr = new FileReader(fFileName);
			final BufferedReader br = new BufferedReader(fr);
			while ((line = br.readLine()) != null) {
				data.add(line);
			}
			br.close();
		} catch (final FileNotFoundException fN) {
			logger.fatal(fN.getMessage());
		} catch (final IOException e) {
			logger.fatal(e.getMessage());
		}
		return data;
	}

	/**
	 * Reads the contents of a file and returns it as a string taking into account all the exceptions that might occur.
	 *
	 * @param file
	 *            {@link File} the file to read
	 * @param encoding
	 *            {@link Encoding} the encoding to expect when putting the file-content to a string
	 * @return the string {@link String} that consists of the file-content
	 */
	public static String readFileToString(final File file, final Encoding encoding) {
		try {
			return new String(readFileToByteArray(file), encoding.getEncoding());
		} catch (final IOException e) {
			logger.fatal("Error reading file ['" + file.getAbsolutePath() + END + StringUtils.getStackTrace(e));
		}
		return null;
	}

	/**
	 * Reads the contents of a file and returns it as a List<String> taking into account all the exceptions that might occur.
	 *
	 * @param file
	 *            {@link File} the file to read
	 * @param encoding
	 *            {@link Encoding} the encoding to expect when putting the file-content to a string
	 * @return the string {@link String} that consists of the file-content
	 */
	public static List<String> readFileToList(final File file, final Encoding encoding) {
		List<String> result = new ArrayList<>();
		try {
			List<String> lines = java.nio.file.Files.readAllLines(Paths.get(file.getAbsolutePath()), encoding.toCharset());

			for (String line : lines) {
				Collections.addAll(result, line.split(";"));
			}
		} catch (final IOException e) {
			logger.fatal("Error reading file ['" + file.getAbsolutePath() + END + StringUtils.getStackTrace(e));
		}
		return result;
	}

	/**
	 * Write a list of strings to a file.
	 *
	 * @param file
	 *            Path to the file
	 * @param text
	 *            list of strings which gets written into the file
	 * @author thl
	 */
	public static void writeListToFile(final File file, final List<String> text) {
		FileWriter fstream = null;
		try {
			fstream = new FileWriter(file);
			final BufferedWriter out = new BufferedWriter(fstream);
			for (Integer i = 0; i < text.size(); i++) {
				try {
					out.write(text.get(i) + "\n");
				} catch (final IOException e) {
					logger.fatal(e.getMessage());
				}
			}
			try {
				out.close();
			} catch (final IOException e) {
				logger.fatal(e.getMessage());
			}
		} catch (final IOException e) {
			logger.fatal(e.getMessage());
		}
	}

	/**
	 * Writes ArrayList to File with possibility to append.
	 *
	 * @param fFile2Write
	 *            file to be written
	 * @param lsOutList
	 *            String to be written
	 * @param append
	 *            true = append to file, false = overwrite
	 * @throws IOException
	 *             the IO exception
	 * @author DBR
	 */
	public static void writeListToFile(final File fFile2Write, final List<String> lsOutList, final boolean append) throws IOException {
		FileWriter fstream = null;
		try {
			fstream = new FileWriter(fFile2Write, append);
			final BufferedWriter out = new BufferedWriter(fstream);
			for (Integer i = 0; i < lsOutList.size(); i++) {
				out.write(lsOutList.get(i) + "\n");
			}
			out.close();
		} catch (final IOException e) {
			logger.fatal(e.getMessage());
		}
	}

	/**
	 * Writes a string to file taking into account the character encoding and all exceptions that might be thrown.
	 * <p>
	 * If the file already exists, the content will be appended (append = true).
	 *
	 * @param file
	 *            {@link File} the file to write to. May or may not exist.
	 * @param encoding
	 *            {@link Encoding} the encoding to be used
	 * @param data
	 *            {@link String} the data to be written
	 */
	public static void writeToFile(final File file, final Encoding encoding, final String data) {
		writeToFile(file, encoding, data, true);
	}

	/**
	 * Writes a string to file taking into account the character encoding and all exceptions that might be thrown.
	 *
	 * @param file
	 *            {@link File} the file to write to. May or may not exist.
	 * @param encoding
	 *            {@link Encoding} the encoding to be used
	 * @param data
	 *            {@link String} the data to be written
	 * @param append
	 *            {@link Boolean} a flag indicating if the content of the file should be overwritten (replaced) or appended at the end of the existing file
	 */
	public static void writeToFile(final File file, final Encoding encoding, final String data, final boolean append) {
		OutputStream out = null;
		Writer writer = null;
		final CharsetEncoder charsetEncoder = Charset.forName(encoding.getEncoding()).newEncoder();
		try {
			out = new FileOutputStream(file, append);
			writer = new OutputStreamWriter(out, charsetEncoder);
			writer.write(data);
		} catch (final IllegalArgumentException e) {
			logger.fatal("Error opening file ['" + file.getAbsolutePath() + END + StringUtils.getStackTrace(e));
		} catch (final IOException e) {
			logger.fatal("Error writing to file ['" + file.getAbsolutePath() + END + StringUtils.getStackTrace(e));
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (final IOException e) {
					logger.fatal("Fatal error closing file ['" + file.getAbsolutePath() + END + StringUtils.getStackTrace(e));
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (final IOException e) {
					logger.fatal("Fatal error closing file ['" + file.getAbsolutePath() + END + StringUtils.getStackTrace(e));
				}
			}
		}
	}

	/**
	 * Gets all sub-directories of given directory.
	 *
	 * @param directory
	 *            AS path
	 * @return file array
	 * @author DBR
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
	 * Prints the last bytes.
	 *
	 * @param from
	 *            the from
	 * @param scope
	 *            the scope
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void printLastBytes(final Path from, final int scope) throws IOException {
		final long size = java.nio.file.Files.size(from);
		try (SeekableByteChannel sourceChannel = java.nio.file.Files.newByteChannel(from, EnumSet.of(StandardOpenOption.READ))) {
			sourceChannel.position(size - scope);
			ByteBuffer buf = ByteBuffer.allocate(scope);
			String encoding = System.getProperty("file.encoding");
			while (sourceChannel.read(buf) > 0) {
				buf.rewind();
				logger.fatal(Charset.forName(encoding).decode(buf).toString());
				buf.flip();
			}
		}
	}

	/**
	 * Creates the contains file name filter.
	 *
	 * @param matches
	 *            {@link List<String>} the matches
	 * @return the filename filter {@link FilenameFilter}
	 */
	public static FilenameFilter createContainsFileNameFilter(final List<String> matches) {
		return new FilenameFilter() {
			@Override
			public boolean accept(final File dir, final String name) {
				return StringUtils.contains(matches, name);
			}
		};
	}

	/**
	 * Creates the replaced contains file name filter.
	 *
	 * @param matches
	 *            {@link List<String>} the matches
	 * @param replaceString
	 *            {@link String} the replace string
	 * @return the filename filter {@link FilenameFilter}
	 */
	public static FilenameFilter createSubStringContainsFileNameFilter(final List<String> matches, final String importInterFacePkg) {
		return new FilenameFilter() {
			@Override
			public boolean accept(final File dir, final String name) {
				return StringUtils.contains(matches, name.substring(importInterFacePkg.length()));
			}
		};
	}

	/**
	 * Creates the contains file filter.
	 *
	 * @param matches
	 *            {@link List<String>} the matches
	 * @return the file filter {@link FileFilter}
	 */
	public static FileFilter createContainsFileFilter(final List<String> matches) {
		return new FileFilter() {
			@Override
			public boolean accept(final File pathname) {
				return StringUtils.contains(matches, pathname.getName());
			}
		};
	}

	/**
	 * Creates the file list.
	 *
	 * @param path
	 *            {@link String} the path
	 * @return the list {@link List<File>}
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
	 * @param targetFolder
	 *            {@link String} the target folder
	 * @return true ({@link boolean}), if successful
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