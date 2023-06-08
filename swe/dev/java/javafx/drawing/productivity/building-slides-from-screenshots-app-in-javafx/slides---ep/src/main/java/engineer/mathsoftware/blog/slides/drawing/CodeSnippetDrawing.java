// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.drawing;

import engineer.mathsoftware.blog.slides.Colors;
import engineer.mathsoftware.blog.slides.Language;
import engineer.mathsoftware.blog.slides.Slide;
import engineer.mathsoftware.blog.slides.SlideSize;
import engineer.mathsoftware.blog.slides.lang.ElementItem;
import engineer.mathsoftware.blog.slides.lang.Parser;
import engineer.mathsoftware.blog.slides.lang.SchemeColors;
import engineer.mathsoftware.blog.slides.lang.Spec;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;

import static engineer.mathsoftware.blog.slides.drawing.Drawings.clearRect;

class CodeSnippetDrawing {
    private final SlideSize size;
    private final Group group;
    private final Rectangle frame;
    private final TextFlow flow;
    private final Rectangle captionFrame;
    private final VBox captionBox;
    private final DoubleProperty captionHeightSpaceProperty;
    private final SlideSize.Predefined sizeItem;
    private final double padding;
    private final double flowPadding;
    private final double textSize;
    private final double arc;

    CodeSnippetDrawing(SlideSize size) {
        this.size = size;
        this.group = new Group();
        this.frame = new Rectangle();
        this.flow = new TextFlow();
        this.captionFrame = new Rectangle();
        this.captionBox = new VBox();
        this.captionHeightSpaceProperty = new SimpleDoubleProperty();
        this.sizeItem = SlideSize.Predefined.from(size);
        this.padding = 96.0 * switch (sizeItem) {
            case HD -> 1.0;
            case FHD -> 1.25;
        };
        this.flowPadding = 64 * switch (sizeItem) {
            case HD -> 1.0;
            case FHD -> 1.25;
        };
        this.textSize = 20.0 * switch (sizeItem) {
            case HD -> 1.0;
            case FHD -> 1.5;
        };
        this.arc = 48.0;
    }

    Group draw(Slide.CodeSnippet codeSnippet) {
        var code = codeSnippet.code();
        var lang = codeSnippet.language();
        var langColor = Colors.color(lang);

        renderCodeSnippetFrame(code, lang);
        codeSnippet.caption().ifPresent(this::renderCaption);

        var background = new Rectangle();

        background.setWidth(size.width());
        background
            .heightProperty()
            .bind(flow
                .heightProperty()
                .add(flowPadding * 2.0)
                .add(padding * 2.0)
                .add(captionHeightSpaceProperty)
            );
        clearRect(group, langColor, background);
        group.getChildren().addAll(frame, flow);

        codeSnippet.caption().ifPresent(caption ->
            group.getChildren().addAll(captionFrame, captionBox)
        );
        return group;
    }

    private void renderCodeSnippetFrame(String code, Language lang) {
        var shadow = newShadow();

        frame.setFill(Color.web("#212121"));
        frame.setWidth(size.width() - padding * 2.0);
        frame
            .heightProperty()
            .bind(flow.
                heightProperty()
                .add(flowPadding * 2.0)
            );
        frame.setX(padding);
        frame.setY(padding);
        frame.setArcWidth(arc);
        frame.setArcHeight(arc);
        frame.setEffect(shadow);

        renderCodeSnippetFlow(code, lang);
    }

    private void renderCodeSnippetFlow(String code, Language lang) {
        var parser = new Parser<>(Spec.keywordTypeOf(lang));
        var font = Font.font("JetBrains Mono", textSize);
        var boldFont = Font.font(
            font.getFamily(),
            FontWeight.BOLD,
            font.getSize()
        );

        Parser
            .tokens(code)
            .stream()
            .map(parser::parseToken)
            .forEach(parsing -> {
                var text = new Text();
                var el = parsing.element();

                text.setText(el.value());
                text.setFill(SchemeColors.color(el));
                text.setFont(el.toEnum() == ElementItem.Type ? boldFont : font);

                flow.getChildren().add(text);
            });

        flow.setMaxWidth(size.width() - padding * 2.0 - flowPadding * 2.0);
        flow.setPrefWidth(size.width() - padding * 2.0 - flowPadding * 2.0);
        flow.setLayoutX(padding + flowPadding);
        flow.setLayoutY(padding + flowPadding);
    }

    private void renderCaption(Slide.Caption caption) {
        var titleLabel = new Label();
        var subTitleLabel = new Label();
        var shadow = newShadow();
        var font = Font.font("Poppins", 24.0);
        var captionArc = arc * 2.0;
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
            .bind(flow
                .widthProperty()
            );
        captionBox.setLayoutX(padding + flowPadding);
        captionBox
            .layoutYProperty()
            .bind(flow.
                heightProperty()
                .add(flowPadding * 2.0)
                .add(padding * 2.0)
            );

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

        captionHeightSpaceProperty
            .bind(captionBox
                .heightProperty()
                .add(padding)
            );

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

    private static DropShadow newShadow() {
        var shadow = new DropShadow();

        shadow.setColor(Color.web("#212121"));
        shadow.setRadius(48.0);
        return shadow;
    }
}
