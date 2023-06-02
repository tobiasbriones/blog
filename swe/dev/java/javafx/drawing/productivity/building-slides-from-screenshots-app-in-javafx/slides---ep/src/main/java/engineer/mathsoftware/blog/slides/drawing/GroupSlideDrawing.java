// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.drawing;

import engineer.mathsoftware.blog.slides.Slide;
import engineer.mathsoftware.blog.slides.SlideSize;
import javafx.scene.Group;
import javafx.scene.layout.HBox;

public class GroupSlideDrawing implements SlideDrawing {
    private final HBox view;
    private SlideSize size;

    public GroupSlideDrawing(HBox view) {
        this.view = view;
        this.size = SlideSize.Predefined.HD.value();
    }

    @Override
    public void setup(SlideSize slideSize) {
        if (slideSize == null) {
            return;
        }
        size = slideSize;
    }

    @Override
    public void draw(Slide slide) {
        var drawing = switch (slide) {
            case Slide.CodeShot codeShot -> new Group();
            case Slide.CodeSnippet codeSnippet -> new Group();
            case Slide.Screenshot screenshot -> drawScreenshot(screenshot);
        };

        view.getChildren().clear();
        view.getChildren().add(drawing);
    }

    private Group drawCodeShot(Slide.CodeShot codeShot) {
        // TODO
        return new Group();
    }

    private Group drawCodeSnippet(Slide.CodeSnippet codeSnippet) {
        // TODO
        return new Group();
    }

    private Group drawScreenshot(Slide.Screenshot screenshot) {
        // TODO
        return new Group();
    }
}
