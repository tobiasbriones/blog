import arrow.core.None
import arrow.core.Option
import arrow.core.firstOrNone
import arrow.core.toOption
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.name
import kotlin.io.path.notExists

fun generateCoverImageIfNeeded(entry: Entry, config: BuildConfig) {
    val coverPath = entry.path.resolve("${entry.name()}.png")

    fun isPr() = entry.path.parent.name == "pr"

    fun isRelease() = entry.path.parent.name == "release"

    if (coverPath.exists()) {
        return
    }

    if (isPr()) {
        texsydoFx(
            entry,
            "pr-cover"
        )
    }
}

fun texsydoFx(entry: Entry, coverCmd: String): Path {
    fun opt(name: String, value: Option<String>) = value
        .fold({ "" }, { "--$name=$it" })

    fun getHeading(): String {
        val index = Files.readString(entry.path.resolve("index.md"))
        val pair = extractOrgAndRepo(index) ?: return ""
        return "${pair.first}/${pair.second}"
    }

    fun getCoverMdTokens(): List<String> {
        val coverPath = entry.path.resolve("${entry.name()}.png.md")

        if (coverPath.notExists()) {
            println("Cover does not exist: $coverPath")
            return listOf()
        }

        val coverMd = Files.readString(coverPath)
        return coverMd.split("\n{2,}".toRegex())
    }

//    fun getSubHeading(tokens: List<String>): Option<String> = tokens
//        .firstOrNone { it.startsWith("`") && it.endsWith("`") }
//        .map { it.removePrefix("`").removeSuffix("`") }
//        .map {}

    val bg = getResourceFilePath("cover/bg.png")
    val profile = getResourceFilePath("cover/profile.jpeg")
    val heading = getHeading()
    val tokens = getCoverMdTokens()
    val abstract = getAbstract(tokens)
    val footer = getFooter(tokens)
    val subHeading = None
    val cmd = """
        | tsd-fx $coverCmd\
        | --bg=$bg\
        | --profile=$profile\
        | --heading=$heading\
        | --abstract=$abstract\
        | ${opt("footer", footer)}\
        | ${opt("subheading", subHeading)}\
    """.trimMargin("|")

    return entry.path
}

fun saveCoverToBuild(entry: Entry, outDir: Path) {
    val filename = entry.name() + ".png"
    val dir = outDir.resolve("_site")
    val output = dir
        .resolve(filename)
        .toFile()

    println("Cover image generated")
}

fun getAbstract(tokens: List<String>): Option<String> = tokens
    .firstOrNone { !it.startsWith("`") }

fun getFooter(tokens: List<String>): Option<String> = tokens
    .firstOrNone { it.startsWith("- ") }
    .fold({ listOf() }, { it.split("\n") })
    .map { it.removePrefix("-").trim() }
    .reduceOrNull { acc: String, s: String -> "$acc, $s" }
    .toOption()

fun getResourceFilePath(resourceName: String): String? {
    val resourceUrl = object {}.javaClass.getResource(resourceName)
    return resourceUrl?.toExternalForm()?.removePrefix("file:")
}

fun splitPipe(data: String): Pair<String, String> = with(data.split("|")) {
    Pair(get(0).trim(), getOrElse(1) { "" }.trim())
}

fun extractOrgAndRepo(link: String): Pair<String, String>? {
    val pattern = Regex("""https://github\.com/([^/]+)/([^/]+)/pull/(\d+)""")
    val matchResult = pattern.find(link)
    return matchResult?.let {
        val (org, repo, _) = it.destructured
        org to repo
    }?.let {
        if (it.second.startsWith(it.first)) {
            Pair(it.first, "@${it.second.removePrefix(it.first)}")
        } else it
    }
}
