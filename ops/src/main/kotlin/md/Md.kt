package md

data class Markdown(val value: String)

data class Index(val content: Markdown) {
    override fun toString(): String = content.value
}

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
