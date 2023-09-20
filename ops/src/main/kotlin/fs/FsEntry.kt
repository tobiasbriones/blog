package fs

import Entry
import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.right
import md.Dictionary
import md.Index
import md.Markdown
import md.toTitleCase
import newEntryFromAbsPath
import path
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Stream
import kotlin.io.path.exists
import kotlin.io.path.name

fun Entry.loadEntries(
    predicate: (Entry) -> Boolean = { true }
): Either<String, List<Entry>> = walk { stream ->
    stream
        .filter(Files::isDirectory)
        .filter(::filterPath)
        .filter(::isEntryDir)
        .filter { it != path } // Not root
        .filter(::filterParents)
        .map { newEntryFromAbsPath(path, it) }
        .filter(predicate)
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

fun hasNotice(entry: Entry): Boolean = with(entry.path) {
    listOf(resolve("images"), resolve("static"))
        .fold(false) { acc, path -> acc || path.resolve("notice.md").exists() }
}

fun generateRootNotice(entries: List<Entry>, dic: Dictionary = Dictionary()): String {
    val newLink: (Entry, String) -> String = { entry, subdir ->
        """
        - [${entry.toTitleCase(dic)} ($subdir)](${
            entry
                .relPath
                .resolve(subdir)
                .resolve("notice.md")
                .toString()
                .replace("\\", "/")
                .removePrefix("/")
        }).
    """.trimIndent()
    }

    return entries
        .map { entry ->
            with(entry.path) {
                var markdownList = ""

                if (resolve("images").resolve("notice.md").exists()) {
                    markdownList += newLink(entry, "images")
                }
                if (resolve("static").resolve("notice.md").exists()) {
                    markdownList += newLink(entry, "static")
                }
                markdownList
            }
        }
        .reduce { acc, next -> "$acc\n$next" }
}

private fun isEntryDir(dir: Path): Boolean = with(
    dir.resolve("index.md")
) {
    Files.exists(this) && Files.isRegularFile(this)
}

private fun filterPath(path: Path): Boolean =
    filterDirName(path.name)


private fun filterDirName(dirName: String): Boolean = with(dirName) {
    !startsWith("_") &&
        !startsWith("out") &&
        !startsWith(".")
}

private fun filterParents(path: Path): Boolean = with(path.toString()) {
    !contains("out${File.separator}") &&
        !contains("${File.separator}_") &&
//            !contains("${File.separator}ops") &&
        !contains(".")
// TODO remove "ops" dir name from filter
}
