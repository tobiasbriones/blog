// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides;

public record SlideSize(int width, int height) {
    public static final SlideSize HD = new SlideSize(720, 1_280);
    public static final SlideSize FHD = new SlideSize(1_080, 1_920);
}
