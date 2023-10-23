// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.drawing.ai;

import javafx.scene.Group;

public interface AIDrawing {
    void setup(AIShape shape);

    Group draw(Group slideDrawing);

    void clear();
}
