// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.ui;

import engineer.mathsoftware.blog.slides.drawing.ai.AIDrawing;
import engineer.mathsoftware.blog.slides.drawing.ai.AIShape;
import engineer.mathsoftware.blog.slides.drawing.ai.GroupAIDrawing;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Group;

import static engineer.mathsoftware.blog.slides.ai.SlideAI.OcrWordDetection;

class SlideAIView {
    private final Group group;
    private final AIDrawing aiDrawing;
    private final ObjectProperty<OcrWordDetection> ocrWordDetectionProperty;

    SlideAIView() {
        group = new Group();
        aiDrawing = new GroupAIDrawing(group);
        ocrWordDetectionProperty = new SimpleObjectProperty<>();
    }

    ObjectProperty<OcrWordDetection> ocrWordDetectionProperty() {
        return ocrWordDetectionProperty;
    }

    void init(Group parent) {
        parent.getChildren().add(group);
        ocrWordDetectionProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateTextBoxes(newValue);
            }
            else {
                clear();
            }
        });
    }

    void show() {
        group.setVisible(true);
    }

    void hide() {
        group.setVisible(false);
    }

    private void updateTextBoxes(OcrWordDetection det) {
        var slideAIDrawing = AIShape.WordSelection.of(det);

        aiDrawing.setup(slideAIDrawing);
        aiDrawing.clear();
        aiDrawing.draw(group);
    }

    private void clear() {
        aiDrawing.clear();
    }
}
