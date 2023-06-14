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
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;

import static engineer.mathsoftware.blog.slides.drawing.Drawings.clearRect;
import static engineer.mathsoftware.blog.slides.drawing.Drawings.newShadow;

class CodeSnippetDrawing {
    private final SlideSize size;
    private final Group group;
    private final Rectangle frame;
    private final TextFlow flow;
    private final CaptionRenderer captionRenderer;
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
        this.captionRenderer = new CaptionRenderer();
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

        captionRenderer.setContentArc(arc);
        captionRenderer
            .widthProperty()
            .bind(flow
                .widthProperty()
            );
        captionRenderer
            .xProperty()
            .set(padding + flowPadding);
        captionRenderer
            .yProperty()
            .bind(frame.
                heightProperty()
                .add(padding * 2.0)
            );
    }

    Group draw(Slide.CodeSnippet codeSnippet) {
        var code = codeSnippet.code();
        var lang = codeSnippet.language();
        var langColor = Colors.color(lang);

        renderCodeSnippetFrame(code, lang);
        codeSnippet.caption().ifPresent(captionRenderer::renderCaption);

        var background = new Rectangle();

        background.setWidth(size.width());
        background
            .heightProperty()
            .bind(frame
                .heightProperty()
                .add(padding * 2.0)
                .add(captionRenderer.heightProperty())
                .add(padding * CaptionRenderer.zeroIfNoCaption(codeSnippet))
            );
        clearRect(group, langColor, background);
        group.getChildren().addAll(frame, flow);

        codeSnippet.caption().ifPresent(caption -> captionRenderer.draw(group));
        return group;
    }

    private void renderCodeSnippetFrame(String code, Language lang) {
        var shadow = newShadow();

        frame.setFill(Color.web("#212121"));
        frame.setWidth(size.width() - padding * 2.0);
        frame
            .heightProperty()
            .bind(flow
                .heightProperty()
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
}
