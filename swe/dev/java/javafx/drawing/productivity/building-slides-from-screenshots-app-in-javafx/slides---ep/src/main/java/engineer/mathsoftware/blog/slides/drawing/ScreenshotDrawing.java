// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.drawing;

import engineer.mathsoftware.blog.slides.Colors;
import engineer.mathsoftware.blog.slides.Slide;
import engineer.mathsoftware.blog.slides.SlideSize;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static engineer.mathsoftware.blog.slides.drawing.Drawings.*;

class ScreenshotDrawing {
    private final SlideSize size;
    private final Group group;
    private final Rectangle background;
    private final CaptionRenderer captionRenderer;
    private final double padding;

    ScreenshotDrawing(SlideSize size) {
        this.size = size;
        this.group = new Group();
        this.background = new Rectangle();
        this.captionRenderer = new CaptionRenderer();
        this.padding = size.width() * 0.1;

        captionRenderer.setContentArc(48.0);
        captionRenderer
            .widthProperty()
            .set(size.width() - padding * 2.0);
        captionRenderer
            .xProperty()
            .set(padding);
        captionRenderer
            .yProperty()
            .set(size.height());
    }

    Group draw(Slide.Screenshot screenshot) {
        var image = screenshot.image();

        drawImage(image, screenshot.background());
        drawCaption(screenshot);
        return group;
    }

    Group draw(Slide.CodeShot codeShot) {
        var image = codeShot.image();
        var lang = codeShot.language();
        var langColor = Colors.color(lang);

        drawImage(image, langColor);
        drawCaption(codeShot);
        return group;
    }

    private void drawImage(Image image, Color bgColor) {
        var iv = new ImageView();

        iv.setSmooth(true);
        iv.setPreserveRatio(true);
        fitImageView(iv, image);
        drawImage(image, iv);
        centerImageView(iv);

        clear(bgColor);
        group.getChildren().add(iv);
    }

    private void drawCaption(Slide slide) {
        slide.caption().ifPresent(caption -> {
            captionRenderer.renderCaption(caption);
            captionRenderer.draw(group);
            background
                .heightProperty()
                .bind(captionRenderer
                    .heightProperty()
                    .add(size.height())
                    .add(padding)
                );
        });
    }

    private void clear(Color color) {
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
}
