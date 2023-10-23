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
    public Group draw(Slide slide) {
        var drawing = switch (slide) {
            case Slide.CodeShot codeShot -> drawCodeShot(codeShot);
            case Slide.CodeSnippet codeSnippet -> drawCodeSnippet(codeSnippet);
            case Slide.Screenshot screenshot -> drawScreenshot(screenshot);
        };

        view.getChildren().clear();
        view.getChildren().add(drawing);
        return drawing;
    }

    private Group drawCodeShot(Slide.CodeShot codeShot) {
        var drawing = new ScreenshotDrawing(size);
        var group = drawing.draw(codeShot);

        // Scale so it fits the ScrollPane better
        group.setScaleX(0.5);
        group.setScaleY(0.5);
        return group;
    }

    private Group drawCodeSnippet(Slide.CodeSnippet codeSnippet) {
        var drawing = new CodeSnippetDrawing(size);
        var group = drawing.draw(codeSnippet);

        // Scale so it fits the ScrollPane better
        group.setScaleX(0.5);
        group.setScaleY(0.5);
        return group;
    }

    private Group drawScreenshot(Slide.Screenshot screenshot) {
        var drawing = new ScreenshotDrawing(size);
        var group = drawing.draw(screenshot);

        // Scale so it fits the ScrollPane better
        group.setScaleX(0.5);
        group.setScaleY(0.5);
        return group;
    }
}
