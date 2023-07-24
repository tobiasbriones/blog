import arrow.core.Either
import arrow.core.None
import arrow.core.Option
import arrow.core.Some
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
    """"https://raw.githubusercontent.com/$username/$repo/gh-pages/${name()}/$path${name()}.png
    """.trimIndent()

val load: (Entry) -> Either<String, String> = {
    try {
        Either.Right(Files.readString(it.path))
    } catch (e: IOException) {
        e.printStackTrace()
        Either.Left(e.message.orEmpty())
    }
}

val loadIndex: (Entry) -> Either<String, String> = {
    try {
        Either.Right(Files.readString(Path.of(it.path.toString(), "index.md")))
    } catch (e: IOException) {
        e.printStackTrace()
        Either.Left(e.message.orEmpty())
    }
}

fun toMarkdown(it: TitleLink): String {
    return "[${it.title}](${it.link})"
}


fun toTitle(index: String, link: String): TitleLink {
    fun extractTitle(input: String): String {
        val lines = input.lines()
        return lines.find { it.startsWith("#") }.orEmpty()
    }

    fun read(contents: String): String {
        val titleLine = extractTitle(contents)
        return titleLine.replace("# ", "")
    }

    return TitleLink(read(index), link)
}

fun toAbstract(index: String): String {
    var text = ""
    var abstractHashFound = false

    for (line in index.lines()) {
        if (line.startsWith("#")) {
            abstractHashFound = true
            continue
        }
        if (!abstractHashFound) {
            continue
        }

        // If it has any cover image
        if (line.startsWith("!") ||
            line.startsWith("<") ||
            line.startsWith("-")
        ) {
            continue
        }

        // Abstract paragraph ended
        if (line.isBlank() || line.startsWith("#")) {
            if (line.startsWith("#")) {
                break
            }
            if (text.isNotBlank()) {
                break
            }
        }
        text += line
    }
    return text
}

fun entries(root: Entry): List<Entry> = Files
    .walk(root.path)
    .use { stream ->
        stream
            .filter(Files::isDirectory)
            .filter(::filterPath)
            .filter(::isEntryDir)
            .filter(::filterParents)
            .map(::Entry)
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
