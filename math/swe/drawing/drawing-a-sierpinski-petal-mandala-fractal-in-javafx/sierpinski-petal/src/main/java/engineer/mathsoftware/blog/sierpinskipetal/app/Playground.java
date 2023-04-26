// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.sierpinskipetal.app;

import javafx.animation.AnimationTimer;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.TextAlignment;

import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
class Playground {
    final Canvas canvas;
    final GraphicsContext ctx;
    final double scale;
    final double cycleDuration;
    final int targetFps;
    final AnimationTimer loop;
    final Recorder recorder;
    double opacity;
    boolean record;
    String title;
    Flower flower;
    BirdCat cat;

    double width() { return canvas.getWidth() / scale; }

    double height() { return canvas.getHeight() / scale; }

    double cx() { return width() / 2; }

    double cy() { return height() / 2; }

    Playground(Canvas canvas, double scale) {
        this.canvas = canvas;
        this.ctx = canvas.getGraphicsContext2D();
        this.scale = scale;
        this.cycleDuration = 1.0;
        this.targetFps = 60;
        this.loop = new FadeAnimLoop(
            this::draw,
            targetFps,
            cycleDuration,
            FadeAnimLoop.TimeMode.Absolute
        );
        this.recorder = new Recorder(canvas, targetFps);
        this.opacity = 1.0;
        this.record = false;
        this.title = "Drawing a Flower";
    }

    void play() {
        initModels();
        ctx.scale(scale, scale);
        loop.start();
    }

    void initModels() {
        initFlower();
        initCat();
    }

    void initFlower() {
        var radius = 100;
        var cx = cx();
        var cy = cy() - radius;
        var color = Color.web("#ffdab9");
        var centerColor = Color.web("#f0f28d");
        flower = new Flower(
            radius,
            color,
            centerColor,
            cx,
            cy
        );
    }

    void initCat() {
        var cx = cx();
        cat = new BirdCat(cx);
    }

    void draw(
        int numAnim,
        double opacity,
        Cycle.State state,
        int tickCount,
        double cycleTime
    ) {
        this.opacity = opacity;

        // Uncomment to Choose Animation //
//        var didDraw = drawFlower(numAnim, state);
        var didDraw = drawCat();

        if (didDraw) {
            drawCompleted(tickCount);
        }
        else {
            stop();
        }
    }

    void drawCompleted(int tickCount) {
        if (record) {
            recordSnapshot(tickCount);
        }
    }

    void stop() {
        loop.stop();
        stopped();
    }

    void stopped() {
        if (record) {
            buildRecording();
        }
    }

    boolean drawFlower(int numAnim, Cycle.State state) {
        if (numAnim > Flower.NUM_ANIMS) {
            return false;
        }

        flower.draw(numAnim, state);
        return true;
    }

    boolean drawCat() {
        reset();
        cat.anim1_MemeTitle();
        return true;
    }

    void reset() {
        clean();

        ctx.setGlobalAlpha(1);
        drawRuler();
        ctx.setGlobalAlpha(opacity);
    }

    void clean() {
        var bgColor = Color.web("#fafafa");

        ctx.setGlobalAlpha(1);
        ctx.setFill(bgColor);
        ctx.fillRect(0.0, 0.0, width(), height());
        drawSource(title);
        ctx.setGlobalAlpha(opacity);
    }

    void recordSnapshot(int tickCount) {
        try {
            recorder.saveSnapshot(tickCount);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    void buildRecording() {
        try {
            recorder.compileVideo();
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    void drawTitle(String title) {
        setDrawingText("extrabold", 36, VPos.BOTTOM);
        ctx.fillText(title, width() / 2, height() - 140);
    }

    void drawSource(String title) {
        setDrawingText("medium", 24, VPos.BOTTOM);

        ctx.fillText(
            "BLOG | mathsoftware.engineer",
            width() / 2,
            height() - 32
        );
        ctx.fillText(
            "Drawing a Sierpinski Petal Mandala Fractal in JavaFX",
            width() / 2,
            height() - 64
        );

        setDrawingText("extrabold", 36, VPos.BOTTOM);
        ctx.fillText(title, width() / 2, height() - 96);
    }

    void drawRuler() {
        ctx.setStroke(Color.web("#757575"));

        setDrawingText("medium", 16, VPos.TOP);

        ctx.strokeLine(0, 16, width(), 16);
        ctx.strokeLine(0, 24, width(), 24);

        for (var x = 50; x < width(); x++) {
            if (x % 50 == 0) {
                ctx.strokeLine(x, 16, x, 36);
                ctx.fillText(String.valueOf(x), x, 40);
            }
            else if (x % 25 == 0) {
                ctx.strokeLine(x, 16, x, 28);
            }
        }

        ctx.setTextAlign(TextAlignment.LEFT);
        ctx.setTextBaseline(VPos.CENTER);
        ctx.strokeLine(16, 0, 16, height());
        ctx.strokeLine(24, 0, 24, height());

        for (var y = 50; y < height(); y++) {
            if (y % 50 == 0) {
                ctx.strokeLine(16, y, 36, y);

                // Don't draw the first one to avoid colliding with
                // horizontal ruler
                if (y == 50) {
                    continue;
                }
                ctx.fillText(String.valueOf(y), 40, y);
            }
            else if (y % 25 == 0) {
                ctx.strokeLine(16, y, 28, y);
            }
        }
    }

    void setDrawingText(
        String weight,
        int size,
        VPos baseline
    ) {
        ctx.setFill(Color.web("#212121"));
        ctx.setTextAlign(TextAlignment.CENTER);
        ctx.setTextBaseline(baseline);
        ctx.setFontSmoothingType(FontSmoothingType.GRAY);
        ctx.setFont(Font.font("poppins " + weight, size));
    }

    void encloseHRuler(
        double radius,
        double cx,
        double cy,
        String txt
    ) {
        ctx.setStroke(Color.web("#4CAF50"));
        ctx.strokeLine(cx - radius, cy, cx + radius, cy);
        ctx.strokeLine(cx - radius, 0, cx - radius, cy + radius);
        ctx.strokeLine(cx + radius, 0, cx + radius, cy + radius);

        setDrawingText("medium", 20, VPos.BOTTOM);
        ctx.fillText(txt, cx, cy - 2);
    }

    void encloseVRuler(
        double radius,
        double cx,
        double cy,
        String txt
    ) {
        ctx.setStroke(Color.web("#4CAF50"));
        ctx.strokeLine(cx, cy - radius, cx, cy + radius);
        ctx.strokeLine(0, cy - radius, cx + radius, cy - radius);
        ctx.strokeLine(0, cy + radius, cx + radius, cy + radius);

        setDrawingText("medium", 20, VPos.CENTER);
        ctx.fillText(txt, cx, cy);
    }

    void fillCenteredCircle(
        double radius,
        double cx,
        double cy,
        Color color
    ) {
        var diameter = 2.0 * radius;

        ctx.setFill(color);
        ctx.fillOval(cx - radius, cy - radius, diameter, diameter);
    }

    void fillCenteredArc(
        double radiusX,
        double radiusY,
        double cx,
        double cy,
        Color color
    ) {
        var diameterX = 2.0 * radiusX;
        var diameterY = 2.0 * radiusY;

        ctx.setFill(color);
        ctx.fillOval(cx - radiusX, cy - radiusY, diameterX, diameterY);
    }

    void strokeCenteredArc(
        double radiusX,
        double radiusY,
        double cx,
        double cy,
        Color color
    ) {
        var diameterX = 2.0 * radiusX;
        var diameterY = 2.0 * radiusY;

        ctx.setStroke(color);
        ctx.strokeOval(cx - radiusX, cy - radiusY, diameterX, diameterY);
    }

    class Flower {
        final static int NUM_ANIMS = 9;
        final int radius;
        final Color color;
        final Color centerColor;
        final double cx;
        final double cy;

        Flower(
            int radius,
            Color color,
            Color centerColor,
            double cx,
            double cy
        ) {
            this.radius = radius;
            this.color = color;
            this.centerColor = centerColor;
            this.cx = cx;
            this.cy = cy;
        }

        void draw(int numAnim, Cycle.State state) {
            switch (numAnim) {
                case 1 -> anim1_Diameter();
                case 2 -> anim2_LeftPetal();
                case 3 -> anim3_TopPetal();
                case 4 -> anim4_RightPetal();
                case 5 -> anim5_BottomPetal();
                case 6 -> anim6_Center();
                case 7 -> anim7_Center();
                case 8 -> anim8_Stem();
                case 9 -> anim9_Flower(state);
            }
        }

        void anim1_Diameter() {
            reset();
            encloseHRuler(radius, cx, cy - radius, "diameter");
            drawTitle("Centering");
        }

        void anim2_LeftPetal() {
            reset();
            fillCenteredCircle(radius, cx - radius / 2, cy, color);
            encloseHRuler(radius, cx - radius / 2, cy, "diameter");
            drawTitle("Petal: Left");
        }

        void anim3_TopPetal() {
            reset();
            fillCenteredCircle(radius, cx - radius / 2, cy, color);
            fillCenteredCircle(radius, cx, cy - radius / 2, color);
            encloseHRuler(radius, cx, cy - radius / 2, "diameter");
            drawTitle("Petal: Top");
        }

        void anim4_RightPetal() {
            reset();
            fillCenteredCircle( radius, cx - radius / 2, cy, color);
            fillCenteredCircle(radius, cx, cy - radius / 2, color);
            fillCenteredCircle(radius, cx + radius / 2, cy, color);
            encloseHRuler(radius, cx + radius / 2, cy, "diameter");
            drawTitle("Petal: Right");
        }

        void anim5_BottomPetal() {
            reset();
            fillPetals();
            encloseHRuler(radius, cx, cy + radius / 2, "diameter");
            drawTitle("Petal: Bottom");
        }

        void anim6_Center() {
            reset();
            fillPetals();
            encloseHRuler(radius, cx, cy, "diameter");
            drawTitle("Flower: Center");
        }

        void anim7_Center() {
            reset();
            fillPetals();
            fillCenteredCircle(radius / 2, cx, cy, centerColor);
            encloseHRuler(radius / 2, cx, cy, "radius");
            drawTitle("Flower: Center");
        }

        void anim8_Stem() {
            reset();

            // Petal
            fillPetals();

            // draw line to delimit the bottom of center and petals
            ctx.strokeLine(0, cy + radius / 2, cx, cy + radius / 2);
            ctx.strokeLine(0, cy + 3 * radius / 2, cx, cy + 3 * radius / 2);

            fillCenteredCircle(radius / 2, cx, cy, centerColor);

            // Flower Stem
            var stemWidth = radius / 4;
            var stemHeight = radius * 2.5;

            ctx.setFill(Color.web("#81c784"));
            ctx.fillRoundRect(cx - stemWidth / 2,
                cy,
                stemWidth,
                stemHeight,
                16,
                16
            );
            encloseVRuler(
                stemHeight / 2,
                cx,
                cy + stemHeight / 2,
                "2.5*radius"
            );

            drawTitle("Flower: Stem");
        }

        void anim9_Flower(Cycle.State state) {
            clean();

            // Eliminate the effect as this is the last anim
            if (state == Cycle.State.FadingOut) {
                ctx.setGlobalAlpha(1);
            }

            drawFlower();
        }

        void drawFlower() {
            var stemWidth = radius / 4;

            // Flower Stem
            ctx.setFill(Color.web("#81c784"));
            ctx.fillRoundRect(
                cx - stemWidth / 2,
                cy,
                stemWidth,
                radius * 2.5,
                16,
                16
            );

            // Petal
            ctx.setFill(color);
            fillPetals();
            ctx.setFill(centerColor);
            fillCenteredCircle(radius / 2, cx, cy, centerColor);
        }

        void fillPetals() {
            fillCenteredCircle(radius, cx - radius / 2, cy, color);
            fillCenteredCircle(radius, cx, cy - radius / 2, color);
            fillCenteredCircle(radius, cx + radius / 2, cy, color);
            fillCenteredCircle(radius, cx, cy + radius / 2, color);
        }
    }

    class BirdCat {
        final double cx;

        BirdCat(double cx) {
            this.cx = cx;
        }

        void anim1_MemeTitle() {
            setDrawingText("extrabold", 24, VPos.CENTER);
            ctx.setFill(Color.web("#5d4037"));
            ctx.fillText(
                "Scientists: The bird cat doesn't exist".toUpperCase(),
                cx,
                100
            );
            ctx.fillText("The Bird Cat:".toUpperCase(), cx, 130);
        }
    }

    record Tuple<T>(T t1, T t2) {}

    static class FadeAnimLoop extends AnimationTimer {
        enum TimeMode { Absolute, Relative }

        interface Animable {
            void draw(
                int numAnim,
                double opacity,
                Cycle.State state,
                int tickCount,
                double cycleTime
            );
        }

        final Animable animable;
        final long targetFPS;
        final TimeMode timeMode;
        final long targetFrameTime;
        final double fadeDuration;
        final double fadeInStartTime;
        final Cycle cycle;
        long lastUpdate;
        int count;
        double opacity;

        FadeAnimLoop(
            Animable animable,
            int targetFPS,
            double cycleDuration,
            TimeMode timeMode
        ) {
            this.animable = animable;
            this.targetFPS = targetFPS;
            this.targetFrameTime = 1_000_000_000 / targetFPS;
            this.timeMode = timeMode;
            this.fadeDuration = cycleDuration / 4.0;
            this.fadeInStartTime = cycleDuration - fadeDuration;
            this.cycle = new Cycle(cycleDuration, fadeDuration);
            this.lastUpdate = 0;
            this.count = 0;
            this.opacity = 1;
        }

        double getOpacity() {
            return switch (cycle.state) {
                case FadingIn -> cycle.time / fadeDuration;
                case Steady -> 1.0;
                case FadingOut ->
                    1.0 - (cycle.time - fadeInStartTime) / fadeDuration;
            };
        }

        @Override
        public void handle(long now) {
            if (lastUpdate == 0) {
                lastUpdate = now;
                cycle.reset();
                return;
            }

            var deltaTime = switch (timeMode) {
                case Absolute -> now - lastUpdate;
                case Relative -> targetFrameTime;
            };

            updateCanvas(deltaTime / 1_000_000_000.0D);
            animable.draw(cycle.count, opacity, cycle.state, count, cycle.time);

            sleep(deltaTime);

            lastUpdate = now;
        }

        void updateCanvas(double deltaTime) {
            cycle.update(deltaTime);
            count++;
            opacity = getOpacity();
        }

        void sleep(long deltaTime) {
            var minimumRestTime = 20L;
            var remainingFrameTime = targetFrameTime - deltaTime;
            var sleepTime = switch (timeMode) {
                case Absolute -> remainingFrameTime > 0 ?
                                 remainingFrameTime / 1_000_000L :
                                 minimumRestTime;
                case Relative -> minimumRestTime;
            };

            // Case 1: It's game loop (absolute time) so it can have time left
            // to sleep for the next tick or it ran out of time already.
            // The animation has to run as synced as possible (e.g. 60FPS).

            // Case 2: It's simulation (relative time), sleep a minimum value
            // to let the CPU rest. The animation has to run as fast as
            // possible.
            try {
                Thread.sleep(sleepTime);
            }
            catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    static class Cycle {
        enum State { FadingIn, Steady, FadingOut }

        final double cycleDuration;
        final double fadeDuration;
        State state = State.FadingIn;
        double time = 0;
        int count = 1;

        Cycle(double cycleDuration, double fadeDuration) {
            if (cycleDuration <= fadeDuration * 2) {
                throw new RuntimeException("Invalid cycle duration");
            }
            this.cycleDuration = cycleDuration;
            this.fadeDuration = fadeDuration;
        }

        void reset() {
            state = State.FadingIn;
            time = 0;
            count = 1;
        }

        void update(double delta) {
            var newTime = time + delta;
            time = newTime;

            if (newTime > cycleDuration) {
                // TODO diff can be greater than cycleDuration
                time = newTime - cycleDuration;
                count++;
            }

            if (time < fadeDuration) {
                state = State.FadingIn;
            }
            else if (time > cycleDuration - fadeDuration) {
                state = State.FadingOut;
            }
            else {
                state = State.Steady;
            }
        }
    }

    static class Recorder {
        static final String RECORDING_DIR = "recording";
        final Canvas canvas;
        final int targetFps;
        final List<Thread> threads;

        Recorder(Canvas canvas, int targetFps) {
            this.canvas = canvas;
            this.targetFps = targetFps;
            this.threads = new ArrayList<>();
        }

        void saveSnapshot(int i) throws IOException {
            saveSnapshot(i, RECORDING_DIR);
        }

        void saveSnapshot(int i, String dir) throws IOException {
            var shot = canvas.snapshot(null, null);
            var file = Paths
                .get("out", dir, "screenshot-" + i + ".png")
                .toFile();
            var par = file.getParentFile();

            if (i <= 1) {
                deleteDirRecursive(par);
            }

            if (!par.exists()) {
                par.mkdirs();
            }

            var t = Thread.startVirtualThread(() -> {
                try {
                    ImageIO.write(
                        SwingFXUtils.fromFXImage(shot, null),
                        "png",
                        file
                    );
                    System.out.println("Screenshot saved to: " + file.getAbsolutePath());
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            });

            threads.add(t);
        }

        void compileVideo() throws IOException, InterruptedException {
            for (var t : threads) {
                t.join();
            }

            var outputPath = Path.of("out/recording.mp4");

            if (Files.exists(outputPath)) {
                Files.delete(outputPath);
            }
            var builder = new ProcessBuilder(
                "out/ffmpeg",
                "-framerate",
                String.valueOf(targetFps),
                "-i",
                "out/recording/screenshot-%d.png",
                "-c:v",
                "libx264",
                "-pix_fmt",
                "yuv420p",
                outputPath
                    .toAbsolutePath()
                    .toString()
               ).redirectErrorStream(true)
                .start();

            try (
                var r = new BufferedReader(
                    new InputStreamReader(
                        builder.getInputStream(),
                        Charset.defaultCharset()
                    )
                )
            ) {
                String line;

                while ((line = r.readLine()) != null) {
                    System.out.println(line);
                }
                var exitCode = builder.waitFor();

                System.out.println("Command exited with code " + exitCode);
            }
        }

        static void deleteDirRecursive(File dir) throws IOException {
            if (!dir.exists()) {
                return;
            }
            Files.walkFileTree(dir.toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult postVisitDirectory(
                    Path dir,
                    IOException exc
                ) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(
                    Path file,
                    BasicFileAttributes attrs
                ) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }
}
