package jekyll

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class JekyllTest {
    private val frontMatter: FrontMatter = FrontMatter(
        "article",
        "Article Title"
    )

    @Test
    fun toMarkdown() {
        assertEquals(
            """
                ---
                permalink: article
                title: 'Article Title'
                ---
                
            """.trimIndent(),
            frontMatter.toMarkdownString()
        )
    }
}
