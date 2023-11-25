// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.sierpinskipetal.app;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

public class AppView {
    public static AppView newInstance() {
        var controller = new AppController();
        var view = new AppView(controller);

        controller.setView(view);
        return view;
    }

    private final VBox view;
    private final AppCanvasView canvasView;
    private final AppController controller;

    private AppView(AppController controller) {
        this.controller = controller;
        this.view = new VBox();
        this.canvasView = new AppCanvasView(controller);
    }

    public Parent getRoot() {
        return view;
    }

    public void init() {
        canvasView.init();

        view.setAlignment(Pos.CENTER);
        view.getChildren()
            .add(canvasView.getNode());

        controller.init();
    }
}
