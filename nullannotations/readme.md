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

When we first decided to use those annotations there where several candidates to pick from. We opted for the version that tried to implement a de-facto standard at that time (`JSR-305`). Those annotations are located in the namespace `javax.annotation`.  

By now `JSR-305` is dead but we're stuck with a ton of classes all referencing those annotations that don't support Java 1.8.  
We don't want to change to another annotation-implementation (other namespace, other names) especially since there's no solid successor to be found right now. The consensus at this very moment is to use the internal Eclipse-annotations and that's not good enough for us since we want to implement our services IDE-agnostic.  

Feel free to use them or help correcting errors or writing tests on the develop-branch of this git-repository.  

This repository is private since this is the master- and release-branch. You may clone it, but it will be read-only.  
If you want to contribute to our repository (push, open pull requests), please use the copy on github located here: [the public github repository][github]

Currently this contains the following annotations:

* ##### `javax.annotation.Nonnull()`  
Applicable on all types.  
Specifies that this type may NOT be null in ANY case.
* ##### `javax.annotation.Nullable()`  
Applicable on all types.  
Specifies that this type MAY be null (or not).
* ##### `javax.annotation.ParametersAreNonnullByDefault(DefaultLocation[])`  
Applicable to packages, types, methods or constructors.  
Specifies that the parameters in that element matching the given `DefaultLocation`s are nonnull by default unless there is:  
    * An explicit nullness annotation  
    * The method overrides a method in a superclass (in which case the annotation of the corresponding parameter in the superclass applies)  
    * There is a default parameter annotation applied to a more tightly nested element.  
    #### Parameters:  
    The optional parameters of this annotation is an array of DefaultLocation.  
    It defaults to the following value: `{ PARAMETER, RETURN_TYPE, TYPE_BOUND, TYPE_ARGUMENT }`  
    ##### DefaultLocation is an enumeration of the following values:  
    ###### PARAMETER  
    Defines that a given `ParametersAreNonnullByDefault` annotation should affect all unannotated parameters of any method or constructor within the scope of the annotated declaration.
    ###### RETURN_TYPE  
    Defines that a given `ParametersAreNonnullByDefault` annotation should affect all unannotated method return types within the scope of the annotated declaration.
    ###### FIELD  
    Defines that a given `ParametersAreNonnullByDefault` annotation should affect all unannotated field types within the scope of the annotated declaration.
    ###### TYPE_PARAMETER  
    Defines that a given `ParametersAreNonnullByDefault` annotation should affect all unannotated type parameter declarations within the scope of the annotated declaration.
    ###### TYPE_BOUND  
    Defines that a given `ParametersAreNonnullByDefault` annotation should affect all unannotated explicit type bounds within the scope of the annotated declaration.
    ###### TYPE_ARGUMENT  
    Defines that a given `ParametersAreNonnullByDefault` annotation should affect all unannotated type arguments within the scope of the annotated declaration (except wildcards and type variables).
    ###### ARRAY_CONTENTS  
    Defines that a given `ParametersAreNonnullByDefault` annotation should affect all unannotated array components within the scope of the annotated declaration.

The way we use it is we annotate all the `package-info.java` files with `@javax.annotation.ParametersAreNonnullByDefault` and act according to the warnings and errors your *Eclipse* confronts you with.  

**Exception:**  
When extending legacy classes (for instance if you'd like to implement `Map.entry<key,value>` you'd have to turn those null-annotations off for that class by annotating it with `@javax.annotation.ParametersAreNonnullByDefault({})` since you could never satisfy the constraints because the base-class has unconstrained parameters.

### Example Configuration in Eclipse
In order to effectively use null-annotations you'll have to setup your Eclipse to honor them and display the resulting warnings or errors accordingly.  
![Image of example Eclipse-config](http://unterrainer.info/images/Eclipse_null-annotations_settings.png)

#### Apache Maven artifact to use in your pom
```xml
<dependency>
    <groupId>info.unterrainer.java.tools</groupId>
    <artifactId>nullannotations</artifactId>
    <version>0.2</version>
</dependency>
```

[github]: https://github.com/UnterrainerInformatik/java