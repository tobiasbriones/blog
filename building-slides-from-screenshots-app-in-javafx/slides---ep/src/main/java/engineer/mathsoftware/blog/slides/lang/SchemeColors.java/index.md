---
permalink: building-slides-from-screenshots-app-in-javafx/slides---ep/src/main/java/engineer/mathsoftware/blog/slides/lang/SchemeColors.java.html
title: "building-slides-from-screenshots-app-in-javafx/slides---ep/src/main/java/engineer/mathsoftware/blog/slides/lang/SchemeColors.java"
---

# SchemeColors.java
```java
// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.lang;

import javafx.scene.paint.Color;

public final class SchemeColors {
    public static Color color(Element element) {
        return switch (element.toEnum()) {
            case Keyword -> Color.web("#7986cb");
            case Symbol -> Color.web("#cc7832");
            case Number -> Color.web("#0288d1");
            case StringLiteral -> Color.web("#558b2f");
            case Comment -> Color.web("#757575");
            case Type, Other -> Color.web("#e0e0e0");
        };
    }

    private SchemeColors() {}
}

```
<div class="social open-gh-btn my-4">
  <a class="btn btn-github" href="https://github.com/tobiasbriones/blog/tree/main/swe/dev/java/javafx/drawing/productivity/building-slides-from-screenshots-app-in-javafx/slides---ep/src/main/java/engineer/mathsoftware/blog/slides/lang/SchemeColors.java" target="_blank">
    <i class="fab fa-github">
      
    </i>
    <span>
      Open in GitHub
    </span>
  </a>
</div>