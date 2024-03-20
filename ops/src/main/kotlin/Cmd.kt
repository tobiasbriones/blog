import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.None
import arrow.core.right
import java.nio.file.Path
import java.util.*

data class BuildConfig(
    val srcDir: Path,
    val outDir: Path,
)

data class DeployConfig(
    val buildConfig: BuildConfig,
    val deployDir: Path,
)

enum class Cmd {
    Entries,
    Build,
    Deploy,
    Serve,
    Create,
    AddPr,
    Notice,
}

fun newOp(value: String): Either<None, Cmd> = try {
    value
        .split("-")
        .joinToString(separator = "") { str ->
            str.replaceFirstChar { ch ->
                ch.titlecase(Locale.getDefault())
            }
        }
        .let { Cmd.valueOf(it) }
        .right()
} catch (e: IllegalArgumentException) {
    Left(None)
}
