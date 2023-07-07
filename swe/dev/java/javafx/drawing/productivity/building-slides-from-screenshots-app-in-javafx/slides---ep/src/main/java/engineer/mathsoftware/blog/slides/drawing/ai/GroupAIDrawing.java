// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.drawing.ai;

import javafx.geometry.BoundingBox;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;

import static engineer.mathsoftware.blog.slides.drawing.ai.AIShape.*;

public class GroupAIDrawing implements AIDrawing {
    private final Group group;
    private AIShape ai;

    public GroupAIDrawing(Group drawing) {
        group = drawing;
        ai = null;
    }

    @Override
    public void setup(AIShape shape) {
        ai = shape;
    }

    @Override
    public Group draw(Group slideDrawing) {
        switch (ai) {
            case WordSelection ocr -> drawWordSelection(
                ocr.wordBoxes(),
                ocr.wordFocus()
            );
        }
        return slideDrawing;
    }

    @Override
    public void clear() {
        group.getChildren().clear();
    }

    private void drawWordSelection(
        Iterable<? extends BoundingBox> boundingBoxes,
        Stateful<? extends BoundingBox, State> state
    ) {
        boundingBoxes.forEach(boundingBox -> drawBoundingBox(
            boundingBox,
            State.Normal
        ));

        state.get().ifPresent(focus -> drawBoundingBox(
            focus.object(),
            focus.state()
        ));
    }

    private void drawBoundingBox(
        BoundingBox boundingBox,
        State state
    ) {
        var rect = new Rectangle();

        rect.setX(boundingBox.getMinX());
        rect.setY(boundingBox.getMinY());
        rect.setWidth(boundingBox.getWidth());
        rect.setHeight(boundingBox.getHeight());
        rect.setStroke(color(state));
        rect.setStrokeWidth(2.0);
        rect.setFill(fill(state));
        rect.setArcWidth(16.0);
        rect.setArcHeight(16.0);
        group.getChildren().add(rect);
    }
}
