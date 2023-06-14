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
    private Group group;
    private Shape shape;
    private Palette palette;
    private double startX;
    private double startY;
    private double endX;
    private double endY;
    private boolean keepProportions;

    public ShapeRenderer() {
        this.group = null;
        this.shape = null;
        this.palette = Palette.Good;
        this.startX = 0.0;
        this.startY = 0.0;
        this.endX = 0.0;
        this.endY = 0.0;
        this.keepProportions = false;
    }

    public void setGroup(Group value) {
        group = value;

        if (shape != null) {
            group.getChildren().add(shape);
        }
    }

    public void setShape(Shape value) {
        shape = value;

        group.getChildren().add(shape);
    }

    public void remove() {
        group.getChildren().remove(shape);
    }

    public void setPalette(Palette value) {
        palette = value;
    }

    public void keepProportions(boolean value) {
        keepProportions = value;
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
        if (shape == null) {
            return;
        }
        switch (shape) {
            case Line line -> renderLine(line);
            case Rectangle rectangle -> renderRectangle(rectangle);
            case Circle circle -> renderCircle(circle);
            default ->
                throw new IllegalStateException("Unexpected value: " + shape);
        }
    }

    private void renderLine(Line line) {
        var color = Colors.color(palette);

        line.setStartX(startX);
        line.setStartY(startY);
        line.setStrokeWidth(2.0);
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
        rectangle.setStrokeWidth(2.0);
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
        circle.setStrokeWidth(2.0);
        circle.setStroke(Colors.color(palette));
        circle.setFill(Color.TRANSPARENT);
    }
}
