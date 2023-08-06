import arrow.core.Either
import arrow.core.None
import java.nio.file.Path

data class BuildConfig(
    val outDir: Path
)

data class DeployConfig(
    val buildConfig: BuildConfig,
    val deployDir: Path,
)

enum class Op {
    Entries,
    Build,
    Deploy,
}

fun newOp(value: String): Either<None, Op> = when (value.lowercase()) {
    "entries" -> Either.Right(Op.Entries)
    "build" -> Either.Right(Op.Build)
    "deploy" -> Either.Right(Op.Deploy)
    else -> Either.Left(None)
}
