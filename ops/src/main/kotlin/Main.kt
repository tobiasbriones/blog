import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.Either.Right
import arrow.core.None
import arrow.core.firstOrNone
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import kotlin.io.path.name
import kotlin.io.path.pathString


//fun <T> and(p1: Predicate<T>, p2: Predicate<T>): Predicate<T> = { value ->
//    p1(value) && p2(value)
//}
//
//fun <T> or(p1: Predicate<T>, p2: Predicate<T>): Predicate<T> = { value ->
//    p1(value) || p2(value)
//}
//
//fun <T> not(p: Predicate<T>): Predicate<T> = { value ->
//    !p(value)
//}
//
//// Higher-order function to wrap a predicate with logging behavior
//fun <T> log(predicate: Predicate<T>, message: String): Predicate<T> = { value ->
//    val result = predicate(value)
//    println("$message: $result")
//    result
//}
//
//// Higher-order function to wrap a predicate with string conversion behavior
//fun <T> stringify(predicate: Predicate<T>): Predicate<T> = { value ->
//    val result = predicate(value)
//    println("Predicate: ${predicate.toRawString()} = $result")
//    result
//}
//
//// Extension function to get a raw string representation of a predicate
//fun <T> Predicate<T>.toRawString(): String {
//    return toString().substringAfter(" ")
//}
//
//fun main() {
//    val isEven: Predicate<Int> = { number ->
//        number % 2 == 0
//    }
//
//    val isPositive: Predicate<Int> = { number ->
//        number > 0
//    }
//
//    val isEvenAndPositive = and(isEven, isPositive)
//    val isOddOrNegative = or(not(isEven), not(isPositive))
//
//    val loggedPredicate = log(isEvenAndPositive, "isEvenAndPositive")
//    val stringifiedPredicate = stringify(isOddOrNegative)
//
//    println(loggedPredicate(4))
//    println(stringifiedPredicate(3))
//}


//fun main() {
//    val root = Entry(Path.of(".."))
//    val entries = entries(root)
//
////    val titles = Tree.Node(
////        Title.Heading("Title" `---` title, 1),
////        listOf(
////            Tree.Node(
////                Title.Heading("Section", 2),
////                listOf(
////                    Tree.Node(
////                        Title.Heading("Subsection", 3),
////                        listOf(
////                            Tree.Leaf(
////                                Title.Heading("Subsubsection", 4)
////                            ),
////                            Tree.Leaf(
////                                Title.Caption("", 4)
////                            )
////                        )
////                    )
////                )
////            )
////        )
////    )
//
////    titles.traverse {
////        when (it) {
////            is Title.Heading -> println(" ".repeat((it.heading - 1) * 4))
////            is Title.Caption -> println(" ".repeat((it.heading - 1) * 4))
////        }
////    }
//
//
//}
//


//data class Product(val id: Id, val name: String) {
//    companion object {
//        operator fun invoke(id: Int, name: String): Option<Product> {
//
//        }
//    }
//}
//
//fun program() {
//    val id = 4
//    val name = "Product A"
//
//    val product = Product(id, name)
//    show(product)
//}
//
//fun show(product: Option<Product>) {
//    product.onSome { product: Product -> println(product) }
//}
//
//fun id(value: Int): Option<Id> {
//    val fold = Id(4)
//        .onLeft(::handle)
//        .getOrNone()
//
//}

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
    // More expressive //
    entries(root `---` Path::of `---` ::Entry)
        .firstOrNone { it.name() == entryName }
        .onSome { build(it, ::BuildConfig `$` Path.of(root, "_out")) }

    // More conservative //
    val entry = Entry(Path.of(root))
    entries(entry)
        .firstOrNone { it.name() == entryName }
        .onSome { build(it, BuildConfig(Path.of(root, "_out"))) }
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

    val frontmatter = """
        ---
        
        ---
    """.trimIndent()
    println(abstract)
}

data class BuildConfig(
    val outDir: Path
)

//        .map { Entry(Path.of(it.path.toString(), "index.md")) }
//        .map { toTitle(it) }
//        .map { toMarkdown(it) }
//        .forEach { println(it) }
enum class Fn {
    Entries,
    Build,
}

fun newFn(value: String): Either<None, Fn> = when (value.lowercase()) {
    "entries" -> Right(Fn.Entries)
    "build" -> Right(Fn.Build)
    else -> Left(None)
}

fun toMarkdown(it: TitleLink): String {
    return "[${it.title}](${it.link})"
}

data class TitleLink(val title: String, val link: String)

fun toTitle(index: String, link: String): TitleLink {
    fun extractTitle(input: String): String {
        val lines = input.lines()
        return lines.find { it.startsWith("#") }.orEmpty()
    }

    fun read(contents: String): String {
        val titleLine = extractTitle(contents)
        return titleLine.replace("# ", "")
    }

    return TitleLink(read(index), link)
}

fun toAbstract(index: String): String {
    var text = ""
    var found = false

    for (line in index.lines()) {
        if (line.startsWith("#")) {
            found = true
            continue
        }
        if (!found) {
            continue
        }
        if (line.isBlank() || line.startsWith("#")) {
            if (line.startsWith("#")) {
                break
            }
            if (text.isNotBlank()) {
                break
            }
        }
        text += line
    }
    return text
}

fun handle(msg: String): None {
    return None
}

fun entries(root: Entry): List<Entry> =
    Files
        .walk(root.path)
        .filter(::filterPath)
        .filter(Files::isRegularFile)
        .filter { it.name == "index.md" }
        .filter { it.parent != null }
        .map { Entry(it.parent) }
        .toList()

fun filterPath(path: Path): Boolean {
    val str = path.pathString
    return !str.startsWith("_") && !str.startsWith("out")
}

data class Entry(val path: Path)

sealed interface Title {
    @JvmInline
    value class Title private constructor(val value: String) {
        init {
            require(value.trim() == value) { "Value is not trimmed" }
            require(value.length in 1..25) { "Value not in rage [1, 25]" }
        }

        companion object {
            operator fun invoke(value: String?): Title {
                requireNotNull(value) { "Value must not be null" }
                return Title(value)
            }
        }
    }

    data class Heading(val value: Title, val heading: Int)

    data class Caption(val value: Title, val heading: Int)
}

val title: (String) -> Title.Title = { Title.Title(it) }

fun Entry.name(): String = name(this)
//fun Entry.cover(): Path = Path.of(path.toString(), "${name()}.png")

//fun name(entry: Entry): String = entry.getName


val name: (Entry) -> String = { (path) -> path.name }

//val toc: (Entry) -> List<Title> = {}

val load: (Entry) -> Either<String, String> = {
    try {
        Right(Files.readString(it.path))
    } catch (e: IOException) {
        e.printStackTrace()
        Left(e.message.orEmpty())
    }
}

val loadIndex: (Entry) -> Either<String, String> = {
    try {
        Right(Files.readString(Path.of(it.path.toString(), "index.md")))
    } catch (e: IOException) {
        e.printStackTrace()
        Left(e.message.orEmpty())
    }
}

fun deleteDirectory(directory: Path) {
    if (Files.isDirectory(directory)) {
        Files.walk(directory)
            .sorted(Comparator.reverseOrder())
            .forEach(Files::delete)
    } else {
        throw IllegalArgumentException("Provided path is not a directory.")
    }
}

fun copyDirectory(sourceDir: Path, targetDir: Path) {
    Files.walk(sourceDir).forEach { sourcePath ->
        val relativePath = sourceDir.relativize(sourcePath)
        val targetPath = targetDir.resolve(relativePath)

        if (Files.isDirectory(sourcePath)) {
            Files.createDirectories(targetPath)
        } else {
            Files.copy(
                sourcePath,
                targetPath,
                StandardCopyOption.REPLACE_EXISTING
            )
        }
    }
}
