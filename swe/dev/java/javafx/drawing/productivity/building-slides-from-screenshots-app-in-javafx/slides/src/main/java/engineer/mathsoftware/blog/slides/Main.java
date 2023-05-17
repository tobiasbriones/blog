// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    private static final double WINDOW_WIDTH = 960.0;
    private static final double WINDOW_HEIGHT = 640.0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        var root = new VBox();
        var scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        var btn = new Button();

        btn.setText("Hello World");
        btn.setOnAction(actionEvent -> System.out.println("Hello World"));

        root.setAlignment(Pos.CENTER);
        root.getChildren()
            .add(btn);

        loadIcons(primaryStage);

        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle("Slides");
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
