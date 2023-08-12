package fs

import org.junit.jupiter.api.Test
import java.nio.file.Path
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PathsTest {
    private val base: String =
        "P:\\tobiasbriones\\test-blog-deploy\\swe\\lang\\fp-in-kotlin"

    @Test
    fun isBrowsable() {
        assertTrue(Path.of(base, "kotlin").isBrowsable())
        assertTrue(Path.of(base, "kotlin", "Pipe.kt").isBrowsable())
        assertTrue(Path.of(base, "slides---ep").isBrowsable())
        assertTrue(Path.of(base, "slides---ep", "src").isBrowsable())

        assertFalse(Path.of(base, "static").isBrowsable())
        assertFalse(Path.of(base, "static", "img.png").isBrowsable())
        assertFalse(Path.of(base, "images").isBrowsable())
        assertFalse(Path.of(base, "images", "img.png").isBrowsable())
    }

    @Test
    fun shouldBeCopied() {
        assertTrue(Path.of(base, "static").shouldBeCopied())
        assertTrue(Path.of(base, "static", "car.pdf").shouldBeCopied())
        assertTrue(Path.of(base, "images").shouldBeCopied())
        assertTrue(Path.of(base, "images", "img.png").shouldBeCopied())
        assertTrue(Path.of(base, "kotlin").shouldBeCopied())
        assertTrue(Path.of(base, "slides---ep").shouldBeCopied())
        assertTrue(Path.of(base, "slides---ep", ".gitignore").shouldBeCopied())
        assertTrue(
            Path.of(base, "slides---ep", "src", "App.java")
                .shouldBeCopied()
        )

        assertFalse(Path.of(base, "kotlin", "project.iml").shouldBeCopied())
        assertFalse(Path.of(base, "kotlin", ".idea").shouldBeCopied())
        assertFalse(Path.of(base, "kotlin", "out").shouldBeCopied())
        assertFalse(Path.of(base, "kotlin", "out", "abc").shouldBeCopied())
        assertFalse(Path.of(base, "slides---ep", "out").shouldBeCopied())
        assertFalse(
            Path.of(base, "slides---ep", "out", "garbage.xml")
                .shouldBeCopied()
        )
        assertFalse(
            Path.of(base, "slides---ep", ".idea", ".gitignore").shouldBeCopied()
        )
    }
}
