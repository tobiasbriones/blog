package fs

import java.nio.file.Path
import kotlin.io.path.toPath

class AppFiles {
    companion object {
        fun pathOf(filePath: Path): Path = Companion::class.java
            .protectionDomain
            .codeSource
            .location
            .toURI()
            .toPath()
            .resolve(filePath)
    }
}
