fun String.wrapText(lineWidth: Int = 80): String {
    val lines = replace("\\r\\n", "\n").lines()
    val wrapped = StringBuilder()
    var count = 0

    for (line in lines) {
        val words = line.trim().split(' ')

        for (word in words) {
            if (count + word.length >= lineWidth) {
                if (count == 0) {
                    continue
                }
                wrapped.appendLine()
                count = 0
            }

            if (count == 0) {
                wrapped.append(word)
                count += word.length
            } else {
                wrapped.append(" $word")
                count += word.length + 1
            }
        }

        wrapped.appendLine()
        count = 0
    }

    return wrapped.toString().removeSuffix("\n")
}
