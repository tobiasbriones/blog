// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.lang;

import engineer.mathsoftware.blog.slides.Enums;

public sealed interface Element extends Enums.ToEnum<ElementItem> {
    record Keyword(String value) implements Element {}

    record Symbol(String value) implements Element {}

    record Type(String value) implements Element {}

    record Number(String value) implements Element {}

    record StringLiteral(String value) implements Element {}

    record Comment(String value) implements Element {}

    record Other(String value) implements Element {}

    record TokenParsing(Element element) {}

    String value();

    @Override
    default Class<ElementItem> enumType() {
        return ElementItem.class;
    }
}
