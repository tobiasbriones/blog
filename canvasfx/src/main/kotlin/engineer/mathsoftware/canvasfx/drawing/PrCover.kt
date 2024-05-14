package engineer.mathsoftware.canvasfx.drawing

import engineer.mathsoftware.canvasfx.absUri
import engineer.mathsoftware.canvasfx.printError
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
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.RadialGradient
import javafx.scene.paint.Stop
import javafx.scene.shape.*
import javafx.scene.text.Font
import kotlin.math.pow
import kotlin.math.sqrt

val fontSizePx = 32.0
val toPx: (Double) -> Double = remToPx(fontSizePx)
val accentColorBlueHex = "#1976D2"
val accentColorHex = "#455A64"
val backgroundColorHex = "#333"
val accentColorBlue = Color.web("#1976D2")
val accentColor = Color.web("#455A64")
val backgroundColor = Color.web("#333")
val backdropBlurBackground = BackdropBlurBackground()
val gradientCenterColor = Color.web("#9ac5db")

data class PrCover(
    val commentBox: CommentBox,
    val bgSrc: String,
    val bgColor: Color,
    val profilePhotoSrc: String,
)

fun extractPrCover(parameters: Map<String, String>): PrCover? {
    val bgSrc = parameters["bg"]
    val profilePhotoSrc = parameters["profile-photo"]
    val heading = parameters["heading"]
    val abstract = parameters["abstract"]
    val footer = parameters["footer"]
        ?.split(",")
        ?.map { it.trim() }
        ?: listOf()
    val subheading = parameters["subheading"]
    val subdomainSrc = parameters["subdomain"]
    val bgColor = parameters["bg-color"] ?: "white"

    if (bgSrc == null || profilePhotoSrc == null || heading == null || abstract == null) {
        printError("Missing required parameters.")
        return null
    }

    return PrCover(
        bgSrc = absUri(bgSrc),
        bgColor = Color.web(bgColor),
        profilePhotoSrc = absUri(profilePhotoSrc),
        commentBox = CommentBox(
            heading = heading,
            abstract = abstract,
            footer = footer,
            subheading = subheading,
            subdomainSrc = subdomainSrc?.let { absUri(it) }
        )
    )
}

data class CommentBox(
    val heading: String,
    val abstract: String,
    val footer: List<String> = listOf(),
    val subheading: String? = null,
    val subdomainSrc: String? = null,
)

data class Release(val version: String, val subVersion: String?)

fun prCover(
    prCover: PrCover,
    release: Release? = null,
): Pane = StackPane().apply {
    loadFonts()

    val (commentBox, bgSrc, bgColor, profilePhotoSrc) = prCover
    val coverWidth = widthProperty()
    val coverHeight = heightProperty()
    prefWidth = 1920.0
    minWidth = 1920.0
    prefHeight = 1080.0
    minHeight = 1080.0

    children.addAll(
        StackPane().apply {
            background = createMsweRadialBackground(bgColor)

            backdropBlurBackground.addSource(this)
        },
        StackPane().apply {
            val updateClip = {
                // Clip the MSWE logo by removing the center circle to the BG
                clip = Shape.subtract(
                    Rectangle().apply {
                        widthProperty().bind(coverWidth)
                        heightProperty().bind(coverHeight)
                    },
                    Circle().apply {
                        radiusProperty().bind(coverHeight.divide(2))
                        centerXProperty().bind(coverWidth.divide(2))
                        centerYProperty().bind(coverHeight.divide(2))
                    }
                )
            }

            background = imageBackground(bgSrc)
            backdropBlurBackground.addSource(this)

            widthProperty().addListener { _ -> updateClip() }
            heightProperty().addListener { _ -> updateClip() }
        },
        VBox().apply { // content
            prefWidth = 1920.0
            prefHeight = 1080.0
            padding = Insets(toPx(1.0), toPx(3.0), toPx(1.0), toPx(3.0))
            alignment = Pos.CENTER

            children.addAll(
                HBox().apply {
                    children.addAll(
                        profilePhoto(profilePhotoSrc, bg = release == null),
                        createCommentBox(commentBox, release)
                    )
                }
            )
        })
}

// Release cover is a PR cover extension
data class ReleaseCover(
    val prCover: PrCover,
    val release: Release,
)

fun extractReleaseCover(parameters: Map<String, String>): ReleaseCover? {
    val prCover = extractPrCover(parameters) ?: return null
    val version = parameters["version"]
    val subversion = parameters["subversion"]

    if (version == null) {
        printError("Missing required parameters.")
        return null
    }
    return ReleaseCover(prCover, Release(version, subversion))
}

fun releaseCover(releaseCover: ReleaseCover): Pane = with(releaseCover) {
    prCover(prCover, release)
}

fun profilePhoto(profileSrc: String, bg: Boolean = true): StackPane =
    StackPane().apply {
        prefWidth = toPx(10.0)
        prefHeight = toPx(10.0)
        maxWidth = toPx(10.0)
        maxHeight = toPx(10.0)
        padding = Insets(toPx(0.5))

        if (bg) {
            background = localImageBackground("mswe.png")
        }

        children.addAll(
            Circle().apply {
                radius = toPx(4.5)
                fill = Color.web("#212121")
            },
            ImageView().apply {
                fitWidth = toPx(8.75)
                fitHeight = toPx(8.75)
                image = Image(profileSrc)
                clip = Rectangle(fitWidth, fitHeight).apply {
                    arcWidth = toPx(fitWidth / 2)
                    arcHeight = toPx(fitHeight / 2)
                }
            }
        )
    }

fun createCommentBox(commentBox: CommentBox, release: Release?): StackPane {
    val commentBoxPane = commentBox.commentBox(release)

    HBox.setMargin(
        commentBoxPane,
        Insets(0.0, 0.0, 0.0, toPx(2.0))
    )
    HBox.setHgrow(commentBoxPane, Priority.ALWAYS)
    return commentBoxPane
}

fun CommentBox.commentBox(release: Release?): StackPane = StackPane().apply {
    val borderRadiusRem = 1.0
    val borderWidthRem = 0.25
    val w = widthProperty()
    val h = heightProperty()

    children.addAll(
        StackPane().apply {
            padding = padding(borderWidthRem, borderWidthRem)
            children.addAll(
                StackPane().apply {
                    backdropBlurBackground.parentBorderRadiusRem =
                        borderRadiusRem - 0.125 // border will be overridden
                    backdropBlurBackground.parentBorderWidthRem = borderWidthRem
                    backdropBlurBackground.destination = this
                },
                commentBoxContent(
                    heading,
                    abstract,
                    footer,
                    subheading,
                    subdomainSrc,
                    release,
                ),
            )
        },
        Rectangle().apply {
            widthProperty().bind(w)
            heightProperty().bind(h)
            fill = Color.TRANSPARENT
            arcWidth = toPx(borderRadiusRem) * 2
            arcHeight = toPx(borderRadiusRem) * 2
            stroke = accentColor
            strokeWidth = toPx(borderWidthRem)
            strokeType = StrokeType.INSIDE
        },
        StackPane().apply {
            padding = padding(borderWidthRem, borderWidthRem)

            children.addAll(
                Group().apply {// triangle
                    val triangleWidth = toPx(1.25)
                    val triangleHeight = 0.5 * sqrt(3.0) * triangleWidth
                    val borderSize = toPx(0.25)
                    val bgColor = Color.web("#263238")

                    alignment = Pos.TOP_LEFT
                    translateX = -triangleWidth - triangleHeight / 2
                    translateY = toPx(5.0) / 2 - triangleWidth / 2
                    rotate = -90.0

                    children.addAll(
                        Polygon().apply {
                            fill = accentColor
                            points.addAll(
                                0.0,
                                0.0,
                                -triangleWidth,
                                triangleHeight,
                                triangleWidth,
                                triangleHeight
                            )
                        },
                        Polygon().apply {
                            val innerWidth =
                                triangleWidth - 2 * sqrt(3.0) * borderSize +
                                  2 * borderSize / sqrt(3.0)
                            val innerHeight = triangleHeight - 3 * borderSize

                            fill = bgColor
                            points.addAll(
                                0.0,
                                triangleHeight - borderSize - innerHeight,
                                -innerWidth,
                                triangleHeight + 1,
                                innerWidth,
                                triangleHeight + 1
                            )
                        },
                    )
                },
            )
        },
    )
}

fun commentBoxContent(
    heading: String,
    abstract: String,
    items: List<String>,
    subheading: String?,
    subdomainSrc: String?,
    release: Release?,
): VBox = VBox().apply {
    children.addAll(
        heading(
            text = heading,
            version = release?.version,
            celeb = release?.subVersion == null,
        ),
        StackPane().apply {
            children.addAll(
                VBox().apply {// .comment-content
                    padding = padding(2.0, 3.0)
                    spacing = toPx(1.0)

                    subheading?.let {
                        children.add(
                            heading(
                                text = subheading,
                                small = true,
                                version = release?.subVersion,
                                celeb = true,
                            )
                        )
                    }

                    children.addAll(
                        abstract
                            .parseMd(textCss(1.5), boldTextCss(1.5))
                            .apply {
                                // Add padding if there are no items below
                                // to avoid collision with the right-bottom logo
                                if (items.isEmpty()) {
                                    padding = Insets(0.0, toPx(3.0), 0.0, 0.0)
                                }
                            },
                        VBox().apply {
                            padding = Insets(0.0, toPx(4.0), 0.0, 0.0)

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
                                                // TODO 42 chars max = one line
                                                it.parseMd(
                                                    textCss(1.5),
                                                    boldTextCss(1.5)
                                                ),
                                            )
                                        }
                                    })
                        }
                    )
                },

                StackPane().apply {// subdomain
                    alignment = Pos.BOTTOM_RIGHT

                    children.add(
                        ImageView().apply {
                            fitWidth = toPx(7.0)
                            fitHeight = toPx(7.0)
                            subdomainSrc?.let {
                                image = Image(subdomainSrc)
                            }
                            opacity = 0.6
                            clip = Rectangle().apply {
                                width = fitWidth
                                height = fitWidth
                                arcWidth = toPx(1.5)
                                arcHeight = toPx(1.5)
                            }
                        }
                    )
                }
            )
        }
    )
}

fun heading(
    text: String,
    small: Boolean = false,
    version: String? = null,
    celeb: Boolean? = false,
): StackPane =
    StackPane().apply {
        prefWidth = toPx(5.0)
        prefHeight = toPx(5.0)

        children.add(
            HBox().apply {
                val pxRem = if (small) 1.5 else 3.0
                val pyRem = 1.0

                padding = padding(pyRem, pxRem)
                style = """
                    -fx-background-color: #263238;
                    -fx-background-radius: ${toPx(0.75)} ${toPx(0.75)} 0 0;
                    -fx-border-width: 0 0 ${toPx(0.25)} 0;
                    -fx-border-radius: ${toPx(0.75)} ${toPx(0.75)} 0 0;
                    -fx-border-color: $accentColorBlueHex;
                """.trimIndent()
                children.add(
                    Label(text).apply {
                        style = boldTextCss(2.0)
                    }
                )

                version?.let {
                    children.add(
                        version(version, celeb ?: true)
                    )
                }
            }
        )
    }

fun version(version: String, celeb: Boolean) = HBox().apply {
    alignment = Pos.CENTER_RIGHT
    spacing = toPx(0.5)

    HBox.setHgrow(this, Priority.ALWAYS)
    if (celeb) {
        children.add(
            ImageView().apply {
                fitWidth = toPx(3.0)
                fitHeight = toPx(3.0)
                image =
                    Image(resPath("data/celebration.png"))
            }
        )
    }
    children.add(
        Label("v$version").apply {
            style = boldTextCss(2.0)
        }
    )
}

fun padding(pyRem: Double, pxRem: Double): Insets =
    Insets(toPx(pyRem), toPx(pxRem), toPx(pyRem), toPx(pxRem))

fun imageBackground(src: String): Background = Background(
    BackgroundImage(
        Image(src),
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

fun localImageBackground(src: String): Background =
    imageBackground(resPath(src))


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

fun createMsweRadialBackground(bgColor: Color): Background {
    val bgWidth = 1920.0
    val bgHeight = 1080.0
    val gradientRadius = sqrt((bgWidth.pow(2.0) + bgHeight.pow(2.0)) / 4)
    val group = Group().apply {
        children.addAll(
            Rectangle().apply {
                width = bgWidth
                height = bgHeight
                fill = bgColor
            },
            ImageView().apply {
                fitWidth = bgHeight
                fitHeight = bgHeight
                x = (bgWidth - bgHeight) / 2
                image = Image(resPath("mswe-1080.png"))
            },
            Circle().apply {
                radius = gradientRadius
                fill = RadialGradient(
                    0.0,
                    0.0,
                    0.5,
                    0.5,
                    1.0,
                    true,
                    CycleMethod.NO_CYCLE,
                    Stop(0.0, gradientCenterColor),
                    Stop(0.5, Color.TRANSPARENT)
                )
                clip = Rectangle(bgWidth, bgHeight)
                opacity = 0.4
                centerX = bgWidth / 2
                centerY = bgHeight / 2
            }
        )
    }

    val writableImage = group.snapshot(
        SnapshotParameters().apply {
            fill = Color.TRANSPARENT
        },
        null
    )

    return Background(
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
    System.setProperty("prism.lcdtext", "false")

    Font.loadFont(
        resPath("fonts/Poppins/Poppins-Medium.ttf"),
        fontSizePx
    )
    Font.loadFont(
        resPath("fonts/Poppins/Poppins-Bold.ttf"),
        fontSizePx
    )
}
