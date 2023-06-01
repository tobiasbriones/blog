// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides;

public record SlideSize(int width, int height) {
    public enum Predefined {
        HD(new SlideSize(1_280, 720)),
        FHD(new SlideSize(1_920, 1_080));

        private final SlideSize value;

        public SlideSize value() {
            return value;
        }

        Predefined(SlideSize value) {
            this.value = value;
        }
    }
}
