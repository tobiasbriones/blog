---
permalink: troubleshooting-diary
title: "Troubleshooting Diary"
description: "In this article, I will document and keep updating routine problems I face as a software engineer with proposed solutions I used to fix them."
ogimage: "https://raw.githubusercontent.com/tobiasbriones/blog/gh-pages/troubleshooting-diary/troubleshooting-diary.png"
---


<!-- Copyright (c) 2023 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# Troubleshooting Diary

<img src="images/troubleshooting-diary.png" alt="Troubleshooting Diary" />

<p align="center">
<b>
Image from [Pixabay](images/notice#cover)
</b>
</p>

---

In this article, I will document and keep updating routine problems I face as a
software engineer with proposed solutions I used to fix them.

## Dev

This section lists troubleshooting I faced during dev projects.

### Sections Manager

Troubleshooting from my university enrollment sections app are listed next.

#### An Illegal Reflective Access Operation has Occurred in Apache POI

This was when Java 9 came out, and reflection operations were affected.


<figure>
<div class="header user-select-none headerless">
    <div class="caption">
        
    </div>

    <div class="menu">
        

        <button type="button" data-code="WARNING: An illegal reflective access operation has occurred
WARNING: Illegal reflective access by org.apache.poi.util.DocumentHelper (file:&#x2F;T:&#x2F;Workspace&#x2F;Java&#x2F;Sections%20Manager&#x2F;libs&#x2F;poi&#x2F;poi-ooxml-3.17.jar) to method com.sun.org.apache.xerces.internal.util.SecurityManager.setEntityExpansionLimit(int)
WARNING: Please consider reporting this to the maintainers of org.apache.poi.util.DocumentHelper
WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
WARNING: All illegal access operations will be denied in a future release
" onclick="onCopyCodeSnippet(this)">
            <span class="material-symbols-rounded">
            content_copy
            </span>

            <div class="tooltip">
                Copied
            </div>
        </button>
    </div>
</div>
{% capture markdownContent %}
```
WARNING: An illegal reflective access operation has occurred
WARNING: Illegal reflective access by org.apache.poi.util.DocumentHelper (file:/T:/Workspace/Java/Sections%20Manager/libs/poi/poi-ooxml-3.17.jar) to method com.sun.org.apache.xerces.internal.util.SecurityManager.setEntityExpansionLimit(int)
WARNING: Please consider reporting this to the maintainers of org.apache.poi.util.DocumentHelper
WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
WARNING: All illegal access operations will be denied in a future release
```

{% endcapture %}

{{ markdownContent | markdownify }}



<figcaption>Illegal Reflective Access Warning in Old Version of Apache POI and Java 9</figcaption>
</figure>

Apache POI 3.x is quite old, while 4.x came later.

The solution was to **update Apache POI to a new version** to get rid of the
warning, as per my
[StackOverflow Question](https://stackoverflow.com/questions/50071996/an-illegal-reflective-access-operation-has-occurred-apache-poi).

### Course Project

Troubleshooting from course projects are listed next.

#### Distributed Text File System

**Documentation:**
[Distributed Text File System | CP | DEV](https://dev.mathsoftware.engineer/distributed-text-file-system---cp).

Addressed issues:

- **Binary Incompatibility Issues:** When refactoring app package name from
  `io.github.tobiasbriones` to `com.github.tobiasbriones`, and loading older
  serialized objects from FS.
- **Storage:** When the linux container running a Java app runs out of space,
  what gives `java.io.IOException: No space left on device`.
- **Fix IntelliJ Gradle JVM:** I needed to change the Gradle JVM version used by
  IntellJ IDEA build tools to the correct version.

## Miscellaneous

I list random troubleshooting experiences I face next.

### Daily Dev

This covers day-to-day annoying issues I have solved before in software
development.

#### Kotlin toList Method not Found

The problem complains
that `Unresolved reference. None of the following candidates is applicable because of receiver type mismatch: `
since the version Kotlin is running doesn't have the method `toList` from
the `Stream` API that was introduced in Java 16.

**The IDE was all right with the code, but it didn't compile in the end.**


<figure>
<div class="header user-select-none headerless">
    <div class="caption">
        
    </div>

    <div class="menu">
        

        <button type="button" data-code="fun entries(root: Entry): List&lt;Entry&gt; =
    Files
        .walk(root.path)
        .filter(::filterPath)
        .filter(Files::isRegularFile)
        .filter { it.name == &quot;index.md&quot; }
        .filter { it.parent != null }
        .map { Entry(it.parent) }
        .toList() &#x2F;&#x2F; &lt;- here
" onclick="onCopyCodeSnippet(this)">
            <span class="material-symbols-rounded">
            content_copy
            </span>

            <div class="tooltip">
                Copied
            </div>
        </button>
    </div>
</div>
{% capture markdownContent %}
```kotlin
fun entries(root: Entry): List<Entry> =
    Files
        .walk(root.path)
        .filter(::filterPath)
        .filter(Files::isRegularFile)
        .filter { it.name == "index.md" }
        .filter { it.parent != null }
        .map { Entry(it.parent) }
        .toList() // <- here
```

{% endcapture %}

{{ markdownContent | markdownify }}



<figcaption>Method "toList" Unresolved by the Compiler</figcaption>
</figure>


<figure>
<div class="header user-select-none headerless">
    <div class="caption">
        
    </div>

    <div class="menu">
        

        <button type="button" data-code="e: file:&#x2F;&#x2F;&#x2F;P:&#x2F;deployment&#x2F;blog&#x2F;ops&#x2F;src&#x2F;main&#x2F;kotlin&#x2F;Main.kt:282:10 Unresolved reference. None of the following candidates is applicable because of receiver type mismatch: 
public inline fun &lt;T&gt; Enumeration&lt;TypeVariable(T)&gt;.toList(): List&lt;TypeVariable(T)&gt; defined in kotlin.collections
public fun &lt;T&gt; Array&lt;out TypeVariable(T)&gt;.toList(): List&lt;TypeVariable(T)&gt; defined in kotlin.collections
public fun BooleanArray.toList(): List&lt;Boolean&gt; defined in kotlin.collections
public fun ByteArray.toList(): List&lt;Byte&gt; defined in kotlin.collections
public fun CharArray.toList(): List&lt;Char&gt; defined in kotlin.collections
public fun CharSequence.toList(): List&lt;Char&gt; defined in kotlin.text
public fun DoubleArray.toList(): List&lt;Double&gt; defined in kotlin.collections
public fun FloatArray.toList(): List&lt;Float&gt; defined in kotlin.collections
public fun IntArray.toList(): List&lt;Int&gt; defined in kotlin.collections
public fun LongArray.toList(): List&lt;Long&gt; defined in kotlin.collections
public fun &lt;T&gt; Pair&lt;TypeVariable(T), TypeVariable(T)&gt;.toList(): List&lt;TypeVariable(T)&gt; defined in kotlin
public fun ShortArray.toList(): List&lt;Short&gt; defined in kotlin.collections
public fun &lt;T&gt; Triple&lt;TypeVariable(T), TypeVariable(T), TypeVariable(T)&gt;.toList(): List&lt;TypeVariable(T)&gt; defined in kotlin
public fun &lt;T&gt; Iterable&lt;TypeVariable(T)&gt;.toList(): List&lt;TypeVariable(T)&gt; defined in kotlin.collections
public fun &lt;K, V&gt; Map&lt;out TypeVariable(K), TypeVariable(V)&gt;.toList(): List&lt;Pair&lt;TypeVariable(K), TypeVariable(V)&gt;&gt; defined in kotlin.collections
public fun &lt;T&gt; Sequence&lt;TypeVariable(T)&gt;.toList(): List&lt;TypeVariable(T)&gt; defined in kotlin.sequences
" onclick="onCopyCodeSnippet(this)">
            <span class="material-symbols-rounded">
            content_copy
            </span>

            <div class="tooltip">
                Copied
            </div>
        </button>
    </div>
</div>
{% capture markdownContent %}
```
e: file:///P:/deployment/blog/ops/src/main/kotlin/Main.kt:282:10 Unresolved reference. None of the following candidates is applicable because of receiver type mismatch: 
public inline fun <T> Enumeration<TypeVariable(T)>.toList(): List<TypeVariable(T)> defined in kotlin.collections
public fun <T> Array<out TypeVariable(T)>.toList(): List<TypeVariable(T)> defined in kotlin.collections
public fun BooleanArray.toList(): List<Boolean> defined in kotlin.collections
public fun ByteArray.toList(): List<Byte> defined in kotlin.collections
public fun CharArray.toList(): List<Char> defined in kotlin.collections
public fun CharSequence.toList(): List<Char> defined in kotlin.text
public fun DoubleArray.toList(): List<Double> defined in kotlin.collections
public fun FloatArray.toList(): List<Float> defined in kotlin.collections
public fun IntArray.toList(): List<Int> defined in kotlin.collections
public fun LongArray.toList(): List<Long> defined in kotlin.collections
public fun <T> Pair<TypeVariable(T), TypeVariable(T)>.toList(): List<TypeVariable(T)> defined in kotlin
public fun ShortArray.toList(): List<Short> defined in kotlin.collections
public fun <T> Triple<TypeVariable(T), TypeVariable(T), TypeVariable(T)>.toList(): List<TypeVariable(T)> defined in kotlin
public fun <T> Iterable<TypeVariable(T)>.toList(): List<TypeVariable(T)> defined in kotlin.collections
public fun <K, V> Map<out TypeVariable(K), TypeVariable(V)>.toList(): List<Pair<TypeVariable(K), TypeVariable(V)>> defined in kotlin.collections
public fun <T> Sequence<TypeVariable(T)>.toList(): List<TypeVariable(T)> defined in kotlin.sequences
```

{% endcapture %}

{{ markdownContent | markdownify }}



<figcaption>The Method "toList" is not Found when Compiling</figcaption>
</figure>

A solution was to add `import kotlin.streams.toList` to import the method
`toList` so it's found. The problem is that is unnecessary, and **the IDE
deleted it because it said it was an unused import.**

There's always a conflict in JVM setups of what you're using: the dependency of
the IDE for Java and JVM projects is evil.

I checked all possible configurations in the IDE, and everything was set up to
JDK 19 (this code used to work before without setting up anything 😣 but it
stopped compiling some day).

In the end, I was able to **set the actual version of the JDK to use by Gradle
by editing the project's `build.gradle.kts` from `11` to `19`** (or the latest):


<figure>
<div class="header user-select-none headerless">
    <div class="caption">
        
    </div>

    <div class="menu">
        

        <button type="button" data-code="kotlin {
    jvmToolchain(19)
}
" onclick="onCopyCodeSnippet(this)">
            <span class="material-symbols-rounded">
            content_copy
            </span>

            <div class="tooltip">
                Copied
            </div>
        </button>
    </div>
</div>
{% capture markdownContent %}
```kotlin
kotlin {
    jvmToolchain(19)
}
```

{% endcapture %}

{{ markdownContent | markdownify }}



<figcaption>Solution to Set the Correct Version of the JDK to Run the Kotlin App</figcaption>
</figure>

### Machine Learning

The next issues are related to machine learning issues like numerical
computations, datasets, etc.

#### TensorFlow Model not Learning due to Unshuffled Training Dataset

When I was training a TensorFlow binary classification CNN model I had to
change/tune some code for training and measuring.

The peculiarity of this problem is that training data has to be shuffled so the
training doesn't get biased due to the way data is sorted in the file system.
**If training takes place with default-sorted disk data then the model won't
learn**, and that was the problem I had.

The API is the following:


<figure>
<div class="header user-select-none headerless">
    <div class="caption">
        
    </div>

    <div class="menu">
        

        <button type="button" data-code="tf.data.Dataset.list_files(split_list, shuffle=False)
" onclick="onCopyCodeSnippet(this)">
            <span class="material-symbols-rounded">
            content_copy
            </span>

            <div class="tooltip">
                Copied
            </div>
        </button>
    </div>
</div>
{% capture markdownContent %}
```
tf.data.Dataset.list_files(split_list, shuffle=False)
```

{% endcapture %}

{{ markdownContent | markdownify }}



<figcaption>Tensorflow "list_files" Method with Explicit File Unshuffling</figcaption>
</figure>

[list_files \| tf.data.Dataset \| TensorFlow v2.12.0](https://www.tensorflow.org/api_docs/python/tf/data/Dataset#list_files)

I was explicitly passing `shuffle=False` since this was necessary for getting
metrics correctly (after training). That is, the same order of files was
required to map them for computing the model metrics on the datasets.

Then, I put a model to train with this new in-house API I was developing, but it
wasn't learning 😬 (something you can check in the training history logs/plots).

After checking everything, I realized the only meaningful change I had done was
that argument when loading the dataset. So, I proceeded to set it to `True`
(which is the default), and the training behavior was fixed.

This might possible since the dataset will provide a default pattern according
to how it's stored, and this will probably introduce bias, and the model might
get locked in a local minimum as per what I was reading about how the Stochastic
Gradient Descent (SGD) works.

**Always make sure to shuffle data before training to avoid biases.**





