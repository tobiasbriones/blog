package fx

import arrow.core.None
import arrow.core.some
import extractOrgAndRepo
import getAbstract
import getFooter
import getResourceFilePath
import org.junit.jupiter.api.Test
import splitPipe
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FxKtTest {
    @Test
    fun extractsAbstract() {
        val abstract = "Abstract paragraph."
        val tokensWithOnlyAbstract = listOf(abstract)
        val tokensWithDataNoFooter = listOf(
            "`Heading | Project X`",
            abstract
        )
        val tokensWithDataAndFooter = listOf(
            "`Heading | Project X`",
            abstract,
            """
                |- Item 1.
                |- Item 2.
            """.trimMargin("|")
        )

        assertEquals(abstract.some(), getAbstract(tokensWithOnlyAbstract))
        assertEquals(abstract.some(), getAbstract(tokensWithDataNoFooter))
        assertEquals(abstract.some(), getAbstract(tokensWithDataAndFooter))
    }

    @Test
    fun extractFooter() {
        val footer = "Item 1., Item 2., Item 3.".some()
        val tokensWithOnlyAbstract = listOf("Abstract paragraph.")
        val tokensWithDataNoFooter = listOf(
            "`Heading | Project X`",
            "Abstract paragraph."
        )
        val tokensWithAbstractAndFooter = listOf(
            "`Heading | Project X`",
            "Abstract paragraph.",
            """
                |- Item 1.
                |- Item 2.
                |- Item 3.
            """.trimMargin("|")
        )
        val tokensWithDataAndFooter = listOf(
            "`Heading | Project X`",
            "Abstract paragraph.",
            """
                |- Item 1.
                |- Item 2.
                |- Item 3.
            """.trimMargin("|")
        )

        assertEquals(None, getFooter(tokensWithOnlyAbstract))
        assertEquals(None, getFooter(tokensWithDataNoFooter))
        assertEquals(footer, getFooter(tokensWithAbstractAndFooter))
        assertEquals(footer, getFooter(tokensWithDataAndFooter))
    }

    @Test
    fun resourcePathWorks() {
        val res = getResourceFilePath("cover/bg.png")

        assertTrue { res?.contains("/resources/main/cover/bg.png") ?: false }
    }

    @Test
    fun splitsHeadingWithPipeIntoPair() {
        val headingNoPipe = "Heading Info"
        val headingWithPipe = "Program X | Context ABC"

        assertEquals(
            Pair("Heading Info", ""),
            splitPipe(headingNoPipe)
        )
        assertEquals(
            Pair("Program X", "Context ABC"),
            splitPipe(headingWithPipe)
        )
    }

    @Test
    fun extractsRepoFromText() {
        val textNormal = """
            Pull request https://github.com/mathswe/lambda/pull/13 by
            tobiasbriones
        """.trimIndent()
        val textWithRedundancy = """
            Pull request https://github.com/mathswe/mathswe.com/pull/13 by
            tobiasbriones
        """.trimIndent()
        val extractedNormal = extractOrgAndRepo(textNormal)
        val extractedWithoutRedundancy = extractOrgAndRepo(textWithRedundancy)

        assertEquals(
            "mathswe/lambda",
            "${extractedNormal?.first}/${extractedNormal?.second}"
        )
        assertEquals(
            "mathswe/@.com",
            "${extractedWithoutRedundancy?.first}/${extractedWithoutRedundancy?.second}"
        )
    }
}