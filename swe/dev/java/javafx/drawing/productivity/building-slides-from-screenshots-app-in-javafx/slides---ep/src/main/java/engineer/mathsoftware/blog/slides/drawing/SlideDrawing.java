// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.drawing;

import engineer.mathsoftware.blog.slides.Slide;
import engineer.mathsoftware.blog.slides.SlideSize;
import javafx.scene.Group;

public interface SlideDrawing {
    void setup(SlideSize slideSize);

    Group draw(Slide slide);
}
