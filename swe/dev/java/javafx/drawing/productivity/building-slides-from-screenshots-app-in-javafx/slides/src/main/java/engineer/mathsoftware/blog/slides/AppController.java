// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides;

import engineer.mathsoftware.blog.slides.data.DataRepository;
import engineer.mathsoftware.blog.slides.data.LocalDataRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.DragEvent;

public class AppController {
    private static final String DATA_ROOT = "data";
    private final DataRepository repository;

    public AppController() {
        this.repository = new LocalDataRepository(DATA_ROOT);
    }

    @FXML
    public void initialize() {}

    @FXML
    private void onDragOver(DragEvent dragEvent) {}

    @FXML
    private void onDragDropped(DragEvent dragEvent) {}

    @FXML
    private void onDragExited(DragEvent dragEvent) {}

    @FXML
    private void onAddButtonAction(ActionEvent event) {}

    @FXML
    private void onClearButtonAction() {}
}
