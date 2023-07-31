import java.nio.file.Path
import kotlin.io.path.name

data class Entry(val path: Path)

fun Entry.name(): String = path.name

data class TitleLink(val title: String, val link: String)

