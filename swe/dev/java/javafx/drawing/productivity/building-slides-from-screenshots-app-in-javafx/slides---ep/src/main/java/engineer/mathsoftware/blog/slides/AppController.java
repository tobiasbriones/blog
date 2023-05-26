// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides;

import engineer.mathsoftware.blog.slides.data.Data;
import engineer.mathsoftware.blog.slides.data.DataRepository;
import engineer.mathsoftware.blog.slides.data.LocalDataRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class AppController implements ImageItemCell.Listener {
    private static final String DATA_ROOT = "data";
    private final DataRepository repository;
    private final ObservableList<ImageItem> images;
    @FXML
    private Parent view;
    @FXML
    private Button addButton;
    @FXML
    private Label statusLabel;
    @FXML
    private ListView<ImageItem> imageList;

    public AppController() {
        this.repository = new LocalDataRepository(DATA_ROOT);
        this.images = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        setStatus("Slides App");
        imageList.setCellFactory(param -> new ImageItemCell(this));
        imageList.setItems(images);

        initAddButton();
        loadImageList();
    }

    @FXML
    private void onDragOver(DragEvent dragEvent) {
        var dragboard = dragEvent.getDragboard();

        if (dragboard.hasFiles()) {
            if (Data.areValidImageFiles(dragboard.getFiles())) {
                setStatus("Dragging files...");
                dragEvent.acceptTransferModes(TransferMode.COPY);
            }
            else {
                setStatus("Drag canceled (invalid files)");
            }
            dragEvent.consume();
        }
    }

    @FXML
    private void onDragExited(DragEvent dragEvent) {
        if (!dragEvent.isDropCompleted()) {
            setStatus("Drag canceled");
        }
        dragEvent.consume();
    }

    @FXML
    private void onDragDropped(DragEvent dragEvent) {
        var board = dragEvent.getDragboard();

        if (board.hasFiles()) {
            createOrUpdateImages(board.getFiles());
            dragEvent.setDropCompleted(true);
            dragEvent.consume();
        }
        else {
            setStatus("Drag canceled (empty)");
        }
    }

    @FXML
    private void onAddButtonAction() {
        openFileViaFileChooser();
    }

    @FXML
    private void onClearButtonAction() {
        var alert = new Alert(
            Alert.AlertType.CONFIRMATION,
            "Delete all the data?",
            ButtonType.YES,
            ButtonType.NO
        );
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            deleteAllImages();
        }
    }

    @FXML
    private void onAddMenuItemAction() {
        openFileViaFileChooser();
    }

    @FXML
    private void onOpenWDMenuItemAction() {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(new File(DATA_ROOT));
            }
            catch (IOException | UnsupportedOperationException e) {
                setStatus(e.getMessage());
            }
        }
        else {
            setStatus("Desktop is not supported");
        }
    }

    @FXML
    private void onClearMenuItemAction() {}

    @FXML
    private void onQuitMenuItemAction() {}

    @Override
    public void onDelete(ImageItem item) {
        try {
            repository.deleteImage(item.filename());
            imageList.getItems().remove(item);
            setStatus("Item deleted");
        }
        catch (IOException e) {
            handleError(e);
        }
    }

    @Override
    public void onArrange(int draggedIdx, int destIdx) {
        var dragged = images.get(draggedIdx);
        var dest = images.get(destIdx);

        images.set(draggedIdx, dest);
        images.set(destIdx, dragged);
        setStatus("Item arranged");
    }

    private void loadImageList() {
        try {
            var loadedImages = repository.readAllImages();

            images.clear();
            images.addAll(loadedImages);
        }
        catch (IOException e) {
            handleError(e);
        }
    }

    private void openFileViaFileChooser() {
        var chooser = new FileChooser();

        chooser.setTitle("Open Files");
        chooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter(
                "Image Files (*.png, *.jpg)",
                "*.png",
                "*.jpg"
            )
        );
        var files = chooser.showOpenMultipleDialog(view.getScene().getWindow());

        if (files != null) {
            createOrUpdateImages(files);
        }
    }

    private void createOrUpdateImages(Iterable<? extends File> files) {
        for (var file : files) {
            var path = file.toPath();

            try {
                repository.createOrUpdateImage(path);

                var imageName = path.getFileName().toString();
                var newImage = repository.readImage(imageName);
                var newImageItem = new ImageItem(imageName, newImage);
                var listItems = imageList.getItems();

                listItems.remove(newImageItem);
                listItems.add(new ImageItem(imageName, newImage));
                setStatus("Files updated");
            }
            catch (IOException e) {
                handleError(e);
            }
        }
    }

    private void initAddButton() {
        var icAdd = new Image(
            Objects.requireNonNull(
                getClass().getResourceAsStream("/ic_add.png")
            )
        );
        var addImageView = new ImageView(icAdd);

        addImageView.setFitWidth(18.0);
        addImageView.setFitHeight(18.0);
        addButton.setGraphic(addImageView);
    }

    private void deleteAllImages() {
        try {
            repository.deleteAllImages();
            imageList.getItems().clear();
            setStatus("All items deleted");
        }
        catch (IOException e) {
            handleError(e);
        }
    }

    private void handleError(IOException e) {
        setStatus(e.getMessage());
        e.printStackTrace();
    }

    private void setStatus(String msg) {
        statusLabel.setText(msg);
    }
}
