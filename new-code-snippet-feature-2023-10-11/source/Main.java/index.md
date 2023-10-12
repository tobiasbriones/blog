---
permalink: new-code-snippet-feature-2023-10-11/source/Main.java.html
title: "new-code-snippet-feature-2023-10-11/source/Main.java"
---

# Main.java
```java
// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

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

        primaryStage.setTitle("Slides");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private URL loadResource(String name) {
        return Objects.requireNonNull(
            getClass().getClassLoader().getResource(name)
        );
    }
}

```
<div class="social open-gh-btn my-4">
  <a class="btn btn-github" href="https://github.com/tobiasbriones/blog/tree/main/mathswe/eng/uiux/platform/feat/new-code-snippet-feature-2023-10-11/source/Main.java" target="_blank">
    <i class="fab fa-github">
      
    </i>
    <span>
      Open in GitHub
    </span>
  </a>
</div>