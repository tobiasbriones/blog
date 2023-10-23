---
permalink: building-slides-from-screenshots-app-in-javafx/slides---ep/src/main/java/engineer/mathsoftware/blog/slides/SlideSize.java.html
title: "building-slides-from-screenshots-app-in-javafx/slides---ep/src/main/java/engineer/mathsoftware/blog/slides/SlideSize.java"
---

# SlideSize.java
```java
// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides;

public record SlideSize(double width, double height) {
    public enum Predefined {
        HD(new SlideSize(1_280.0, 720.0)),
        FHD(new SlideSize(1_920.0, 1_080.0));

        public static Predefined from(SlideSize size) {
            if (size.equals(FHD.value())) {
                return FHD;
            }
            return HD;
        }

        private final SlideSize value;

        public SlideSize value() {
            return value;
        }

        Predefined(SlideSize value) {
            this.value = value;
        }
    }
}

```
<div class="social open-gh-btn my-4">
  <a class="btn btn-github" href="https://github.com/tobiasbriones/blog/tree/main/swe/dev/java/javafx/drawing/productivity/building-slides-from-screenshots-app-in-javafx/slides---ep/src/main/java/engineer/mathsoftware/blog/slides/SlideSize.java" target="_blank">
    <i class="fab fa-github">
      
    </i>
    <span>
      Open in GitHub
    </span>
  </a>
</div>