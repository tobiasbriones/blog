package html

import arrow.core.None
import arrow.core.Option
import arrow.core.getOrElse

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

data class I(
    override val children: List<Tag> = listOf(),
    override val attributes: Map<Attribute, List<String>> = mapOf(),
    override val content: Option<String> = None,
) : Tag

data class Strong(
    override val children: List<Tag> = listOf(),
    override val attributes: Map<Attribute, List<String>> = mapOf(),
    override val content: Option<String> = None,
) : Tag

data class Img(
    override val children: List<Tag> = listOf(),
    override val attributes: Map<Attribute, List<String>> = mapOf(),
    override val content: Option<String> = None,
) : Tag

data class Section(
    override val children: List<Tag>,
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
        if (isSingle()) {
            return "$indent<$name$attributesString />"
        }
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

fun Tag.isSingle() = when (this) {
    is Img -> true
    else -> false
}

enum class Attribute {
    Class,
    Href,
    Target,
    Src,
    Alt,
}

val Attribute.htmlName: String get() = name.lowercase()

fun Attribute.toHtmlString(values: List<String>) =
    if (values.isEmpty()) ""
    else "$htmlName=\"${values.joinToString(" ")}\""

interface AttributeList {
    val attributes: Map<Attribute, List<String>>
}
