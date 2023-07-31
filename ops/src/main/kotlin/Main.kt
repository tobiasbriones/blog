import arrow.core.*
import arrow.core.Either.Left
import arrow.core.Either.Right
import fs.*
import jekyll.FileResource
import jekyll.FrontMatter
import jekyll.ResourceExtension
import jekyll.codeSnippetBlockHtml
import md.Index
import md.Markdown
import md.extractAbstract
import md.navHtml
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import kotlin.io.path.*


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
    val index = entry.loadIndex().getOrNull() ?: Index(Markdown(""))
    val title = toTitle(index, entry.path.fileName.toString())
    val abstract = index.extractAbstract()
    val coverUrl = coverUrl(entry)

    val frontMatter = FrontMatter(
        title.link,
        title.title,
        Some(abstract),
        Some(coverUrl)
    )
    val toc = navHtml(index)

    val prod = frontMatter.toString() + toc + index
    saveIndex(entryDir, prod)

    buildSubdirectories(config.outDir, entry)
}

fun buildSubdirectories(outDir: Path, entry: Entry) {
    val filter: (Path) -> Boolean = { it.name != "images" }

    entry
        .subdirectories()
        .fold(
            { print(it) },
            {
                it.filter(filter)
                    .forEach {
                        buildSubdirectory(
                            outDir,
                            entry.name(),
                            it
                        )
                    }
            }
        )
}

fun buildSubdirectory(outDir: Path, entryName: String, relPath: Path) {
    val subPath = Path.of(entryName, relPath.toString())
    val path = Path.of(outDir.toString(), subPath.toString())

    if (path.isDirectory()) {
        Files
            .list(path)
            .forEach {
                buildSubdirectory(
                    outDir,
                    entryName,
                    Path.of(relPath.toString(), it.name)
                )
            }
        addDirectoryIndex(outDir, entryName, relPath)
    } else {
        addContentIndex(outDir, entryName, relPath)
    }
}

fun addDirectoryIndex(outDir: Path, entryName: String, relPath: Path) {
    val subPath = Path.of(entryName, relPath.toString())
    val path = Path.of(outDir.toString(), subPath.toString())
    val name = relPath.fileName.toString()
    // front matter is being useless
//    val frontMatter = FrontMatter(
//        subPath.toString().replace("\\", "/"),
//        name,
//        "Files on $name"
//    )
//    val frontMatterMd = frontMatter.toMarkdown()
    val navigationTreeHtml = createNavigationTreeHtml(path.toFile())
    val sb = StringBuilder()

//    sb.append(frontMatterMd)
//    sb.append("\n")
    sb.append("# $name")
    sb.append("\n")
    sb.append(navigationTreeHtml)
    val index = sb.toString()

    saveIndex(path, index)
}

fun addContentIndex(outDir: Path, entryName: String, relPath: Path) {
    val subPath = Path.of(entryName, relPath.toString())
    val path = Path.of(outDir.toString(), subPath.toString())
    val name = relPath.fileName.toString()
    val fileContentHtml = createContentHtml(path)
    val sb = StringBuilder()

    sb.append("# $name")
    sb.append("\n")
    sb.append(fileContentHtml)
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


fun createNavigationTreeHtml(rootPath: File): String {
    fun createHTMLTree(file: File): String {
        val children = file.listFiles()
        return if (file.isDirectory && children != null) {
            "<li>${file.name}<ul>${children.joinToString("") { createHTMLTree(it) }}</ul></li>"
        } else {
            "<li>${file.name}</li>"
        }
    }

    return """
        <ul>
            ${createHTMLTree(rootPath)}
        </ul>
    """.trimIndent()
}

fun exec_deploy(root: String, entryName: String) {
    val entries = entries(root `---` Path::of `---` ::Entry)
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

//    runCommand("git push origin gh-pages", config.deployDir)
//        .fold(
//            { println(it) },
//            { println(it) },
//        )
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
