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
[![Maven Central](https://img.shields.io/maven-central/v/info.unterrainer.java.tools/utils.svg)](https://repo1.maven.org/maven2/info/unterrainer/java/tools/utils)
[![Javadocs](https://img.shields.io/maven-central/v/info.unterrainer.java.tools/utils.svg?label=Javadocs)](http://www.javadoc.io/doc/info.unterrainer.java.tools/utils)

# utils

This jar contains various utility-classes used throughout our programs.  
Feel free to use them or help correcting errors or writing tests on the develop-branch of this git-repository.  

This repository is private since this is the master- and release-branch. You may clone it, but it will be read-only.  
If you want to contribute to our repository (push, open pull requests), please use the copy on github located here: [the public github repository][github]

Currently this contains the following utility classes:

##### FileUtils  
This class contains methods to read and write files from and to lists or other data-structures. It eases the creation or deletion of directory trees or bulk-moving and bulk-copying them.  
#### HrfUtils
This class is concerned with the conversion to human readable forms (HRF).  
##### StringUtils  
This class contains a method to get a formatted stacktrace as a string and several other helper methods.  
##### DateUtils
Contains helper methods to convert from and to ISO8601 dates like `2009-06-30T18:30:00+02:00`.  
##### NullUtils
This class only contains a single method, which is called `defaultIfNull(T obj, T defaultValue)`.  
##### SerializationUtils
Eases your life when doing JAXB-XML- or ByteArray- serialization and deserialization. 

All of our projects facilitate the [Project Lombok][lombok]. So please download it and 'install' it in your preferred IDE by clicking on the downloaded jar-file. Then all compile-errors should vanish.  

**Beware:** Project Lombok currently doesn't play well with Eclipse Mars (4.5). You'll get build-errors using the extension methods as of now.

#### Apache Maven artifact to use in your pom
```xml
<dependency>
    <groupId>info.unterrainer.java.tools</groupId>
    <artifactId>utils</artifactId>
    <version>0.2</version>
</dependency>
```

---
This program is brought to you by [Unterrainer Informatik][homepage]  
Project lead is [Gerald Unterrainer][geraldmail]

[geraldmail]: mailto:gerald@unterrainer.info
[lombok]: https://projectlombok.org
[github]: https://github.com/UnterrainerInformatik/java
[homepage]: http://www.unterrainer.info