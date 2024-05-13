package fx

import `$`
import BuildConfig
import Entry
import arrow.core.*
import fs.AppFiles
import fx.CoverCmd.PrCover
import fx.CoverCmd.ReleaseCover
import handleError
import name
import path
import runCommand
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import java.util.regex.Pattern
import kotlin.io.path.absolutePathString
import kotlin.io.path.exists
import kotlin.io.path.name
import kotlin.io.path.notExists

enum class CoverCmd {
    PrCover, ReleaseCover;

    override fun toString(): String = when (this) {
        PrCover -> "pr-cover"
        ReleaseCover -> "release-cover"
    }
}

fun generateCoverImageIfNeeded(
    entry: Entry,
    config: BuildConfig,
    title: String
) {
    val coverPath = entry.path.resolve("${entry.name()}.png")

    fun getCoverCmd(): Option<CoverCmd> = when (entry.path.parent.name) {
        "pr" -> PrCover.some()
        "release" -> ReleaseCover.some()
        else -> None
    }

    if (coverPath.exists()) {
        return
    }

    when (val cmd = getCoverCmd()) {
        None -> println("Article not applicable to automatic cover image.")
        is Some -> texsydoFx(
            entry,
            title,
            cmd.value,
            config.outDir
        )
    }
}

fun texsydoFx(
    entry: Entry,
    title: String,
    coverCmd: CoverCmd,
    outDir: Path,
) {
    val index = Files.readString(entry.path.resolve("index.md"))
    val orgRepoPair = extractOrgAndRepo(index) ?: return
    val repoPath = "${orgRepoPair.first}/${orgRepoPair.second}"

    fun opt(name: String, value: Option<String>) = value
        .fold({ "" }, { "--$name=$it" })

    fun getTempOutputDir(): String =
        Files
            .createTempFile("tsf-fx_", "_${entry.name()}")
            .absolutePathString()

    fun getBuildOutput(): Path =
        outDir
            .resolve(entry.name())
            .resolve("${entry.name()}.png")

    fun getHeading(): String {
        val pair = orgRepoPair
            .let {
                if (it.second.startsWith(it.first)) {
                    Pair(it.first, "@${it.second.removePrefix(it.first)}")
                } else it
            }

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

    fun inferSubheadingFromRelease(): Option<String> =
        with(splitPipe(title)) {
            if (second.isNotBlank())
                removeSuffixAfterVersion(first).some().map { "\"$it\"" }
            else None
        }

    fun inferSubHeadingFromPr(): Option<String> = extractBranchNames(index)
        .filter { !it.contains("/") } // Only first-class branches
        .groupBy { it }
        .maxByOrNull { it.value.size }
        .toOption()
        .map { it.key }
        .map { title -> // Capitalize Title
            title
                .split("-")
                .joinToString(" ") { word -> // Capitalize First Word
                    word.replaceFirstChar { ch ->
                        if (ch.isLowerCase()) ch.titlecase(Locale.getDefault())
                        else ch.toString()
                    }
                }
        }
        .map { "\"$it\"" }

    fun getSubdomainLogo(): Option<String> {
        return getFilePath("cover/repo/${orgRepoPair.second}.png")
    }

    val bg = getFilePath("cover/bg.png").getOrNull() ?: return
    val profile = when (coverCmd) {
        PrCover -> getFilePath("cover/profile.jpeg")
            .getOrNull()
            ?: return

        ReleaseCover -> getFilePath("cover/org/${orgRepoPair.first}.png")
            .getOrNull()
            ?: return
    }
    val heading = getHeading()
    val tokens = getCoverMdTokens()
    val abstract = getAbstract(tokens).getOrNull() ?: return
    val output = getTempOutputDir()
    val footer = getFooter(tokens)
    val subheading = when (coverCmd) {
        PrCover -> inferSubHeadingFromPr()
        ReleaseCover -> inferSubheadingFromRelease()
    }
    val subdomain = getSubdomainLogo()
    val version = when (coverCmd) {
        PrCover -> None
        ReleaseCover -> inferRepoVersion(index, repoPath)
    }
    val subversion = when (coverCmd) {
        PrCover -> None
        ReleaseCover -> inferSubheadingVersion(title)
    }
    val cmd = listOf(
        "tsd-fx $coverCmd",
        "--bg=$bg",
        "--profile-photo=$profile",
        "--heading=$heading",
        "--abstract=$abstract",
        "--output=$output",
        opt("footer", footer),
        opt("subheading", subheading),
        opt("subdomain", subdomain),
        opt("version", version),
        opt("subversion", subversion),
    )
        .filter { it.isNotBlank() }
        .reduce { acc, s -> "$acc $s" }

    runCommand(cmd)
        .onLeft(handleError `$` "Failed to generate cover image")
        .onRight { println("✔ Generate cover image") }
        .getOrNull() ?: return

    Files.move(Path.of(output), getBuildOutput())
}

fun getAbstract(tokens: List<String>): Option<String> = tokens
    .firstOrNone { !it.startsWith("`") }
    .map { it.replace("\n", "") }
    .map { "\"$it\"" }

fun getFooter(tokens: List<String>): Option<String> = tokens
    .firstOrNone { it.startsWith("- ") }
    .fold({ listOf() }, { it.split("\n") })
    .map { it.removePrefix("-").trim() }
    .filterNot(String::isBlank)
    .reduceOrNull { acc: String, s: String -> "$acc, $s" }
    .toOption()
    .map { "\"$it\"" }

fun getFilePath(resourceName: String): Option<String> {
    val path = AppFiles.pathOf(Path.of(resourceName))

    return if (path.exists()) path.absolutePathString().some()
    else None
}

fun splitPipe(data: String): Pair<String, String> = with(data.split("|")) {
    Pair(get(0).trim(), getOrElse(1) { "" }.trim())
}

fun removeSuffixAfterVersion(input: String): String {
    val versionPattern = Regex(""" v\d+(\.\d+)*$""")
    val matchResult = versionPattern.find(input)
    return matchResult?.let {
        val versionIndex = it.range.first
        input.substring(0, versionIndex)
    } ?: input
}

fun inferRepoVersion(index: String, repoPath: String): Option<String> {
    val githubUrlPattern =
        "https://github.com/$repoPath/releases/tag/v(\\d+\\.\\d+\\.\\d+)"
    val pattern = Pattern.compile(githubUrlPattern)
    val matcher = pattern.matcher(index)

    return if (matcher.find()) Some(matcher.group(1)) else None
}

fun inferSubheadingVersion(title: String): Option<String> = splitPipe(title)
    .let {
        if (it.second.isNotBlank()) { // has context or subheading
            val pattern = Pattern.compile("""v(\d+\.\d+\.\d+)""")
            val matcher = pattern.matcher(title)

            return if (matcher.find()) Some(matcher.group(1)) else None
        } else None
    }

fun extractOrgAndRepo(index: String): Pair<String, String>? {
    val pattern = Regex(
        """https://github\.com/([^/]+)/([^/]+)/(pull|releases)/"""
    )
    val matchResult = pattern.find(index)

    return matchResult?.let {
        val (org, repo, _) = it.destructured
        org to repo
    }.let {
        if (it == null) {
            println("Fail to infer Org and Repo from Article")
        }
        it
    }
}

fun extractBranchNames(text: String): List<String> {
    val branchNames = mutableListOf<String>()
    val pattern = Regex(
        """---\s*(.*?)\s*\{: \.pr-subtitle \}""",
        RegexOption.DOT_MATCHES_ALL
    )

    pattern
        .findAll(text)
        .forEach { match ->
            val extractedText = match.groupValues[1]

            val branchPattern = Regex("""`[\w/-]+ <- ([\w/-]+)`""")
            val branchMatchResult = branchPattern.find(extractedText)
            branchMatchResult?.let { branchNames.add(it.groupValues[1]) }
        }
    return branchNames
}
