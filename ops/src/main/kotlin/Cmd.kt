import arrow.core.Either
import arrow.core.None
import java.nio.file.Path

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
}

fun newOp(value: String): Either<None, Cmd> = when (value.lowercase()) {
    "entries" -> Either.Right(Cmd.Entries)
    "build" -> Either.Right(Cmd.Build)
    "deploy" -> Either.Right(Cmd.Deploy)
    else -> Either.Left(None)
}
