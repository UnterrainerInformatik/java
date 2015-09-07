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
[![Maven Central](https://img.shields.io/maven-central/v/info.unterrainer.java.tools.reporting/splitstopwatch.svg)](https://repo1.maven.org/maven2/info/unterrainer/java/tools/reporting/splitstopwatch)
[![Javadocs](https://img.shields.io/maven-central/v/info.unterrainer.java.tools.reporting/splitstopwatch.svg?label=Javadocs)](http://www.javadoc.io/doc/info.unterrainer.java.tools.reporting/splitstopwatch)

# splitstopwatch

This class implements a stopWatch.

This repository is private since this is the master- and release-branch. You may clone it, but it will be read-only.  
If you want to contribute to our repository (push, open pull requests), please use the copy on github located here: [the public github repository][github]

Additionally to the normal stopWatch-functionality it may be used to debug out split-times as well. It measures the split-times and keeps track of the overall times in a variable.
Don't be afraid to stop the watch. Stopping doesn't mean you loose any value whatsoever. Think of it as a real-life stopWatch where you may press the start-button at any time after previously pressing the stop-button.

This class provides useful overloads that allow writing to a PrintStream in a way that your measurement doesn't get compromised (the stopWatch is paused while writing to the stream). You may initialize it with a PrintStream so that you can use all the overloads that take a string-argument or System.out is used as a default.
All the write-operations are performed as a printLine-call, so you don't need to close your assigned text with a newline-character.

This class is automatically created using millisecond-precision. If you want to enable nanoseconds-precision albeit performance impacts, though the impact of this is very small indeed, you may do so after creating the stopWatch via the setIsNanoPrecision-Setter.

All public methods within this class are synchronized so you may use it concurrently within many threads.
It has a property 'isActive' that defaults to true. When this is set to false all calls to this class are aborted within a single if-statement in the called method. This is a convenience function so that you may leave your logging-code in the production code. 

All of our projects facilitate the [Project Lombok][lombok]. So please download it and 'install' it in your preferred IDE by clicking on the downloaded jar-file. Then all compile-errors should vanish.  

**Beware:** Project Lombok currently doesn't play well with Eclipse Mars (4.5). You'll get build-errors using the extension methods as of now.

#### Example
    
```java
SplitStopWatch ssw = new SplitStopWatch();
ssw.start("started.");
  Thread.sleep(10);
ssw.split("split.");
  Thread.sleep(10);
ssw.stop("stopped.");
```

#### Apache Maven artifact to use in your pom
```xml
<dependency>
    <groupId>info.unterrainer.java.tools.reporting</groupId>
    <artifactId>splitstopwatch</artifactId>
    <version>0.2</version>
</dependency>
```

[lombok]: https://projectlombok.org
[github]: https://github.com/UnterrainerInformatik/java