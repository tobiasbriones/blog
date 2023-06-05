// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.drawing;

import engineer.mathsoftware.blog.slides.Colors;
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
            case Slide.CodeShot codeShot -> drawCodeShot(codeShot);
            case Slide.CodeSnippet codeSnippet -> drawCodeSnippet(codeSnippet);
            case Slide.Screenshot screenshot -> drawScreenshot(screenshot);
        };

        view.getChildren().clear();
        view.getChildren().add(drawing);
    }

    private Group drawCodeShot(Slide.CodeShot codeShot) {
        var group = new Group();
        var screenshotView = new ImageView();
        var image = codeShot.image();
        var lang = codeShot.language();
        var langColor = Colors.color(lang);

        // Scale so it fits the ScrollPane better
        group.setScaleX(0.5);
        group.setScaleY(0.5);

        screenshotView.setSmooth(true);
        screenshotView.setPreserveRatio(true);
        fitImageView(screenshotView, image);
        drawImage(image, screenshotView);
        centerImageView(screenshotView);

        clear(group, langColor);
        group.getChildren().add(screenshotView);
        return group;
    }

    private Group drawCodeSnippet(Slide.CodeSnippet codeSnippet) {
        // TODO
        return new Group();
    }

    private Group drawScreenshot(Slide.Screenshot screenshot) {
        var group = new Group();
        var screenshotView = new ImageView();
        var image = screenshot.image();

        // Scale so it fits the ScrollPane better
        group.setScaleX(0.5);
        group.setScaleY(0.5);

        screenshotView.setSmooth(true);
        screenshotView.setPreserveRatio(true);
        fitImageView(screenshotView, image);
        drawImage(image, screenshotView);
        centerImageView(screenshotView);

        clear(group, Color.WHITE);
        group.getChildren().add(screenshotView);
        return group;
    }

    private void clear(Group group, Color color) {
        var background = new Rectangle();

        background.setWidth(size.width());
        background.setHeight(size.height());
        clearRect(group, color, background);
    }

    private void centerImageView(ImageView iv) {
        var imageLeft = (size.width() - iv.getFitWidth()) / 2.0;
        var imageTop = (size.height() - iv.getFitHeight()) / 2.0;
        iv.setX(imageLeft);
        iv.setY(imageTop);
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

    private void fitImageView(ImageView iv, Image originalImage) {
        var w = originalImage.getWidth();
        var h = originalImage.getHeight();

        iv.setFitWidth(w);
        iv.setFitHeight(h);

        // If the original image is bigger (out of bound) resize the ImageView
        if (size.width() < w || size.height() < h) {
            if (size.width() < w) {
                var hRatio = iv.getFitHeight() / iv.getFitWidth();

                iv.setFitWidth(size.width());
                iv.setFitHeight(size.width() * hRatio);
            }
            if (size.height() < h) {
                var wRatio = iv.getFitWidth() / iv.getFitHeight();

                iv.setFitWidth(size.height() * wRatio);
                iv.setFitHeight(size.height());
            }
        }
    }

    private static void clearRect(
        Group group,
        Color color,
        Rectangle background
    ) {
        background.setFill(color);
        group.getChildren().add(background);
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

        clip.setWidth(iv.getFitWidth());
        clip.setHeight(iv.getFitHeight());
        clip.setArcWidth(arc);
        clip.setArcHeight(arc);

        iv.setImage(image);
        iv.setClip(clip);

        var roundedImage = iv.snapshot(params, null);

        iv.setClip(null);
        return roundedImage;
    }
}
