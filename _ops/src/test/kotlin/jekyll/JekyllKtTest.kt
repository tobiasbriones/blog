package jekyll

import org.junit.jupiter.api.Test

class JekyllKtTest {
    val frontMatter = FrontMatter(
        "/article",
        "title"
    )

    @Test
    fun toMarkdown() {
        assert(
            frontMatter.toMarkdownString() == """
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
            frontMatter.toMarkdownString() == "FP in Kotlin"
        )
    }
}
