// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.sierpinskipetal;

import engineer.mathsoftware.blog.sierpinskipetal.app.AppView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    private static final double WINDOW_WIDTH = 960.0;
    private static final double WINDOW_HEIGHT = 960.0;

    public static void main(String[] args) {
        launch(args);
    }

    private final AppView view;

    public Main() {
        super();
        view = AppView.newInstance();
    }

    @Override
    public void start(Stage primaryStage) {
        var scene = new Scene(view.getRoot(), WINDOW_WIDTH, WINDOW_HEIGHT);

        view.init();

        loadIcons(primaryStage);
        primaryStage.setTitle("Sierpinski Petal");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static void loadIcons(Stage stage) {
        stage.getIcons()
             .addAll(
                 new Image("app-16x16.png"),
                 new Image("app-32x32.png"),
                 new Image("app-64x64.png"),
                 new Image("app-256x256.png"),
                 new Image("app-512x512.png")
             );
    }
}
