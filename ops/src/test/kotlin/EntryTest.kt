import java.io.File
import java.nio.file.Path
import kotlin.test.Test

class EntryTest {
    val entry = Entry(
        File(
            javaClass.classLoader.getResource("fp-in-kotlin.md").file
        ).toPath()
    )

    @Test
    fun loadIndex() {
        assert(entry.loadIndex().isRight())
    }
}
