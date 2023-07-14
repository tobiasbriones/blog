// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.ui;

import engineer.mathsoftware.blog.slides.Enums;
import engineer.mathsoftware.blog.slides.Palette;
import engineer.mathsoftware.blog.slides.ShapeItem;
import engineer.mathsoftware.blog.slides.data.ImageItem;
import engineer.mathsoftware.blog.slides.drawing.ShapeRenderer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.util.*;

class SlideDrawingController {
    private final Deque<ShapeRenderer> shapes;
    private final ChangeState changes;
    private final ObjectProperty<ShapeItem> shapeProperty;
    private final ObjectProperty<Palette> paletteProperty;
    private final AIController aiController;
    private final AIInvalidation aiInvalidation;
    private final AutoSave autoSave;
    private final BooleanProperty autoSaveProperty;
    private Group group;
    private ScrollPane scrollPane;
    private boolean shiftPressed;

    SlideDrawingController() {
        shapes = new LinkedList<>();
        changes = new ChangeState();
        paletteProperty = new SimpleObjectProperty<>();
        shapeProperty = new SimpleObjectProperty<>();
        aiController = new AIController();
        aiInvalidation = new AIInvalidation(this::loadAI);
        autoSave = new AutoSave();
        autoSaveProperty = new SimpleBooleanProperty();
        group = null;
        scrollPane = null;
        shiftPressed = false;
    }

    void setStatus(BackgroundStatus newStatus) {
        aiController.setStatus(newStatus);
        autoSave.setStatus(newStatus);
    }

    void setDrawing(Group newDrawing) {
        if (group != null) {
            unbindEvents();
        }
        group = newDrawing;

        autoSave.setDrawing(group);
        aiInvalidation.slideChanged();
        clearState();
        bindEvents();
    }

    BooleanProperty autoSaveProperty() {
        return autoSaveProperty;
    }

    void onSlideChanged(ImageItem newItem) {
        autoSave.onSlideChanged(newItem);
    }

    void onDrawingChanged(ImageItem item) {
        changes.setCurrentItem(item);
        loadState();

        autoSave.onDrawingChanged(item);
    }

    void setScrollPane(ScrollPane newScrollPane) {
        scrollPane = newScrollPane;
    }

    void setShapeComboBox(ComboBox<ShapeItem> shapeComboBox) {
        shapeComboBox.getItems().addAll(ShapeItem.values());
        shapeComboBox.setConverter(new Enums.EnglishConverter<>(ShapeItem.class));
        shapeComboBox.getSelectionModel().select(0);
        shapeProperty.bind(shapeComboBox.valueProperty());
    }

    void setShapePaletteComboBox(ComboBox<Palette> shapePaletteComboBox) {
        shapePaletteComboBox.getItems().addAll(Palette.values());
        shapePaletteComboBox.setConverter(new Enums.EnglishConverter<>(Palette.class));
        shapePaletteComboBox.getSelectionModel().select(0);
        paletteProperty.bind(shapePaletteComboBox.valueProperty());
    }

    void setShapeBackButton(Button shapeBackButton) {
        var icBack = new Image(
            Objects.requireNonNull(
                getClass().getResourceAsStream("/ic_back.png")
            )
        );
        var iv = new ImageView(icBack);

        iv.setFitWidth(18.0);
        iv.setFitHeight(18.0);
        shapeBackButton.setGraphic(iv);
        shapeBackButton.setOnAction(event -> popShape());
    }

    void saveCurrentSlide() {
        autoSave.saveSlide();
    }

    private void bindEvents() {
        group.setOnMouseMoved(
            event -> aiController.onMouseMoved(event.getX(), event.getY())
        );
        group.setOnMouseExited(event -> aiController.onMouseExited());
        group.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                var startX = event.getX();
                var startY = event.getY();
                var shape = pushShape();

                shape.start(startX, startY);

                scrollPane.setPannable(false);
            }
            else {
                aiController
                    .onMouseClicked()
                    .ifPresent(line -> {
                        var shape = pushShape();

                        clearAiLineRow();
                        shape.start(line.getStartX(), line.getStartY());
                        shape.end(line.getEndX(), line.getEndY());
                        shape.render();
                        saveState();
                    });
            }
        });
        group.setOnMouseDragged(event -> {
            if (event.getButton() != MouseButton.SECONDARY) {
                return;
            }
            if (shapes.peek() == null) {
                return;
            }
            var shape = shapes.peek();
            var currentX = event.getX();
            var currentY = event.getY();
            var normX = normalizeX(currentX);
            var normY = normalizeY(currentY);

            shape.keepProportions(shiftPressed);
            shape.end(normX, normY);
            shape.render();
        });

        group.setOnMouseReleased(event -> {
            saveState();
            scrollPane.setPannable(true);
        });

        group.setOnScroll((ScrollEvent event) -> {
            var zoomFactor = 1.05;
            var deltaY = event.getDeltaY();

            if (deltaY < 0.0) {
                zoomFactor = 2.0 - zoomFactor;
            }
            group.setScaleX(group.getScaleX() * zoomFactor);
            group.setScaleY(group.getScaleY() * zoomFactor);
        });

        group.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            newValue.setOnKeyPressed(event -> {
                switch (event.getCode()) {
                    case SHIFT -> shiftPressed = true;
                    case F1 -> onF1Pressed();
                }
            });

            newValue.setOnKeyReleased(event -> {
                switch (event.getCode()) {
                    case SHIFT -> shiftPressed = false;
                    case F1 -> aiController.onHideTextBoxes();
                }
            });
        });

        autoSaveProperty.addListener((observable, oldValue, newValue) ->
            autoSave.enable(newValue)
        );
    }

    private void clearAiLineRow() {
        aiController
            .getFocusLinesInRow()
            .forEach(line -> shapes
                .stream()
                .filter(shape ->
                    shape.getStartX() == line.getStartX()
                        && shape.getEndX() == line.getEndX()
                        && shape.getStartY() == line.getStartY()
                        && shape.getEndY() == line.getEndY()
                )
                .findFirst()
                .ifPresent(shape -> {
                    shape.remove();
                    shapes.remove(shape);
                }));
    }

    private void unbindEvents() {
        group.setOnMousePressed(null);
    }

    private void saveState() {
        changes.saveCopy(shapes);
    }

    private void loadState() {
        clearState();
        changes
            .get()
            .ifPresent(state ->
                state
                    .shapes()
                    .forEach(this::restoreShape)
            );
    }

    private void restoreShape(ShapeRenderer shape) {
        shape.setGroup(group);
        shape.render();
        shapes.addLast(shape);
    }

    private void clearState() {
        shapes.clear();
    }

    private ShapeRenderer pushShape() {
        var shape = switch (shapeProperty.get()) {
            case Line -> new Line();
            case Rectangle -> new Rectangle();
            case Circle -> new Circle();
        };
        var renderer = new ShapeRenderer(shape, paletteProperty.get());

        renderer.setGroup(group);
        shapes.push(renderer);
        return renderer;
    }

    private void popShape() {
        if (shapes.isEmpty()) {
            return;
        }
        var shape = shapes.pop();

        shape.remove();
        saveState();
    }

    private void onF1Pressed() {
        aiInvalidation.validate();
        aiController.onShowTextBoxes();
    }

    private void loadAI() {
        aiController.init(group);
    }

    private double normalizeX(double x) {
        var bounds = group.getBoundsInLocal();
        return Math.max(0.0, Math.min(bounds.getWidth() - 1.0, x));
    }

    private double normalizeY(double y) {
        var bounds = group.getBoundsInLocal();
        return Math.max(0.0, Math.min(bounds.getHeight() - 1.0, y));
    }

    private static class ChangeState {
        record SlideDrawingState(
            Collection<ShapeRenderer> shapes
        ) {}

        final Map<ImageItem, SlideDrawingState> changes;
        ImageItem currentItem;

        ChangeState() {
            changes = new HashMap<>();
            currentItem = null;
        }

        void setCurrentItem(ImageItem newCurrentItem) {
            currentItem = newCurrentItem;
        }

        Optional<SlideDrawingState> get() {
            return Optional.ofNullable(changes.get(currentItem));
        }

        void saveCopy(
            Deque<ShapeRenderer> shapes
        ) {
            if (currentItem == null) {
                return;
            }
            var shapesCopy = new ArrayList<>(shapes);
            var state = new SlideDrawingState(shapesCopy);

            changes.put(currentItem, state);
        }
    }

    private static class AIInvalidation {
        final Runnable validator;
        boolean isInvalid;

        AIInvalidation(Runnable validator) {
            this.validator = validator;
            this.isInvalid = true;
        }

        void validate() {
            if (isInvalid) {
                validator.run();
            }
            isInvalid = false;
        }

        void slideChanged() {
            isInvalid = true;
        }
    }
}
