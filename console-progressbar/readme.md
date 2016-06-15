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
[![Maven Central](https://img.shields.io/maven-central/v/info.unterrainer.java.tools.reporting/console-progressbar.svg)](https://repo1.maven.org/maven2/info/unterrainer/java/tools/reporting/console-progressbar)
[![Javadocs](https://img.shields.io/maven-central/v/info.unterrainer.java.tools.reporting/console-progressbar.svg?label=Javadocs)](http://www.javadoc.io/doc/info.unterrainer.java.tools.reporting/console-progressbar)

# console-progressbar

This repository is private since this is the master- and release-branch. You may clone it, but it will be read-only.  
If you want to contribute to our repository (push, open pull requests), please use the copy on github located here: [the public github repository][github]

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

All of our projects facilitate the [Project Lombok][lombok]. So please download it and 'install' it in your preferred IDE by clicking on the downloaded jar-file. Then all compile-errors should vanish.  

**Beware:** Project Lombok currently doesn't play well with Eclipse Mars (4.5). You'll get build-errors using the extension methods as of now.

#### Example
```java
    ConsoleProgressBar bar = ConsoleProgressBar.builder().maxValue((double) list.size()).controlCharacterSupport(!isForFileOut).build();

    int count = 0;
	for (String s : list) {
		doSomething(s);
        
        bar.updateValue(++count).redraw(System.out);
	}
    
	bar.complete().redraw(System.out);
	System.out.println("\n");
```

#### Apache Maven artifact to use in your pom
```xml
<dependency>
    <groupId>info.unterrainer.java.tools.reporting</groupId>
    <artifactId>console-progressbar</artifactId>
    <version>0.2</version>
</dependency>
```

---
This program is brought to you by [Unterrainer Informatik][homepage]  
Project lead is [Gerald Unterrainer][geraldmail]

[geraldmail]: mailto:gerald@unterrainer.info
[lombok]: https://projectlombok.org
[github]: https://github.com/UnterrainerInformatik/java