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
    Notice,
}

fun newOp(value: String): Either<None, Cmd> = try {
    Cmd
        .valueOf(value.replaceFirstChar {
            if (it.isLowerCase())
                it.titlecase(Locale.getDefault())
            else it.toString()
        })
        .right()
} catch (e: IllegalArgumentException) {
    Left(None)
}
