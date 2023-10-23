// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.Optional;

public sealed interface Slide {
    record CodeSnippet(
        String code,
        Language language,
        Optional<Caption> caption
    ) implements Slide {}

    record CodeShot(
        Image image,
        Language language,
        Optional<Caption> caption
    ) implements Slide {}

    record Screenshot(
        Image image,
        Color background,
        Optional<Caption> caption
    ) implements Slide {}

    Optional<Caption> caption();

    record Caption(String title, String subtitle) {}
}
