// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.lang;

public sealed interface Element {
    record Keyword(String value) implements Element {}

    record Symbol(String value) implements Element {}

    record Number(String value) implements Element {}

    record Other(String value) implements Element {}

    record TokenParsing(Element element) {}

    String value();
}
