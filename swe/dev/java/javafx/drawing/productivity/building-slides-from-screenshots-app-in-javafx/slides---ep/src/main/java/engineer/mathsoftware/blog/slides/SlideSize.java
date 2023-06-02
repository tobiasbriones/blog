// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides;

public record SlideSize(double width, double height) {
    public enum Predefined {
        HD(new SlideSize(1_280.0, 720.0)),
        FHD(new SlideSize(1_920.0, 1_080.0));

        public static Predefined from(SlideSize size) {
            if (size.equals(FHD.value())) {
                return FHD;
            }
            return HD;
        }

        private final SlideSize value;

        public SlideSize value() {
            return value;
        }

        Predefined(SlideSize value) {
            this.value = value;
        }
    }
}
