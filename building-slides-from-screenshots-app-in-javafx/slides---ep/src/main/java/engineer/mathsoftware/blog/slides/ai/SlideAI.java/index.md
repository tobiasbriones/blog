---
permalink: building-slides-from-screenshots-app-in-javafx/slides---ep/src/main/java/engineer/mathsoftware/blog/slides/ai/SlideAI.java.html
title: "building-slides-from-screenshots-app-in-javafx/slides---ep/src/main/java/engineer/mathsoftware/blog/slides/ai/SlideAI.java"
---

# SlideAI.java
```java
// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.ai;

import javafx.geometry.BoundingBox;
import javafx.scene.image.Image;

import java.util.List;

public sealed interface SlideAI {
    record OcrWordDetection(List<BoundingBox> wordBoxes) implements SlideAI {
        public static OcrWordDetection from(Image image) {
            return new OcrWordDetection(Ocr.textBoxes(image));
        }
    }
}

```
<div class="social open-gh-btn my-4">
  <a class="btn btn-github" href="https://github.com/tobiasbriones/blog/tree/main/swe/dev/java/javafx/drawing/productivity/building-slides-from-screenshots-app-in-javafx/slides---ep/src/main/java/engineer/mathsoftware/blog/slides/ai/SlideAI.java" target="_blank">
    <i class="fab fa-github">
      
    </i>
    <span>
      Open in GitHub
    </span>
  </a>
</div>