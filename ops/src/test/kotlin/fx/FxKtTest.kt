package fx

import arrow.core.None
import arrow.core.some
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FxKtTest {
    @Test
    fun extractsAbstract() {
        val abstract = "Abstract paragraph."
        val abstractWrap = "\"$abstract\"".some()
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

        assertEquals(abstractWrap, getAbstract(tokensWithOnlyAbstract))
        assertEquals(abstractWrap, getAbstract(tokensWithDataNoFooter))
        assertEquals(abstractWrap, getAbstract(tokensWithDataAndFooter))
    }

    @Test
    fun extractFooter() {
        val footer = "Item 1., Item 2., Item 3."
        val footerWrapped = "\"$footer\"".some()
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
        assertEquals(footerWrapped, getFooter(tokensWithAbstractAndFooter))
        assertEquals(footerWrapped, getFooter(tokensWithDataAndFooter))
    }

    @Test
    fun resourcePathWorks() {
        val res = getFilePath("cover/bg.png")

        assertTrue(res.isSome())
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
            "mathswe/mathswe.com",
            "${extractedWithoutRedundancy?.first}/${extractedWithoutRedundancy?.second}"
        )
    }

    @Test
    fun extractsSourceBranchNames() {
        val textNone = """
            # Article

            Par.

            ---

            **Publish cookie-consent v0.1.0**

            Mar 28: PR [#1](https://github.com/mathswe/legal/pull/1) merged
            by [tobiasbriones](https://github.com/tobiasbriones)
            {: .pr-subtitle }
        """.trimIndent()

        val textSimple = """
            ---

            **Publish cookie-consent v0.1.0**

            Mar 28: PR [#1](https://github.com/mathswe/legal/pull/1) merged
            into `main <- cookie-consent`
            by [tobiasbriones](https://github.com/tobiasbriones)
            {: .pr-subtitle }
        """.trimIndent()

        val textMultiple = """
            # Article

            Abstract.

            ---

            **Publish cookie-consent v0.1.0**

            Mar 28: PR [#1](https://github.com/mathswe/legal/pull/1) merged
            into `main <- cookie-consent`
            by [tobiasbriones](https://github.com/tobiasbriones)
            {: .pr-subtitle }

            Par.

            ---

            **Publish cookie-consent v0.1.0**

            Mar 28: PR [#1](https://github.com/mathswe/legal/pull/1) merged
            into `main <- cookie-consent`
            by [tobiasbriones](https://github.com/tobiasbriones)
            {: .pr-subtitle }

            Par.

            ---

            **Publish cookie-consent v0.1.0**

            Mar 28: PR [#1](https://github.com/mathswe/legal/pull/1) merged
            into `main <- legal/ops`
            by [tobiasbriones](https://github.com/tobiasbriones)
            {: .pr-subtitle }

            Par.
        """.trimIndent()

        assertEquals(
            listOf(),
            extractBranchNames(textNone)
        )
        assertEquals(
            listOf("cookie-consent"),
            extractBranchNames(textSimple)
        )
        assertEquals(
            listOf("cookie-consent", "cookie-consent", "legal/ops"),
            extractBranchNames(textMultiple)
        )
    }

    @Test
    fun infersRepoVersion() {
        val index = """
            # Title
            
            Abstract text.
            
            ## Intro
            
            Release for [Feature](https://github.com/mathswe/lambda/releases/tag/v0.12.1)
            with refactorizations and features.
            
            ## Feature X
            
            Par.
            
            Difference 
            [link](https://github.com/mathswe/mathswe.com/releases/tag/v0.2.0) here.
            
            ## Done
            
            Par.
        """.trimIndent()

        assertEquals(
            "0.12.1".some(), inferRepoVersion(index, "mathswe/lambda")
        )
    }

    @Test
    fun infersMicroserviceVersion() {
        val articleTitleWithSubheading =
            "Workforce Model v0.23.5 | MathSwe Lambda"
        val onlyRepoRelease = "Cookie Banner v1.23.0"

        assertEquals(
            "0.23.5".some(),
            inferSubheadingVersion(articleTitleWithSubheading)
        )

        assertEquals(None, inferSubheadingVersion(onlyRepoRelease))
    }
}
