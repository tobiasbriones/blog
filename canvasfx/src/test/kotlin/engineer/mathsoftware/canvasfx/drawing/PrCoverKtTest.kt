package engineer.mathsoftware.canvasfx.drawing

import engineer.mathsoftware.canvasfx.resPath
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.file.Path


class CoverPrKtTest : Application() {
    override fun start(primaryStage: Stage) {
        val drawing = prCover(
            PrCover(
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

        primaryStage.title = "Texsydo FX: PR Cover"
        primaryStage.scene = scene
        primaryStage.setMaximized(true)
        primaryStage.show()
    }
}

fun main() {
//    Application.launch(CoverPrKtTest::class.java)
    val preview =
        """app pr-cover preview --bg=bg.png --profile-photo=profile.jpeg --heading=texsydo/@MVP --abstract="Powering **Textual System Documenting** with the new **Texsydo** MVP." --footer="Integrates PR command.,Develops CoverPr FX.,Deploys CanvasFX." --subheading="Web (SWAM GX)" --subdomain=subdomain.png"""
    val command =
        """app pr-cover --bg=bg.png --profile-photo=profile.jpeg --heading=texsydo/@MVP --abstract="Powering **Textual System Documenting** with the new **Texsydo** MVP." --footer="Integrates PR command.,Develops CoverPr FX.,Deploys CanvasFX." --subheading="Web (SWAM GX)" --subdomain=subdomain.png"""

//    val o = runCommand(preview)
    val o = runCommand(command)
    println(o)
}


fun runCommand(
    command: String,
    workingDir: Path? = null,
): String {
    val winCommand = "cmd /c $command"
    val processBuilder = ProcessBuilder(
        *winCommand
            .split("\\s+".toRegex())
            .toTypedArray()
    )

    processBuilder.redirectErrorStream(false)

    if (workingDir != null) {
        processBuilder.directory(workingDir.toFile())
    }

    return try {
        val process = processBuilder.start()
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val stdErrReader =
            BufferedReader(InputStreamReader(process.errorStream))
        val output = StringBuilder()
        val stdErr = StringBuilder()
        var line: String?

        // Capture standard output
        while (reader.readLine().also { line = it } != null) {
            output.append(line).append("\n")
        }

        // Capture standard error
        while (stdErrReader.readLine().also { line = it } != null) {
            stdErr.append(line).append("\n")
        }
        val exitCode = process.waitFor()
        if (exitCode == 0) {
            println("code 0")
            output.toString()
        }
        else {
            "Command failed with exit code $exitCode, and error message $stdErr"
        }
    }
    catch (e: Exception) {
        e.printStackTrace()
        e.message.orEmpty()
    }
}
