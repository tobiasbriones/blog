// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.sierpinskipetal.app;

import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

class AppCanvasView {
    private static final double CANVAS_SCALE = 1.0;
    private static final double CANVAS_WIDTH = 900.0 * CANVAS_SCALE;
    private static final double CANVAS_HEIGHT = 900.0 * CANVAS_SCALE;
    private static final Color bgColor = Color.web("#fafafa");
    private final Canvas canvas;
    private final AppController controller;

    AppCanvasView(AppController controller) {
        this.canvas = new Canvas();
        this.controller = controller;
    }

    Node getNode() {
        return canvas;
    }

    void init() {
        canvas.setWidth(CANVAS_WIDTH);
        canvas.setHeight(CANVAS_HEIGHT);

        draw();
    }

    void draw() {
        var ctx = canvas.getGraphicsContext2D();

        ctx.setFill(bgColor);
        ctx.fillRect(0.0, 0.0, CANVAS_WIDTH, CANVAS_HEIGHT);
    }
}
