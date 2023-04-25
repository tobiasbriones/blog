<!-- Copyright (c) 2023 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# Drawing a Sierpinski Petal Mandala Fractal in JavaFX

## Getting Started

Tools and project set up is given next, so you can build the basics for
developing the underlying drawing in JavaFX.

First, make sure to have Java 20+ installed on your development machine. If
you're using Linux, I recommend using [SDKMAN](https://sdkman.io) for
[installing Java](https://sdkman.io/usage#installdefault) and managing different
versions. If you're using Windows, you can download the
[installer from foojay.io](https://foojay.io/download).

In addition to the recommended Java installation method mentioned above, I
highly suggest using the **Zulu (FX) distribution** to get the FX mods out of
the box!

As always, I'm using IntelliJ IDEA as the IDE of choice, so next, we need to set
up the JavaFX app.

For creating a new JavaFX app, follow
[Beginning JavaFX Applications with IntelliJ IDE \| foojay.io](https://foojay.io/today/beginning-javafx-with-intellij)
with one of the approach given by the author. I suggest using the "Plain"
approach for this project.

Our app package name is `engineer.mathsoftware.blog.sierpinskipetal` by
following the reverse DNS Java convention for app packages. You'll need the
`Main.java` class and the `package-info.java` on the root package
(`sierpinskipetal`).

It's important to note the `module-info.java` in the module's source root
(`src` dir), as we use Java modules to import JavaFX mods in this case.
Exporting your root package is also necessary, as JavaFX is a framework that
uses reflection and needs to find your `Main` class.

If you're using Java 20 or so, then "record patterns" and "pattern matching for
switch" features are still not stable. If so, make sure to enable the "Preview"
version in IntelliJ.

With this, Java and JavaFX should be available on your machine as well as the
dev tools, environment, and starting project, so we can start writing the JavaFX
code.

### Hello World

Our Hello World app looks like the following:

```java
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

        primaryStage.setTitle("Sierpinski Petal");

        btn.setText("Hello World");
        btn.setOnAction(actionEvent -> System.out.println("Hello World"));

        root.setAlignment(Pos.CENTER);
        root.getChildren()
            .add(btn);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
```

<figcaption>
<p align="center"><strong>Hello World Program</strong></p>
</figcaption>

![Hello World](hello-world.png)

<figcaption>
<p align="center"><strong>Hello World Scene</strong></p>
</figcaption>

#### Package App

This package will implement the JavaFX GUI application.

I applied an MVC architectural pattern to design the `app` `package` since the
GUI part of the application is minimal. Otherwise, I'd use MVP[^x] to design
JavaFX desktop apps. I thought about it, but the tradeoff was remarkable since
applying MVP for a basic GUI would imply an extra overhead for this article, and
I can't burn readers with one of my in-house MVP[^x] libs [^x].

[^x]: Or MVVM if possible

[^x]: Notice that MVC is a poor old-school design, e.g., it uses concrete
    classes, unlike MVP, which is a bit better and relies on "contract"
    interfaces that enrich our design with better abstractions

[^x]: MVC is a horrible (and most popular out there) pattern I'd only use for
    Android apps I used to write 10 years ago in 2013, but it's good for basic
    GUIs if there's no better support from the GUI tooling

So the complex part of this package will be the `Canvas` graphics instead.

First, create the `package` from the base `sierpinskipetal` one:

```java
/**
 * Implements the JavaFX application for drawing and animating the underlying
 * proposed graphics (e.g., sierpinski petal fractals).
 */
package engineer.mathsoftware.blog.sierpinskipetal.app;
```

<figcaption>
<p align="center"><strong>Definition of the "app" Package</strong></p>
</figcaption>

Two classes will be added next:

- `AppView`: Visual component that renders our GUI and graphics.
- `AppController`: Logic control of the view.

The package will be accessed via the `AppView` class, so it keeps familiar to
what we had before:

```java
public class AppView {
    public static AppView newInstance() {
        var controller = new AppController();
        var view = new AppView(controller);

        controller.setView(view);
        return view;
    }

    private final VBox view;
    private final AppController controller;

    private AppView(AppController controller) {
        this.controller = controller;
        this.view = new VBox();
    }

    public Parent getRoot() {
        return view;
    }

    public void init() {
        var btn = new Button();

        btn.setText("Hello World");
        btn.setOnAction(actionEvent -> System.out.println("Hello World"));

        view.setAlignment(Pos.CENTER);
        view.getChildren()
            .add(btn);
        controller.init();
    }
}
```

<figcaption>
<p align="center"><strong>Hello World Moved to the AppView Class</strong></p>
</figcaption>

`AppView` provides the `newInstance` static factory method that gets us a
properly built view with its `AppController` created and set up.

Now, the `getRoot` accessor will be used to add the `Parent` `Node` to the main
`Scene`.

The `init` method avoids abusing the view's constructor [^x].

[^x]: Abusing the constructor to build the whole GUI is an antipattern I always
    see in the Java culture

Now add the controller:

```java
class AppController {
    private AppView view;

    AppController() {
        this.view = null;
    }

    void setView(AppView value) {
        this.view = value;
    }

    void init() {}
}
```

<figcaption>
<p align="center"><strong>Creation of AppController</strong></p>
</figcaption>

Then, any control logic will be placed there later.

Finally, the `Main` class will look like this (I made the window's height bigger
too):

```java
public class Main extends Application {
    private static final double WINDOW_WIDTH = 960.0;
    private static final double WINDOW_HEIGHT = 960.0;

    public static void main(String[] args) { launch(args); }

    private final AppView view = AppView.newInstance();

    @Override
    public void start(Stage primaryStage) {
        var scene = new Scene(view.getRoot(), WINDOW_WIDTH, WINDOW_HEIGHT);

        view.init();
        primaryStage.setTitle("Sierpinski Petal");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
```

Which produces the same "Hello World" app [above](#hello-world) (but a little
bigger).

#### Canvas View

It's time to get create the canvas component to move our "Hello World" to
`Canvas`.

The view is `AppCanvasView` as follows:

```java
class AppCanvasView {
    private static final double CANVAS_WIDTH = 900.0;
    private static final double CANVAS_HEIGHT = 900.0;
    private static final Color bgColor = Color.web("#fafafa");
    private final Canvas canvas;

    AppCanvasView(AppController controller) {
        this.canvas = new Canvas();
    }

    Node getNode() {
        return canvas;
    }

    void init() {
        canvas.setWidth(CANVAS_WIDTH);
        canvas.setHeight(CANVAS_HEIGHT);

        draw();
    }

    void draw() {
        var ctx = canvas.getGraphicsContext2D();

        ctx.setFill(bgColor);
        ctx.fillRect(0.0, 0.0, CANVAS_WIDTH, CANVAS_HEIGHT);

        ctx.setFill(Color.web("#212121"));
        ctx.setTextAlign(TextAlignment.CENTER);
        ctx.setTextBaseline(VPos.CENTER);
        ctx.fillText("Hello World", CANVAS_WIDTH / 2.0, CANVAS_HEIGHT / 2.0);
    }
}
```

It has definitions for the `Canvas` size (that can be scaled later for advanced
high-resolution productions).

It has the `Canvas` `Node` that refers to the JavaFX component we're using.

The `init` method is available, so it can be initialized like the other objects
we created in the `app` package.

The method `draw` is clearing the `Canvas` with a white-gray color, and draws
the "Hello World" text in the middle of the screen.

Notice that I'm not using `interface`s or further abstractions in this package
because of what I said above. For example, the methods `init`, and `draw`, etc.,
should be part of a public API, and interfaces should be used to weaken the
coupling among objects (MVP-like), but that's not this package design's scope.

The result is the "Hello World" `Canvas` rendered in the middle of the window:

![Canvas Hello World](canvas-hello-world.png)

<figcaption>
<p align="center"><strong>Canvas Hello World</strong></p>
</figcaption>
