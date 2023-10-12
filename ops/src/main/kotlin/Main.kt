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
import java.io.File
import java.nio.charset.MalformedInputException
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

val ignoredPaths: Set<String> = getIgnoredRelPaths()

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
    Serve -> execServe(root)
    Create -> execCreate(root, arg1, arg2)
    Notice -> execNotice(root)
}

fun execCreate(root: Path, entryName: String, tags: String) {
    val entries = Entry(root)
        .loadEntries()
        .map { paths -> paths.map { it.path.name } }
        .onLeft(handleError `$` "Failed to load entries at $root")
        .getOrNull() ?: return

    if (!Regex("^(?!.*[\\sA-Z])[a-z0-9_-]+\$").matches(entryName)) {
        printError `$` "Value $entryName is an invalid entry name"
        return
    }
    if (entries.contains(entryName)) {
        printError `$` "Entry $entryName already exists"
        return
    }

    val relPath = tags
        .split(",")
        .joinToString(File.separator)
        .plus("${File.separator}$entryName")

    val path = root.resolve(relPath)
    val entry = Entry(path)
    val title = entry.toTitleCase(dic)

    runCommand("git checkout main")
        .onLeft(handleError `$` "Failed to checkout to branch main")
        .onRight { println("✔ Checkout to branch main") }
        .getOrNull() ?: return

    runCommand("git pull")
        .onLeft(handleError `$` "Failed to pull to branch main")
        .onRight { println("✔ Update branch main") }
        .getOrNull() ?: return

    runCommand("git checkout -b ${entry.name()}")
        .onLeft(handleError `$` "Failed to checkout to branch ${entry.name()}")
        .onRight { println("✔ Checkout to branch ${entry.name()}") }
        .getOrNull() ?: return

    path.createDirectories()

    saveIndex(
        path, """
        <!-- Copyright (c) 2023 Tobias Briones. All rights reserved. -->
        <!-- SPDX-License-Identifier: CC-BY-4.0 -->
        <!-- This file is part of https://github.com/tobiasbriones/blog -->

        # $title

    """.trimIndent()
    )

    runCommand("git add ${path.resolve("index.md")}", root)
        .onLeft(handleError `$` "Failed to add files to Git")
        .onRight { println("✔ Add files to Git") }
        .getOrNull() ?: return

    runCommand("""git commit -m "Create entry ${entry.name()}"""")
        .onLeft(handleError `$` "Failed to commit files to Git")
        .onRight { println("✔ Commit files to Git") }
        .getOrNull() ?: return

    println("""✔ Create entry "$title" at $path""")
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

fun execBuild(root: Path, target: String, jekyll: Boolean = false) {
    val config = buildConfigOf(root)

    val buildAll: (List<Entry>) -> Unit = { entries ->
        println("⚙ Building ${entries.size} articles at $root...")
        entries.forEach { build(it, config) }
    }

    val buildEntry: (List<Entry>, String) -> Unit = { entries, entryName ->
        println("⚙ Building $entryName at $root...")
        entries
            .firstOrNone { it.name() == entryName }
            .onSome { build(it, config) }
            .onNone {
                printError `$` "Failed to build, entry not found: $entryName"
            }
    }

    val buildDefault: (List<Entry>) -> Unit = { entries ->
        runCommand("git branch --show-current")
            .onLeft(handleError `$` "Failed to check current branch")
            .getOrNull()
            ?.trim()
            ?.run {
                if (this == "main") buildAll(entries)
                else buildEntry(entries, this)
            }
    }

    val route: (List<Entry>) -> Unit = { entries ->
        if (target == ".") buildDefault(entries)
        else buildEntry(entries, target)
    }

    if (target.isBlank()) {
        printError `$` "Enter the target argument"
        return
    }

    checkCurrentBranchIsNotGhPages()

    if (Error.failed) {
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
        "bundle exec jekyll clean",
        outDir
    )
        .onLeft(handleError `$` "Failed to build Jekyll site")
        .onRight(::println)

    runCommand(
        "bundle exec jekyll build",
        outDir
    )
        .onLeft(handleError `$` "Failed to build Jekyll site")
        .onRight(::println)

    println("✔ Build Jekyll site")
}

fun checkCurrentBranchIsNotGhPages() {
    val branch = runCommand("git branch --show-current")
        .onLeft(handleError `$` "Failed to check current branch")
        .getOrNull()
        ?.trim()

    if (branch == "gh-pages") {
        printError `$` "Current branch is gh-pages, move to another branch"
    }
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

    fun isIgnoredPath(path: Path): Boolean =
        ignoredPaths // if it's a directly ignored directory
            .contains(
                path
                    .relativeTo(srcDir)
                    .toString()
                    .replace("\\", "/")
            )

    fun prepare() {
        if (outDir.notExists()) {
            outDir.createDirectories()
        }
        if (outEntryDir.exists()) {
            deleteDirectory(outEntryDir)
        }
        outEntryDir.createDirectory()

        copyJekyllRootFiles(outDir)
        copyDirectory(entry.path, outEntryDir) {
            it.shouldBeCopied() && !isIgnoredPath(it)
        }

        // workaround to remove ignored subdirectories not listed from Git
        // ignore
        deleteEmptyDirectories(outEntryDir)
    }

    fun copyStaticFiles() {
        val staticDir = entry.path.resolve("static")
        val outStaticDir = outEntryDir.resolve("static")

        if (!staticDir.exists()) {
            return
        }
        if (!outStaticDir.exists()) {
            outStaticDir.createDirectory()
        }

        Files
            .list(staticDir)
            .filter(Path::isRegularFile)
            .forEach {
                it.copyTo(
                    outStaticDir.resolve(it.name),
                    overwrite = true,
                )
            }
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

    val home = Entry(srcDir).toTitleCase(dic)
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
        index.generateNav(home),
        index.generateToC(),
        index.parse(entry, dic),
        subdirNav,
    )

    jekyll
        .saveNavigation(outDir)
        .onLeft(printError)

    jekyll
        .saveToc(outDir)
        .onLeft(printError)

    saveIndex(outEntryDir, jekyll.toMarkdownString())
    buildIndex(srcDir, outDir)
    buildSubdirectories(outDir, entry)
    copyStaticFiles()

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
                .map { it.subDirectoriesNav(dic) }
        }
}

fun buildSubdirectories(outDir: Path, entry: Entry) {
    entry
        .loadSubdirectories()
        .fold(printError) { paths ->
            paths.filter(Path::isBrowsable)
                .forEach {
                    // Direct entry subdirectories

                    buildSubdirectory(
                        outDir,
                        entry.relPath,
                        entry.name(),
                        it
                    )

                    if (it.name.endsWith("---ep")) {
                        buildEp(it, outDir, entry.name(), entry.path)
                    }
                }
        }
}

fun buildEp(epDir: Path, outDir: Path, entryName: String, entryPath: Path) {
    val epRoot = entryPath.resolve(epDir.toString())
    val packageJson = epRoot.resolve("package.json")
    val subPath = Path.of(entryName, epDir.toString())
    val path = Path.of(outDir.toString(), subPath.toString())

    if (packageJson.exists() && packageJson.isRegularFile()) {
        val appSrc = epRoot.resolve("app")
        val appOut = path.resolve("app")
        val appHtml = appOut.parent.resolve("app.html")

        if (appSrc.exists()) {
            deleteDirectory(appSrc)
        }

        runCommand("cmd.exe /c npm i", epRoot)
            .onLeft(handleError `$` "Failed to install Node deps at $epDir")
            .onRight(::println)
            .getOrNull() ?: return

        runCommand("cmd.exe /c npm run build:prod", epRoot)
            .onLeft(handleError `$` "Failed to build $epDir")
            .onRight(::println)
            .getOrNull() ?: return

        appSrc.moveTo(appOut)

        appOut
            .resolve("index.html")
            .moveTo(appHtml)

        val html = appHtml
            .readText()
            .replace("</title>", """</title><base href="app/"/>""")

        appHtml.writeText(html)

        println("✔ Build ${epDir.name}")
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

fun Path.fromEntryRelPathToGithubUrl(subPath: Path): Either<String, String> {
    val githubPath = parent
        .toString()
        .replace("\\", "/")
        .let { "$it/${subPath.toString().replace("\\", "/")}" }
        .let { "tree/main$it" }

    return getGitHubRepoUrl()
        .map { "$it/$githubPath" }
}

fun addDirectoryIndex(
    outDir: Path,
    entryName: String,
    entryRelPath: Path, // src entry path, i.e. classes. e.g. /swe/dev/.../article
    relPath: Path
) {
    val subPath = Path.of(entryName, relPath.toString())
    val path = Path.of(outDir.toString(), subPath.toString())

    val originUrl = entryRelPath
        .fromEntryRelPathToGithubUrl(subPath)
        .onLeft(printError)
        .getOrNull() ?: return

    val name = with(relPath.fileName.toString()) {
        if (relPath.parent == null) Entry(relPath).toTitleCase(dic)
        else this
    }

    val navigationTreeHtml =
        createNavigationTreeHtml(path `---` Files::list, originUrl)

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
    sb.append(openInGitHubButton(originUrl).toHtmlString())
    val index = sb.toString()

    saveIndex(path, index)
}

fun getGitHubRepoUrl(): Either<String, String> =
    runCommand("git config remote.origin.url")
        .onLeft(handleError `$` "Failed to get Git remote info")
        .map(String::fromGitOriginToRepoUrl)

fun String.fromGitOriginToRepoUrl(): String = with(replace("\n", "")) {
    if (contains("https")) this
    else removePrefix("git@")
        .removeSuffix(".git")
        .replace(":", "/")
        .let { "https://$it" }
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

    val originUrl = entryRelPath
        .fromEntryRelPathToGithubUrl(subPath)
        .onLeft(printError)
        .getOrNull() ?: return

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
    sb.append(openInGitHubButton(originUrl).toHtmlString())
    val index = sb.toString()


    // If the file is like .gitignore, then its directory /.gitignore/ will
    // be hidden and unable to access by Git, so it should be /_.gitignore
    // this doesn't matter as the permalink is the same as the original
    val clearedPath = if (path.name.startsWith("."))
        Path.of(path.parent.toString(), "-${path.name}")
    else path

    val fileDir = Path.of("${clearedPath}_")

    fileDir.createDirectory()
    path.moveTo(fileDir.resolve(name))
    fileDir.moveTo(clearedPath)

    saveIndex(clearedPath, index)
}

fun createContentMarkdownString(path: Path): String =
    when (val ext = getFileExtension(path)) {
        "png", "jpg", "gif" -> createImageHtml(path)
        "md" -> Files.readString(path)
        else -> codeSnippetBlockHtml(
            FileResource(
                try {
                    Files.readString(path)
                } catch (e: MalformedInputException) {
                    path.name
                },
                ResourceExtension(ext),
            )
        )
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

    commitRootFiles(config.outDir, config.srcDir)

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

fun commitRootFiles(outDir: Path, srcDir: Path) {
    copyJekyllRootFilesFromBuild(outDir, srcDir)
        .onLeft(
            handleError `$`
                "Failed to copy build Jekyll root files from $outDir to $srcDir"
        )
        .getOrNull() ?: return

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

    runCommand("""git commit -m "Add root files"""")
        .onLeft(handleError `$` "Failed to commit root files to Git")
        .onRight { println("✔ Commit root files to Git") }
        .getOrNull() ?: return
}

fun commitFromBuild(entry: Entry, config: BuildConfig) {
    val (srcDir, outDir) = config

    // e.g. blog/article, from gh-pages branch
    val articleProdPath = srcDir.resolve(entry.name())

    // e.g. blog/out/build/blog/article
    val articleBuildPath = outDir.resolve(entry.name())

    // e.g. blog/out/build/blog/index.md
    val indexBuildPath = outDir.resolve("index.md")

    // e.g. blog/index.md, from gh-pages branch
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

    runCommand("""git commit -m "Deploy ${entry.name()}"""")
        .onLeft(handleError `$` "Failed to commit files to Git")
        .onRight { println("✔ Commit files to Git") }
        .getOrNull() ?: return
}

fun execNotice(root: Path) {
    val entries = Entry(root)
        .loadEntries(::hasNotice)
        .getOrNull() ?: return
    val notice = generateRootNotice(entries, dic)

    println(notice)
}

fun coverUrl(entry: Entry): String =
    entry
        .coverPath()
        .map {
            entry.coverGitHubUrl(
                "tobiasbriones",
                "blog",
                it.toString()
            )
        }
        .getOrElse { "" }


fun getIgnoredRelPaths(): Set<String> {
    val ignoredFiles =
        runCommand("git ls-files --ignored -o --exclude-standard")
            .onLeft(
                handleError `$` "Failed to get ignored Git files to exclude from building"
            )
            .map { ignored -> ignored.lines().toSet() }
            .getOrNull() ?: setOf()

    val ignoredDirs =
        runCommand("git status --porcelain --ignored")
            .onLeft(
                handleError `$` "Failed to get ignored Git files to exclude from building"
            )
            .map { dir ->
                dir
                    .lines()
                    .map { it.removePrefix("!! ") }
                    .map { it.removeSuffix("/") }
                    .toSet()
            }
            .getOrNull() ?: setOf()

    return ignoredFiles + ignoredDirs
}

fun execServe(root: Path) {
    embeddedServer(
        Netty,
        port = 8080,
        module = { serve(root) }
    ).start(wait = true)
}

fun Application.serve(root: Path) {
    val web = root.resolve(
        Path.of("out", "build", root.name, "_site")
    )

    environment.log.info("Serving $web")

    routing {
        get("/") {
            call.respondFile(
                web.resolve("index.html").toFile()
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

            val file = web.resolve(resPath)

            if (file.isRegularFile()) {
                call.respondFile(file.toFile())
            } else {
                call.respondFile(web.resolve("$resPath.html").toFile())
            }
        }
    }
}
