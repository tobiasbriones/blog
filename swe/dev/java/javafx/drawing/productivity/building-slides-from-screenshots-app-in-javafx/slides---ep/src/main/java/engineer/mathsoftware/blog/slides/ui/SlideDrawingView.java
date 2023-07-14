// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.ui;

import engineer.mathsoftware.blog.slides.*;
import engineer.mathsoftware.blog.slides.data.ImageItem;
import engineer.mathsoftware.blog.slides.drawing.GroupSlideDrawing;
import engineer.mathsoftware.blog.slides.drawing.SlideDrawing;
import javafx.beans.InvalidationListener;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.util.Optional;

class SlideDrawingView {
    public record SlideState(
        SlideItem slideItem,
        ImageItem imageItem,
        Language language,
        Color background,
        String code,
        SlideSize size,
        boolean captionEnable,
        Optional<Slide.Caption> caption
    ) {}

    interface ChangeListener {
        void onSlideChange(SlideState state);

        void setState(ImageItem item);
    }

    private final SlideDrawingController controller;
    private final SlideDrawing drawing;
    private final ObjectProperty<SlideItem> slideProperty;
    private final ObjectProperty<ImageItem> imageProperty;
    private final ObjectProperty<Language> languageProperty;
    private final ObjectProperty<Color> backgroundProperty;
    private final ObjectProperty<String> codeProperty;
    private final ObjectProperty<SlideSize> sizeProperty;
    private final BooleanProperty captionEnableProperty;
    private final StringProperty captionTitleProperty;
    private final StringProperty captionSubTitleProperty;
    private ChangeListener l;
    private boolean isStateLoading;

    SlideDrawingView(HBox view) {
        controller = new SlideDrawingController();
        drawing = new GroupSlideDrawing(view);
        slideProperty = new SimpleObjectProperty<>();
        imageProperty = new SimpleObjectProperty<>();
        languageProperty = new SimpleObjectProperty<>();
        backgroundProperty = new SimpleObjectProperty<>();
        codeProperty = new SimpleObjectProperty<>();
        sizeProperty = new SimpleObjectProperty<>();
        captionEnableProperty = new SimpleBooleanProperty();
        captionTitleProperty = new SimpleStringProperty();
        captionSubTitleProperty = new SimpleStringProperty();
        l = null;
        isStateLoading = false;
    }

    void setStatus(BackgroundStatus newStatus) {
        controller.setStatus(newStatus);
    }

    void setOnChangeListener(ChangeListener newListener) {
        l = newListener;
    }

    ObjectProperty<SlideItem> slideProperty() {
        return slideProperty;
    }

    ObjectProperty<ImageItem> imageProperty() {
        return imageProperty;
    }

    ObjectProperty<Language> languageProperty() {
        return languageProperty;
    }

    ObjectProperty<Color> backgroundProperty() {
        return backgroundProperty;
    }

    ObjectProperty<String> codeProperty() {
        return codeProperty;
    }

    ObjectProperty<SlideSize> sizeProperty() {
        return sizeProperty;
    }

    BooleanProperty captionEnableProperty() {
        return captionEnableProperty;
    }

    StringProperty captionTitleProperty() {
        return captionTitleProperty;
    }

    StringProperty captionSubTitleProperty() {
        return captionSubTitleProperty;
    }

    BooleanProperty autoSaveProperty() {
        return controller.autoSaveProperty();
    }

    void init() {
        InvalidationListener updateAll = ignore -> updateSlide();

        slideProperty.addListener(updateAll);
        languageProperty.addListener(updateAll);
        backgroundProperty.addListener(updateAll);
        codeProperty.addListener(updateAll);
        sizeProperty.addListener(updateAll);
        captionEnableProperty.addListener(updateAll);
        captionTitleProperty.addListener(updateAll);
        captionSubTitleProperty.addListener(updateAll);
        imageProperty.addListener(this::onImageChange);
        updateSlide();
    }

    void setViews(
        ScrollPane scrollPane,
        ComboBox<ShapeItem> shapeComboBox,
        ComboBox<Palette> shapePaletteComboBox,
        Button shapeBackButton
    ) {
        controller.setScrollPane(scrollPane);
        controller.setShapeComboBox(shapeComboBox);
        controller.setShapePaletteComboBox(shapePaletteComboBox);
        controller.setShapeBackButton(shapeBackButton);
    }

    void saveCurrentSlide() {
        controller.saveCurrentSlide();
    }

    private void onImageChange(
        ObservableValue<? extends ImageItem> observable,
        ImageItem oldValue,
        ImageItem newValue
    ) {
        if (l == null) {
            return;
        }
        // Avoids rendering while all properties are being set
        isStateLoading = true;
        l.setState(newValue);
        isStateLoading = false;

        controller.onSlideChanged(newValue);

        // Update manually
        updateSlide();
    }

    private void updateSlide() {
        if (isStateLoading) {
            return;
        }
        if (imageProperty.isNull().get()) {
            return;
        }

        var item = slideProperty.get();
        var slide = switch (item) {
            case CodeSnippet -> new Slide.CodeSnippet(
                codeProperty.get(),
                languageProperty.get(),
                getCaption()
            );
            case CodeShot -> new Slide.CodeShot(
                imageProperty.get().image(),
                languageProperty.get(),
                getCaption()
            );
            case Screenshot -> new Slide.Screenshot(
                imageProperty.get().image(),
                backgroundProperty.get(),
                getCaption()
            );
        };

        drawing.setup(sizeProperty.get());
        var group = drawing.draw(slide);

        if (l != null) {
            l.onSlideChange(new SlideState(
                slideProperty.get(),
                imageProperty.get(),
                languageProperty.get(),
                backgroundProperty.get(),
                codeProperty.get(),
                sizeProperty.get(),
                captionEnableProperty.get(),
                getCaption()
            ));
        }

        controller.setDrawing(group);
        controller.onDrawingChanged(imageProperty.get());
    }

    private Optional<Slide.Caption> getCaption() {
        return captionEnableProperty.get()
            ? Optional
            .of(new Slide.Caption(
                captionTitleProperty.get(),
                captionSubTitleProperty.get()
            ))
            : Optional.empty();
    }
}
