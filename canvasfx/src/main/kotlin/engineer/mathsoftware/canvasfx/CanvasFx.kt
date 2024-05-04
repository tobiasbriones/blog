package engineer.mathsoftware.canvasfx

import engineer.mathsoftware.canvasfx.drawing.coverPr
import engineer.mathsoftware.canvasfx.drawing.extractPrCover
import javafx.application.Application
import javafx.application.Platform
import javafx.embed.swing.SwingFXUtils
import javafx.scene.Scene
import javafx.scene.image.WritableImage
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.io.File
import java.io.IOException
import java.nio.file.Path
import javax.imageio.ImageIO


class CanvasFx : Application() {
    override fun start(primaryStage: Stage) {
        val cmd = parameters.unnamed.firstOrNull()

        if (cmd == null) {
            printError(
                "No command-line arguments provided: Missing command " +
                  "to render."
            )
            Platform.exit()
            return
        }

        if (parameters.named.isEmpty()) {
            printError(
                "No command-line arguments provided: Missing command args."
            )
            Platform.exit()
            return
        }


        val drawing = when (cmd) {
            "pr-cover" -> prCoverDrawing()
            else       -> {
                printError("Invalid command: $cmd")
                null
            }
        }

        if (drawing == null) {
            Platform.exit()
            return
        }

        val preview = isPreview()
        val scene = when (preview) {
            true  -> Scene(VBox(drawing))
            false -> Scene(drawing, drawing.prefWidth, drawing.prefHeight)
        }

        if (preview) {
            primaryStage.title = "Texsydo FX"
            primaryStage.scene = scene
            primaryStage.isMaximized = true
            primaryStage.show()
            primaryStage.toFront()
        }
        else {
            val output = getOutputPath()

            takeSnapshot(drawing, output)
            Platform.exit()
        }
    }

    private fun isPreview(): Boolean = parameters
        .unnamed
        .contains("preview")

    private fun prCoverDrawing(): Pane? {
        val coverPr = extractPrCover(parameters.named) ?: return null

        println("Rendering $coverPr")
        println()

        return coverPr(coverPr)
    }

    private fun takeSnapshot(root: Pane, output: String) {
        val outputFile = File(output)
        val writableImage = WritableImage(
            root.width.toInt(), root.height.toInt()
        )

        root.snapshot(null, writableImage)

        try {
            ImageIO.write(
                SwingFXUtils.fromFXImage(writableImage, null),
                "png",
                outputFile
            )

            println("Image saved at ${outputFile.toPath().toUri()}")
        }
        catch (e: IOException) {
            printError(e)
        }
    }

    private fun getOutputPath(): String = parameters
        .named["output"]
        ?.let { absPath(it) }
        ?: absPath("cover.png")
}

fun remToPx(fontSizePx: Double): (Double) -> Double =
    { rem -> rem * fontSizePx }

fun resPath(path: String): String = CanvasFx::class
    .java
    .classLoader
    .getResource("cover-pr/$path")
    ?.toURI()
    .let { it.toString() }

fun absPath(inputPath: String): String = with(Path.of(inputPath)) {
    if (isAbsolute) toString()
    else {
        val root = System.getProperty("user.dir")
        Path.of(root, inputPath).toString()
    }
}

fun absUri(inputPath: String): String = with(Path.of(inputPath)) {
    if (isAbsolute) toUri().toString()
    else {
        val root = System.getProperty("user.dir")
        Path.of(root, inputPath).toUri().toString()
    }
}

fun printError(msg: String) {
    System.err.println("Error: $msg")
    println()
}

fun printError(e: Exception) {
    System.err.println("Exception Error:")
    e.printStackTrace()
    println()
}

fun main() {
    Application.launch(CanvasFx::class.java)
}
