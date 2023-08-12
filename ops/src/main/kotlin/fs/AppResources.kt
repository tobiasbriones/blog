package fs

import java.nio.file.Path
import kotlin.io.path.toPath

class AppResources {
    companion object {
        fun pathOf(resourcePath: Path): Path = ClassLoader
            .getSystemResource(resourcePath.toString())
            ?.toURI()
            ?.toPath()
            ?: Path
                .of("", "src", "main", "resources", resourcePath.toString())
                .toAbsolutePath()
    }
}
