package md

import `---`
import Entry
import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.lastOrNone
import path
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import kotlin.io.path.absolutePathString
import kotlin.io.path.relativeTo

data class Markdown(val value: String)

fun Markdown.parse(entry: Entry): Markdown = parseCodeSnippets(entry)

fun Markdown.parseCodeSnippets(entry: Entry): Markdown =
    copy(value = parseCodeSnippets(value, entry))

fun parseCodeSnippets(value: String, entry: Entry): String {
    val sb = StringBuilder(value.length)
    val mdSnippetBlock = StringBuilder()
    var title = ""
    var abstract = ""

    for (line in value.lines()) {
        // Caption line
        if (line.matches("^`[^`]*`$".toRegex())) {
            val captions = line
                .removePrefix("`")
                .removeSuffix("`")
                .split("|")
                .map(String::trim)

            if (captions.isNotEmpty()) {
                title = captions[0]
                abstract =
                    captions.subList(1, captions.size).joinToString(" | ")
                continue
            }
        }

        // Start of the MD code block
        if (line.startsWith("```") && title.isNotBlank() && mdSnippetBlock.isBlank()) {
            mdSnippetBlock.append(line)
            mdSnippetBlock.append("\n")
            continue
        }

        // It's saving a code block
        if (mdSnippetBlock.isNotBlank()) {
            mdSnippetBlock.append(line)
            mdSnippetBlock.append("\n")

            // End of the MD code block
            if (line.startsWith("```")) {
                val code = mdSnippetBlock.toString() `---`
                    ::mdSnippetBlockToCode `---`
                    ::encodeToHtml `---`
                    { it + "\n" }

                val fileName = abstract
                    .split("|")
                    .lastOrNone()
                    .map { it.trim() }

                val linkButtonHtml = with(entry.findFile(fileName)) {
                    when (this) {
                        None -> ""
                        is Some -> """
<button type="button" data-path="${
                            this.value.relativeTo(entry.path).toString()
                                .replace("\\", "/")
                        }" onclick="onOpenCodeSnippetLink(this)">
    <span class="material-symbols-rounded">
    open_in_new
    </span>
</button>
""".trimIndent()
                    }
                }

                val html = """
<figure>
<div class="header user-select-none">
    <div class="caption">
        $abstract
    </div>

    <div class="menu">
        $linkButtonHtml

        <button type="button" data-code="$code" onclick="onCopyCodeSnippet(this)">
            <span class="material-symbols-rounded">
            content_copy
            </span>

            <div class="tooltip">
                Copied
            </div>
        </button>
    </div>
</div>
{% capture markdownContent %}
$mdSnippetBlock
{% endcapture %}

{{ markdownContent | markdownify }}

<figcaption>$title</figcaption>
</figure>
""".trimIndent()

                sb.append(html)
                sb.append("\n")

                mdSnippetBlock.clear()
                title = ""
                abstract = ""
            }
            continue
        }

        sb.append(line)
        sb.append("\n")
    }

    return sb.toString()
}

fun mdSnippetBlockToCode(mdSnippet: String): String = mdSnippet
    .trim()
    .lines()
    .run { subList(1, size - 1) }
    .joinToString("\n")

fun encodeToHtml(input: String): String {
    val result = StringBuilder()
    for (char in input) {
        when (char) {
            '<' -> result.append("&lt;")
            '>' -> result.append("&gt;")
            '&' -> result.append("&amp;")
            '"' -> result.append("&quot;")
            '\'' -> result.append("&#x27;")
            '/' -> result.append("&#x2F;")
            else -> result.append(char)
        }
    }
    return result.toString()
}

data class Index(val content: Markdown) {
    override fun toString(): String = content.value
}

fun Index.parse(entry: Entry): Index = copy(content = content.parse(entry))

fun Index.extractTitle(): String = content
    .value
    .lines()
    .find { it.startsWith("#") }
    .orEmpty()
    .replace("# ", "")

fun Index.extractAbstract(): String {
    var text = ""
    var abstractHashFound = false
    var coverFound = false

    for (line in content.value.lines()) {
        if (line.startsWith("#")) {
            abstractHashFound = true
            continue
        }
        if (!abstractHashFound) {
            continue
        }

        // If it has any cover image
        if (line.startsWith("!") || line.startsWith("<")) {
            coverFound = true
        } else if (line.startsWith("-")) {
            coverFound = false
            continue
        }

        if (coverFound) {
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

        if (text.isNotBlank()) {
            text += "\n"
        }
        text += line
    }
    return text
}

fun codeSnippetBlock(langCode: String, code: String): String = StringBuilder()
    .append("```$langCode")
    .append("\n")
    .append(code)
    .append("\n")
    .append("```")
    .toString()

fun Entry.findFile(name: Option<String>): Option<Path> = when (name) {
    None -> None
    is Some -> findFile(path, name.value)
}

fun findFile(root: Path, name: String): Option<Path> {
    val parseFileName: (String) -> String = {
        if (it.contains(" ")) // Java file
            it
                .split(" ")
                .last()
                .run { "$this.java" }
        else
            it
    }

    val fileName = parseFileName(name)
    val options = setOf(FileVisitOption.FOLLOW_LINKS)
    val searchResult = mutableListOf<Path>()

    Files.walkFileTree(
        root,
        options,
        Int.MAX_VALUE,
        object : SimpleFileVisitor<Path>() {
            override fun visitFile(
                file: Path,
                attrs: BasicFileAttributes
            ): FileVisitResult {
                val path = file.absolutePathString().replace("\\", "/")

                if (path.contains("/app/") || path.contains("/node_modules/")) {
                    return FileVisitResult.CONTINUE
                }

                if (file.fileName.toString() == fileName) {
                    searchResult.add(file)
                }
                return FileVisitResult.CONTINUE
            }
        })

    return when {
        searchResult.isEmpty() -> None
        else -> Some(searchResult.first())
    }
}
