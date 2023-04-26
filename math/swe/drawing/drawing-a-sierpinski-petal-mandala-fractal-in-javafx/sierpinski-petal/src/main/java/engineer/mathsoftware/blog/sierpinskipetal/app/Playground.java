// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.sierpinskipetal.app;

import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.TextAlignment;

@SuppressWarnings("ALL")
class Playground {
    final Canvas canvas;
    final GraphicsContext ctx;
    final double scale;
    double opacity;
    String title;

    double width() { return canvas.getWidth() / scale; }

    double height() { return canvas.getHeight() / scale; }

    double cx() { return width() / 2; }

    double cy() { return height() / 2; }

    Playground(Canvas canvas, double scale) {
        this.canvas = canvas;
        this.ctx = canvas.getGraphicsContext2D();
        this.scale = scale;
        this.opacity = 1.0;
        this.title = "Drawing a Flower";
    }

    void play() {
        reset();

        var radius = 100;
        var cx = cx();
        var cy = cy()+radius / 4;
        var color = Color.web("#131313");
        var anim = new Flower(
            radius,
            color,
            cx,
            cy
        );

        anim.anim1_Diameter();
    }

    void reset() {
        clean();

        ctx.setGlobalAlpha(1);
        drawRuler();
        ctx.setGlobalAlpha(opacity);
    }

    void clean() {
        var bgColor = Color.web("#fafafa");

        ctx.setGlobalAlpha(1);
        ctx.setFill(bgColor);
        ctx.fillRect(0.0, 0.0, width(), height());
        drawSource(title);
        ctx.setGlobalAlpha(opacity);
    }

    void drawSource(String title) {
        setDrawingText("medium", 24, VPos.BOTTOM);

        ctx.fillText(
            "BLOG | mathsoftware.engineer",
            width() / 2,
            height() - 32
        );
        ctx.fillText(
            "Drawing a Sierpinski Petal Mandala Fractal in JavaFX",
            width() / 2,
            height() - 64
        );

        setDrawingText("extrabold", 36, VPos.BOTTOM);
        ctx.fillText(title, width() / 2, height() - 96);
    }

    void drawRuler() {
        ctx.setStroke(Color.web("#757575"));

        setDrawingText("medium", 16, VPos.TOP);

        ctx.strokeLine(0, 16, width(), 16);
        ctx.strokeLine(0, 24, width(), 24);

        for (var x = 50; x < width(); x++) {
            if (x % 50 == 0) {
                ctx.strokeLine(x, 16, x, 36);
                ctx.fillText(String.valueOf(x), x, 40);
            }
            else if (x % 25 == 0) {
                ctx.strokeLine(x, 16, x, 28);
            }
        }

        ctx.setTextAlign(TextAlignment.LEFT);
        ctx.setTextBaseline(VPos.CENTER);
        ctx.strokeLine(16, 0, 16, height());
        ctx.strokeLine(24, 0, 24, height());

        for (var y = 50; y < height(); y++) {
            if (y % 50 == 0) {
                ctx.strokeLine(16, y, 36, y);

                // Don't draw the first one to avoid colliding with
                // horizontal ruler
                if (y == 50) {
                    continue;
                }
                ctx.fillText(String.valueOf(y), 40, y);
            }
            else if (y % 25 == 0) {
                ctx.strokeLine(16, y, 28, y);
            }
        }
    }

    void setDrawingText(
        String weight,
        int size,
        VPos baseline
    ) {
        ctx.setFill(Color.web("#212121"));
        ctx.setTextAlign(TextAlignment.CENTER);
        ctx.setTextBaseline(baseline);
        ctx.setFontSmoothingType(FontSmoothingType.GRAY);
        ctx.setFont(Font.font("poppins " + weight, size));
    }

    void encloseHRuler(
        double radius,
        double cx,
        double cy,
        String txt
    ) {
        ctx.setStroke(Color.web("#4CAF50"));
        ctx.strokeLine(cx - radius, cy, cx + radius, cy);
        ctx.strokeLine(cx - radius, 0, cx - radius, cy + radius);
        ctx.strokeLine(cx + radius, 0, cx + radius, cy + radius);

        setDrawingText("medium", 20, VPos.BOTTOM);
        ctx.fillText(txt, cx, cy - 2);
    }

    void fillCenteredCircle(
        double radius,
        double cx,
        double cy
    ) {
        var diameter = 2.0 * radius;
        ctx.fillOval(cx - radius, cy - radius, diameter, diameter);
    }

    void fillCenteredArc(
        double radiusX,
        double radiusY,
        double cx,
        double cy
    ) {
        var diameterX = 2.0 * radiusX;
        var diameterY = 2.0 * radiusY;
        ctx.fillOval(cx - radiusX, cy - radiusY, diameterX, diameterY);
    }

    void strokeCenteredArc(
        double radiusX,
        double radiusY,
        double cx,
        double cy
    ) {
        var diameterX = 2.0 * radiusX;
        var diameterY = 2.0 * radiusY;
        ctx.strokeOval(cx - radiusX, cy - radiusY, diameterX, diameterY);
    }

    class Flower {
        final int radius;
        final Color color;
        final double cx;
        final double cy;

        Flower(
            int radius,
            Color color,
            double cx,
            double cy
        ) {
            this.radius = radius;
            this.color = color;
            this.cx = cx;
            this.cy = cy;
        }

        void anim1_Diameter() {
            reset();

            ctx.setFill(color);
            encloseHRuler(radius, cx, cy - radius, "diameter");
        }
    }
}
