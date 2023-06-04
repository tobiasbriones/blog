// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides;

import javafx.scene.image.Image;

public sealed interface Slide {
    record CodeSnippet(String code, Language language) implements Slide {}

    record CodeShot(Image image, Language language) implements Slide {}

    record Screenshot(Image image) implements Slide {}
}
