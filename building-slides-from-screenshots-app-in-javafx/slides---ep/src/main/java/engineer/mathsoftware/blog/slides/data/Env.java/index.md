---
permalink: building-slides-from-screenshots-app-in-javafx/slides---ep/src/main/java/engineer/mathsoftware/blog/slides/data/Env.java.html
title: "building-slides-from-screenshots-app-in-javafx/slides---ep/src/main/java/engineer/mathsoftware/blog/slides/data/Env.java"
---

# Env.java
```java
// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.data;

import java.nio.file.Path;

public final class Env {
    public static final String DATA_ROOT = "data";

    public static final String SAVE_DIR = Path
        .of(DATA_ROOT, "presentation")
        .toString();

    private Env() {}
}

```
<div class="social open-gh-btn my-4">
  <a class="btn btn-github" href="https://github.com/tobiasbriones/blog/tree/main/swe/dev/java/javafx/drawing/productivity/building-slides-from-screenshots-app-in-javafx/slides---ep/src/main/java/engineer/mathsoftware/blog/slides/data/Env.java" target="_blank">
    <i class="fab fa-github">
      
    </i>
    <span>
      Open in GitHub
    </span>
  </a>
</div>