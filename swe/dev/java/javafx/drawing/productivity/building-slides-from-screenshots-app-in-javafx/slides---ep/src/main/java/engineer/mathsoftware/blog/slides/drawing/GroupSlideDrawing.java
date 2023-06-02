// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.drawing;

import engineer.mathsoftware.blog.slides.Slide;
import engineer.mathsoftware.blog.slides.SlideSize;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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
        var group = new Group();
        var backgroundView = new Rectangle();
        var screenshotView = new ImageView();
        var width = size.width();
        var height = size.height();
        var image = screenshot.image();
        var imageLeft = (width - image.getWidth()) / 2.0;
        var imageRight = (height - image.getHeight()) / 2.0;

        backgroundView.setWidth(size.width());
        backgroundView.setHeight(size.height());
        backgroundView.setFill(Color.WHITE);

        // Scale so it fits the ScrollPane better
        group.setScaleX(0.5);
        group.setScaleY(0.5);

        drawImage(image, screenshotView);

        screenshotView.setSmooth(true);
        screenshotView.setX(imageLeft);
        screenshotView.setY(imageRight);

        group.getChildren().addAll(backgroundView, screenshotView);
        return group;
    }

    private static void drawImage(Image image, ImageView iv) {
        var shadow = new DropShadow();
        var arc = getImageCornerRadius(image);

        var roundedImage = getRoundedImage(image, iv, arc);

        shadow.setColor(Color.web("#212121"));
        shadow.setRadius(48.0);

        iv.setEffect(shadow);
        iv.setImage(roundedImage);
    }

    private static double getImageCornerRadius(Image image) {
        var minDim = Math.min(image.getWidth(), image.getHeight());
        var unitDim = 48.0;
        var unitArc = 16.0 / 4.0;
        var proportion = minDim / unitDim;
        return proportion >= 1.0 ? proportion * unitArc : unitArc * 4.0;
    }

    private static Image getRoundedImage(Image image, ImageView iv, double arc) {
        var clip = new Rectangle();
        var params = new SnapshotParameters();

        params.setFill(Color.TRANSPARENT);

        clip.setWidth(image.getWidth());
        clip.setHeight(image.getHeight());
        clip.setArcWidth(arc);
        clip.setArcHeight(arc);

        iv.setImage(image);
        iv.setClip(clip);

        var roundedImage = iv.snapshot(params, null);

        iv.setClip(null);
        return roundedImage;
    }
}
