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
package info.unterrainer.java.tools.utils;

import info.unterrainer.java.tools.utils.files.FileUtils;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class FileUtilsTests {

	private Path tempFile;

	/**
	 * This method is called every time a test is started.
	 */
	@Before
	public void Initialize() {
	}

	/**
	 * This method is called every time a test is completed.
	 */
	@After
	public void TearDown() {
		try {
			Files.deleteIfExists(tempFile);
			tempFile = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test readToList.
	 */
	@Test
	public void readToListTest() {
		tempFile = writeTempFile(getLines());
		try {

			List<String> result = FileUtils.readToList(tempFile);
			Assert.assertThat(result, CoreMatchers.equalTo(getLines()));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Nullable
	private Path writeTempFile(List<String> contents) {
		try {
			Path tempFile = Files.createTempFile("FileUtilTests", "");
			Files.write(tempFile, contents);
			return tempFile;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<String> getLines() {
		return Arrays.asList("line1", "line2", "line3");
	}
}
