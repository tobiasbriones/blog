package md

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.getOrElse
import java.util.*
import kotlin.collections.ArrayDeque

sealed interface Tag : ClassList {
    val name: String get() = javaClass.simpleName.lowercase()
    val children: List<Tag>
    val content: Option<String>
}

data class Div(
    override val children: List<Tag>,
    override val classList: List<String> = listOf(),
    override val content: Option<String> = None,
) : Tag

data class Nav(
    override val children: List<Tag>,
    override val classList: List<String> = listOf(),
    override val content: Option<String> = None,
) : Tag

data class A(
    override val children: List<Tag> = listOf(),
    override val classList: List<String> = listOf(),
    override val content: Option<String> = None,
    val href: String
) : Tag

data class Span(
    override val children: List<Tag> = listOf(),
    override val classList: List<String> = listOf(),
    override val content: Option<String>,
) : Tag

data class Ul(
    override val children: List<Tag>,
    override val classList: List<String> = listOf(),
    override val content: Option<String> = None,
) : Tag

data class Li(
    override val children: List<Tag> = listOf(),
    override val classList: List<String> = listOf(),
    override val content: Option<String> = None,
) : Tag

fun Tag.toHtmlString(indentNumber: Int = 0): String {
    val indent = " ".repeat(indentNumber * 2)
    val contentString = content.getOrElse { "" }
    val classesString = classList.joinToString(" ")
    val attributesString = if (classList.isEmpty()) "" else
        " class=\"$classesString\""
    val childrenString =
        children
            .joinToString("\n") {
                it.toHtmlString(indentNumber + 1)
            }
    return """
        
        |$indent<$name$attributesString>
        |$indent  $contentString$childrenString
        |$indent</$name>
    """.trimMargin("|").trimStart()
}

interface ClassList {
    val classList: List<String>
}

fun Index.generateNavHtml(): String =
    Nav(
        listOf(
            A(
                listOf(
                    Span(
                        classList = listOf("material-symbols-rounded"),
                        content = Some("home"),
                    ),
                    Span(
                        content = Some("Blog"),
                    ),
                ),
                listOf("home"),
                None,
                "/",
            ),
            A(
                content = Some(extractTitle()),
                classList = listOf("title"),
                href = "#",
            ),
            Div(
                classList = listOf("article"),
                children = listOf(tocList(content))
            )
        )
    ).toHtmlString()
//
//"""
//    <nav>
//        <a href="/" class="home">
//            <span class="material-symbols-rounded">home</span>
//            <span>Blog</span>
//        </a>
//
//            <a href="#" class="title">${extractTitle()}</a>
//
//        <div class="article">
//            ${tocListHtml(content)}
//        </div>
//    </nav>
//""".trimIndent()

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
                    href = "#$href",
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
