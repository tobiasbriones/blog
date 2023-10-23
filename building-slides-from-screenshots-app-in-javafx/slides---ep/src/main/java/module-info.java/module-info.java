// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

module engineer.mathsoftware.blog.slides {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires tess4j;
    exports engineer.mathsoftware.blog.slides.data;
    exports engineer.mathsoftware.blog.slides.ui;
    opens engineer.mathsoftware.blog.slides.ui to javafx.fxml;
}
