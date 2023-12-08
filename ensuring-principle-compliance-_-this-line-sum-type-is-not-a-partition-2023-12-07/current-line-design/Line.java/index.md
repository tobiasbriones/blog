---
permalink: ensuring-principle-compliance-_-this-line-sum-type-is-not-a-partition-2023-12-07/current-line-design/Line.java.html
title: "ensuring-principle-compliance-_-this-line-sum-type-is-not-a-partition-2023-12-07/current-line-design/Line.java"
---

# Line.java
```java
// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

public sealed interface Line extends Shape {
    record Segment(
        double sx,
        double sy,
        double ex,
        double ey
    ) implements Line {
        @Override
        public Segment minus(double minusRadius) {
            var x = ex - sx;
            var y = ey - sy;
            var angle = atan(y / x);
            var dx = minusRadius * cos(angle);
            var dy = minusRadius * sin(angle);
            return new Segment(
                sx + dx,
                sy + dy,
                ex - dx,
                ey - dy
            );
        }
    }

    record HSegment(
        double cx,
        double cy,
        double radius
    ) implements Line {
        @Override
        public double sx() {
            return cx - radius;
        }

        @Override
        public double sy() {
            return cy;
        }

        @Override
        public double ex() {
            return cx + radius;
        }

        @Override
        public double ey() {
            return cy;
        }

        @Override
        public HSegment minus(double minusRadius) {
            return new HSegment(cx, cy, radius - minusRadius);
        }
    }

    record VSegment(
        double cx,
        double cy,
        double radius
    ) implements Line {
        @Override
        public double sx() {
            return cx;
        }

        @Override
        public double sy() {
            return cy + radius;
        }

        @Override
        public double ex() {
            return cx;
        }

        @Override
        public double ey() {
            return cy - radius;
        }

        @Override
        public VSegment minus(double minusRadius) {
            return new VSegment(cx, cy, radius - minusRadius);
        }
    }

    double sx();

    double sy();

    double ex();

    double ey();

    Line minus(double minusRadius);

    @Override
    default double area() {
        return 0.0;
    }
}

```
<div class="social open-gh-btn my-4">
  <a class="btn btn-github" href="https://github.com/tobiasbriones/blog/tree/main/swe/design/cs/fp/java/canvasplay/ensuring-principle-compliance-_-this-line-sum-type-is-not-a-partition-2023-12-07/current-line-design/Line.java" target="_blank">
    <i class="fab fa-github">
      
    </i>
    <span>
      Open in GitHub
    </span>
  </a>
</div>