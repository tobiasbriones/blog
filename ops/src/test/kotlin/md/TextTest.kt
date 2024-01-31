package md

import Entry
import java.nio.file.Path
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class TextTest {
    // Dictionaries have to be defined as per scope, some words are universal
    // others are specific to the underlying article only
    private val dic: Dictionary = Dictionary(
        uppercase = setOf(
            "bsd", "mit", "ep", "bi", "unah-vs", "ai", "ml",
            "sar",
            "hn:", // edge case, I have to add ":" so it's recognized, see
            // test case
            "2dp"
        ),
        composed = mapOf(
            "mathswe" to listOf("math", "swe"),
            "github" to listOf("git", "hub"),
            "gitlab" to listOf("git", "lab"),
            "youtube" to listOf("you", "tube"),
            "" to listOf("", ""),
            "" to listOf("", ""),
        ),
        acronym = mapOf(
            "ddo" to "Data-Driven Organizations",
        ),
        custom = mapOf(
            "intellij idea" to "IntelliJ IDEA",
            "msw mvp app" to "Math Software MVP App",
        )
    )

    @Test
    fun toTitleCase() {
        val entry: (String) -> Entry = { Entry(Path.of("/swe/abc/$it")) }
        val cases = mapOf(
            entry(
                "basic-title"
            ) to "Basic Title",

            entry(
                "temporal-coupled-article-2023-08-13"
            ) to "Temporal Coupled Article (2023/08/13)",

            entry(
                "example-title-_-everything-is-relative"
            ) to "Example Title: Everything is Relative",

            entry(
                "example-project---blog"
            ) to "Example Project | Blog",

            entry(
                "title-with-actual-high--level-hyphen"
            ) to "Title with Actual High-Level Hyphen",

            entry(
                "removing-cyclic-dependencies--_--java-vs-go-2023-05-28"
            ) to "Removing Cyclic Dependencies, Java vs Go (2023/05/28)",

            entry(
                "license-change-from-mit-to-bsd--3--clause-for-code-snippets-2023-04-13"
            ) to "License Change from MIT to BSD-3-Clause for Code Snippets (2023/04/13)",

            entry(
                "4-years-since-vocational-fair-at-unah--vs-2023-05-09"
            ) to "4 Years Since Vocational Fair at UNAH-VS (2023/05/09)",

            entry(
                "ddo-and-power-bi-overview"
            ) to "Data-Driven Organizations and Power BI Overview",

            entry(
                "finishing-writing-the-documentation-for-my-next-ep-2023-07-14"
            ) to "Finishing Writing the Documentation for my Next EP (2023/07/14)",

            entry(
                "how-i-use-intellij-idea"
            ) to "How I Use IntelliJ IDEA",

            entry(
                "this-is-a-state--of--the--art-ai-model"
            ) to "This is a State-of-the-Art AI Model",

            entry(
                "sar-hn-_-sales-tax"
            ) to "SAR HN: Sales Tax",

            entry(
                "updating-from-angular-v15-to-v17---2dp-repsymo-2023-12-06"
            ) to "Updating from Angular V15 to V17 | 2DP Repsymo (2023/12/06)",

            entry(
                "initializing-operations---msw-mvp-app-2024-01-30"
            ) to "Initializing Operations | Math Software MVP App (2024/01/30)"
        )

        cases.forEach { assertEquals(it.value, it.key.toTitleCase(dic)) }
    }

    @Test
    fun toTitleCaseNoDictionary() {
        val entry: (String) -> Entry = { Entry(Path.of("/swe/xyz/$it")) }
        val cases = mapOf(
            entry(
                "basic-title"
            ) to "Basic Title",

            entry(
                "temporal-coupled-article-2023-08-13"
            ) to "Temporal Coupled Article (2023/08/13)",
        )

        cases.forEach { assertEquals(it.value, it.key.toTitleCase()) }
    }

    @Test
    fun split() {
        val str = "how i work in intellij idea with kotlin"

        assertContentEquals(
            listOf("how", "i", "work", "in", "intellij idea", "with", "kotlin"),
            dic.split(str),
        )
    }
}
