package md

import `---`
import Entry
import arrow.core.Some
import html.*
import html.Attribute.*
import html.Attribute.Target
import java.nio.file.Path
import java.util.*
import java.util.stream.Stream
import kotlin.collections.ArrayDeque
import kotlin.io.path.name

fun Index.generateNav(home: String): Nav =
    Nav(
        attributes = mapOf(
            AriaLabel to listOf("Navigation")
        ),
        children = listOf(
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
                        content = Some(home),
                    ),
                )
            ),
        )
    )

fun Index.generateToC(): Nav =
    Nav(
        attributes = mapOf(
            Class to listOf("toc", "user-select-none"),
            AriaLabel to listOf("Table of Contents")
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
    )

fun List<String>.subDirectoriesNav(dic: Dictionary): Div {
    val dirName: (String) -> String = { it.removeSuffix("---ep") }
    val iconName: (String) -> String =
        { if (it.endsWith("---ep")) "Example Project" else "Subdirectory" }
    val iconSrc: (String) -> String =
        { if (it.endsWith("---ep")) IC_EP else IC_FOLDER }
    val subdirTitleCase: (String) -> String =
        { Entry(Path.of(it `---` dirName)).toTitleCase(dic) }

    return Div(
        attributes = mapOf(
            Class to listOf("my-4")
        ),
        children = map { subdir ->
            Div(
                attributes = mapOf(
                    Class to listOf("subdir-btn", "my-4")
                ),
                children = listOf(
                    A(
                        attributes = mapOf(
                            Class to listOf("btn"),
                            Href to listOf(subdir),
                        ),
                        children = listOf(
                            Img(
                                attributes = mapOf(
                                    Src to listOf(subdir `---` iconSrc),
                                    Alt to listOf(subdir `---` iconName)
                                )
                            ),
                            Span(
                                content = Some(subdir `---` subdirTitleCase)
                            )
                        ),
                    )
                )
            )
        }
    )
}

fun openInGitHubButton(githubUrl: String): Div = Div(
    attributes = mapOf(
        Class to listOf("social open-gh-btn", "my-4"),
    ),
    children = listOf(
        A(
            attributes = mapOf(
                Class to listOf(
                    "btn",
                    "btn-github",
                ),
                Href to listOf(
                    githubUrl
                ),
                Target to listOf("_blank")
            ),
            children = listOf(
                I(
                    attributes = mapOf(
                        Class to listOf("fab", "fa-github")
                    )
                ),
                Span(
                    content = Some("Open in GitHub")
                )
            ),
        )
    )
)

fun createNavigationTreeHtml(
    files: Stream<Path>,
    originUrl: String,
): String {
    // If the filename is like .gitignore its directory was renamed to
    // _.gitignore/.gitignore.html to avoid hidden directories
    val isHidden: (Path) -> Boolean = {
        it.name.startsWith("-.")
    }
    val name: (Path) -> String = {
        if (isHidden(it))
            it.name.removePrefix("-")
        else it.name
    }

    return Ul(
        children = files
            .map { child ->
                // Send hidden files like .gitignore to GitHub since they're
                // not supported in Netlify
                if (isHidden(child)) Li(
                    children = listOf(
                        A(
                            attributes = mapOf(
                                Href to listOf(
                                    "$originUrl/${name(child)}"
                                ),
                                Target to listOf("_blank")
                            ),
                            content = Some(name(child)),
                        )
                    )
                )
                // For normal files
                else Li(
                    children = listOf(
                        A(
                            attributes = mapOf(
                                Href to listOf(child.name)
                            ),
                            content = Some(child.name),
                        )
                    )
                )
            }
            .toList()
    ).toHtmlString()
}

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
