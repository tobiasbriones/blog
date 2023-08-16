package fs

import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.toPath

class AppFiles {
    companion object {
        fun pathOf(filePath: Path): Path {
            val execDir = Companion::class.java
                .protectionDomain
                .codeSource
                .location
                .toURI()
                .toPath()

            // Prod path, i.e., files/ accessed from
            // {install_dir}/lib/{app}.jar/
            val prodFiles = execDir.parent.parent.resolve("files")

            if (prodFiles.exists()) {
                return prodFiles.resolve(filePath)
            }

            // Local path, i.e., src/main/files accessed from
            // build/classes/kotlin/main
            return execDir
                .parent
                .parent
                .parent
                .parent // project root
                .resolve("src/main/files")
                .resolve(filePath)
        }
    }
}
