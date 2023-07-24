import java.nio.file.Path
import kotlin.io.path.name

data class Entry(val path: Path)

val title: (String) -> Title.Title = { Title.Title(it) }

fun Entry.name(): String = name(this)

val name: (Entry) -> String = { (path) -> path.name }


data class TitleLink(val title: String, val link: String)

sealed interface Title {
    @JvmInline
    value class Title private constructor(val value: String) {
        init {
            require(value.trim() == value) { "Value is not trimmed" }
            require(value.length in 1..25) { "Value not in rage [1, 25]" }
        }

        companion object {
            operator fun invoke(value: String?): Title {
                requireNotNull(value) { "Value must not be null" }
                return Title(value)
            }
        }
    }

    data class Heading(val value: Title, val heading: Int)

    data class Caption(val value: Title, val heading: Int)
}