---
permalink: building-slides-from-screenshots-app-in-javafx/slides---ep/src/main/java/engineer/mathsoftware/blog/slides/Slide.java.html
title: "building-slides-from-screenshots-app-in-javafx/slides---ep/src/main/java/engineer/mathsoftware/blog/slides/Slide.java"
---

# Slide.java
```java
// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.Optional;

public sealed interface Slide {
    record CodeSnippet(
        String code,
        Language language,
        Optional<Caption> caption
    ) implements Slide {}

    record CodeShot(
        Image image,
        Language language,
        Optional<Caption> caption
    ) implements Slide {}

    record Screenshot(
        Image image,
        Color background,
        Optional<Caption> caption
    ) implements Slide {}

    Optional<Caption> caption();

    record Caption(String title, String subtitle) {}
}

```
<div class="social open-gh-btn my-4">
  <a class="btn btn-github" href="https://github.com/tobiasbriones/blog/tree/main/swe/dev/java/javafx/drawing/productivity/building-slides-from-screenshots-app-in-javafx/slides---ep/src/main/java/engineer/mathsoftware/blog/slides/Slide.java" target="_blank">
    <i class="fab fa-github">
      
    </i>
    <span>
      Open in GitHub
    </span>
  </a>
</div>