package md

import TestResources
import org.junit.jupiter.api.Test
import java.nio.file.Path
import kotlin.io.path.readText
import kotlin.test.assertEquals

class MdTest {
    private val fpInKotlin: Index = Index(
        Markdown(
            TestResources
                .pathOf(Path.of("fp-in-kotlin", "index.md"))
                .readText()
        )
    )
    private val piDay: Index = Index(
        Markdown(
            TestResources
                .pathOf(Path.of("pi-day", "index.md"))
                .readText()
        )
    )

    @Test
    fun extractTitle() {
        assertEquals("FP in Kotlin", fpInKotlin.extractTitle())
    }

    @Test
    fun extractAbstract() {
        assertEquals(
            """
            I'll document particular insights about Kotlin functional designs I figure out.
        """.trimIndent(),
            fpInKotlin.extractAbstract()
        )

        assertEquals(
            """
            Pi is my official and favorite constant and today is 3/14 day when the Pi
            constant is celebrated.
        """.trimIndent(),
            piDay.extractAbstract()
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

        assertEquals(expected, codeBlock)
    }

    @Test
    fun codeSnippetParsing() {
        val mdCodeBlock = """
            `Definition of an "Article" DSL | Pipe.kt`

            ```kotlin
            data class Article(val title: Title, val content: String)

            @JvmInline
            value class Title(val value: String) {
                override fun toString(): String = value
            }

            val title: (String) -> Title = { Title(it) }
            ```
        """.trimIndent()
        val expected = """
            
            <figure>
            {% capture markdownContent %}

            `Pipe.kt`

            ```kotlin
            data class Article(val title: Title, val content: String)

            @JvmInline
            value class Title(val value: String) {
                override fun toString(): String = value
            }

            val title: (String) -> Title = { Title(it) }
            ```

            {% endcapture %}

            {{ markdownContent | markdownify }}

            <figcaption>Definition of an "Article" DSL</figcaption>
            </figure>
            
        """.trimIndent()

//        assertEquals(expected, Markdown(mdCodeBlock).parseCodeSnippets().value)
    }
}
