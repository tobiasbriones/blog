package md

import `$`
import `---`
import Entry
import arrow.core.*
import name
import path
import java.io.IOException
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import javax.imageio.ImageIO
import kotlin.io.path.absolutePathString
import kotlin.io.path.exists
import kotlin.io.path.name
import kotlin.io.path.relativeTo
import kotlin.system.exitProcess

data class Markdown(val value: String)

fun Markdown.parse(entry: Entry, dic: Dictionary): Markdown =
    parseCodeSnippets(entry)
        .parseImages(dic, entry)

fun Markdown.parseCodeSnippets(entry: Entry): Markdown =
    copy(value = parseCodeSnippets(value, entry))

fun Markdown.parseImages(dic: Dictionary, entry: Entry): Markdown =
    copy(value = parseImages(value, dic, entry))

fun parseImages(value: String, dic: Dictionary, entry: Entry): String {
    val sb = StringBuilder(value.length)
    val imgPattern = "!\\[(.*?)]\\((.*?)\\)".toRegex()

    data class Img(val path: String, val alt: String)

    fun imageHtml(path: String, alt: String): String {
        val title = Entry(Path.of(path)).toTitleCase(dic).removeExtension()
        val altValue = alt.ifBlank { title }

        // Cover image //
        return if (path.contains(entry.name())) """
                <img src="$path" alt="$altValue" />
            """.trimIndent()
        // Normal Image //
        else """
                <figure>
                    <img src="$path" alt="$altValue" />
                    <figcaption>$title</figcaption>
                </figure>
            """.trimIndent()
    }

    fun carouselHtml(firstImagePath: String): String {
        fun getSequence(): List<String> {
            val seq = mutableListOf(firstImagePath)
            var i = 2

            while (true) {
                val nextImagePath = firstImagePath
                    .replace("_seq-1.", "_seq-$i.")

                if (!entry.path.resolve(nextImagePath).exists()) {
                    break
                }
                seq.add(nextImagePath)
                i++
            }
            return seq
        }

        fun isPortrait(): Boolean =
            try {
                val path = entry.path.resolve(firstImagePath)

                if (path.exists()) {
                    val image = ImageIO.read(path.toFile())
                    val width = image.width
                    val height = image.height
                    val ratio = width / height

                    ratio < 0.75
                } else {
                    false
                }
            } catch (e: IOException) {
                false
            }

        data class CarItem(val index: Int, val src: String, val alt: String)

        val activeClass: (CarItem) -> String =
            { if (it.index == 0) "active" else "" }
        val ariaCurrent: (CarItem) -> String =
            { if (it.index == 0) "aria-current=\"true\"" else "" }


        val id = firstImagePath
            .substringAfterLast("/")
            .removeExtension()
            .removeSuffix("_seq-1")

        val title = Entry(Path.of(id)).toTitleCase(dic).removeExtension()

        val portrait = if (isPortrait()) "portrait" else ""

        val carouselClasses = "carousel slide $portrait"

        val seq = getSequence()
        val items = seq
            .mapIndexed { idx, img ->
                CarItem(
                    idx, img, img.substringAfterLast("/")
                )
            }
        val indicators = items
            .map {
                """
                    |<button
                    |    type="button"
                    |    data-bs-target="#$id"
                    |    data-bs-slide-to="${it.index}"
                    |    aria-label="Slide ${it.index + 1}"
                    |    class="${it `---` activeClass}"
                    |    ${it `---` ariaCurrent}
                    |>
                    |</button>
                """.trimMargin("|")
            }
            .reduce { acc, next -> "$acc\n$next" }
            .run {
                """
                    |<div class="carousel-indicators">
                    |    $this
                    |</div>
                """.trimMargin("|")
            }
        val control = """|
            |<button
            |    class="carousel-control-prev"
            |    type="button"
            |    data-bs-target="#$id"
            |    data-bs-slide="prev"
            |>
            |   <div class="icon">
            |     <span
            |        class="carousel-control-prev-icon"
            |        aria-hidden="true">
            |     </span>
            |   </div>
            |    <span class="visually-hidden">Previous</span>
            |</button>
            |
            |<button
            |    class="carousel-control-next"
            |    type="button"
            |    data-bs-target="#$id"
            |    data-bs-slide="next"
            |>
            |   <div class="icon">
            |     <span
            |        class="carousel-control-next-icon"
            |        aria-hidden="true">
            |      </span>
            |    </div>
            |    <span class="visually-hidden">Next</span>
            |</button>
        """.trimMargin("|")


        return items
            .map {
                """
                |<div class="carousel-item ${it `---` activeClass}">
                |    <img class="d-block" src="${it.src}" alt="${it.alt}">
                |</div>
            """.trimMargin("|")
            }
            .reduce { acc, next -> "$acc\n$next" }
            .run {
                """
                    |<div>
                    |<div id="$id" class="$carouselClasses" data-bs-ride="false"
                    |>
                    |$indicators
                    |
                    |<div class="carousel-inner">
                    |    $this
                    |</div>
                    |
                    |$control
                    |
                    |
                    |<button type="button" class="zoom">
                    |<span class="material-symbols-rounded enter">
                    |zoom_in
                    |</span>
                    |
                    |<span class="material-symbols-rounded exit">
                    |zoom_out
                    |</span>
                    |</button>
                    |
                    |<button type="button" class="fullscreen">
                    |<span class="material-symbols-rounded enter">
                    |fullscreen
                    |</span>
                    |
                    |<span class="material-symbols-rounded exit">
                    |fullscreen_exit
                    |</span>
                    |</button>
                    |
                    |</div>
                    |
                    |<h5>$title</h5>
                    |
                    |</div>
                """.trimMargin("|")
            }
    }

    fun mapImageHtml(path: String, alt: String): String =
        with(path.substringAfterLast("_")) {
            when {
                startsWith("seq-1.") -> carouselHtml(path)
                else -> imageHtml(path, alt)
            }
        }

    fun videoHtml(name: String, path: String): String {
        val title = Entry(Path.of(path)).toTitleCase(dic).removeExtension()
        val poster = with("poster-_-$name") {
            if (entry.path.resolve("static/$this.png").exists())
                "$this.png"
            else
                "$this.jpg"
        }
        return """
                <figure>
                    <video poster="static/$poster" controls>
                      <source
                        src="$path"
                        type="video/mp4"
                      >
                      Your browser does not support the video tag.
                    </video>

                    <figcaption>$title</figcaption>
                </figure>
            """.trimIndent()
    }

    for (line in value.lines()) {
        imgPattern
            .find(line)
            ?.let {
                val alt = it.groups[1]?.value
                val path = it.groups[2]?.value

                if (alt != null && path != null)
                    Some(Img(path, alt))
                else
                    None
            }
            .toOption()
            .flatten()
            .onSome { (path, alt) ->
                val ext = path.getExtension()
                val name = Path.of(path).name.removeExtension()
                val html = when (ext) {
                    "mp4" -> videoHtml(name, path)
                    else -> mapImageHtml(path, alt)
                }

                sb.append(html)
                sb.append("\n")
            }
            .onNone {
                sb.append(line)
                sb.append("\n")
            }
    }

    return sb.toString()
}

fun String.getExtension(): String {
    val lastDotIndex = lastIndexOf(".")
    return if (lastDotIndex != -1) {
        substring(lastDotIndex + 1, length)
    } else {
        this
    }
}

fun String.removeExtension(): String {
    val lastDotIndex = lastIndexOf(".")
    return if (lastDotIndex != -1) {
        substring(0, lastDotIndex)
    } else {
        this
    }
}

fun parseCodeSnippets(value: String, entry: Entry): String {
    data class Caption(
        val title: String,
        val heading: String = "",
        val abstract: String = ""
    )

    fun Caption.isNotEmpty() = title.isNotBlank()

    val pipeTokens: (List<String>) -> String = { it.joinToString(" | ") }

    val isHighLevelText: (String) -> Boolean = {
        it.first() == it.first().uppercaseChar() && it.contains(" ")
    }

    val captionFromPipe: (List<String>) -> Caption = {
        when (it.size) {
            0 -> Caption("")
            1 -> Caption(it[0])
            2 -> if (it[1] `---` isHighLevelText)
                Caption(title = it[1], abstract = it[0])
            else
                Caption(title = it[0], heading = it[1])

            else -> if (it[1] `---` isHighLevelText)
                Caption(
                    title = it[1],
                    abstract = it[0],
                    heading = pipeTokens `$` it.subList(2, it.size)
                )
            else
                Caption(
                    title = it[0],
                    heading = pipeTokens `$` it.subList(1, it.size)
                    // The last tokens are left for the heading
                )
        }
    }

    val captionFromDsl: (String) -> Caption = {
        captionFromPipe `$` it
            .removePrefix("`")
            .removeSuffix("`")
            .split("|")
            .map(String::trim)
    }

    val shortHeading: (String) -> String = {
        with(it.split("|").map(String::trim)) {
            when (size) {
                1, 2 -> it
                else ->
                    // e.g, met draw | class TreeAxesCanvas | mrm-canvas.ts
                    //  to met draw | class TreeAxesCanvas
                    if (get(1).contains("class"))
                        subList(0, 2).joinToString(" | ")
                    else it
            }
        }
    }

    val sb = StringBuilder(value.length)
    val mdSnippetBlock = StringBuilder()
    var caption = Caption("")

    val preprocessed = StringBuilder(value.length)
    var dslLine = ""

    for (line in value.lines()) {
        // If it's a DSL line (i.e., '{ DSL code }') but it's multiline, make
        // it single line to easy parsing
        if (line.matches("^`[^`]*".toRegex()) && !line.matches("`$".toRegex())) {
            // It closes the "`" but has more text in the same line in the
            // end e.g. "`Title | file.ts` this file is..."
            if (line.count { it == '`' } % 2 == 0) {
                println("Syntax Error: Invalid DSL line $line")
                exitProcess(1)
            }
            dslLine += line
            continue
        }

        if (dslLine.isNotBlank()) {
            if (line.trim().endsWith("`")) {
                dslLine += line

                preprocessed.append(dslLine)
                preprocessed.append("\n")
                dslLine = ""
                continue
            } else {
                println("Syntax Error: Invalid DSL line $dslLine \n $line")
                exitProcess(1)
            }
        }

        preprocessed.append(line)
        preprocessed.append("\n")
    }

    for (line in preprocessed.lines()) {
        // Caption line, e.g. `Image Title | package data | LocalRepository.kt`
        if (line.matches("^`[^`]*`$".toRegex())) {
            caption = captionFromDsl(line)
            continue
        }

        // Start of the MD code block
        if (
            line.startsWith("```") &&
            caption.isNotEmpty() &&
            mdSnippetBlock.isBlank()
        ) {
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

                val filePath = caption
                    .heading
                    .split("|")
                    .map { it.trim() }
                    .map { findFile(entry.path, it) }
                    .firstOrNone { it.isSome() }
                    .flatten()

                val linkButtonHtml = with(filePath) {
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

                with(caption) {
                    val headerless = if (heading.isBlank()) "headerless" else ""
                    val headerClasses = "header user-select-none $headerless"

                    val abstractHtml = if (abstract.isBlank()) "" else """
                        <div class="abstract">$abstract</div>
                    """.trimIndent()

                    val html = """
<figure>
<div class="$headerClasses">
    <div class="caption">
        ${heading `---` shortHeading}
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

$abstractHtml

<figcaption>$title</figcaption>
</figure>
""".trimIndent()

                    sb.append(html)
                    sb.append("\n")
                }
                mdSnippetBlock.clear()
                caption = Caption("")
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

fun Index.parse(entry: Entry, dic: Dictionary): Index = copy(
    content = content.parse(entry, dic)
)

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

                if (path.contains("---ep/app/") ||
                    path.contains("/node_modules/")
                ) {
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
