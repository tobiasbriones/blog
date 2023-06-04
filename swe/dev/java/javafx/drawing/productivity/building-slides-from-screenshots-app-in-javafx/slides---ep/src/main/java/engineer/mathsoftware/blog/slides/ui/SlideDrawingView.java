// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.ui;

import engineer.mathsoftware.blog.slides.Language;
import engineer.mathsoftware.blog.slides.Slide;
import engineer.mathsoftware.blog.slides.SlideItem;
import engineer.mathsoftware.blog.slides.SlideSize;
import engineer.mathsoftware.blog.slides.data.ImageItem;
import engineer.mathsoftware.blog.slides.drawing.GroupSlideDrawing;
import engineer.mathsoftware.blog.slides.drawing.SlideDrawing;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.HBox;

class SlideDrawingView {
    public record SlideState(
        SlideItem slideItem,
        ImageItem imageItem,
        Language language,
        String code,
        SlideSize size
    ) {}

    interface ChangeListener {
        void onSlideChange(SlideState state);

        void setState(ImageItem item);
    }

    private final SlideDrawing drawing;
    private final ObjectProperty<SlideItem> slideProperty;
    private final ObjectProperty<ImageItem> imageProperty;
    private final ObjectProperty<Language> languageProperty;
    private final ObjectProperty<String> codeProperty;
    private final ObjectProperty<SlideSize> sizeProperty;
    private ChangeListener l;
    private boolean isStateLoading;

    SlideDrawingView(HBox view) {
        this.drawing = new GroupSlideDrawing(view);
        this.slideProperty = new SimpleObjectProperty<>();
        this.imageProperty = new SimpleObjectProperty<>();
        this.languageProperty = new SimpleObjectProperty<>();
        this.codeProperty = new SimpleObjectProperty<>();
        this.sizeProperty = new SimpleObjectProperty<>();
        this.l = null;
        this.isStateLoading = false;
    }

    void setOnChangeListener(ChangeListener value) {
        l = value;
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

    ObjectProperty<String> codeProperty() {
        return codeProperty;
    }

    ObjectProperty<SlideSize> sizeProperty() {
        return sizeProperty;
    }

    void init() {
        InvalidationListener updateAll = ignore -> updateSlide();

        slideProperty.addListener(updateAll);
        languageProperty.addListener(updateAll);
        codeProperty.addListener(updateAll);
        sizeProperty.addListener(updateAll);
        imageProperty.addListener(this::onImageChange);
    }

    private void onImageChange(
        ObservableValue<? extends ImageItem> observable,
        ImageItem oldValue,
        ImageItem newValue
    ) {
        isStateLoading = true;

        // Avoids rendering while all properties are being set
        if (l != null) {
            l.setState(newValue);
        }
        isStateLoading = false;

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
                languageProperty.get()
            );
            case CodeShot -> new Slide.CodeShot(
                imageProperty.get().image(),
                languageProperty.get()
            );
            case Screenshot ->
                new Slide.Screenshot(imageProperty.get().image());
        };

        drawing.setup(sizeProperty.get());
        drawing.draw(slide);

        if (l != null) {
            l.onSlideChange(new SlideState(
                slideProperty.get(),
                imageProperty.get(),
                languageProperty.get(),
                codeProperty.get(),
                sizeProperty.get()
            ));
        }
    }
}
