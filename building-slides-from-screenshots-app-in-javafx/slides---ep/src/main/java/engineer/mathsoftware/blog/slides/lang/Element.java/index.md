---
permalink: building-slides-from-screenshots-app-in-javafx/slides---ep/src/main/java/engineer/mathsoftware/blog/slides/lang/Element.java.html
title: "building-slides-from-screenshots-app-in-javafx/slides---ep/src/main/java/engineer/mathsoftware/blog/slides/lang/Element.java"
---

# Element.java
```java
// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.lang;

import engineer.mathsoftware.blog.slides.Enums;

public sealed interface Element extends Enums.ToEnum<ElementItem> {
    record Keyword(String value) implements Element {}

    record Symbol(String value) implements Element {}

    record Type(String value) implements Element {}

    record Number(String value) implements Element {}

    record StringLiteral(String value) implements Element {}

    record Comment(String value) implements Element {}

    record Other(String value) implements Element {}

    record TokenParsing(Element element) {}

    String value();

    @Override
    default Class<ElementItem> enumType() {
        return ElementItem.class;
    }
}

```
<div class="social open-gh-btn my-4">
  <a class="btn btn-github" href="https://github.com/tobiasbriones/blog/tree/main/swe/dev/java/javafx/drawing/productivity/building-slides-from-screenshots-app-in-javafx/slides---ep/src/main/java/engineer/mathsoftware/blog/slides/lang/Element.java" target="_blank">
    <i class="fab fa-github">
      
    </i>
    <span>
      Open in GitHub
    </span>
  </a>
</div>