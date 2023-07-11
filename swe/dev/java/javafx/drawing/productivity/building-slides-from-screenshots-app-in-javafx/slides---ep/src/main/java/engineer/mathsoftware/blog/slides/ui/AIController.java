// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.ui;

import engineer.mathsoftware.blog.slides.drawing.ai.Stateful;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.BoundingBox;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.shape.Line;
import javafx.scene.transform.Scale;

import java.util.*;

import static engineer.mathsoftware.blog.slides.ai.SlideAI.OcrWordDetection;
import static engineer.mathsoftware.blog.slides.drawing.ai.AIShape.State;
import static engineer.mathsoftware.blog.slides.drawing.ai.AIShape.WordSelection;

class AIController {
    private final ObjectProperty<WordSelection> wordSelectionProperty;
    private final List<BoundingBox> aiBoxes;
    private Group group;
    private SlideAIView aiView;
    private Image slideDrawingSnapshot;
    private BackgroundStatus status;

    AIController() {
        wordSelectionProperty = new SimpleObjectProperty<>();
        aiBoxes = new ArrayList<>();
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

        aiBoxes.clear();
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

    Optional<Line> onMouseClicked() {
        if (aiView == null) {
            return Optional.empty();
        }
        if (!aiView.isShowing()) {
            return Optional.empty();
        }
        if (wordSelectionProperty.isNull().get()) {
            return Optional.empty();
        }
        var sel = wordSelectionProperty.get();
        var focus = sel.wordFocus();

        focus.get().ifPresent(
            stateFocus -> aiView.setWordSelectionFocus(
                stateFocus.object(),
                State.Selected
            )
        );
        return focus
            .get()
            .map(Stateful.Focus::object)
            .map(this::sumLine);
    }

    List<Line> getFocusLinesInRow() {
        var rect = wordSelectionProperty
            .get()
            .wordFocus()
            .get()
            .map(Stateful.Focus::object)
            .orElse(null);

        if (rect == null) {
            return List.of();
        }
        return aiBoxes
            .stream()
            .filter(box -> isInSameRow(rect, box))
            .map(box -> new Line(
                box.getMinX(),
                box.getMaxY(),
                box.getMaxX(),
                box.getMaxY()
            ))
            .toList();
    }

    private Line sumLine(BoundingBox rect) {
        aiBoxes.add(rect);

        var selection = aiBoxes
            .stream()
            .filter(box -> isInSameRow(rect, box))
            .reduce((b1, b2) -> {
                    var x1 = Math.min(b1.getMinX(), b2.getMinX());
                    var x2 = Math.max(b1.getMaxX(), b2.getMaxX());
                    var y1 = Math.min(b1.getMinY(), b2.getMinY());
                    var y2 = Math.max(b1.getMaxY(), b2.getMaxY());
                    return new BoundingBox(x1, y1, x2 - x1, y2 - y1);
                }
            )
            .orElse(rect);

        if (!selection.equals(rect)) {
            aiBoxes.remove(rect);
            aiBoxes.add(selection);
        }
        return new Line(
            selection.getMinX(),
            selection.getMaxY(),
            selection.getMaxX(),
            selection.getMaxY()
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

    private static boolean isInSameRow(BoundingBox rect, BoundingBox box) {
        return box.getMinY() <= rect.getMaxY() && box.getMaxY() >= rect.getMinY();
    }
}
