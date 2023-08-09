import fs.loadIndex
import java.nio.file.Path
import kotlin.test.Test

class EntryTest {
    private val entry: Entry = Entry(
        TestResources.pathOf(Path.of("fp-in-kotlin"))
    )

    @Test
    fun loadIndex() {
        assert(entry.loadIndex().isRight())
    }
}
