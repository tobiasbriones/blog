import arrow.core.Either
import arrow.core.left
import arrow.core.right
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.file.Path

fun runCommand(
    command: String, workingDir: Path? = null
): Either<String, String> {
    val processBuilder =
        ProcessBuilder(*command.split("\\s+".toRegex()).toTypedArray())

    processBuilder.redirectErrorStream(true)

    if (workingDir != null) {
        processBuilder.directory(workingDir.toFile())
    }

    return try {
        val process = processBuilder.start()
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val output = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            output.append(line).append("\n")
        }

        val exitCode = process.waitFor()
        if (exitCode == 0) {
            output.toString().right()
        } else {
            "Error: Command failed with exit code $exitCode".left()
        }
    } catch (e: Exception) {
        "Error: ${e.message}".left()
    }
}
