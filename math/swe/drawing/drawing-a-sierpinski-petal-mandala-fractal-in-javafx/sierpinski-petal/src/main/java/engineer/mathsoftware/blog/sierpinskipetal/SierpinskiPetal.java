// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.sierpinskipetal;

import javafx.scene.paint.Color;

public sealed interface SierpinskiPetal {
    record Petal(
        double radius,
        Color color,
        Color centerColor
    ) implements SierpinskiPetal {}
}
