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
 
# General JAVA Tools

This section contains various useful projects that should help your development-process.

The general JAVA tools section of our GIT repository is free. You may copy, use or rewrite every single one of its contained projects to your hearts content.
In order to get help with basic GIT commands you may try [the GIT cheat-sheet][coding] on our [homepage][homepage].  

This repository is private since this is the master- and release-branch. You may clone it, but it will be read-only.  
If you want to contribute to our repository (push, open pull requests), please use the copy on github located here: [the public github repository][github]

All of our projects facilitate the [Project Lombok][lombok]. So please download it and 'install' it in your preferred IDE by clicking on the downloaded jar-file. Then all compile-errors should vanish.  
**Beware:** Project Lombok currently doesn't play well with Eclipse Mars (4.5). You'll get build-errors using the extension methods as of now.

## utils

This jar contains various utility-classes used throughout our programs.  
Feel free to use them or help correcting errors or writing tests on the develop-branch of this git-repository.  

For additional information check the `readme.md` file in the sub-directory.  

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

For additional information check the `readme.md` file in the sub-directory.  

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

For additional information check the `readme.md` file in the sub-directory.  

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

For additional information check the `readme.md` file in the sub-directory.  

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

For additional information check the `readme.md` file in the sub-directory.  

##### Apache Maven artifact to use in your pom
```xml
<dependency>
    <groupId>info.unterrainer.java.tools</groupId>
    <artifactId>csv-tools</artifactId>
    <version>0.2</version>
</dependency>
```

## nullannotations

This project contains an implementation of annotations that may be used as a replacement for the (more common) Eclipse null-annotations.  

For additional information check the `readme.md` file in the sub-directory.  

##### Apache Maven artifact to use in your pom
```xml
<dependency>
    <groupId>info.unterrainer.java.tools</groupId>
    <artifactId>nullannotations</artifactId>
    <version>0.2</version>
</dependency>
```

[homepage]: http://www.unterrainer.info
[coding]: http://www.unterrainer.info/Home/Coding
[lombok]: https://projectlombok.org
[github]: https://github.com/UnterrainerInformatik/java