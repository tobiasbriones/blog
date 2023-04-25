// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.sierpinskipetal.app;

class AppController {
    private AppView view;

    AppController() {
        this.view = null;
    }

    void setView(AppView value) {
        this.view = value;
    }
}
