package engineer.mathsoftware.canvasfx.drawing

import engineer.mathsoftware.canvasfx.resPath
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.VBox
import javafx.stage.Stage


class CoverPrKtTest : Application() {
    override fun start(primaryStage: Stage) {
        val drawing = coverPr(
            CoverPr(
                bgSrc = resPath("data/bg.png"),
                profilePhotoSrc = resPath("data/profile.jpeg"),
                commentBox = CommentBox(
                    heading = "mathswe/@.com",
                    abstract = "It integrates the **Cookie Consent** v0.2.0 " +
                      "into the banner and customization pane:",
                    footer = listOf(
                        "Requests to process consents.",
                        "Performs refactorizations.",
                        "**@ui:** Displays effective consent details."
                    ),
                    subheading = "/legal",
                    subdomainSrc = resPath("data/subdomain.png")
                )
            )
        )

        val scene = Scene(VBox(drawing))

        primaryStage.title = "Canvas FX: Cover PR"
        primaryStage.scene = scene
        primaryStage.setMaximized(true);
        primaryStage.show()
    }
}

fun main() {
    Application.launch(CoverPrKtTest::class.java)
}
