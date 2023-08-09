import Cmd.*
import arrow.core.*
import fs.*
import html.Attribute
import html.Div
import html.Img
import html.toHtmlString
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
    val cmd = arg(0)
    val root = getRootPath()
        .mapLeft { "Failed to load project root path: $it" }
        .onLeft(::println)
        .getOrNull() ?: return

    newOp(cmd)
        .fold(
            { println("Invalid operation: $cmd. Valid ops are: ${Cmd.values()}") },
            { execute(it, root, arg(1)) }
        )
}

fun execute(cmd: Cmd, root: Path, arg1: String): Unit = when (cmd) {
    Entries -> execEntries(root)
    Build -> execBuild(root, arg1)
    Deploy -> execDeploy(root, arg1)
}

fun execEntries(root: Path) {
    Entry(root)
        .loadEntries()
        .map { paths -> paths.map { it.path.fileName } }
        .mapLeft { "Failed to load entries at root $root: $it" }
        .fold(::println) { paths ->
            paths.forEach(::println)
        }
}

fun execBuild(root: Path, entryName: String) {
    val config = buildConfigOf(root)

    val buildAll: (List<Entry>) -> Unit = { entries ->
        println("Building ${entries.size} articles...")
        entries.forEach { build(it, config) }
    }

    val buildEntry: (List<Entry>) -> Unit = { entries ->
        entries
            .firstOrNone { it.name() == entryName }
            .onSome { build(it, config) }
            .onNone { println("Failed to build, entry not found: $entryName") }
    }

    val route: (List<Entry>) -> Unit = { entries ->
        if (entryName == ".") buildAll(entries) else buildEntry(entries)
    }

    Entry(root)
        .loadEntries()
        .fold(
            handleError("Failed to load entries for root $root"),
            route
        )
}

fun buildConfigOf(root: Path): BuildConfig =
    BuildConfig(
        root,
        Path.of(
            root.toString(),
            "out",
            "build",
            root.name,
        ),
    )

fun build(entry: Entry, config: BuildConfig) {
    val (srcDir, outDir) = config
    val entryDir = Path.of(outDir.toString(), entry.name())

    fun prepare() {
        if (outDir.notExists()) {
            outDir.createDirectories()
            copyJekyllRootFiles(outDir)
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
        .mapLeft(handleError("Failed to load entry index for entry $entry"))
        .getOrNull() ?: return

    val subdirNav = entry
        .generateSubdirectoriesNav()
        .mapLeft(
            handleError(
                """Failed to generate navigation of article subdirectories for 
                   entry $entry""".trimIndent()
            )
        )
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
    buildIndex(srcDir, outDir)
    buildSubdirectories(outDir, entry)
    println("✔ Built article: ${entry.name()}")
}

fun buildIndex(srcDir: Path, outDir: Path) {
    val title: (Entry) -> Either<Unit, String> = { entry ->
        entry
            .loadIndex()
            .mapLeft(handleError("Failed to load index for entry $entry"))
            .map(Index::extractTitle)
    }

    Entry(srcDir)
        .loadEntries()
        .mapLeft(handleError("Failed to load entries for root $srcDir"))
        .map { entries ->
            """
            |# Blog | Math Software Engineer
            |
            |${
                entries.joinToString("\n") {
                    "- [${title(it).getOrElse { "" }}](${it.name()})"
                }
            }
        """.trimMargin("|")
        }
        .map { index ->
            Path
                .of(outDir.toString(), "index.md")
                .writeText(index)
        }
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
    val fileContentHtml = createContentMarkdownString(path)
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

    val fileDir = Path.of("${path}_")

    fileDir.createDirectory()
    path.moveTo(fileDir.resolve(name))
    fileDir.moveTo(path)

    saveIndex(path, index)
}

fun createContentMarkdownString(path: Path): String {
    return when (val ext = getFileExtension(path)) {
        "png", "jpg", "gif" -> createImageHtml(path)
        else -> codeSnippetBlockHtml(
            FileResource(
                Files.readString(path), ResourceExtension(ext)
            )
        )
    }
}

fun createImageHtml(path: Path): String = Img(
    attributes = mapOf(
        Attribute.Src to listOf(path.name),
        Attribute.Alt to listOf(path.name),
    )
).toHtmlString()

fun execDeploy(root: Path, entryName: String) {
    val gitClean = runCommand("git status --porcelain")
        .onLeft(::handleError `$` "Failed to check Git status")
        .map { it `---` String::trim `---` String::isEmpty }
        .getOrNull() ?: return

    if (!gitClean) {
        println("Git repository has uncommitted changes")
        return
    }

    println("Deploying ${entryName}...")

    runCommand("git checkout main")
        .onLeft(::handleError `$` "Failed to checkout to branch main")
        .onRight { println("✔ Checkout to branch main") }
        .getOrNull() ?: return

    runCommand("git pull")
        .onLeft(::handleError `$` "Failed to pull to branch main")
        .onRight { println("✔ Update branch main") }
        .getOrNull() ?: return

    execBuild(root, entryName)

    runCommand("git checkout gh-pages")
        .onLeft(::handleError `$` "Failed to checkout to branch gh-pages")
        .onRight { println("✔ Checkout to branch gh-pages") }
        .getOrNull() ?: return

    runCommand("git pull")
        .onLeft(::handleError `$` "Failed to pull to branch gh-pages")
        .onRight { println("✔ Update branch gh-pages") }
        .getOrNull() ?: return

    val entries = Entry(root)
        .loadEntries()
        .onLeft(::handleError `$` "Failed to load entries for root $root")
        .getOrNull() ?: return

    val config = buildConfigOf(root)

    if (entryName == ".") {
        entries.forEach { commitFromBuild(it, config) }
    } else {
        entries
            .firstOrNone { it.name() == entryName }
            .onSome { commitFromBuild(it, config) }
            .onNone { println("Entry not found: $entryName") }
    }

    runCommand("git push origin gh-pages")
        .onLeft(::handleError `$` "Failed to push branch gh-pages")
        .onRight { println("✔ Push branch gh-pages to origin") }
        .getOrNull() ?: return

    runCommand("git checkout main")
        .onLeft(::handleError `$` "Failed to checkout to branch main")
        .onRight { println("✔ Checkout to branch main") }
        .getOrNull() ?: return

    println("✔ Deploy $entryName")
}

fun commitFromBuild(entry: Entry, config: BuildConfig) {
    val (srcDir, outDir) = config
    val articleProdPath = srcDir.resolve(entry.name())
    val articleBuildPath = outDir.resolve(entry.name())
    val indexBuildPath = outDir.resolve("index.md")
    val indexProdPath = srcDir.resolve("index.md")

    if (articleBuildPath.notExists()) {
        println("❌ Article build directory does not exist")
        return
    }
    if (indexBuildPath.notExists()) {
        println("❌ Index build file does not exist")
        return
    }

    indexProdPath.deleteIfExists()
    indexBuildPath.copyTo(indexProdPath)

    deleteDirectory(articleProdPath)
        .onLeft(
            ::handleError `$` "Failed to delete (clean) production directory"
        )
        .getOrNull() ?: return

    copyDirectory(articleBuildPath, articleProdPath)
        .onLeft(
            ::handleError `$` "Failed to copy build to production directory"
        )
        .getOrNull() ?: return

    runCommand("git add ${entry.name()} index.md")
        .onLeft(::handleError `$` "Failed to add files to Git")
        .onRight { println("✔ Add files") }
        .getOrNull() ?: return

    runCommand("""git commit -m "Deploy ${entry.name()}" --no-gpg-sign""")
        .onLeft(::handleError `$` "Failed to commit files to Git")
        .onRight { println("✔ Commit files") }
        .getOrNull() ?: return
}

fun handleError(errorMsg: String): (String) -> Unit = { cause ->
    println("$errorMsg: $cause")
}

fun coverUrl(entry: Entry): String =
    entry
        .coverPath()
        .map {
            entry.coverGitHubUrl(
                "tobiasbriones",
                "blog",
                it.name + "/"
            )
        }
        .getOrElse { "" }
