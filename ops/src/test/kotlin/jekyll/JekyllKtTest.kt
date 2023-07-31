package jekyll

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class JekyllKtTest {
    val frontMatter = FrontMatter(
        "/article",
        "title"
    )

    @Test
    fun toMarkdown() {
        assert(
            frontMatter.toMarkdown() == """
                ---
                permlink: /article
                title: title
                ---
            """.trimIndent()
        )
    }

    @Test
    fun extractTitle() {
        assert(
            frontMatter.toMarkdown() == "FP in Kotlin"
        )
    }
}
