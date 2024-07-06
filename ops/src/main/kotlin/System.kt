import arrow.core.Either
import arrow.core.left
import arrow.core.right
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.file.Path
import java.util.*

fun runCommand(
    command: String,
    workingDir: Path? = null
): Either<String, String> {
    val processBuilder = getProcessBuilder(command)

    processBuilder.redirectErrorStream(false)

    if (workingDir != null) {
        processBuilder.directory(workingDir.toFile())
    }

    return try {
        val process = processBuilder.start()
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val stdErrReader = BufferedReader(InputStreamReader(process.errorStream))
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
            output.toString().right()
        } else {
            "Command failed with exit code $exitCode, and error message $stdErr"
                .left()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        e.message.orEmpty().left()
    }
}

fun getProcessBuilder(command: String): ProcessBuilder {
    val os = System.getProperty("os.name").lowercase(Locale.getDefault())
    return if (os.contains("win")) ProcessBuilder(
        *"cmd /c $command"
            .split("\\s+".toRegex())
            .toTypedArray()
    )
    else ProcessBuilder("sh", "-c", command)
}
