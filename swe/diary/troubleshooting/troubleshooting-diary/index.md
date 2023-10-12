<!-- Copyright (c) 2023 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# Troubleshooting Diary

![](images/troubleshooting-diary.png)

<p align="center">
<b>
Image from 
<a href="images/notice#cover">Pixabay</a>
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

`Illegal Reflective Access Warning in Old Version of Apache POI and Java 9`

```
WARNING: An illegal reflective access operation has occurred
WARNING: Illegal reflective access by org.apache.poi.util.DocumentHelper (file:/T:/Workspace/Java/Sections%20Manager/libs/poi/poi-ooxml-3.17.jar) to method com.sun.org.apache.xerces.internal.util.SecurityManager.setEntityExpansionLimit(int)
WARNING: Please consider reporting this to the maintainers of org.apache.poi.util.DocumentHelper
WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
WARNING: All illegal access operations will be denied in a future release
```

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

`Method "toList" Unresolved by the Compiler`

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

`The Method "toList" is not Found when Compiling`

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

`Solution to Set the Correct Version of the JDK to Run the Kotlin App`

```kotlin
kotlin {
    jvmToolchain(19)
}
```

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

`Tensorflow "list_files" Method with Explicit File Unshuffling`

```
tf.data.Dataset.list_files(split_list, shuffle=False)
```

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
