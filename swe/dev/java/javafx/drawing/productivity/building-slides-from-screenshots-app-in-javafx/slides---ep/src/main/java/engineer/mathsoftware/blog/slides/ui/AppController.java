// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.ui;

import engineer.mathsoftware.blog.slides.*;
import engineer.mathsoftware.blog.slides.data.*;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AppController implements
    ImageItemCell.Listener,
    SlideDrawingView.ChangeListener {

    private final DataRepository repository;
    private final ObservableList<ImageItem> images;
    private final Map<ImageItem, SlideDrawingView.SlideState> changes;
    private SlideDrawingView slideDrawingView;
    @FXML
    private Parent view;
    @FXML
    private Button addButton;
    @FXML
    private Button newButton;
    @FXML
    private Label statusLabel;
    @FXML
    private Label secondaryStatusLabel;
    @FXML
    private ListView<ImageItem> imageList;
    @FXML
    private Pagination pagination;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private HBox slideBox;
    @FXML
    private VBox screenshotBox;
    @FXML
    private VBox codeSnippetBox;
    @FXML
    private CheckMenuItem autoSaveCheckMenuItem;
    @FXML
    private ComboBox<SlideItem> slideComboBox;
    @FXML
    private ComboBox<Language> languageComboBox;
    @FXML
    private ComboBox<SlideSize.Predefined> sizeComboBox;
    @FXML
    private TextField saveField;
    @FXML
    private ColorPicker backgroundPicker;
    @FXML
    private TextArea codeTextArea;
    @FXML
    private CheckBox captionCheckBox;
    @FXML
    private VBox captionBox;
    @FXML
    private TextArea captionTitleArea;
    @FXML
    private TextArea captionSubTitleArea;
    @FXML
    private ComboBox<ShapeItem> shapeComboBox;
    @FXML
    private ComboBox<Palette> shapePaletteComboBox;
    @FXML
    private Button shapeBackButton;

    public AppController() {
        this.repository = new LocalDataRepository(Env.DATA_ROOT);
        this.images = FXCollections.observableArrayList();
        this.changes = new HashMap<>();
    }

    @FXML
    public void initialize() {
        setStatus("Slides App");
        imageList.setCellFactory(param -> new ImageItemCell(this));
        imageList.setItems(images);

        initAddButton();
        initNewButton();
        loadImageList();

        initMasterView();
        initDetail();
        initSlideDrawingView();
        initMenu();
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
    private void onNewButtonAction() {
        var dialog = new TextInputDialog();

        dialog.setTitle("Slide Name");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter the slide name:");

        dialog
            .showAndWait()
            .filter(x -> !x.isBlank())
            .ifPresent(this::createNewSlide);
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
    private void onSaveCurrentSlideItemAction() {
        slideDrawingView.saveCurrentSlide();
    }

    @FXML
    private void onOpenWDMenuItemAction() {
        openInExplorer(Env.DATA_ROOT);
    }

    @FXML
    private void onOpenPDMenuItemAction() {
        openInExplorer(Env.SAVE_DIR);
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

    @Override
    public void onSlideChange(SlideDrawingView.SlideState state) {
        changes.put(state.imageItem(), state);
    }

    @Override
    public void setState(ImageItem item) {
        if (!changes.containsKey(item)) {
            slideComboBox.setValue(SlideItem.Screenshot);
            languageComboBox.setValue(Language.Java);
            backgroundPicker.setValue(Color.WHITE);
            codeTextArea.setText("");
            captionCheckBox.setSelected(false);
            captionTitleArea.setText("");
            captionSubTitleArea.setText("");
            sizeComboBox.setValue(SlideSize.Predefined.HD);
            return;
        }
        var state = changes.get(item);

        slideComboBox.setValue(state.slideItem());
        languageComboBox.setValue(state.language());
        backgroundPicker.setValue(state.background());
        codeTextArea.setText(state.code());
        captionCheckBox.setSelected(state.captionEnable());
        state.caption().ifPresent(caption -> {
            captionTitleArea.setText(caption.title());
            captionSubTitleArea.setText(caption.subtitle());
        });
        sizeComboBox.setValue(SlideSize.Predefined.from(state.size()));
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

    private void createNewSlide(String name) {
        var newImage = new Image(Objects.requireNonNull(
            getClass().getClassLoader().getResourceAsStream("app-512x512.png")
        ));
        var newItem = new ImageItem(name + ".png", newImage);

        try {
            repository.createOrUpdateImage(newItem);
            images.remove(newItem);
            images.add(newItem);
            setStatus("New slide created");
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

    private void initNewButton() {
        var icNew = new Image(
            Objects.requireNonNull(
                getClass().getResourceAsStream("/ic_new.png")
            )
        );
        var newImageView = new ImageView(icNew);

        newImageView.setFitWidth(18.0);
        newImageView.setFitHeight(18.0);
        newButton.setGraphic(newImageView);
    }

    private void initMenu() {
        slideDrawingView
            .autoSaveProperty()
            .bind(autoSaveCheckMenuItem
                .selectedProperty()
            );
    }

    private void initMasterView() {
        updatePaginationCount();

        images.addListener((InvalidationListener) l -> updatePaginationCount());

        imageList
            .getSelectionModel()
            .selectedIndexProperty()
            .map(Integer.class::cast)
            .addListener((observable, oldValue, newValue) -> pagination
                .setCurrentPageIndex(newValue)
            );

        imageList
            .getSelectionModel()
            .select(0);

        pagination
            .currentPageIndexProperty()
            .map(Integer.class::cast)
            .addListener((observable, oldValue, newValue) -> imageList
                .getSelectionModel()
                .select(newValue)
            );
    }

    private void initDetail() {
        var slideProperty = slideComboBox.valueProperty();

        slideComboBox.getItems().addAll(SlideItem.values());
        slideComboBox.setConverter(new Enums.EnglishConverter<>(SlideItem.class));
        slideComboBox.setValue(SlideItem.Screenshot);

        languageComboBox.getItems().addAll(Language.values());
        languageComboBox.setValue(Language.Java);
        languageComboBox
            .visibleProperty()
            .bind(slideProperty
                .isEqualTo(SlideItem.CodeSnippet)
                .or(slideProperty.isEqualTo(SlideItem.CodeShot))
            );
        languageComboBox
            .managedProperty()
            .bind(languageComboBox
                .visibleProperty()
            );

        sizeComboBox
            .getItems()
            .addAll(SlideSize
                .Predefined.values()
            );
        sizeComboBox
            .getSelectionModel()
            .select(0);

        saveField
            .setText(Path
                .of(Env.DATA_ROOT, "out")
                .toAbsolutePath()
                .toString()
            );

        screenshotBox
            .visibleProperty()
            .bind(slideProperty
                .isEqualTo(SlideItem.Screenshot)
            );
        screenshotBox
            .managedProperty()
            .bind(screenshotBox
                .visibleProperty()
            );

        codeSnippetBox
            .visibleProperty()
            .bind(slideProperty
                .isEqualTo(SlideItem.CodeSnippet)
            );
        codeSnippetBox
            .managedProperty()
            .bind(codeSnippetBox
                .visibleProperty()
            );

        captionBox
            .visibleProperty()
            .bind(captionCheckBox
                .selectedProperty()
            );
        captionBox
            .managedProperty()
            .bind(captionBox
                .visibleProperty()
            );
    }

    private void initSlideDrawingView() {
        slideDrawingView = new SlideDrawingView(slideBox);

        slideDrawingView
            .slideProperty()
            .bind(slideComboBox
                .valueProperty()
            );
        slideDrawingView
            .imageProperty()
            .bind(imageList
                .getSelectionModel()
                .selectedItemProperty()
            );
        slideDrawingView
            .languageProperty()
            .bind(languageComboBox
                .valueProperty()
            );
        slideDrawingView
            .backgroundProperty()
            .bind(backgroundPicker
                .valueProperty()
            );
        slideDrawingView
            .codeProperty()
            .bind(codeTextArea
                .textProperty()
            );
        slideDrawingView
            .sizeProperty()
            .bind(sizeComboBox
                .valueProperty()
                .map(SlideSize.Predefined::value)
            );
        slideDrawingView
            .captionEnableProperty()
            .bind(captionCheckBox
                .selectedProperty()
            );
        slideDrawingView
            .captionTitleProperty()
            .bind(captionTitleArea
                .textProperty()
            );
        slideDrawingView
            .captionSubTitleProperty()
            .bind(captionSubTitleArea
                .textProperty()
            );

        slideDrawingView
            .setViews(
                scrollPane,
                shapeComboBox,
                shapePaletteComboBox,
                shapeBackButton
            );
        slideDrawingView.setOnChangeListener(this);
        slideDrawingView.setStatus(this::setSecondaryStatus);
        slideDrawingView.init();
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

    private void setSecondaryStatus(String msg) {
        secondaryStatusLabel.setText(msg);
    }

    private void openInExplorer(String dir) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(new File(dir));
            }
            catch (IOException | UnsupportedOperationException e) {
                setStatus(e.getMessage());
            }
        }
        else {
            setStatus("Desktop is not supported");
        }
    }
}
