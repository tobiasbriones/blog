package engineer.mathsoftware.canvasfx

import engineer.mathsoftware.canvasfx.drawing.coverPr
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
import javax.imageio.ImageIO


class CanvasFx : Application() {
    override fun start(primaryStage: Stage) {
        val drawing = coverPr()
        val preview = false
        val scene = when (preview) {
            true  -> Scene(VBox(drawing))
            false -> Scene(drawing, drawing.prefWidth, drawing.prefHeight)
        }

        primaryStage.title = "Canvas FX"
        primaryStage.scene = scene

        if (preview) {
            primaryStage.show()
        }
        else {
            takeSnapshot(drawing)
            Platform.exit()
        }
    }

    private fun takeSnapshot(root: Pane) {
        val outputFile = File("snapshot.png")
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
        }
        catch (e: IOException) {
            println(e)
        }
    }
}


fun remToPx(fontSizePx: Double): (Double) -> Double =
    { rem -> rem * fontSizePx }

fun resPath(path: String): String = CanvasFx::class
    .java
    .classLoader
    .getResource("cover-pr/$path")
    ?.toURI()
    .let { it.toString() }

fun main() {
    Application.launch(CanvasFx::class.java)
}
