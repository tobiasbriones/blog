---
permalink: overriding-hash-code-and-equals-in-a-multi--representation-record/email---ep/build.gradle.kts.html
title: "overriding-hash-code-and-equals-in-a-multi--representation-record/email---ep/build.gradle.kts"
---

# build.gradle.kts
```
// Copyright (c) 2024 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

plugins {
    kotlin("jvm") version "1.9.22"
    application
}

group = "engineer.mathsoftware"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("MainKt")
}

```
<div class="social open-gh-btn my-4">
  <a class="btn btn-github" href="https://github.com/tobiasbriones/blog/tree/main/swe/dev/java/design/overriding-hash-code-and-equals-in-a-multi--representation-record/email---ep/build.gradle.kts" target="_blank">
    <i class="fab fa-github">
      
    </i>
    <span>
      Open in GitHub
    </span>
  </a>
</div>