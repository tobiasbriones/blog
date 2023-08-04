package md

import arrow.core.*
import md.Attribute.*
import java.util.*
import kotlin.collections.ArrayDeque

sealed interface Tag : AttributeList {
    val name: String get() = javaClass.simpleName.lowercase()
    val children: List<Tag>
    val content: Option<String>
}

data class Div(
    override val children: List<Tag>,
    override val attributes: Map<Attribute, List<String>> = mapOf(),
    override val content: Option<String> = None,
) : Tag

data class Nav(
    override val children: List<Tag>,
    override val attributes: Map<Attribute, List<String>> = mapOf(),
    override val content: Option<String> = None,
) : Tag

data class A(
    override val children: List<Tag> = listOf(),
    override val attributes: Map<Attribute, List<String>> = mapOf(),
    override val content: Option<String> = None,
) : Tag

data class Span(
    override val children: List<Tag> = listOf(),
    override val attributes: Map<Attribute, List<String>> = mapOf(),
    override val content: Option<String>,
) : Tag

data class Ul(
    override val children: List<Tag>,
    override val attributes: Map<Attribute, List<String>> = mapOf(),
    override val content: Option<String> = None,
) : Tag

data class Li(
    override val children: List<Tag> = listOf(),
    override val attributes: Map<Attribute, List<String>> = mapOf(),
    override val content: Option<String> = None,
) : Tag

fun Tag.toHtmlString(indentNumber: Int = 0): String {
    val indent = " ".repeat(indentNumber * 2)
    val attributesString = with(attributes) {
        if (isEmpty()) ""
        else map { it.key.toHtmlString(it.value) }
            .joinToString(" ")
            .let { " $it" }
    }

    val contentString = content.getOrElse { "" }

    if (children.isEmpty()) {
        return """
            |$indent<$name$attributesString>
            |$indent  $contentString
            |$indent</$name>
        """.trimMargin("|")
    }

    val childrenString =
        children
            .joinToString("\n") {
                it.toHtmlString(indentNumber + 1)
            }
    val contentLine =
        if (contentString.isEmpty()) "" else indent + contentString

    return """
        |$indent<$name$attributesString>
        |$contentLine$childrenString
        |$indent</$name>
    """.trimMargin("|")
}

enum class Attribute {
    Class,
    Href,
}

val Attribute.htmlName: String get() = name.lowercase()

fun Attribute.toHtmlString(values: List<String>) =
    if (values.isEmpty()) ""
    else "$htmlName=\"${values.joinToString(" ")}\""

interface AttributeList {
    val attributes: Map<Attribute, List<String>>
}

fun Index.generateNavHtml(): String =
    Nav(
        listOf(
            A(
                attributes = mapOf(
                    Class to listOf("home"),
                    Href to listOf("/"),
                ),
                children = listOf(
                    Span(
                        attributes = mapOf(
                            Class to listOf("material-symbols-rounded"),
                        ),
                        content = Some("home"),
                    ),
                    Span(
                        content = Some("Blog"),
                    ),
                )
            ),
            Div(
                attributes = mapOf(
                    Class to listOf("article"),
                ),
                children = listOf(
                    A(
                        attributes = mapOf(
                            Class to listOf("title"),
                            Href to listOf("#"),
                        ),
                        content = Some(extractTitle()),
                    ),
                    tocList(content),
                )
            ),
        )
    ).toHtmlString()

private fun tocList(markdown: Markdown): Ul {
    data class Holder(
        val indent: Int,
        val headingText: String,
        val href: String,
    )

    data class LineResult(val matchResult: MatchResult, val indent: Int)

    data class HeadingResult(val indent: Int, val headingText: String)

    // Regex to match h2 or ## headings
    val headingRegex = Regex("""^##\s*(.+)$""")

    val headingTextFromMatchResult: (LineResult) -> HeadingResult =
        { (matchResult, indent) ->
            HeadingResult(
                indent,
                matchResult
                    .groupValues[1]
                    .replaceFirst("^#+\\s*".toRegex(), "")
                    .trim()
            )
        }

    val hrefFromHeadingText: (String) -> String = { headingText ->
        headingText
            .lowercase(Locale.getDefault())
            .replace(' ', '-')
    }

    val titles = markdown
        .value
        .lines()
        .mapNotNull(headingRegex::find)
        .map { matchResult ->
            LineResult(
                matchResult,
                matchResult.value.takeWhile { it == '#' }.length - 1
            )
        }
        .map(headingTextFromMatchResult)
        .map { (indent, headingText) ->
            Holder(
                indent,
                headingText,
                hrefFromHeadingText(headingText)
            )
        }

    val rootUl = Ul(children = mutableListOf())
    val parents = ArrayDeque<Ul>()
    var currentIndent = 1

    parents.add(rootUl)
    for ((indent, headingText, href) in titles) {
        val li = Li(
            children = mutableListOf(
                A(
                    attributes = mapOf(
                        Href to listOf("#$href")
                    ),
                    content = Some(headingText),
                ),
            )
        )

        if (indent == currentIndent) {
            (parents.last().children as MutableList).add(li)
        } else if (currentIndent < indent) {
            while (currentIndent < indent) {
                // ...<ul><li><a></a> <ul><li><a></a>...
                val liParent = parents
                    .last()
                    .children
                    .last()
                val newParent = Ul(children = mutableListOf(li))

                (liParent.children as MutableList).add(newParent)
                parents.addLast(newParent)
                currentIndent++
            }
        } else {
            while (currentIndent > indent) {
                parents.removeLast()
                currentIndent--
            }
            (parents.last().children as MutableList).add(li)
        }
        currentIndent = indent
    }
    return parents.first()
}
