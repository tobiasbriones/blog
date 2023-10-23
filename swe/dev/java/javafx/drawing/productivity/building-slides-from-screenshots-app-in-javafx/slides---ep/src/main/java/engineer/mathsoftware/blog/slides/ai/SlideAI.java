// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.ai;

import javafx.geometry.BoundingBox;
import javafx.scene.image.Image;

import java.util.List;

public sealed interface SlideAI {
    record OcrWordDetection(List<BoundingBox> wordBoxes) implements SlideAI {
        public static OcrWordDetection from(Image image) {
            return new OcrWordDetection(Ocr.textBoxes(image));
        }
    }
}
