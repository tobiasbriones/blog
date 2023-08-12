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
    deployAll()

//    serve()
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

fun runCmd(cmd: String) {
    main(cmd.split(" ").toTypedArray())
}
