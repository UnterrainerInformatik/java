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
 
# nullannotations

This jar contains an implementation of annotations that may be used as a replacement for the (more common) Eclipse null-annotations.  
Feel free to use them or help correcting errors or writing tests on the develop-branch of this git-repository.  

This repository is private since this is the master- and release-branch. You may clone it, but it will be read-only.  
If you want to contribute to our repository (push, open pull requests), please use the copy on github located here: [the public github repository][github]

Currently this contains the following annotations:

* ##### Nonnull  
Applicable on all types.  
Specifies that this type may NOT be null in ANY case.
* ##### Nullable  
Applicable on all types.  
Specifies that this type MAY be null (or not).
* ##### ParametersAreNonnullByDefault
Applicable to packages, types, methods or constructors.  
Specifies that the parameters in that element are nonnull by default unless there is:  
    * An explicit nullness annotation  
    * The method overrides a method in a superclass (in which case the annotation of the corresponding parameter in the superclass applies)  
    * There is a default parameter annotation applied to a more tightly nested element.  


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

[lombok]: https://projectlombok.org
[github] : https://github.com/UnterrainerInformatik/java