package fs

import Entry
import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.right
import md.Index
import md.Markdown
import newEntryFromAbsPath
import path
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Stream

fun Entry.loadEntries(): Either<String, List<Entry>> = walk { stream ->
    stream
        .filter(Files::isDirectory)
        .filter(::filterPath)
        .filter(::isEntryDir)
        .filter(::filterParents)
        .map { newEntryFromAbsPath(path, it) }
        .toList()
}

fun Entry.loadSubdirectories(): Either<String, List<Path>> = try {
    Files
        .list(path)
        .filter(Files::isDirectory)
        .map(Path::getFileName)
        .toList()
        .right()
} catch (e: IOException) {
    e.printStackTrace()
    Left("Fail to list entry subdirectories: ${e.message}")
}

fun Entry.loadIndex(): Either<String, Index> = try {
    Index(
        Markdown(
            Files.readString(Path.of(path.toString(), "index.md"))
        )
    ).right()
} catch (e: IOException) {
    e.printStackTrace()
    Left(e.message.orEmpty())
}

fun <V> Entry.walk(block: (Stream<Path>) -> V): Either<String, V> = try {
    Files
        .walk(path)
        .use(block)
        .right()
} catch (e: IOException) {
    e.printStackTrace()
    Left(e.message.orEmpty())
}


private fun isEntryDir(dir: Path): Boolean = with(
    Path.of(
        dir.toString(), "index" +
                ".md"
    )
) {
    Files.exists(this) && Files.isRegularFile(this)
}

private fun filterPath(path: Path): Boolean =
    filterDirName(path.fileName.toString())


private fun filterDirName(dirName: String): Boolean = with(dirName) {
    !startsWith("_") && !startsWith("out") && !startsWith(".")
}

private fun filterParents(path: Path): Boolean = with(path.toString()) {
    !contains("out${File.separator}") && !contains("${File.separator}_") &&
            !contains("${File.separator}ops")
// TODO remove "ops" dir name from filter
}
