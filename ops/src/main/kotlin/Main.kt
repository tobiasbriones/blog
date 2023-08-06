import Op.*
import arrow.core.*
import fs.*
import html.*
import jekyll.*
import md.*
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.*


fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Provide arguments")
        return
    }
    val arg: (Int) -> String = { args.getOrElse(it) { "" } }
    val op = arg(0)

    newOp(op)
        .fold(
            { println("Invalid operation: $op. Valid ops are: ${Op.values()}") },
            { execute(it, arg(1), arg(2)) }
        )
}

fun execute(op: Op, arg1: String, arg2: String): Unit = when (op) {
    Entries -> execEntries(arg1)
    Build -> execBuild(arg1, arg2)
    Deploy -> execDeploy(arg1, arg2)
}

fun execEntries(root: String): Unit = with(Path.of(root)) {
    if (notExists()) {
        println("Root path doesn't exist $this")
        return
    }
    Entry(this)
        .loadEntries()
        .map { paths -> paths.map { it.path.fileName } }
        .fold(::println) { paths ->
            paths.forEach(::println)
        }
}

fun execBuild(root: String, entryName: String) {
    val entries: List<Entry> = (root `---` Path::of `---` ::Entry)
        .loadEntries()
        .fold(
            {
                println("Failed to load entries for root $root: $it")
                listOf()
            },
            { it }
        )
    val config = ::BuildConfig `$` Path.of(
        root,
        "out",
        Path.of(root).name
    )

    if (entryName == ".") {
        entries
            .forEach { build(it, config) }
    } else {
        entries
            .firstOrNone { it.name() == entryName }
            .onSome { build(it, config) }
    }
}

fun build(entry: Entry, config: BuildConfig) {
    val (outDir) = config
    val entryDir = Path.of(outDir.toString(), entry.name())

    fun prepare() {
        if (outDir.notExists()) {
            outDir.createDirectories()
        }
        if (entryDir.exists()) {
            deleteDirectory(entryDir)
        }
        entryDir.createDirectory()

        copyDirectory(entry.path, entryDir)
    }

    prepare()
    val index = entry
        .loadIndex()
        .mapLeft { "Failed to load entry index for entry $entry: $it" }
        .onLeft(::println)
        .getOrNull() ?: return

    val subdirNav = entry
        .generateSubdirectoriesNav()
        .mapLeft {
            """
            Failed to generate navigation of article subdirectories for entry
             $entry: $it
        """.trimIndent()
        }
        .onLeft(::println)
        .getOrNull() ?: return

    val permalink = entry.path.name
    val title = index.extractTitle()
    val abstract = index.extractAbstract()
    val coverUrl = coverUrl(entry)
    val jekyll = JekyllIndex(
        FrontMatter(
            permalink,
            title,
            Some(abstract),
            Some(coverUrl)
        ),
        index.generateNav(),
        index,
        subdirNav,
    )

    saveIndex(entryDir, jekyll.toMarkdownString())

    buildSubdirectories(config.outDir, entry)
}

fun Entry.generateSubdirectoriesNav(): Either<String, Option<Div>> {
    val filter: (Path) -> Boolean = { it.name != "images" }

    return loadSubdirectories()
        .map { paths ->
            paths
                .filter(filter)
                .map(Path::name)
                .toList()
                .toOption()
                .flatMap { if (it.isEmpty()) None else Some(it) }
                .map { it.subDirectoriesNav() }
        }
}

fun buildSubdirectories(outDir: Path, entry: Entry) {
    val filter: (Path) -> Boolean = { it.name != "images" }

    entry
        .loadSubdirectories()
        .fold(
            { print(it) },
            {
                it.filter(filter)
                    .forEach {
                        buildSubdirectory(
                            outDir,
                            entry.relPath,
                            entry.name(),
                            it
                        )
                    }
            }
        )
}

fun buildSubdirectory(
    outDir: Path,
    entryRelPath: Path,
    entryName: String,
    relPath: Path
) {
    val subPath = Path.of(entryName, relPath.toString())
    val path = Path.of(outDir.toString(), subPath.toString())

    if (path.isDirectory()) {
        Files
            .list(path)
            .forEach {
                buildSubdirectory(
                    outDir,
                    entryRelPath,
                    entryName,
                    Path.of(relPath.toString(), it.name)
                )
            }
        addDirectoryIndex(outDir, entryName, entryRelPath, relPath)
    } else {
        addContentIndex(outDir, entryName, entryRelPath, relPath)
    }
}

fun addDirectoryIndex(
    outDir: Path,
    entryName: String,
    entryRelPath: Path, // src entry path, i.e. tags. e.g. /swe/dev/.../article
    relPath: Path
) {
    val subPath = Path.of(entryName, relPath.toString())
    val path = Path.of(outDir.toString(), subPath.toString())
    val githubPath = entryRelPath
        .parent
        .toString()
        .replace("\\", "/")
        .let { "$it/${subPath.toString().replace("\\", "/")}" }
        .let { "tree/main$it" }
    val name = relPath.fileName.toString()
    val navigationTreeHtml =
        path `---` Files::list `---` ::createNavigationTreeHtml
    val frontMatter = FrontMatter(
        subPath.toString().replace("\\", "/"),
        subPath.toString().replace("\\", "/"),
    )
    val sb = StringBuilder()

    sb.append(frontMatter.toMarkdownString())
    sb.append("\n")
    sb.append("# $name")
    sb.append("\n")
    sb.append(navigationTreeHtml)
    sb.append("\n")
    sb.append(openInGitHubButton(githubPath).toHtmlString())
    val index = sb.toString()

    saveIndex(path, index)
}

fun addContentIndex(
    outDir: Path,
    entryName: String,
    entryRelPath: Path,
    relPath: Path
) {
    val subPath = Path.of(entryName, relPath.toString())
    val path = Path.of(outDir.toString(), subPath.toString())
    val githubPath = entryRelPath
        .parent
        .toString()
        .replace("\\", "/")
        .let { "$it/${subPath.toString().replace("\\", "/")}" }
        .let { "tree/main$it" }
    val name = relPath.fileName.toString()
    val fileContentHtml = createContentHtml(path)
    val frontMatter = FrontMatter(
        subPath.toString().replace("\\", "/"),
        subPath.toString().replace("\\", "/"),
    )
    val sb = StringBuilder()

    sb.append(frontMatter.toMarkdownString())
    sb.append("\n")
    sb.append("# $name")
    sb.append("\n")
    sb.append(fileContentHtml)
    sb.append("\n")
    sb.append(openInGitHubButton(githubPath).toHtmlString())
    val index = sb.toString()

    val fileDir = Path.of(path.parent.toString(), name)

    path.deleteIfExists()
    fileDir.createDirectory()

    saveIndex(fileDir, index)
}

fun createContentHtml(path: Path): String {
    return when (val ext = getFileExtension(path)) {
        "png", "jpg", "gif" -> createImageHtml(path)
        else -> codeSnippetBlockHtml(
            FileResource(
                Files.readString(path), ResourceExtension(ext)
            )
        )
    }
}

fun createImageHtml(path: Path): String {
    val name = path.name
    return """
        <img src=${path} alt="$name" />
    """".trimIndent()
}

fun execDeploy(root: String, entryName: String) {
    val entries = (root `---` Path::of `---` ::Entry)
        .loadEntries()
        .mapLeft { "Failed to load entries for root $root: $it" }
        .onLeft(::println)
        .getOrNull() ?: return

    val config = DeployConfig(
        ::BuildConfig `$` Path.of(root, "_out"),
        Path.of("P:\\deployment\\tmp\\blog")
    )

//    listOf(
//        "git checkout gh-pages",
//        "git pull origin gh-pages gh-pages",
//    )
//        .forEach { cmd ->
//            runCommand(cmd, config.deployDir)
//                .fold(
//                    { println(it) },
//                    { println(it) },
//                )
//        }

    if (entryName == ".") {
        entries
            .forEach { deploy(it, config) }
    } else {
        entries
            .firstOrNone { it.name() == entryName }
            .onSome { deploy(it, config) }
            .onNone { println("Entry not found: $entryName") }
    }


    fun deployIndex() {
        fun name(entry: Entry) = entry
            .loadIndex()
            .fold(
                {
                    println(it)
                    entry.name()
                },
                {
                    it.extractTitle()
                }
            )

        val index = """
            |# Blog | Math Software Engineer
            |
            |${
            entries.joinToString("\n") {
                "- [${name(it)}](${it.name()})"
            }
        }
        """.trimMargin("|")

        Path.of(config.deployDir.toString(), "index.md")
            .writeText(index)
    }

    deployIndex()

//    runCommand("git push origin gh-pages", config.deployDir)
//        .fold(
//            { println(it) },
//            { println(it) },
//        )
}

fun deploy(entry: Entry, config: DeployConfig) {
    val rootDeployPath = Path.of(
        config.deployDir.toString(),
//        "blog",
    )

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
            rootDeployPath.toString(),
            entry.name(),
        )

//        if (!rootDeployPath.exists()) {
//            rootDeployPath.createDirectory()
//        }
        copyDirectory(src, dst)
    }

    println("Deploying ${entry.name()}...")

    build(entry, config.buildConfig)

    println("Built")

    deleteIfExists()
    copyFromBuild()

    println("Files copied")

//    println("Committing...")
//
//    listOf(
//        "git add ${entry.name()}",
//        "git commit -m \"Deploy ${entry.name()}\" --no-gpg-sign",
//    )
//        .forEach { cmd ->
//            println(cmd)
//            runCommand(cmd, config.deployDir)
//                .fold(
//                    { println(it) },
//                    { println(it) },
//                )
//        }
}

fun coverUrl(entry: Entry): String =
    entry
        .coverPath()
        .map { entry.coverGitHubUrl("tobiasbriones", "blog", it.name + "/") }
        .getOrElse { "" }
