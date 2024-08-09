package engineer.mathsoftware.tsdfx.drawing

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MdKtTest {
    @Test
    fun boldMdTextSplit() {
        val inputStrings = listOf(
            "Normal Text 123",
            "Normal",
            "It integrates the Cookie Consent v0.2.0 into the banner and customization pane:",
            "Text with **bold markdown** words",
            "Text with **bold** words",
            "**word**",
            "Text with bold ** words**",
            "**Text** with many **bold** **words**"
        )

        val expectedResults = listOf(
            listOf(MdStyledText("Normal Text 123", MdTextStyle.Normal)),
            listOf(MdStyledText("Normal", MdTextStyle.Normal)),
            listOf(
                MdStyledText(
                    "It integrates the Cookie Consent v0.2.0 into the banner and customization pane:",
                    MdTextStyle.Normal
                )
            ),
            listOf(
                MdStyledText("Text with ", MdTextStyle.Normal),
                MdStyledText("bold markdown", MdTextStyle.Bold),
                MdStyledText(" words", MdTextStyle.Normal)
            ),
            listOf(
                MdStyledText("Text with ", MdTextStyle.Normal),
                MdStyledText("bold", MdTextStyle.Bold),
                MdStyledText(" words", MdTextStyle.Normal)
            ),
            listOf(MdStyledText("word", MdTextStyle.Bold)),
            listOf(
                MdStyledText("Text with bold ", MdTextStyle.Normal),
                MdStyledText(" words", MdTextStyle.Bold)
            ),
            listOf(
                MdStyledText("Text", MdTextStyle.Bold),
                MdStyledText(" with many ", MdTextStyle.Normal),
                MdStyledText("bold", MdTextStyle.Bold),
                MdStyledText(" ", MdTextStyle.Normal),
                MdStyledText("words", MdTextStyle.Bold)
            )
        )

        inputStrings.forEachIndexed { index, inputString ->
            val actualResult = inputString.splitByStyle()
            val expectedResult = expectedResults[index]
            assertEquals(
                expectedResult,
                actualResult,
                "Test failed for input: $inputString"
            )
        }
    }
}
