// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.drawing.ai;

import engineer.mathsoftware.blog.slides.ai.SlideAI;
import javafx.geometry.BoundingBox;
import javafx.scene.paint.Color;

import java.util.List;

import static engineer.mathsoftware.blog.slides.drawing.ai.States.*;

public sealed interface AIShape {
    record WordSelection(
        List<BoundingBox> wordBoxes,
        Stateful<BoundingBox, State> wordFocus
    ) implements AIShape {
        public static WordSelection of(SlideAI.OcrWordDetection detection) {
            return new WordSelection(detection.wordBoxes(), new WordSelectionState());
        }
    }

    enum State {
        Normal,
        Hovered,
        Selected
    }

    static Color color(State state) {
        return Color.web(switch (state) {
            case Normal -> "#4FC3F7";
            case Hovered -> "#607d8b";
            case Selected -> "#455a64";
        });
    }
}

