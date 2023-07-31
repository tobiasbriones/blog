package fs

import arrow.core.Either
import arrow.core.Either.*
import arrow.core.None
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.stream.Stream
import kotlin.io.path.name

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

fun copyDirectory(sourceDir: Path, targetDir: Path): Either<String, None> {
    val copyFileFromWalk: (Stream<Path>) -> Right<None> = { stream ->
        stream.forEach { sourcePath ->
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
        Right(None)
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
