package jekyll

import arrow.core.*
import fs.AppResources
import fs.copyDirectory
import html.Div
import html.Nav
import html.toHtmlString
import md.Index
import md.codeSnippetBlock
import java.io.IOException
import java.nio.file.Path

data class JekyllIndex(
    val frontMatter: FrontMatter,
    val nav: Nav,
    val index: Index,
    val subdirNav: Option<Div>,
)

fun JekyllIndex.toMarkdownString(): String = """
    |${frontMatter.toMarkdownString()}
    |
    |${nav.toHtmlString()}
    |
    |$index
    |
    |${subdirNav.map(Div::toHtmlString).getOrElse { "" }}
""".trimMargin("|")

data class FrontMatter(
    val permalink: String,
    val title: String,
    val description: Option<String> = None,
    val ogimage: Option<String> = None,
)

fun FrontMatter.toMarkdownString(): String {
    val strOf: (String, Option<String>) -> String = { attr, value ->
        value
            .map { "\n$attr: $it" }
            .getOrElse { "" }
    }

    return StringBuilder()
        .append("---")
        .append("\n")
        .append("permalink: $permalink/")
        .append("\n")
        .append("title: $title")
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

fun copyJekyllRootFiles(dst: Path): Either<String, Unit> = try {
    copyDirectory(
        AppResources.pathOf(Path.of("jekyll")),
        dst
    )
} catch (e: IOException) {
    e.printStackTrace()
    e.message.orEmpty().left()
}
