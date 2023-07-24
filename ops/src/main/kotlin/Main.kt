import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.Either.Right
import arrow.core.None
import arrow.core.firstOrNone
import arrow.core.getOrElse
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import kotlin.io.path.name


fun main(args: Array<String>) {
    if (args.isNotEmpty()) {
        val fn = args.getOrElse(0) { "" }
        val arg1 = args.getOrElse(1) { "" }
        val arg2 = args.getOrElse(2) { "" }

        newFn(fn)
            .fold(
                { println("Invalid function: $fn") },
                { execute(it, arg1, arg2) }
            )
    }
}

fun execute(fn: Fn, arg1: String, arg2: String) = when (fn) {
    Fn.Entries -> exec_entries(arg1)
    Fn.Build -> exec_build(arg1, arg2)
    Fn.Deploy -> exec_deploy(arg1, arg2)
}

fun exec_entries(root: String) {
    val rootPath = Path.of(root)

    if (!Files.exists(rootPath)) {
        println("Root path doesn't exist $rootPath")
        return
    }
    entries(Entry(rootPath))
        .map { it.path.fileName }
        .forEach { println(it) }
}

fun exec_build(root: String, entryName: String) {
    val entries = entries(root `---` Path::of `---` ::Entry)

    if (entryName == ".") {
        entries
            .forEach { build(it, ::BuildConfig `$` Path.of(root, "_out")) }
    } else {
        entries
            .firstOrNone { it.name() == entryName }
            .onSome { build(it, ::BuildConfig `$` Path.of(root, "_out")) }
    }
}

fun build(entry: Entry, config: BuildConfig) {
    val (outDir) = config
    val entryDir = Path.of(outDir.toString(), entry.name())

    fun prepare() {
        if (!Files.exists(outDir)) {
            Files.createDirectory(outDir)
        }
        if (Files.exists(entryDir)) {
            deleteDirectory(entryDir)
        }
        Files.createDirectory(entryDir)

        copyDirectory(entry.path, entryDir)
    }

    prepare()
    val index = loadIndex(entry).getOrNull() ?: ""
    val title = toTitle(index, entry.path.fileName.toString())
    val abstract = toAbstract(index)
    val coverUrl = coverUrl(entry)

    val frontMatter = """
        ---
        permlink: /${title.link}
        title: ${title.title}
        description: $abstract
        ogimage: $coverUrl
        ---
        
        
    """.trimIndent()
    val toc = tocHtml(entry, index)

    val prod = frontMatter + toc + index
    saveIndex(entryDir, prod)
}

fun exec_deploy(root: String, entryName: String) {
    val entries = entries(root `---` Path::of `---` ::Entry)
    val config = DeployConfig(
        ::BuildConfig `$` Path.of(root, "_out"),
        Path.of("P:\\deployment\\tmp\\blog")
    )

    listOf(
        "git checkout gh-pages",
        "git pull origin gh-pages gh-pages",
    )
        .forEach { cmd ->
            runCommand(cmd, config.deployDir)
                .fold(
                    { println(it) },
                    { println(it) },
                )
        }

    if (entryName == ".") {
        entries
            .forEach { deploy(it, config) }
    } else {
        entries
            .firstOrNone { it.name() == entryName }
            .onSome { deploy(it, config) }
    }

    runCommand("git push origin gh-pages", config.deployDir)
        .fold(
            { println(it) },
            { println(it) },
        )
}

fun deploy(entry: Entry, config: DeployConfig) {
    fun deleteIfExists() {
        val path = Path.of(
            config.deployDir.toString(),
            entry.name(),
        )

        if (Files.isDirectory(path)) {
            deleteDirectory(path)
        }
    }

    fun copyFromBuild() {
        val src = Path.of(
            config.buildConfig.outDir.toString(),
            entry.name(),
        )
        val dst = Path.of(
            config.deployDir.toString(),
            entry.name(),
        )

        copyDirectory(src, dst)
    }

    println("Deploying ${entry.name()}...")

    build(entry, config.buildConfig)

    println("Built")

    deleteIfExists()
    copyFromBuild()

    println("Files copied")

    println("Committing...")

    listOf(
        "git add ${entry.name()}",
        "git commit -m \"Deploy ${entry.name()}\" --no-gpg-sign",
    )
        .forEach { cmd ->
            println(cmd)
            runCommand(cmd, config.deployDir)
                .fold(
                    { println(it) },
                    { println(it) },
                )
        }
}

fun coverUrl(entry: Entry): String =
    entry
        .coverPath()
        .map { entry.coverGitHubUrl("tobiasbriones", "blog", it.name + "/") }
        .getOrElse { "" }

fun tocHtml(entry: Entry, index: String) = """
    <nav>
        <a href="/" class="home">
            <span class="material-symbols-rounded">
            home
            </span>
            <span>
            Blog
            </span>
        </a>
        
        <div class="article">
            <a href="#" class="title">${entry.name()}</a>

            ${tocListHtml(index)}
        </div>
    </nav>
    
    
""".trimIndent()

fun tocListHtml(markdown: String): String {
    val headingRegex =
        Regex("""^##\s*(.+)$""") // Regex to match h2 or ## headings
    val lines = markdown.lines()
    val htmlBuilder = StringBuilder("<ul>")

    var currentIndent = 0

    for (line in lines) {
        val matchResult = headingRegex.find(line)
        if (matchResult != null) {
            val headingText =
                matchResult.groupValues[1].replaceFirst("^#+\\s*".toRegex(), "")
                    .trim()
            val indent = line.takeWhile { it == ' ' }.length / 2

            if (indent > currentIndent) {
                htmlBuilder.append("<ul>")
                htmlBuilder.append("\n")
            } else if (indent < currentIndent) {
                repeat(currentIndent - indent) { htmlBuilder.append("</ul>") }
            }

            htmlBuilder.append(
                """
                <li><a href="#${
                    headingText.lowercase(Locale.getDefault()).replace(' ', '-')
                }">
                $headingText</a>
                """
            )
            currentIndent = indent
        }
    }

    repeat(currentIndent) { htmlBuilder.append("</ul>") }
    htmlBuilder.append("</ul>")

    return htmlBuilder.toString()
}

data class BuildConfig(
    val outDir: Path
)

data class DeployConfig(
    val buildConfig: BuildConfig,
    val deployDir: Path,
)

enum class Fn {
    Entries,
    Build,
    Deploy,
}

fun newFn(value: String): Either<None, Fn> = when (value.lowercase()) {
    "entries" -> Right(Fn.Entries)
    "build" -> Right(Fn.Build)
    "deploy" -> Right(Fn.Deploy)
    else -> Left(None)
}
