<!-- Copyright (c) 2023 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# Troubleshooting Diary

![Cover](images/cover.png)

Image from [Pixabay](images#cover)

---

In this article, I will document and keep updating routine problems I face as a
software engineer with proposed solutions I used to fix them.

## Dev

This section lists troubleshooting I faced during dev projects.

### Course Project

Troubleshooting from course projects are listed next.

#### Distributed Text File System

Documentation:
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

### Dev

This covers day-to-day annoying issues I have solved before in software
development.

### Machine Learning

The next issues are related to machine learning issues like numerical
computations, datasets, etc.
