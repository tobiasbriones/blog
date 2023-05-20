// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides;

import engineer.mathsoftware.blog.slides.data.Data;
import engineer.mathsoftware.blog.slides.data.DataRepository;
import engineer.mathsoftware.blog.slides.data.LocalDataRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;

import java.io.File;
import java.util.Objects;

public class AppController {
    private static final String DATA_ROOT = "data";
    private final DataRepository repository;
    @FXML
    private Button addButton;
    @FXML
    private Label statusLabel;

    public AppController() {
        this.repository = new LocalDataRepository(DATA_ROOT);
    }

    @FXML
    public void initialize() {
        statusLabel.setText("Slides App");

        initAddButton();
    }

    @FXML
    private void onDragOver(DragEvent dragEvent) {
        if (
            dragEvent.getDragboard()
                     .hasFiles()
                && Data.areValidImageFiles(dragEvent.getDragboard()
                                                    .getFiles())
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
    private void onClearButtonAction() {}

    private void createOrUpdateImages(Iterable<? extends File> files) {
        // TODO
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
}
