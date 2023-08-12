package fs

import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.Either.Right
import arrow.core.None
import arrow.core.left
import arrow.core.right
import java.io.IOError
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.stream.Stream
import kotlin.io.path.isDirectory
import kotlin.io.path.name

fun getRootPath(): Either<String, Path> = try {
    val cwd = Path.of("").toAbsolutePath()

    generateSequence(cwd) { it.parent }
        .firstOrNull { it.resolve(".git").isDirectory() }
        ?.right()
        ?: "$cwd is not a Git project".left()
} catch (e: IOError) {
    e.printStackTrace()
    e.message.orEmpty().left()
}

fun deleteDirectory(directory: Path): Either<String, None> {
    val deleteFilesFromWalk: (Stream<Path>) -> Right<None> = { stream ->
        stream
            .sorted(Comparator.reverseOrder())
            .forEach(Files::delete)
        Right(None)
    }

    if (!Files.isDirectory(directory)) {
        return Left("Provided path is not a directory")
    }
    return try {
        Files.walk(directory).use { deleteFilesFromWalk(it) }
    } catch (e: IOException) {
        e.printStackTrace()
        Left("Fail to delete directory $directory: ${e.message}")
    }
}

fun copyDirectory(
    sourceDir: Path,
    targetDir: Path,
    predicate: (Path) -> Boolean = { true },
): Either<String, Unit> {
    val copyFileFromWalk: (Stream<Path>) -> Right<Unit> = { stream ->
        stream
            .filter(predicate)
            .forEach { sourcePath ->
                val relativePath = sourceDir.relativize(sourcePath)
                val targetPath = targetDir.resolve(relativePath)

                if (Files.isDirectory(sourcePath)) {
                    Files.createDirectories(targetPath)
                } else {
                    Files.copy(
                        sourcePath,
                        targetPath,
                        StandardCopyOption.REPLACE_EXISTING
                    )
                }
            }
        Right(Unit)
    }

    return try {
        Files.walk(sourceDir).use { copyFileFromWalk(it) }
    } catch (e: IOException) {
        e.printStackTrace()
        Left("Fail to copy directory $sourceDir into $targetDir: ${e.message}")
    }
}

fun getFileExtension(path: Path): String {
    val fileName = path.name
    val dotIndex = fileName.lastIndexOf('.')
    return if (dotIndex > 0 && dotIndex < fileName.length - 1) {
        fileName.substring(dotIndex + 1)
    } else {
        ""
    }
}
