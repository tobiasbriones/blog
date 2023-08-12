import Cmd.*
import arrow.core.*
import fs.*
import html.Attribute
import html.Div
import html.Img
import html.toHtmlString
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import jekyll.*
import md.*
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.*

object Error {
    var failed: Boolean = false
        private set

    fun handle(errorMsg: String): (String) -> Unit = { cause ->
        print("$errorMsg: $cause")
    }

    fun print(errorMsg: String) {
        failed = true
        println("❌ $errorMsg")
    }
}

val handleError: (String) -> (String) -> Unit = { Error::handle `$` it }
val printError: (String) -> Unit = { Error::print `$` it }

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        printError `$` "Provide arguments"
        return
    }
    val arg: (Int) -> String = { args.getOrElse(it) { "" } }
    val cmd = arg(0)
    val root = getRootPath()
        .onLeft { printError `$` "Failed to load project root path: $it" }
        .getOrNull() ?: return

    newOp(cmd)
        .fold(
            {
                printError `$` """
                    Invalid operation: $cmd.
                    Valid ops are: ${
                    Cmd.values().toList().map { it.name.lowercase() }
                }
                """.trimIndent()
            },
            { execute(it, root, arg(1), arg(2)) }
        )
}

fun execute(
    cmd: Cmd,
    root: Path,
    arg1: String,
    arg2: String,
): Unit = when (cmd) {
    Entries -> execEntries(root)
    Build -> execBuild(root, arg1, arg2 == "jekyll")
    Deploy -> execDeploy(root, arg1)
    Serve -> execServe()
}

fun execEntries(root: Path) {
    Entry(root)
        .loadEntries()
        .map { paths -> paths.map { it.path.fileName } }
        .onLeft(handleError `$` "Failed to load entries at $root")
        .onRight { paths ->
            println("✔ ${paths.size} entries at $root")
            paths.forEach(::println)
        }
}

fun execBuild(root: Path, entryName: String, jekyll: Boolean = false) {
    val config = buildConfigOf(root)

    val buildAll: (List<Entry>) -> Unit = { entries ->
        println("⚙ Building ${entries.size} articles at $root...")
        entries.forEach { build(it, config) }
    }

    val buildEntry: (List<Entry>) -> Unit = { entries ->
        println("⚙ Building $entryName at $root...")
        entries
            .firstOrNone { it.name() == entryName }
            .onSome { build(it, config) }
            .onNone {
                printError `$` "Failed to build, entry not found: $entryName"
            }
    }

    val route: (List<Entry>) -> Unit = { entries ->
        if (entryName == ".") buildAll(entries) else buildEntry(entries)
    }

    if (entryName.isBlank()) {
        printError `$` "Enter the entry name argument"
        return
    }

    Entry(root)
        .loadEntries()
        .onLeft(handleError `$` "Failed to load entries for root $root")
        .map(route)

    if (jekyll) {
        buildJekyll(config.outDir)
    }
}

fun buildJekyll(outDir: Path) {
    println("Building Jekyll site...")

    runCommand(
        "wsl bundle exec jekyll clean",
        outDir
    )
        .onLeft(handleError `$` "Failed to build Jekyll site")
        .onRight(::println)

    runCommand(
        "wsl bundle exec jekyll build",
        outDir
    )
        .onLeft(handleError `$` "Failed to build Jekyll site")
        .onRight(::println)

    println("✔ Build Jekyll site")
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
    val outEntryDir = Path.of(outDir.toString(), entry.name())

    fun prepare() {
        if (outDir.notExists()) {
            outDir.createDirectories()
        }
        if (outEntryDir.exists()) {
            deleteDirectory(outEntryDir)
        }
        outEntryDir.createDirectory()

        copyJekyllRootFiles(outDir)
        copyDirectory(entry.path, outEntryDir) { it.shouldBeCopied() }
    }

    prepare()
    val index = entry
        .loadIndex()
        .mapLeft { handleError `$` "Failed to load entry index for entry $entry" }
        .getOrNull() ?: return

    val subdirNav = entry
        .generateSubdirectoriesNav()
        .onLeft(
            handleError `$` """
                Failed to generate navigation of article subdirectories for entry $entry
                """.trimIndent()

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

    saveIndex(outEntryDir, jekyll.toMarkdownString())
    buildIndex(srcDir, outDir)
    buildSubdirectories(outDir, entry)
    println("✔ Build article ${entry.name()}")
}

fun buildIndex(srcDir: Path, outDir: Path) {
    val title: (Entry) -> Either<Unit, String> = { entry ->
        entry
            .loadIndex()
            .mapLeft {
                handleError `$` "Failed to load index for entry $entry" `$` it
            }
            .map(Index::extractTitle)
    }

    Entry(srcDir)
        .loadEntries()
        .mapLeft(handleError `$` "Failed to load entries for root $srcDir")
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
    return loadSubdirectories()
        .map { paths ->
            paths
                .filter(Path::isBrowsable)
                .map(Path::name)
                .toList()
                .toOption()
                .flatMap { if (it.isEmpty()) None else Some(it) }
                .map { it.subDirectoriesNav() }
        }
}

fun buildSubdirectories(outDir: Path, entry: Entry) {
    entry
        .loadSubdirectories()
        .fold(printError) { paths ->
            paths.filter(Path::isBrowsable)
                .forEach {
                    buildSubdirectory(
                        outDir,
                        entry.relPath,
                        entry.name(),
                        it
                    )
                }
        }
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

    if (!path.isBrowsable()) {
        return
    }

    val githubPath = entryRelPath
        .parent
        .toString()
        .replace("\\", "/")
        .let { "$it/${subPath.toString().replace("\\", "/")}" }
        .let { "tree/main$it" }
    val name = relPath.fileName.toString()
    val fileContentHtml = createContentMarkdownString(path)
    val frontMatter = FrontMatter(
        subPath.toString().replace("\\", "/") + ".html",
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
        "md" -> Files.readString(path)
        else -> codeSnippetBlockHtml(
            FileResource(
                Files.readString(path),
                ResourceExtension(ext),
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
        .onLeft(handleError `$` "Failed to check Git status")
        .map { it `---` String::trim `---` String::isEmpty }
        .getOrNull() ?: return

    if (!gitClean) {
        printError `$` "Git repository ${root.name} has uncommitted changes"
        return
    }

    if (entryName.isBlank()) {
        printError `$` "Enter the entry name argument"
        return
    }

    println("⚙ Deploying ${if (entryName == ".") "all" else entryName} at $root...")

    runCommand("git checkout main")
        .onLeft(handleError `$` "Failed to checkout to branch main")
        .onRight { println("✔ Checkout to branch main") }
        .getOrNull() ?: return

    runCommand("git pull")
        .onLeft(handleError `$` "Failed to pull to branch main")
        .onRight { println("✔ Update branch main") }
        .getOrNull() ?: return

    execBuild(root, entryName)

    if (Error.failed) {
        return
    }

    val entries = Entry(root)
        .loadEntries()
        .onLeft(handleError `$` "Failed to load entries for root $root")
        .getOrNull() ?: return

    runCommand("git checkout gh-pages")
        .onLeft(handleError `$` "Failed to checkout to branch gh-pages")
        .onRight { println("✔ Checkout to branch gh-pages") }
        .getOrNull() ?: return

    runCommand("git pull")
        .onLeft(handleError `$` "Failed to pull to branch gh-pages")
        .onRight { println("✔ Update branch gh-pages") }
        .getOrNull() ?: return

    val config = buildConfigOf(root)

    commitRootFiles(config.srcDir)

    if (entryName == ".") {
        entries.takeWhile {
            commitFromBuild(it, config)
            !Error.failed
        }
    } else {
        entries
            .firstOrNone { it.name() == entryName }
            .onSome { commitFromBuild(it, config) }
            .onNone { printError `$` "Entry not found: $entryName" }
    }

    if (Error.failed) {
        return
    }

    runCommand("git push origin gh-pages")
        .onLeft(handleError `$` "Failed to push branch gh-pages")
        .onRight { println("✔ Push branch gh-pages to origin") }
        .getOrNull() ?: return

    runCommand("git checkout main")
        .onLeft(handleError `$` "Failed to checkout to branch main")
        .onRight { println("✔ Checkout to branch main") }
        .getOrNull() ?: return

    println("✔ Deploy $entryName")
}

fun commitRootFiles(srcDir: Path) {
    copyJekyllRootFiles(srcDir)

    val gitClean = runCommand("git status --porcelain")
        .onLeft(handleError `$` "Failed to check Git status")
        .map { it `---` String::trim `---` String::isEmpty }
        .getOrNull() ?: return

    if (gitClean) {
        return
    }

    runCommand("git add .", srcDir)
        .onLeft(handleError `$` "Failed to add root files to Git")
        .onRight { println("✔ Add root files to Git") }
        .getOrNull() ?: return

    runCommand("""git commit -m "Add root files" --no-gpg-sign""")
        .onLeft(handleError `$` "Failed to commit root files to Git")
        .onRight { println("✔ Commit root files to Git") }
        .getOrNull() ?: return
}

fun commitFromBuild(entry: Entry, config: BuildConfig) {
    val (srcDir, outDir) = config
    val articleProdPath = srcDir.resolve(entry.name())
    val articleBuildPath = outDir.resolve(entry.name())
    val indexBuildPath = outDir.resolve("index.md")
    val indexProdPath = srcDir.resolve("index.md")

    println("Committing build of ${entry.name()}")
    if (articleBuildPath.notExists()) {
        printError `$` "Article build directory does not exist"
        return
    }
    if (indexBuildPath.notExists()) {
        printError `$` "Index build file does not exist"
        return
    }

    indexProdPath.deleteIfExists()
    indexBuildPath.copyTo(indexProdPath)

    if (articleProdPath.exists()) {
        deleteDirectory(articleProdPath)
            .onLeft(
                handleError `$` "Failed to delete (clean) production directory"
            )
            .getOrNull() ?: return
    }

    copyDirectory(articleBuildPath, articleProdPath)
        .onLeft(
            handleError `$` "Failed to copy build to production directory"
        )
        .getOrNull() ?: return

    val gitClean = runCommand("git status --porcelain")
        .onLeft(handleError `$` "Failed to check Git status")
        .map { it `---` String::trim `---` String::isEmpty }
        .getOrNull() ?: return

    if (gitClean) {
        println(
            "No changes for entry ${entry.name()}, cancelling its deployment"
        )
        return
    }

    runCommand("git add ${entry.name()} index.md", srcDir)
        .onLeft(handleError `$` "Failed to add files to Git")
        .onRight { println("✔ Add files to Git") }
        .getOrNull() ?: return

    runCommand("""git commit -m "Deploy ${entry.name()}" --no-gpg-sign""")
        .onLeft(handleError `$` "Failed to commit files to Git")
        .onRight { println("✔ Commit files to Git") }
        .getOrNull() ?: return
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

fun execServe() {
    embeddedServer(
        Netty,
        port = 8080,
        module = Application::serve
    ).start(wait = true)
}

fun Application.serve() {
    val root = Path.of(
        "", "out", "build", "test-blog-deploy", "_site"
    )

    routing {
        get("/") {
            call.respondFile(
                root.resolve("index.html").toFile()
            )
        }

        get("/{path...}") {
            val pathSegments = call.parameters.getAll("path") ?: emptyList()
            val resPath = pathSegments.joinToString("/")
                .replace(
                    Regex("\\.html+"),
                    ""
                ) // ->
            // Fix of bug, it leads fp-in-kotlin to fp-in-kotlin.html.html
            // .html...

            println(resPath)

            val file = root.resolve(resPath)

            if (file.isRegularFile()) {
                call.respondFile(file.toFile())
            } else {
                call.respondFile(root.resolve("$resPath.html").toFile())
            }
        }
    }
}
