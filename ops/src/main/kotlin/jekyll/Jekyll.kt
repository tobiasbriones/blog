package jekyll

import arrow.core.*
import fs.AppFiles
import fs.copyDirectory
import html.Div
import html.Nav
import html.toHtmlString
import md.Index
import md.codeSnippetBlock
import java.io.IOException
import java.nio.file.Path
import kotlin.io.path.writeText

data class JekyllIndex(
    val frontMatter: FrontMatter,
    val nav: Nav,
    val toc: Nav,
    val index: Index,
    val subdirNav: Option<Div>,
)

fun JekyllIndex.toMarkdownString(): String = """
    |'${frontMatter.toMarkdownString()}
    |'
    |'$index
    |'
    |'${subdirNav.map(Div::toHtmlString).getOrElse { "" }}
""".trimMargin("|'")

fun JekyllIndex.saveNavigation(root: Path): Either<String, Unit> =
    saveInclude(root, "nav.html", nav.toHtmlString())

fun JekyllIndex.saveToc(root: Path): Either<String, Unit> =
    saveInclude(root, "${frontMatter.permalink}_toc.html", toc.toHtmlString())

fun JekyllIndex.saveInclude(
    root: Path,
    file: String,
    content: String,
): Either<String, Unit> =
    try {
        root
            .resolve(Path.of("_includes", file))
            .writeText(content)
            .right()
    } catch (e: IOException) {
        e.printStackTrace()
        e.message.orEmpty().left()
    }

data class FrontMatter(
    val permalink: String,
    val title: String,
    val description: Option<String> = None,
    val ogimage: Option<String> = None,
)

fun FrontMatter.toMarkdownString(): String {
    val strOf: (String, Option<String>) -> String = { attr, value ->
        value
            .map { it.replace("\n", " ") }
            .map {
                """
                
                $attr: "${it.replace("\"", "\\\"")}"
                """.trimIndent()
            }
            .getOrElse { "" }
    }

    return StringBuilder()
        .append("---")
        .append("\n")
        .append("permalink: $permalink")
        .append("\n")
        .append("""title: "$title"""")
        .append(strOf("description", description))
        .append(strOf("ogimage", ogimage))
        .append("\n")
        .append("---")
        .append("\n")
        .toString()
}

fun codeSnippetBlockHtml(res: FileResource): String = codeSnippetBlock(
    res.extension.langCode(),
    res.content,
)

fun copyJekyllRootFilesFromBuild(
    outDir: Path,
    dst: Path
): Either<String, Unit> = try {
    copyJekyllRootFiles(dst)
        .flatMap {
            // Copy generated files in the build process
            copyDirectory(
                outDir.resolve("_includes"),
                dst.resolve("_includes"),
            )
        }
} catch (e: IOException) {
    e.printStackTrace()
    e.message.orEmpty().left()
}

fun copyJekyllRootFiles(dst: Path): Either<String, Unit> = try {
    copyDirectory(
        AppFiles.pathOf(Path.of("jekyll", "Gemfile")).parent,
        dst,
    )
} catch (e: IOException) {
    e.printStackTrace()
    e.message.orEmpty().left()
}
