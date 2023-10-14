
import fs.generateRootNotice
import fs.hasNotice
import fs.loadEntries
import java.nio.file.Path
import kotlin.io.path.relativeTo

// MAKE SURE to add the working directory of the Git repository to test
// For example: P:\tobiasbriones\test-blog-deploy
// to run the program in that project
fun main() {
//    entries()
//    buildOne()
//    buildOneJekyll()
//    buildAll()
//    buildAllJekyll()
//    deployOne()
//    deployAll()
//    create()
//    serve()

//    testRootNoticeLinks()

    val secondaryPath = Path.of(
        "article",
        "images",
        "article.png"
    )

    val entry = Entry(Path.of("article"))

    println(
        Path.of(
        "P:\\tobiasbriones\\blog\\mathswe\\engineer\\news\\drawing-a-tree-on" +
            "-canvas-with-xy-coordinates\\cleaning-up-details-2023-10-06" +
                "\\cleaning-up-details-2023-10-06.png")
            .relativeTo(Path.of(
                "P:\\tobiasbriones\\blog\\mathswe\\engineer\\news\\drawing-a-tree-on" +
                    "-canvas-with-xy-coordinates\\cleaning-up-details-2023-10-06"
            ))
            .toString()
            .replace("\\", "/")

    )

    //runCmd("build drawing-a-tree-on-canvas-with-xy-coordinates")
//    runCmd("build building-slides-from-screenshots-app-in-javafx jekyll")
//    runCmd("build 4-years-since-vocational-fair-at-unah-vs-2023-05-09")
//    runCmd("create automating-the-platform-operations-and-beyond-2023-08-31 mathswe,eng,automation,platform,ops")
//    runCmd("deploy automating-the-platform-operations-and-beyond-2023-08-31")

}

fun entries() {
    runCmd("entries")
}

fun buildOne() {
    runCmd("build fp-in-kotlin")
}

fun buildOneJekyll() {
    runCmd("build fp-in-kotlin jekyll")
}

fun buildAll() {
    runCmd("build .")
}

fun buildAllJekyll() {
    runCmd("build . jekyll")
}

fun deployOne() {
    runCmd("deploy fp-in-kotlin")
}

fun deployAll() {
    runCmd("deploy .")
}

fun serve() {
    runCmd("serve")
}

fun create() {
    runCmd("create test-article swe,test,cli,example")
}

fun testRootNoticeLinks() {
    val entries = Entry(Path.of(""))
        .loadEntries(::hasNotice)
        .getOrNull() ?: return
    val notice = generateRootNotice(entries, dic)

    println(notice)
}

fun runCmd(cmd: String) {
    main(cmd.split(" ").toTypedArray())
}
