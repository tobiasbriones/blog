// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.lang;

import javafx.scene.paint.Color;

public final class SchemeColors {
    public static Color color(Element element) {
        return switch (element.toEnum()) {
            case Keyword -> Color.web("#7986cb");
            case Symbol -> Color.web("#cc7832");
            case Number -> Color.web("#0288d1");
            case StringLiteral -> Color.web("#558b2f");
            case Comment -> Color.web("#757575");
            case Type, Other -> Color.web("#e0e0e0");
        };
    }

    private SchemeColors() {}
}
