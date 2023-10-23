---
permalink: building-slides-from-screenshots-app-in-javafx/slides---ep/src/main/java/engineer/mathsoftware/blog/slides/Colors.java.html
title: "building-slides-from-screenshots-app-in-javafx/slides---ep/src/main/java/engineer/mathsoftware/blog/slides/Colors.java"
---

# Colors.java
```java
// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides;

import javafx.scene.paint.Color;

public final class Colors {
    public static Color color(Palette palette) {
        return Color.web(palette.colorCode());
    }

    public static Color color(Language language) {
        var code = switch (language) {
            case CSharp -> "#178600";
            case CSS -> "#563d7c";
            case Golang -> "#00ADD8";
            case Haskell -> "#5e5086";
            case HTML -> "#e34c26";
            case Java -> "#b07219";
            case JavaScript -> "#f1e05a";
            case Kotlin -> "#A97BFF";
            case Lean -> "#83579A";
            case PureScript -> "#1D222D";
            case Python -> "#3572A5";
            case Rust -> "#dea584";
            case TypeScript -> "#2b7489";
        };
        return Color.web(code);
    }

    private Colors() {}
}

```
<div class="social open-gh-btn my-4">
  <a class="btn btn-github" href="https://github.com/tobiasbriones/blog/tree/main/swe/dev/java/javafx/drawing/productivity/building-slides-from-screenshots-app-in-javafx/slides---ep/src/main/java/engineer/mathsoftware/blog/slides/Colors.java" target="_blank">
    <i class="fab fa-github">
      
    </i>
    <span>
      Open in GitHub
    </span>
  </a>
</div>