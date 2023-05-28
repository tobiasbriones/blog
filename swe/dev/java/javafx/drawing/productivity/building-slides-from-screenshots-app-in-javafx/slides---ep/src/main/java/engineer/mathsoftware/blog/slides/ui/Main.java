// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class Main extends Application {
    private static final double WINDOW_WIDTH = 1360.0;
    private static final double WINDOW_HEIGHT = 640.0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        var root = FXMLLoader.<Parent>load(loadResource("app.fxml"));
        var scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        scene.getStylesheets().add(
            loadResource("app.css").toExternalForm()
        );
        loadIcons(primaryStage);

        primaryStage.setTitle("Slides");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private URL loadResource(String name) {
        return Objects.requireNonNull(
            getClass().getClassLoader().getResource(name)
        );
    }

    private static void loadIcons(Stage stage) {
        stage
            .getIcons()
            .addAll(
                new Image("app-16x16.png"),
                new Image("app-32x32.png"),
                new Image("app-64x64.png"),
                new Image("app-256x256.png"),
                new Image("app-512x512.png")
            );
    }
}
