package engineer.mathsoftware.tsdfx.drawing

import javafx.scene.text.Text
import javafx.scene.text.TextFlow

enum class MdTextStyle { Normal, Bold }

data class MdStyledText(val text: String, val style: MdTextStyle)

fun String.splitByStyle(): List<MdStyledText> {
    val regex = "\\*\\*(.*?)\\*\\*".toRegex()
    val styledTexts = mutableListOf<MdStyledText>()
    var lastIndex = 0

    regex.findAll(this).forEach { match ->
        val plainText = substring(lastIndex, match.range.first)

        if (plainText.isNotEmpty()) {
            styledTexts.add(MdStyledText(plainText, MdTextStyle.Normal))
        }
        styledTexts.add(MdStyledText(match.groupValues[1], MdTextStyle.Bold))
        lastIndex = match.range.last + 1
    }
    val remainingText = substring(lastIndex)

    if (remainingText.isNotEmpty()) {
        styledTexts.add(MdStyledText(remainingText, MdTextStyle.Normal))
    }
    return styledTexts
}

fun String.parseMd(
    normalTextCss: String,
    boldTextCss: String,
): TextFlow = TextFlow().apply {
    children.addAll(
        splitByStyle()
            .map {
                Text(it.text).apply {
                    style = when (it.style) {
                        MdTextStyle.Normal -> normalTextCss
                        MdTextStyle.Bold   -> boldTextCss
                    }
                }
            }
    )
}
