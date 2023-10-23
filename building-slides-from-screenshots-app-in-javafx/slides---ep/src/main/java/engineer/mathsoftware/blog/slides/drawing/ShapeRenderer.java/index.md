---
permalink: building-slides-from-screenshots-app-in-javafx/slides---ep/src/main/java/engineer/mathsoftware/blog/slides/drawing/ShapeRenderer.java.html
title: "building-slides-from-screenshots-app-in-javafx/slides---ep/src/main/java/engineer/mathsoftware/blog/slides/drawing/ShapeRenderer.java"
---

# ShapeRenderer.java
```java
// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.drawing;

import engineer.mathsoftware.blog.slides.Colors;
import engineer.mathsoftware.blog.slides.Palette;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import static engineer.mathsoftware.blog.slides.drawing.Drawings.*;

public class ShapeRenderer {
    private final Shape shape;
    private final Palette palette;
    private Group group;
    private double startX;
    private double startY;
    private double endX;
    private double endY;
    private double strokeWidth;
    private boolean keepProportions;

    public ShapeRenderer(Shape shape, Palette palette) {
        this.shape = shape;
        this.palette = palette;
        this.group = null;
        this.startX = 0.0;
        this.startY = 0.0;
        this.endX = 0.0;
        this.endY = 0.0;
        this.strokeWidth = 4.0;
        this.keepProportions = false;
    }

    public void setGroup(Group newGroup) {
        group = newGroup;

        group.getChildren().add(shape);
    }

    public double getStartX() {
        return startX;
    }

    public double getStartY() {
        return startY;
    }

    public double getEndX() {
        return endX;
    }

    public double getEndY() {
        return endY;
    }

    public void setStrokeWidth(double newStrokeWidth) {
        strokeWidth = newStrokeWidth;
    }

    public void remove() {
        group.getChildren().remove(shape);
    }

    public void keepProportions(boolean proportions) {
        keepProportions = proportions;
    }

    public void start(double x, double y) {
        startX = x;
        startY = y;
    }

    public void end(double x, double y) {
        endX = x;
        endY = y;
    }

    public void render() {
        switch (shape) {
            case Line line -> renderLine(line);
            case Rectangle rectangle -> renderRectangle(rectangle);
            case Circle circle -> renderCircle(circle);
            default ->
                throw new IllegalStateException("Unexpected shape: " + shape);
        }
    }

    private void renderLine(Line line) {
        var color = Colors.color(palette);

        line.setStartX(startX);
        line.setStartY(startY);
        line.setStrokeWidth(strokeWidth);
        line.setStroke(color);
        line.setFill(color);

        if (keepProportions) {
            var xDiff = Math.abs(startX - endX);
            var yDiff = Math.abs(startY - endY);
            var horizontal = xDiff >= yDiff;

            if (horizontal) {
                line.setEndX(endX);
                line.setEndY(startY);
            }
            else {
                line.setEndX(startX);
                line.setEndY(endY);
            }
        }
        else {
            line.setEndX(endX);
            line.setEndY(endY);
        }
    }

    private void renderRectangle(Rectangle rectangle) {
        var width = Math.abs(endX - startX);
        var height = Math.abs(endY - startY);
        var arc = getCornerRadius(width, height);

        rectangle.setX(Math.min(startX, endX));
        rectangle.setY(Math.min(startY, endY));
        rectangle.setWidth(width);
        rectangle.setHeight(height);
        rectangle.setArcWidth(arc);
        rectangle.setArcHeight(arc);
        rectangle.setStrokeWidth(strokeWidth);
        rectangle.setStroke(Colors.color(palette));
        rectangle.setFill(Color.TRANSPARENT);
    }

    private void renderCircle(Circle circle) {
        var width = Math.abs(endX - startX);
        var height = Math.abs(endY - startY);
        var radius = Math.max(width, height);

        circle.setCenterX(Math.min(startX, endX));
        circle.setCenterY(Math.min(startY, endY));
        circle.setRadius(radius);
        circle.setStrokeWidth(strokeWidth);
        circle.setStroke(Colors.color(palette));
        circle.setFill(Color.TRANSPARENT);
    }
}

```
<div class="social open-gh-btn my-4">
  <a class="btn btn-github" href="https://github.com/tobiasbriones/blog/tree/main/swe/dev/java/javafx/drawing/productivity/building-slides-from-screenshots-app-in-javafx/slides---ep/src/main/java/engineer/mathsoftware/blog/slides/drawing/ShapeRenderer.java" target="_blank">
    <i class="fab fa-github">
      
    </i>
    <span>
      Open in GitHub
    </span>
  </a>
</div>