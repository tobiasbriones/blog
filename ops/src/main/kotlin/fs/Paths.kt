package fs

import java.io.File
import java.nio.file.Path
import kotlin.io.path.name


fun Path.isBrowsable(): Boolean = with(
    listOf(Regex("images"), Regex("static"))
) {
    if (nameMatchesAnyOf(this)) false
    else hasAnyParentLike(this)
}

fun Path.shouldBeCopied(): Boolean = with(
    listOf(
        Regex("out"),
        Regex(".*\\.iml\$"), // iml extension
        Regex(
            "\\.(?!(gitignore)\\b)[^.]+"
        ) // hidden dirs like ".idea" with exceptions like ".gitignore"
    )
) {
    if (nameMatchesAnyOf(this)) false
    else hasAnyParentLike(this)
}

fun Path.hasAnyParentLike(parents: List<Regex>): Boolean = with(toString()) {
    val sep = Regex.escape(File.separator)
    parents
        .map { Regex("${sep}$it${sep}") }
        .fold(true) { acc, next ->
            acc && !next.containsMatchIn(this)
        }
}

fun Path.nameMatchesAnyOf(names: List<Regex>): Boolean =
    names.fold(false) { acc, next -> acc || next.matches(name) }
