// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.ui;

import engineer.mathsoftware.blog.slides.Enums;
import engineer.mathsoftware.blog.slides.Language;
import engineer.mathsoftware.blog.slides.SlideItem;
import engineer.mathsoftware.blog.slides.data.Data;
import engineer.mathsoftware.blog.slides.data.DataRepository;
import engineer.mathsoftware.blog.slides.data.ImageItem;
import engineer.mathsoftware.blog.slides.data.LocalDataRepository;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
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
    @FXML
    private Pagination pagination;
    @FXML
    private Group slideGroup;
    @FXML
    private ImageView slideView;
    @FXML
    private VBox codeSnippetBox;
    @FXML
    private ComboBox<SlideItem> slideComboBox;
    @FXML
    private ComboBox<Language> languageComboBox;
    @FXML
    private VBox viewPaneBox;

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

        initMasterView();
        initDetail();
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
        showDeleteAllAlert();
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
    private void onClearMenuItemAction() {
        showDeleteAllAlert();
    }

    @FXML
    private void onQuitMenuItemAction() {
        Platform.exit();
    }

    @FXML
    private void onAboutMenuItemAction() {
        var alert = new Alert(
            Alert.AlertType.INFORMATION,
            "Slides App | blog | mathsoftware.engineer",
            ButtonType.CLOSE
        );

        alert.showAndWait();
    }

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
        imageList.getSelectionModel().clearAndSelect(destIdx);
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

    private void initMasterView() {
        updatePaginationCount();

        images.addListener((InvalidationListener) l -> updatePaginationCount());

        imageList
            .getSelectionModel()
            .selectedIndexProperty()
            .map(Integer.class::cast)
            .addListener((observable, oldValue, newValue) -> pagination.setCurrentPageIndex(newValue));

        imageList.getSelectionModel().select(0);

        pagination
            .currentPageIndexProperty()
            .map(Integer.class::cast)
            .addListener((observable, oldValue, newValue) -> imageList.getSelectionModel().select(newValue));

        slideView
            .imageProperty()
            .bind(imageList
                .getSelectionModel()
                .selectedItemProperty()
                .map(ImageItem::image)
            );
        slideView
            .fitWidthProperty()
            .bind(viewPaneBox
                .widthProperty()
                .subtract(32)
            );
    }

    private void initDetail() {
        var slideProperty = slideComboBox.valueProperty();

        slideComboBox.getItems().addAll(SlideItem.values());
        slideComboBox.setConverter(new Enums.EnglishConverter<>(SlideItem.class));
        slideComboBox.setValue(SlideItem.CodeSnippet);
        slideComboBox.setOnAction(event -> updateSlide());

        languageComboBox.getItems().addAll(Language.values());
        languageComboBox.setConverter(new Enums.EnglishConverter<>(Language.class));
        languageComboBox.setValue(Language.Java);
        languageComboBox.setOnAction(event -> updateSlide());
        languageComboBox
            .visibleProperty()
                .bind(slideProperty
                    .isEqualTo(SlideItem.CodeSnippet)
                    .or(slideProperty.isEqualTo(SlideItem.CodeShot))
                );

        codeSnippetBox
            .visibleProperty()
            .bind(slideProperty
                .isEqualTo(SlideItem.CodeSnippet)
            );
    }

    private void updateSlide() {
        // TODO
    }

    private void showDeleteAllAlert() {
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

    private void updatePaginationCount() {
        var size = images.size();

        if (size > 0) {
            pagination.setVisible(true);
            pagination.setPageCount(images.size());
        }
        else {
            pagination.setVisible(false);
            pagination.setPageCount(1);
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
