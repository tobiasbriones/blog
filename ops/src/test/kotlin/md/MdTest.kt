package md

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File
import java.nio.file.Files

class MdTest {
    private val index: Index = Index(
        Markdown(
            Files.readString(
                File(
                    javaClass.classLoader.getResource("fp-in-kotlin.md").file
                ).toPath()
            )
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