package jekyll

import arrow.core.None
import arrow.core.Option
import arrow.core.getOrElse
import md.codeSnippetBlock

interface ToMarkdown {
    fun toMarkdown()
}

data class FrontMatter(
    val permlink: String,
    val title: String,
    val description: Option<String> = None,
    val ogimage: Option<String> = None,
)

fun FrontMatter.toMarkdown(): String {
    val strOf: (String, Option<String>) -> String = { attr, value ->
        value
            .map { "\n$attr: $it" }
            .getOrElse { "" }
    }

    return StringBuilder()
        .append("---")
        .append("\n")
        .append("permlink: $permlink")
        .append("\n")
        .append("title: $title")
        .append(strOf("description", description))
        .append(strOf("ogimage", ogimage))
        .append("\n")
        .append("---")
        .toString()
}

fun codeSnippetBlockHtml(res: FileResource): String = codeSnippetBlock(
    res.extension.langCode(),
    res.content,
)
