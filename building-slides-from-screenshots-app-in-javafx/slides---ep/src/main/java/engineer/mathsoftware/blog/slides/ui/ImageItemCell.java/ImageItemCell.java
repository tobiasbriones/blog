// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.ui;

import engineer.mathsoftware.blog.slides.data.ImageItem;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Rectangle;

class ImageItemCell extends ListCell<ImageItem> {
    interface Listener {
        void onDelete(ImageItem item);

        void onArrange(int draggedIdx, int destIdx);
    }

    private final Listener l;
    private final HBox view;
    private final ImageView imageView;
    private final Label nameLabel;
    private final Button deleteButton;
    private final Tooltip tip;

    ImageItemCell(Listener l) {
        super();
        this.l = l;
        this.view = new HBox();
        this.imageView = new ImageView();
        this.nameLabel = new Label();
        this.deleteButton = new Button();
        this.tip = new Tooltip();

        init();
    }

    @Override
    protected void updateItem(ImageItem item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setText(null);
            setGraphic(null);
        }
        else {
            updateItem(item);
        }
    }

    private void init() {
        var imageParent = new HBox();

        imageView.setFitWidth(128.0);
        imageView.setFitHeight(64.0);
        imageView.setPreserveRatio(true);

        imageParent.setPrefWidth(144.0);
        imageParent.setPrefHeight(96.0);
        imageParent.setAlignment(Pos.CENTER);
        imageParent.getChildren().add(imageView);

        var maxNameLength = 20;

        nameLabel
            .textProperty()
            .addListener((observable, oldValue, newValue) -> {
                if (newValue.length() > maxNameLength) {
                    var txt = newValue.substring(0, maxNameLength) + "...";
                    nameLabel.setText(txt);
                }
            });

        var deleteParent = new HBox();

        deleteButton.setText("X");
        deleteButton.setStyle("-fx-text-fill: #b00020;");

        HBox.setHgrow(deleteParent, Priority.ALWAYS);
        deleteParent.setSpacing(16.0);
        deleteParent.setAlignment(Pos.CENTER_RIGHT);
        deleteParent.getChildren().add(deleteButton);

        Tooltip.install(view, tip);

        setDragAndDropItemSort();

        getStyleClass().add("cell");

        view.setPrefHeight(96.0);
        view.setAlignment(Pos.CENTER_LEFT);
        view.setSpacing(16.0);
        view.getChildren().addAll(imageParent, nameLabel, deleteParent);
    }

    private void updateItem(ImageItem item) {
        nameLabel.setText(item.filename());
        imageView.setImage(item.image());
        imageView.setSmooth(true);

        var boundingBox = imageView.getLayoutBounds();
        var clip = new Rectangle(
            boundingBox.getWidth(), boundingBox.getHeight()
        );

        clip.setArcWidth(8.0);
        clip.setArcHeight(8.0);
        imageView.setClip(clip);

        deleteButton.setOnAction(event -> onDeleteButtonAction(item));
        tip.setText(item.filename());

        setGraphic(view);
    }

    private void onDeleteButtonAction(ImageItem item) {
        var alert = new Alert(
            Alert.AlertType.CONFIRMATION,
            "Delete " + item.filename() + "?",
            ButtonType.YES,
            ButtonType.NO
        );
        alert.showAndWait();

        if(alert.getResult() == ButtonType.YES) {
            l.onDelete(item);
        }
    }

    private void setDragAndDropItemSort() {
        setOnDragDetected(this::onDragDetected);
        setOnDragOver(this::onDragOver);
        setOnDragEntered(this::onDragEntered);
        setOnDragExited(this::onDragExited);
        setOnDragDropped(this::onDragDropped);
        setOnDragDone(DragEvent::consume);
    }

    private void onDragDetected(MouseEvent event) {
        var item = getItem();

        if (item == null) {
            return;
        }

        var idx = getListView().getItems().indexOf(item);
        var dragboard = startDragAndDrop(TransferMode.MOVE);
        var content = new ClipboardContent();

        content.putString(String.valueOf(idx));
        dragboard.setDragView(view.snapshot(null, null));
        dragboard.setContent(content);

        event.consume();
    }

    private void onDragOver(DragEvent event) {
        if (event.getGestureSource() != this && event.getDragboard().hasString()) {
            event.acceptTransferModes(TransferMode.MOVE);
            event.consume();
        }
    }

    private void onDragEntered(DragEvent event) {
        if (event.getGestureSource() != this && event.getDragboard().hasString()) {
            getStyleClass().add("entered");
            event.consume();
        }
    }

    private void onDragExited(DragEvent event) {
        if (event.getGestureSource() != this && event.getDragboard().hasString()) {
            getStyleClass().remove("entered");
            event.consume();
        }
    }

    private void onDragDropped(DragEvent event) {
        var item = getItem();

        if (item == null) {
            return;
        }

        var dragboard = event.getDragboard();

        if (dragboard.hasString()) {
            var items = getListView().getItems();
            int draggedIdx = Integer.parseInt(dragboard.getString());
            int destIdx = items.indexOf(item);

            l.onArrange(draggedIdx, destIdx);

            event.setDropCompleted(true);
            event.consume();
        }
    }
}
