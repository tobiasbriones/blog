package engineer.mathsoftware.canvasfx.drawing

import engineer.mathsoftware.canvasfx.remToPx
import engineer.mathsoftware.canvasfx.resPath
import javafx.beans.property.SimpleObjectProperty
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.SnapshotParameters
import javafx.scene.control.Label
import javafx.scene.effect.BoxBlur
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
val backdropBlurBackground = BackdropBlurBackground()

fun coverPr(): Pane = StackPane().apply {// root background
    loadFonts()

    prefWidth = 1920.0
    prefHeight = 1080.0
    background = imageBackground("data/bg.png")
    backdropBlurBackground.addSource(this)

    children.add(StackPane().apply { // MSWE background
        prefWidth = 1920.0
        prefHeight = 1080.0
        background = imageBackground("mswe-radial.png")
        backdropBlurBackground.addSource(this)

        children.addAll(VBox().apply { // content
            prefWidth = 1920.0
            prefHeight = 1080.0
            padding = Insets(toPx(1.0), toPx(3.0), toPx(1.0), toPx(3.0))
            alignment = Pos.CENTER

            children.addAll(
                HBox().apply {
                    children.addAll(
                        profilePhoto(),
                        createCommentBox()
                    )
                }
            )
        })
    })
}

fun profilePhoto(): StackPane = StackPane().apply {
    prefWidth = toPx(10.0)
    prefHeight = toPx(10.0)
    maxWidth = toPx(10.0)
    maxHeight = toPx(10.0)
    padding = Insets(toPx(0.5))
    background = imageBackground("mswe.png")

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
}

fun createCommentBox(): StackPane {
    val commentBox = commentBox()

    HBox.setMargin(
        commentBox,
        Insets(0.0, 0.0, 0.0, toPx(2.0))
    )
    HBox.setHgrow(commentBox, Priority.ALWAYS)
    return commentBox
}

fun commentBox(): StackPane = StackPane().apply {
    val borderRadiusRem = 1.0
    val borderWidthRem = 0.25

    style = """
        -fx-border-radius: ${toPx(borderRadiusRem)};
        -fx-border-width: ${toPx(borderWidthRem)};
        -fx-border-color: $accentColorHex;
    """.trimIndent()

    children.addAll(
        StackPane().apply {
            backdropBlurBackground.parentBorderRadiusRem = borderRadiusRem
            backdropBlurBackground.parentBorderWidthRem = borderWidthRem
            backdropBlurBackground.destination = this
        },
        commentBoxContent(),
    )
}

fun commentBoxContent(): VBox = VBox().apply {
    val abstract = "It integrates the Cookie Consent v0.2.0 into the banner" +
      " and customization pane:"
    val items = listOf("Item 1", "Item 2", "Item 3")

    children.addAll(
        heading(text = "texsydo/@MVP"),
        VBox().apply {// .comment-content
            padding = padding(2.0, 3.0)
            spacing = toPx(1.0)
            children.addAll(
                heading(text = "CanvasFX", small = true),
                Label(abstract).apply {
                    isWrapText = true
                    style = textCss(1.5)
                },
                VBox().apply {
                    children.addAll(
                        items
                            .map {
                                HBox().apply {
                                    spacing = toPx(0.75)
                                    alignment = Pos.CENTER_LEFT
                                    children.addAll(
                                        Circle().apply {
                                            radius = toPx(0.25)
                                            fill = Color.WHITE
                                        },
                                        Label(it).apply {
                                            style = textCss(1.5)
                                        },
                                    )
                                }
                            })
                }
            )
        }
    )
}

fun heading(text: String, small: Boolean = false): HBox = HBox().apply {
    val pxRem = if (small) 1.5 else 3.0

    padding = padding(1.0, pxRem)
    style = """
        -fx-background-color: #263238;
        -fx-background-radius: ${toPx(0.75)} ${toPx(0.75)} 0 0;
        -fx-border-width: 0 0 ${toPx(0.25)} 0;
        -fx-border-radius: ${toPx(0.75)} ${toPx(0.75)} 0 0;
        -fx-border-color: $accentColorBlueHex;
    """.trimIndent()
    children.add(
        Text(text).apply {
            style = boldTextCss(2.0)
        }
    )
}

fun padding(pyRem: Double, pxRem: Double): Insets =
    Insets(toPx(pyRem), toPx(pxRem), toPx(pyRem), toPx(pxRem))

fun imageBackground(src: String): Background = Background(
    BackgroundImage(
        Image(resPath(src)),
        BackgroundRepeat.NO_REPEAT,
        BackgroundRepeat.NO_REPEAT,
        BackgroundPosition.CENTER,
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

class BackdropBlurBackground {
    var destination: Pane? = null
        set(value) {
            field = value

            field
                ?.backgroundProperty()
                ?.bind(backgroundProperty)
            field
                ?.widthProperty()
                ?.addListener { _ -> updateBackground() }
            field
                ?.heightProperty()
                ?.addListener { _ -> updateBackground() }
        }

    var parentBorderRadiusRem: Double = 0.0

    var parentBorderWidthRem: Double = 0.0

    private val backgroundProperty = SimpleObjectProperty<Background>()
    private val backgrounds = mutableListOf<Image?>()

    fun addSource(pane: Pane) {
        val idx = backgrounds.size

        backgrounds.add(null)
        pane
            .widthProperty()
            .addListener { _ -> updateSource(pane, idx) }
        pane
            .heightProperty()
            .addListener { _ -> updateSource(pane, idx) }
        pane
            .backgroundProperty()
            .addListener { _ -> updateSource(pane, idx) }
    }

    private fun updateSource(pane: Pane, idx: Int) {
        if (!(pane.width > 0 && pane.height > 0 && pane.background != null)) {
            return
        }
        backgrounds[idx] = pane.background.images.firstOrNull()?.image

        updateBackground()
    }

    private fun updateBackground() {
        val dst = destination

        if (
          dst == null
          || backgrounds.size == 0
          || backgrounds.contains(null)
        ) {
            return
        }

        val sceneBounds = dst.localToScene(dst.boundsInLocal)

        if (
          sceneBounds.minX < 0.0
          || sceneBounds.minY < 0.0
          || sceneBounds.width <= 0.0
          || sceneBounds.height <= 0.0
        ) {
            return
        }

        val clipRect = Rectangle(
            sceneBounds.minX + toPx(parentBorderWidthRem),
            sceneBounds.minY + toPx(parentBorderWidthRem),
            sceneBounds.width,
            sceneBounds.height,
        )
            .apply {
                arcWidth =
                    toPx(parentBorderRadiusRem - parentBorderWidthRem) * 2
                arcHeight =
                    toPx(parentBorderRadiusRem - parentBorderWidthRem) * 2
            }

        val maxWidth = backgrounds.maxOf { it?.width ?: 0.0 }
        val maxHeight = backgrounds.maxOf { it?.height ?: 0.0 }
        val group = Group().apply {
            children.addAll(backgrounds.map { ImageView(it) })
            children.add(
                Rectangle().apply {
                    width = maxWidth
                    height = maxHeight
                    fill = Color.web("#333")
                    opacity = 0.9
                },
            )
            clip = clipRect
//            effect = GaussianBlur(toPx(1.25))
            effect = BoxBlur(toPx(0.25), toPx(0.25), 3)
        }

        val writableImage = group.snapshot(
            SnapshotParameters().apply {
                fill = Color.TRANSPARENT
            },
            null
        )

        backgroundProperty
            .set(
                Background(
                    BackgroundImage(
                        writableImage,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
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
            )
    }
}

fun textCss(sizeRem: Double = 1.0): String = """
    |-fx-font-family: "Poppins Medium", sans-serif;
    |-fx-fill: white;
    |-fx-text-fill: white;
    |-fx-font-size: ${toPx(sizeRem)};
    |-fx-font-smoothing-type: gray;
""".trimMargin()

fun boldTextCss(sizeRem: Double = 1.0): String = """
    |-fx-font-family: "Poppins Bold", sans-serif;
    |-fx-fill: white;
    |-fx-text-fill: white;
    |-fx-font-size: ${toPx(sizeRem)};
    |-fx-font-smoothing-type: gray;
""".trimMargin()

fun loadFonts() {
    Font.loadFont(
        resPath("fonts/Poppins/Poppins-Medium.ttf"),
        fontSizePx
    )
    Font.loadFont(
        resPath("fonts/Poppins/Poppins-Bold.ttf"),
        fontSizePx
    )
}
