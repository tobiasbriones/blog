// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides;

public enum Palette {
    Good("#4caf50"),
    Error("#b00020");

    private final String colorCode;

    Palette(String color) {
        this.colorCode = color;
    }

    public String colorCode() {
        return colorCode;
    }
}
