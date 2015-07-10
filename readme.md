```
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
 
# General JAVA Tools

The general JAVA tools section of our GIT repository is free. You may copy, use or rewrite every single one of its contained projects to your hearts content.
In order to get help with basic GIT commands you may try [the GIT cheat-sheet][2] on our [homepage][1].

## utils

This jar contains various utility-classes used throughout our programs.  
Feel free to use them or help correcting errors or writing tests on the develop-branch of this git-repository.  

Currently this contains the following utility classes:

* ##### FileUtils  
This class contains methods to read and write files from and to lists or other data-structures. It eases the creation or deletion of directory trees or bulk-moving and bulk-copying them.
* ##### StringUtils  
This class contains a method to get a formatted stacktrace as a string and several other helper methods.
* ##### DateUtils
Contains helper methods to convert from and to ISO8601 dates like `2009-06-30T18:30:00+02:00`.
* ##### NullUtils
This class only contains a single method, which is called `defaultIfNull(T obj, T defaultValue)`.
* ##### SerializationUtils
Eases your life when doing JAXB-XML- or ByteArray- serialization and deserialization.

##### Apache Maven artifact to use in your pom
```xml
<dependency>
    <groupId>info.unterrainer.java.tools</groupId>
    <artifactId>utils</artifactId>
    <version>0.2</version>
</dependency>
```

## data-structures

This jar contains several classes designed to do common tasks.

Currently this contains the following classes:

* ##### Tuple2  
This class is a simple generic implementation of a two-tuple.
* ##### Interval  
Is exactly what the name suggests. An interval defined by a minimum and a maximum with getters and setters.
* ##### Fader  
The fader consists of an interval and adds a value (double) to it. The value is always in between the interval.  
It adds a percentage getter and setter as well allowing you to do conversions from a percentage-value to your interval and backwards.

##### Apache Maven artifact to use in your pom
```xml
<dependency>
    <groupId>info.unterrainer.java.tools</groupId>
    <artifactId>data-structures</artifactId>
    <version>0.2</version>
</dependency>
```

## console-progressbar

The console-progressbar is designed to show progress when writing long-running console-applications.  
It was designed to be used with consoles that support control-characters (like cmd) or that don't (Eclipse console implementation before Mars (4.5)).  

It comes with three flavors:

* SimpleInsertBar
Works with non-control-character enabled consoles.
* ProgressBar
Your ASCII-progressbar.
* PercentGauge
Displays the percentage.

You may extend the visual representations by implementing new graphical variants. 

##### Example
```java
    ConsoleProgressBar bar = ConsoleProgressBar.builder().maxValue((double) list.size()).controlCharacterSupport(!isForFileOut).build();

    int count = 0;
	for (String s : list) {
		doSomething(s);
        
        bar.updateValue(++count).draw(System.out);
	}
    
	bar.complete().draw(System.out);
	System.out.println("\n");
```

##### Apache Maven artifact to use in your pom
```xml
<dependency>
    <groupId>info.unterrainer.java.tools.reporting</groupId>
    <artifactId>console-progressbar</artifactId>
    <version>0.2</version>
</dependency>
```

## splitstopwatch

This class implements a stopWatch.

Additionally to the normal stopWatch-functionality it may be used to debug out split-times as well. It measures the split-times and keeps track of the overall times in a variable.
Don't be afraid to stop the watch. Stopping doesn't mean you loose any value whatsoever. Think of it as a real-life stopWatch where you may press the start-button at any time after previously pressing the stop-button.

This class provides useful overloads that allow writing to a PrintStream in a way that your measurement doesn't get compromised (the stopWatch is paused while writing to the stream). You may initialize it with a PrintStream so that you can use all the overloads that take a string-argument or System.out is used as a default.
All the write-operations are performed as a printLine-call, so you don't need to close your assigned text with a newline-character.

This class is automatically created using millisecond-precision. If you want to enable nanoseconds-precision albeit performance impacts, though the impact of this is very small indeed, you may do so after creating the stopWatch via the setIsNanoPrecision-Setter.

All public methods within this class are synchronized so you may use it concurrently within many threads.
It has a property 'isActive' that defaults to true. When this is set to false all calls to this class are aborted within a single if-statement in the called method. This is a convenience function so that you may leave your logging-code in the production code.

##### Example
    
```java
SplitStopWatch ssw = new SplitStopWatch();
ssw.start("started.");
  Thread.sleep(10);
ssw.split("split.");
  Thread.sleep(10);
ssw.stop("stopped.");
```

##### Apache Maven artifact to use in your pom
```xml
<dependency>
    <groupId>info.unterrainer.java.tools.reporting</groupId>
    <artifactId>splitstopwatch</artifactId>
    <version>0.2</version>
</dependency>
```

## csv-tools

This library should ease dealing with (admittedly legacy) CSV files.  
For the younger ones: CSV means Comma Separated Values and is a very old system to write lists to files. It's commonly used to export to Excel since that is the default program to open .csv files on most systems.  
You may read or write those files using this library and you may specify all possible variations of different control-characters like new-line or end-of-row (or quoting).  

##### Example CsvReader
```
StringReader stringReader = new StringReader("\"test\";test1;A 01;t;;");

csvReader = CsvReader.builder().stringReader(stringReader).columnSeparator(';').rowSeparator(newLine).fieldDelimiter("\"").build();
List<String> row = csvReader.readRow();
```

##### Example CsvWriter
```
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

##### Apache Maven artifact to use in your pom
```xml
<dependency>
    <groupId>info.unterrainer.java.tools</groupId>
    <artifactId>csv-tools</artifactId>
    <version>0.2</version>
</dependency>
```

[1]: http://www.unterrainer.info
[2]: http://www.unterrainer.info/Home/Coding