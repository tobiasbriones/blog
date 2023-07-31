package md

import jekyll.FileResource
import jekyll.langCode
import java.util.*

fun navHtml(index: Index) = """
    <nav>
        <a href="/" class="home">
            <span class="material-symbols-rounded">
            home
            </span>
            <span>
            Blog
            </span>
        </a>
        
        <div class="article">
            <a href="#" class="title">${index.extractTitle()}</a>

            ${tocListHtml(index.content)}
        </div>
    </nav>
""".trimIndent()

fun tocListHtml(markdown: Markdown): String {
    val headingRegex =
        Regex("""^##\s*(.+)$""") // Regex to match h2 or ## headings
    val lines = markdown.value.lines()
    val htmlBuilder = StringBuilder("<ul>")

    var currentIndent = 0

    for (line in lines) {
        val matchResult = headingRegex.find(line)
        if (matchResult != null) {
            val headingText =
                matchResult.groupValues[1].replaceFirst("^#+\\s*".toRegex(), "")
                    .trim()
            val indent = line.takeWhile { it == ' ' }.length / 2

            if (indent > currentIndent) {
                htmlBuilder.append("<ul>")
                htmlBuilder.append("\n")
            } else if (indent < currentIndent) {
                repeat(currentIndent - indent) { htmlBuilder.append("</ul>") }
            }

            htmlBuilder.append(
                """
                <li><a href="#${
                    headingText.lowercase(Locale.getDefault()).replace(' ', '-')
                }">
                $headingText</a>
                """
            )
            currentIndent = indent
        }
    }

    repeat(currentIndent) { htmlBuilder.append("</ul>") }
    htmlBuilder.append("</ul>")

    return htmlBuilder.toString()
}
