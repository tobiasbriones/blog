// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.ui;

import engineer.mathsoftware.blog.slides.Enums;
import engineer.mathsoftware.blog.slides.Palette;
import engineer.mathsoftware.blog.slides.ShapeItem;
import engineer.mathsoftware.blog.slides.drawing.ShapeRenderer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;

class SlideDrawingController {
    private final Deque<ShapeRenderer> shapes;
    private final ObjectProperty<ShapeItem> shapeProperty;
    private final ObjectProperty<Palette> paletteProperty;
    private final AIController aiController;
    private Group group;
    private ScrollPane scrollPane;
    private boolean shiftPressed;

    SlideDrawingController() {
        shapes = new LinkedList<>();
        paletteProperty = new SimpleObjectProperty<>();
        shapeProperty = new SimpleObjectProperty<>();
        aiController = new AIController();
        group = null;
        scrollPane = null;
        shiftPressed = false;
    }

    void setStatus(BackgroundStatus value) {
        aiController.setStatus(value);
    }

    void setDrawing(Group value) {
        if (group != null) {
            unbindEvents();
        }
        group = value;

        aiController.init(group);
        bindEvents();
        addShapes();
    }

    void setScrollPane(ScrollPane value) {
        scrollPane = value;
    }

    void setShapeComboBox(ComboBox<ShapeItem> value) {
        value.getItems().addAll(ShapeItem.values());
        value.setConverter(new Enums.EnglishConverter<>(ShapeItem.class));
        value.getSelectionModel().select(0);
        shapeProperty.bind(value.valueProperty());
    }

    void setShapePaletteComboBox(ComboBox<Palette> value) {
        value.getItems().addAll(Palette.values());
        value.setConverter(new Enums.EnglishConverter<>(Palette.class));
        value.getSelectionModel().select(0);
        paletteProperty.bind(value.valueProperty());
    }

    void setShapeBackButton(Button value) {
        var icBack = new Image(
            Objects.requireNonNull(
                getClass().getResourceAsStream("/ic_back.png")
            )
        );
        var iv = new ImageView(icBack);

        iv.setFitWidth(18.0);
        iv.setFitHeight(18.0);
        value.setGraphic(iv);
        value.setOnAction(event -> popShape());
    }

    private void bindEvents() {
        group.setOnMousePressed(event -> {
            if (event.getButton() != MouseButton.SECONDARY) {
                return;
            }
            var startX = event.getX();
            var startY = event.getY();
            var shape = pushShape();

            shape.start(startX, startY);

            scrollPane.setPannable(false);
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

        group.setOnMouseReleased(event -> scrollPane.setPannable(true));

        group.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            newValue.setOnKeyPressed(event -> {
                switch (event.getCode()) {
                    case SHIFT -> shiftPressed = true;
                    case CONTROL -> aiController.onShowTextBoxes();
                }
            });

            newValue.setOnKeyReleased(event -> {
                switch (event.getCode()) {
                    case SHIFT -> shiftPressed = false;
                    case CONTROL -> aiController.onHideTextBoxes();
                }
            });
        });
    }

    private void unbindEvents() {
        group.setOnMousePressed(null);
    }

    private void addShapes() {
        shapes.forEach(shape -> shape.setGroup(group));
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
    }

    private double normalizeX(double x) {
        var bounds = group.getBoundsInLocal();
        return Math.max(0.0, Math.min(bounds.getWidth() - 1.0, x));
    }

    private double normalizeY(double y) {
        var bounds = group.getBoundsInLocal();
        return Math.max(0.0, Math.min(bounds.getHeight() - 1.0, y));
    }
}
