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
[![Maven Central](https://img.shields.io/maven-central/v/info.unterrainer.java.tools/data-structures.svg)](https://repo1.maven.org/maven2/info/unterrainer/java/tools/data-structures)
[![Javadocs](https://img.shields.io/maven-central/v/info.unterrainer.java.tools/data-structures.svg?label=Javadocs)](http://www.javadoc.io/doc/info.unterrainer.java.tools/data-structures)

# data-structures

This jar contains several classes designed to do common tasks.

This repository is private since this is the master- and release-branch. You may clone it, but it will be read-only.  
If you want to contribute to our repository (push, open pull requests), please use the copy on github located here: [the public github repository][github]

Currently this contains the following classes:

##### Tuple2  
This class is a simple generic implementation of a two-tuple.
##### Interval  
Is exactly what the name suggests. An interval defined by a minimum and a maximum with getters and setters.
##### Fader  
The fader consists of an interval and adds a value (double) to it. The value is always in between the interval.  
It adds a percentage getter and setter as well allowing you to do conversions from a percentage-value to your interval and backwards.

All of our projects facilitate the [Project Lombok][lombok]. So please download it and 'install' it in your preferred IDE by clicking on the downloaded jar-file. Then all compile-errors should vanish.  
We use the following parts of Project Lombok:  

**Beware:** Project Lombok currently doesn't play well with Eclipse Mars (4.5). You'll get build-errors using the extension methods as of now.

#### Apache Maven artifact to use in your pom
```xml
<dependency>
    <groupId>info.unterrainer.java.tools</groupId>
    <artifactId>data-structures</artifactId>
    <version>0.2</version>
</dependency>
```

[lombok]: https://projectlombok.org
[github]: https://github.com/UnterrainerInformatik/java