// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.drawing;

import engineer.mathsoftware.blog.slides.Colors;
import engineer.mathsoftware.blog.slides.Language;
import engineer.mathsoftware.blog.slides.Slide;
import engineer.mathsoftware.blog.slides.SlideSize;
import engineer.mathsoftware.blog.slides.lang.Parser;
import engineer.mathsoftware.blog.slides.lang.SchemeColors;
import engineer.mathsoftware.blog.slides.lang.Spec;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import static engineer.mathsoftware.blog.slides.drawing.Drawings.*;

class CodeSnippetDrawing {
    private final SlideSize size;
    private final Group group;
    private final Rectangle frame;
    private final TextFlow flow;
    private final SlideSize.Predefined sizeItem;
    private final double padding;
    private final double flowPadding;
    private final double textSize;

    CodeSnippetDrawing(SlideSize size) {
        this.size = size;
        this.group = new Group();
        this.frame = new Rectangle();
        this.flow = new TextFlow();
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
    }

    Group draw(Slide.CodeSnippet codeSnippet) {
        var code = codeSnippet.code();
        var lang = codeSnippet.language();
        var langColor = Colors.color(lang);

        renderCodeSnippetFrame(code, lang);

        var background = new Rectangle();

        background.setWidth(size.width());
        background
            .heightProperty()
            .bind(flow
                .heightProperty()
                .add(flowPadding * 2.0)
                .add(padding * 2.0)
            );
        clearRect(group, langColor, background);
        group.getChildren().addAll(frame, flow);
        return group;
    }

    private void renderCodeSnippetFrame(String code, Language lang) {
        var shadow = new DropShadow();
        var arc = 48.0 * switch (sizeItem) {
            case HD -> 1.0;
            case FHD -> 2.0;
        };

        shadow.setColor(Color.web("#212121"));
        shadow.setRadius(48.0);

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

        Parser
            .tokens(code)
            .stream()
            .map(parser::parseToken)
            .forEach(parsing -> {
                var text = new Text();
                var el = parsing.element();

                text.setFont(font);
                text.setText(el.value());
                text.setFill(SchemeColors.color(el));
                flow.getChildren().add(text);
            });

        flow.setMaxWidth(size.width() - padding * 2.0 - flowPadding * 2.0);
        flow.setPrefWidth(size.width() - padding * 2.0 - flowPadding * 2.0);
        flow.setLayoutX(padding + flowPadding);
        flow.setLayoutY(padding + flowPadding);
    }
}
