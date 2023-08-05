import java.nio.file.Path
import kotlin.io.path.name

data class Entry(val rootPath: Path, val relPath: Path = Path.of(""))

fun newEntryFromAbsPath(rootPath: Path, absPath: Path): Entry = Entry(
    rootPath,
    Path.of(absPath.toString().removePrefix(rootPath.toString()))
)

val Entry.path: Path
    get() = Path.of(rootPath.toString(), relPath.toString())

fun Entry.name(): String = path.name

data class TitleLink(val title: String, val link: String)

