import arrow.core.*
import arrow.core.Either.*
import md.Index
import md.Markdown
import md.extractTitle
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Stream


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

fun Entry.tags(): List<String> = relPath
    .toString()
    .split("/")

fun saveIndex(entryDir: Path, index: String) {
    Files.writeString(Path.of(entryDir.toString(), "index.md"), index)
}
