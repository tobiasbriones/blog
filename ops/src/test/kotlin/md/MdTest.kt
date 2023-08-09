package md

import TestResources
import org.junit.jupiter.api.Test
import java.nio.file.Path
import kotlin.io.path.readText

class MdTest {
    private val index: Index = Index(
        Markdown(
            TestResources
                .pathOf(Path.of("fp-in-kotlin", "index.md"))
                .readText()
        )
    )

    @Test
    fun extractTitle() {
        assert(index.extractTitle() == "FP in Kotlin")
    }

    @Test
    fun extractAbstract() {
        assert(
            index.extractAbstract() == """
            I'll document particular insights about Kotlin functional designs I figure out.
        """.trimIndent()
        )
    }

    @Test
    fun codeSnippetBlock() {
        val codeBlock = codeSnippetBlock(
            "kotlin",
            """
            data class Article(val title: Title, val content: String)

            @JvmInline
            value class Title(val value: String) {
                override fun toString(): String = value
            }

            val title: (String) -> Title = { Title(it) }
        """.trimIndent()
        )
        val expected = """
            ```kotlin
            data class Article(val title: Title, val content: String)

            @JvmInline
            value class Title(val value: String) {
                override fun toString(): String = value
            }

            val title: (String) -> Title = { Title(it) }
            ```
        """.trimIndent()

        assert(codeBlock == expected)
    }
}