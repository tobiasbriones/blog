// MAKE SURE to add the working directory of the Git repository to test
// For example: P:\tobiasbriones\test-blog-deploy
// to run the program in that project
fun main() {
//    entries()
//    buildOne()
//    buildAll()
    deployOne()
//    deployAll()
}

fun entries() {
    runCmd("entries")
}

fun buildOne() {
    runCmd("build fp-in-kotlin")
}

fun buildAll() {
    runCmd("build .")
}

fun deployOne() {
    runCmd("deploy fp-in-kotlin")
}

fun deployAll() {
    runCmd("deploy .")
}

fun runCmd(cmd: String) {
    main(cmd.split(" ").toTypedArray())
}
