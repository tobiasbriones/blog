---
permalink: building-slides-from-screenshots-app-in-javafx/slides---ep/src/main/java/engineer/mathsoftware/blog/slides/data/ImageItem.java.html
title: "building-slides-from-screenshots-app-in-javafx/slides---ep/src/main/java/engineer/mathsoftware/blog/slides/data/ImageItem.java"
---

# ImageItem.java
```java
// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.data;

import javafx.scene.image.Image;

import java.util.Objects;

public record ImageItem(
    String filename,
    Image image
) {
    @Override
    public int hashCode() {
        return Objects.hash(filename);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ImageItem imageItem)
            && Objects.equals(filename, imageItem.filename());
    }
}

```
<div class="social open-gh-btn my-4">
  <a class="btn btn-github" href="https://github.com/tobiasbriones/blog/tree/main/swe/dev/java/javafx/drawing/productivity/building-slides-from-screenshots-app-in-javafx/slides---ep/src/main/java/engineer/mathsoftware/blog/slides/data/ImageItem.java" target="_blank">
    <i class="fab fa-github">
      
    </i>
    <span>
      Open in GitHub
    </span>
  </a>
</div>