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
 
# utils

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

All of our projects facilitate the [Project Lombok][lombok]. So please download it and 'install' it in your preferred IDE by clicking on the downloaded jar-file. Then all compile-errors should vanish.  
We use the following parts of Project Lombok:  

* @Data
* @Getter/@Setter
* @Builder
* @NoArgsConstructor/@AllArgsConstructor
* @UtilityClass *(experimental - outlook: positive)*
* @Accessors *(experimental - outlook: positive)*
* @ExtensionMethos *(experimental - outlook: positive)*

**Beware:** Project Lombok currently doesn't play well with Eclipse Mars (4.5). You'll get build-errors using the extension methods as of now.

#### Apache Maven artifact to use in your pom
```xml
<dependency>
    <groupId>info.unterrainer.java.tools</groupId>
    <artifactId>utils</artifactId>
    <version>0.2</version>
</dependency>
```

[lombok]: https://projectlombok.org