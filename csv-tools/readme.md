```java
/**************************************************************************
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
 ***************************************************************************/
```
[![Maven Central](https://img.shields.io/maven-central/v/info.unterrainer.java.tools/csv-tools.svg)](https://repo1.maven.org/maven2/info/unterrainer/java/tools/csv-tools)
[![Javadocs](https://img.shields.io/maven-central/v/info.unterrainer.java.tools/csv-tools.svg?label=Javadocs)](http://www.javadoc.io/doc/info.unterrainer.java.tools/csv-tools)

# csv-tools

This repository is private since this is the master- and release-branch. You may clone it, but it will be read-only.  
If you want to contribute to our repository (push, open pull requests), please use the copy on github located here: [the public github repository][github]

This library should ease dealing with (admittedly legacy) CSV files.  

For the younger readers: CSV means Comma Separated Values and is a very old system to write lists to files. It's commonly used to export to Excel since that is the default program to open .csv files on most systems.  
You may read or write those files using this library and you may specify all possible variations of different control-characters like new-line or end-of-row (or quoting). 

All of our projects facilitate the [Project Lombok][lombok]. So please download it and 'install' it in your preferred IDE by clicking on the downloaded jar-file. Then all compile-errors should vanish.  
We use the following parts of Project Lombok:  

**Beware:** Project Lombok currently doesn't play well with Eclipse Mars (4.5). You'll get build-errors using the extension methods as of now.

#### Example CsvReader
```java
StringReader stringReader = new StringReader("\"test\";test1;A 01;t;;");

csvReader = CsvReader.builder().stringReader(stringReader).columnSeparator(';').rowSeparator(newLine).fieldDelimiter("\"").build();
List<String> row = csvReader.readRow();
```

#### Example CsvWriter
```java
CsvWriter csvWriter = CsvWriter.builder().stringWriter(stringWriter).columnSeparator(';').rowSeparator(newLine).fieldDelimiter("\"").build();

csvWriter.write("Great");
csvWriter.write("Totally");
csvWriter.write("This is a" + newLine + "break");
// The next two lines do the same as calling csvWriter.writeLine("");
csvWriter.write();
csvWriter.writeLine();

csvWriter.write();
csvWriter.write("Gr;eat");
csvWriter.writeLine("Totally");

csvWriter.write("Great");
csvWriter.write("ssfe\"s");
csvWriter.write("");
csvWriter.write("Totally");

csvWriter.close();
```

#### Apache Maven artifact to use in your pom
```xml
<dependency>
    <groupId>info.unterrainer.java.tools</groupId>
    <artifactId>csv-tools</artifactId>
    <version>0.2</version>
</dependency>
```

[lombok]: https://projectlombok.org
[github]: https://github.com/UnterrainerInformatik/java