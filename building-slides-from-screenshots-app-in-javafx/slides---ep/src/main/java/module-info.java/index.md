---
permalink: building-slides-from-screenshots-app-in-javafx/slides---ep/src/main/java/module-info.java.html
title: "building-slides-from-screenshots-app-in-javafx/slides---ep/src/main/java/module-info.java"
---

# module-info.java
```java
// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

module engineer.mathsoftware.blog.slides {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires tess4j;
    exports engineer.mathsoftware.blog.slides.data;
    exports engineer.mathsoftware.blog.slides.ui;
    opens engineer.mathsoftware.blog.slides.ui to javafx.fxml;
}

```
<div class="social open-gh-btn my-4">
  <a class="btn btn-github" href="https://github.com/tobiasbriones/blog/tree/main/swe/dev/java/javafx/drawing/productivity/building-slides-from-screenshots-app-in-javafx/slides---ep/src/main/java/module-info.java" target="_blank">
    <i class="fab fa-github">
      
    </i>
    <span>
      Open in GitHub
    </span>
  </a>
</div>