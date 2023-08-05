import arrow.core.*
import arrow.core.Either.*
import md.Index
import md.Markdown
import md.extractTitle
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path


fun Entry.coverPath(): Option<Path> {
    val mainPath = Path.of(path.toString(), "${path.fileName}.png")
    val secondaryPath = Path.of(
        path.toString(),
        "images",
        "${path.fileName}.png"
    )

    return if (Files.exists(mainPath))
        Some(mainPath)
    else if (Files.exists(secondaryPath))
        Some(secondaryPath)
    else None
}

fun Entry.coverGitHubUrl(username: String, repo: String, path: String = "") =
    """https://raw.githubusercontent.com/$username/$repo/gh-pages/${name()}/$path${name()}.png
    """.trimIndent()

fun Entry.subdirectories(): Either<String, List<Path>> = try {
    Files.list(path)
        .filter { Files.isDirectory(it) }
        .map { it.fileName }
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

fun Entry.tags(): List<String> = relPath
    .toString()
    .split("/")

fun toMarkdown(it: TitleLink): String {
    return "[${it.title}](${it.link})"
}

fun toTitle(index: Index, link: String): TitleLink =
    TitleLink(index.extractTitle(), link)

fun entries(root: Entry): List<Entry> = Files
    .walk(root.path)
    .use { stream ->
        stream
            .filter(Files::isDirectory)
            .filter(::filterPath)
            .filter(::isEntryDir)
            .filter(::filterParents)
            .map { newEntryFromAbsPath(root.path, it) }
            .toList()
    }

fun isEntryDir(dir: Path): Boolean = with(Path.of(dir.toString(), "index.md")) {
    Files.exists(this) && Files.isRegularFile(this)
}

fun filterPath(path: Path): Boolean =
    filterDirName(path.fileName.toString())


fun filterDirName(dirName: String): Boolean = with(dirName) {
    !startsWith("_") && !startsWith("out") && !startsWith(".")
}

fun filterParents(path: Path): Boolean = with(path.toString()) {
    !contains("_out${File.separator}")
}

fun saveIndex(entryDir: Path, index: String) {
    Files.writeString(Path.of(entryDir.toString(), "index.md"), index)
}
