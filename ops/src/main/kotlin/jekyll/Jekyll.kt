package jekyll

import arrow.core.Either
import arrow.core.None
import arrow.core.Option
import arrow.core.getOrElse
import html.Div
import html.Nav
import html.toHtmlString
import md.Index
import md.codeSnippetBlock

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
