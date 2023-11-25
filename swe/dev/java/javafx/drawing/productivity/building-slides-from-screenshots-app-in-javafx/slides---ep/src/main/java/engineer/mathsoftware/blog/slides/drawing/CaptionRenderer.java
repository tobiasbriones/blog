// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.drawing;

import engineer.mathsoftware.blog.slides.Slide;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import static engineer.mathsoftware.blog.slides.drawing.Drawings.newShadow;

class CaptionRenderer {
    private final Rectangle captionFrame;
    private final VBox captionBox;
    private final DoubleProperty heightProperty;
    private final DoubleProperty widthProperty;
    private final DoubleProperty xProperty;
    private final DoubleProperty yProperty;
    private double contentArc;

    CaptionRenderer() {
        this.captionFrame = new Rectangle();
        this.captionBox = new VBox();
        this.widthProperty = new SimpleDoubleProperty();
        this.heightProperty = new SimpleDoubleProperty();
        this.xProperty = new SimpleDoubleProperty();
        this.yProperty = new SimpleDoubleProperty();
        this.contentArc = 0.0;

        heightProperty
            .bind(captionBox
                .heightProperty()
            );
    }

    void setContentArc(double newArc) {
        contentArc = newArc;
    }

    DoubleProperty widthProperty() {
        return widthProperty;
    }

    DoubleProperty heightProperty() {
        return heightProperty;
    }

    DoubleProperty xProperty() {
        return xProperty;
    }

    DoubleProperty yProperty() {
        return yProperty;
    }

    void draw(Group drawing) {
        drawing.getChildren().addAll(captionFrame, captionBox);
    }

    void renderCaption(Slide.Caption caption) {
        var titleLabel = new Label();
        var subTitleLabel = new Label();
        var shadow = newShadow();
        var font = Font.font("Poppins", 24.0);
        var captionArc = contentArc * 2.0;
        var boldFont = Font.font(
            font.getFamily(),
            FontWeight.EXTRA_BOLD,
            36.0
        );

        captionBox.setAlignment(Pos.CENTER);
        captionBox.setSpacing(16.0);
        captionBox.setPadding(new Insets(32.0, 64.0, 32.0, 64.0));
        captionBox
            .prefWidthProperty()
            .bind(widthProperty);
        captionBox
            .layoutXProperty()
            .bind(xProperty);
        captionBox
            .layoutYProperty()
            .bind(yProperty);

        titleLabel.setText(caption.title());
        titleLabel.setTextFill(Color.web("#e0e0e0"));
        titleLabel.setFont(boldFont);
        titleLabel.setTextAlignment(TextAlignment.CENTER);
        titleLabel.getStyleClass().add("text");
        captionBox.getChildren().add(titleLabel);

        if (!caption.subtitle().isBlank()) {
            subTitleLabel.setText(caption.subtitle());
            subTitleLabel.setTextFill(Color.web("#e0e0e0"));
            subTitleLabel.setFont(font);
            subTitleLabel.setTextAlignment(TextAlignment.CENTER);
            subTitleLabel.getStyleClass().add("text");
            captionBox.getChildren().add(subTitleLabel);
            captionArc *= 2.0;
        }

        captionFrame.setFill(Color.web("#212121"));
        captionFrame
            .widthProperty()
            .bind(captionBox
                .widthProperty()
            );
        captionFrame
            .heightProperty()
            .bind(captionBox
                .heightProperty()
            );
        captionFrame
            .xProperty()
            .bind(captionBox
                .layoutXProperty()
            );
        captionFrame.
            yProperty()
            .bind(captionBox
                .layoutYProperty()
            );
        captionFrame.setArcWidth(captionArc);
        captionFrame.setArcHeight(captionArc);

        shadow.setRadius(shadow.getRadius() / 2.0);
        captionFrame.setEffect(shadow);
    }

    static double zeroIfNoCaption(Slide slide) {
        return slide.caption().isPresent() ? 1.0 : 0.0;
    }
}
