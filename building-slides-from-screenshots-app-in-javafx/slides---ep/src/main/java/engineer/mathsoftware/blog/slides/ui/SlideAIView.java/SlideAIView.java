// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.ui;

import engineer.mathsoftware.blog.slides.drawing.ai.AIDrawing;
import engineer.mathsoftware.blog.slides.drawing.ai.AIShape;
import engineer.mathsoftware.blog.slides.drawing.ai.GroupAIDrawing;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.BoundingBox;
import javafx.scene.Group;

import static engineer.mathsoftware.blog.slides.drawing.ai.AIShape.*;

class SlideAIView {
    private final Group group;
    private final AIDrawing aiDrawing;
    private final ObjectProperty<WordSelection> wordSelectionProperty;

    SlideAIView() {
        group = new Group();
        aiDrawing = new GroupAIDrawing(group);
        wordSelectionProperty = new SimpleObjectProperty<>();
    }

    ObjectProperty<WordSelection> wordSelectionProperty() {
        return wordSelectionProperty;
    }

    void init(Group parent) {
        parent.getChildren().add(group);
        wordSelectionProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateTextBoxes(newValue);
            }
            else {
                clear();
            }
        });
    }

    boolean isShowing() {
        return group.isVisible();
    }

    void show() {
        group.setVisible(true);
    }

    void hide() {
        group.setVisible(false);
    }

    void setWordSelectionFocus(BoundingBox box, State state) {
        if (wordSelectionProperty.isNull().get()) {
            return;
        }
        var sel = wordSelectionProperty.get();

        sel.wordFocus().set(box, state);
        updateTextBoxes(sel);
    }

    private void updateTextBoxes(AIShape det) {
        aiDrawing.setup(det);
        aiDrawing.clear();
        aiDrawing.draw(group);
    }

    private void clear() {
        aiDrawing.clear();
    }
}
