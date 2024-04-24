package react

import Entry
import arrow.core.left
import arrow.core.toOption
import name
import org.jsoup.Jsoup
import printError
import java.io.IOException
import java.nio.file.Path

fun buildReact(entry: Entry, outDir: Path) {
    val body = readHtmlBodyFromBuild(entry, outDir)

    println(body)
}

fun readHtmlBodyFromBuild(entry: Entry, outDir: Path): String = outDir
    .resolve("_site")
    .resolve("${entry.name()}.html")
    .run {
        try {
            Jsoup
                .parse(toFile())
                .selectFirst("section.main")
                .toOption()
                .map { it.html() }
                .toEither { "Fail to find main section of HTML output" }
        } catch (e: IOException) {
            "Fail to parse output HTML: ${e.message}".left()
        }
    }
    .onLeft(printError)
    .getOrNull() ?: ""
