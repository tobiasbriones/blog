// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.ui;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.transform.Scale;

import static engineer.mathsoftware.blog.slides.ai.SlideAI.OcrWordDetection;
import static engineer.mathsoftware.blog.slides.drawing.ai.AIShape.*;

class AIController {
    private final ObjectProperty<WordSelection> wordSelectionProperty;
    private Group group;
    private SlideAIView aiView;
    private Image slideDrawingSnapshot;
    private BackgroundStatus status;

    AIController() {
        wordSelectionProperty = new SimpleObjectProperty<>();
        group = null;
        aiView = null;
        slideDrawingSnapshot = null;
        status = null;
    }

    void setStatus(BackgroundStatus newStatus) {
        status = newStatus;
    }

    void init(Group slideDrawing) {
        group = slideDrawing;
        aiView = new SlideAIView();
        slideDrawingSnapshot = null;

        aiView.init(slideDrawing);
        aiView.hide();
        aiView.wordSelectionProperty().bind(wordSelectionProperty);
        loadSlideDrawingSnapshot();
        loadOcr();
    }

    void onShowTextBoxes() {
        aiView.show();
    }

    void onHideTextBoxes() {
        aiView.hide();
    }

    void onMouseMoved(double x, double y) {
        if (aiView == null) {
            return;
        }
        if (!aiView.isShowing()) {
            return;
        }
        if (wordSelectionProperty.isNull().get()) {
            return;
        }
        var match = wordSelectionProperty
            .get()
            .wordBoxes()
            .stream()
            .filter(box -> box.contains(x, y))
            // Avoid selecting the whole slide box
            .filter(box -> box.getHeight() < 100.0)
            .findFirst();

        match.ifPresentOrElse(
            box -> aiView.setWordSelectionFocus(box, State.Hovered),
            () -> aiView.setWordSelectionFocus(null, null)
        );
    }

    void onMouseClicked() {
        if (aiView == null) {
            return;
        }
        if (!aiView.isShowing()) {
            return;
        }
        if (wordSelectionProperty.isNull().get()) {
            return;
        }
        var sel = wordSelectionProperty.get();
        var focus = sel.wordFocus();

        focus.get().ifPresent(
            stateFocus -> aiView.setWordSelectionFocus(
                stateFocus.object(),
                State.Selected
            )
        );
    }

    void onMouseExited() {
        if (aiView == null) {
            return;
        }
        aiView.setWordSelectionFocus(null, null);
    }

    private void loadSlideDrawingSnapshot() {
        if (slideDrawingSnapshot == null) {
            var params = new SnapshotParameters();
            var invScaleX = 1.0 / group.getScaleX();
            var invScaleY = 1.0 / group.getScaleY();

            params.setTransform(new Scale(invScaleX, invScaleY));
            slideDrawingSnapshot = group.snapshot(params, null);
        }
    }

    private void loadOcr() {
        clearOcr();
        setStatusMsg("Loading AI...");

        Thread.startVirtualThread(
            () -> {
                try {
                    var det = OcrWordDetection.from(slideDrawingSnapshot);

                    Platform.runLater(() -> {
                        loadOcr(det);
                        setStatusMsg("AI loaded");
                    });
                }
                catch (RuntimeException e) {
                    setStatusMsg(e.getMessage());
                }
            }
        );
    }

    private void loadOcr(OcrWordDetection det) {
        var slideAIDrawing = WordSelection.of(det);
        wordSelectionProperty.set(slideAIDrawing);
    }

    private void clearOcr() {
        wordSelectionProperty.set(null);
    }

    private void setStatusMsg(String msg) {
        if (status != null) {
            status.setStatus(msg);
        }
    }
}
