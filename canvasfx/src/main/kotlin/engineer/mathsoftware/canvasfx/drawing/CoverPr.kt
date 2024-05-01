package engineer.mathsoftware.canvasfx.drawing

import engineer.mathsoftware.canvasfx.remToPx
import engineer.mathsoftware.canvasfx.resPath
import javafx.geometry.Insets
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import javafx.scene.text.Font
import javafx.scene.text.Text


val fontSizePx = 32.0
val toPx: (Double) -> Double = remToPx(fontSizePx)
val accentColorBlueHex = "#1976D2"
val accentColorHex = "#455A64"
val backgroundColorHex = "#333"
val accentColorBlue = Color.web("#1976D2")
val accentColor = Color.web("#455A64")
val backgroundColor = Color.web("#333")

val font = Font.loadFont(
    resPath("fonts/Poppins/Poppins-Medium.ttf"),
    fontSizePx
)
val fontBold = Font.loadFont(
    resPath("fonts/Poppins/Poppins-Bold.ttf"),
    fontSizePx
)

fun coverPr(): Pane = StackPane().apply {// root background
    stylesheets.add(resPath("style.css"))
    prefWidth = 1920.0
    prefHeight = 1080.0
    background = Background(
        BackgroundImage(
            Image(resPath("data/bg.png")),
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.DEFAULT,
            BackgroundSize.DEFAULT
        )
    )
    children.add(StackPane().apply { // MSWE background
        prefWidth = 1920.0
        prefHeight = 1080.0

        background = Background(
            BackgroundImage(
                Image(resPath("mswe-radial.png")),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT
            )
        )
        children.addAll(HBox().apply { // content
            prefWidth = 1920.0
            prefHeight = 1080.0
            padding = Insets(toPx(1.0), toPx(3.0), toPx(1.0), toPx(3.0))
            children.addAll(
                StackPane().apply {
                    prefWidth = toPx(10.0)
                    prefHeight = toPx(10.0)
                    maxWidth = toPx(10.0)
                    maxHeight = toPx(10.0)
                    padding = Insets(toPx(0.5))
                    background = Background(
                        BackgroundImage(
                            Image(resPath("mswe.png")),
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundPosition.DEFAULT,
                            BackgroundSize(
                                100.0,
                                100.0,
                                true,
                                true,
                                true,
                                false
                            )
                        )
                    )
                    children.addAll(
                        Circle().apply {
                            radius = toPx(4.5)
                            fill = Color.web("#212121")
                        },
                        ImageView().apply {
                            fitWidth = toPx(8.75)
                            fitHeight = toPx(8.75)
                            image = Image(resPath("data/profile.jpeg"))
                            clip = Rectangle(fitWidth, fitHeight).apply {
                                arcWidth = toPx(fitWidth / 2)
                                arcHeight = toPx(fitHeight / 2)
                            }
                        }
                    )
                },
                VBox().apply {// .comment
                    HBox.setMargin(
                        this,
                        Insets(0.0, 0.0, 0.0, toPx(2.0))
                    )
                    style = """
                        -fx-background-radius: ${toPx(1.0)};
                        -fx-border-radius: ${toPx(1.0)};
                        -fx-border-width: ${toPx(0.25)};
                        -fx-border-color: $accentColorHex;
                    """.trimIndent()
                    children.addAll(
                        HBox().apply {// .user-info
                            style = """
                                -fx-background-color: #263238;
                                -fx-border-width: ${toPx(0.25)};
                                -fx-border-radius: ${toPx(0.75)};
                                -fx-border-color: $accentColorBlueHex;
                                -fx-border-top: none;
                            """.trimIndent()
                            padding = Insets(
                                toPx(1.0),
                                toPx(3.0),
                                toPx(1.0),
                                toPx(3.0)
                            )
                            children.add(
                                Text("Title").apply {
                                    setFont(fontBold)
                                    style = "-fx-font-size: ${toPx(2.0)}"
                                }
                            )
                        },
                        Button("asdasda")
                    )
                }
            )
        }
        )
    })
}