<!-- Copyright (c) 2023 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# Building Slides from Screenshots App in JavaFX

## Getting Started

First, make sure to have Java 20+ installed on your development machine. You
might need:

- [SDKMAN](https://sdkman.io).
- [Java installer from foojay.io](https://foojay.io/download).
- [IntelliJ IDEA](https://www.jetbrains.com/idea).
- [Scene Builder](https://gluonhq.com/products/scene-builder).

I highly suggest using the **Zulu (FX) distribution** to get the FX mods out of
the box!

For creating a new JavaFX app, follow
[Beginning JavaFX Applications with IntelliJ IDE \| foojay.io](https://foojay.io/today/beginning-javafx-with-intellij)
with one of the approach given by the author. I suggest using the "Plain"
approach for this project.

Our app package name is `engineer.mathsoftware.blog.slides`.

The following is the initial app project.

`module-info.java`

```java
module engineer.mathsoftware.blog.slides {
    requires javafx.controls;
    requires javafx.fxml;
    opens engineer.mathsoftware.blog.slides to javafx.fxml;
    exports engineer.mathsoftware.blog.slides;
}
```

<figcaption>
<p align="center"><strong>Definition of Application Modules</strong></p>
</figcaption>

As given above, it's required JavaFX `controls`, and `fxml` mods since I decided
to use FXML with the Scene Builder. Then, the main app package has to be opened
to the `fxml` mod, so it can use reflection on our app, as well as exporting it
so JavaFX in general can see our app via reflection.

Now, we move forward the main package.

`package-info.java`

```java
/**
 * Provides a desktop application that converts screenshots into professional
 * slides that tell a story.
 */
package engineer.mathsoftware.blog.slides;
```

<figcaption>
<p align="center"><strong>Main Application Package</strong></p>
</figcaption>

`Main.java`

```java
public class Main extends Application {
    private static final double WINDOW_WIDTH = 1360.0;
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

        primaryStage.setTitle("Slides");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
```

<figcaption>
<p align="center"><strong>Initial Hello World App</strong></p>
</figcaption>

![Hello World](hello-world.png)

<figcaption>
<p align="center"><strong>Hello World</strong></p>
</figcaption>
