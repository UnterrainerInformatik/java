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
 
# console-progressbar

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

#### Example
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

#### Apache Maven artifact to use in your pom
```xml
<dependency>
    <groupId>info.unterrainer.java.tools.reporting</groupId>
    <artifactId>console-progressbar</artifactId>
    <version>0.2</version>
</dependency>
```