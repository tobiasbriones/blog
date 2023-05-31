// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides;

import javafx.scene.image.Image;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public sealed interface Slide {
    record CodeSnippet(String code) implements Slide {}

    record CodeShot(Image image) implements Slide {}

    record Screenshot(Image image) implements Slide {}

    enum Values {
        CodeSnippet,
        CodeShot,
        Screenshot;

        public static Optional<Values> from(String str) {
            return Arrays
                .stream(values())
                .filter(x -> x.name().equals(str))
                .findFirst();
        }

        public static List<String> strings() {
            return Arrays.stream(values()).map(Enum::name).toList();
        }
    }
}
