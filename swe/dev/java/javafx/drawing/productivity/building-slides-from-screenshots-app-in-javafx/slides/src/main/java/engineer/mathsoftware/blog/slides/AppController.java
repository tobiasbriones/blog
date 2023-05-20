// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides;

import engineer.mathsoftware.blog.slides.data.Data;
import engineer.mathsoftware.blog.slides.data.DataRepository;
import engineer.mathsoftware.blog.slides.data.LocalDataRepository;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class AppController implements ImageItemCell.Listener {
    private static final String DATA_ROOT = "data";
    private final DataRepository repository;
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
    }

    @FXML
    public void initialize() {
        statusLabel.setText("Slides App");
        imageList.setCellFactory(param -> new ImageItemCell(this));

        initAddButton();
        loadImageList();
    }

    @FXML
    private void onDragOver(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasFiles()
            && Data.areValidImageFiles(dragEvent.getDragboard().getFiles())
        ) {
            statusLabel.setText("Dragging files...");
            dragEvent.acceptTransferModes(TransferMode.COPY);
        }
        else {
            statusLabel.setText("Drag canceled (invalid files)");
            dragEvent.consume();
        }
    }

    @FXML
    private void onDragDropped(DragEvent dragEvent) {
        var board = dragEvent.getDragboard();

        if (board.hasFiles()) {
            createOrUpdateImages(board.getFiles());
            statusLabel.setText("Files updated");
            dragEvent.setDropCompleted(true);
        }
        else {
            statusLabel.setText("Drag canceled (empty)");
            dragEvent.consume();
        }
    }

    @FXML
    private void onDragExited(DragEvent dragEvent) {
        if (!dragEvent.isDropCompleted()) {
            statusLabel.setText("Drag canceled");
        }
    }

    @FXML
    private void onAddButtonAction(ActionEvent event) {}

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

    @Override
    public void onDelete(ImageItem item) {
        try {
            repository.deleteImage(item.filename());
            imageList.getItems().remove(item);
        }
        catch (IOException e) {
            handleError(e);
        }
    }

    private void loadImageList() {
        try {
            var images = repository.readAllImages();
            imageList.setItems(FXCollections.observableList(images));
        }
        catch (IOException e) {
            handleError(e);
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
        }
        catch (IOException e) {
            handleError(e);
        }
    }

    private void handleError(IOException e) {
        statusLabel.setText(e.getMessage());
        e.printStackTrace();
    }
}
