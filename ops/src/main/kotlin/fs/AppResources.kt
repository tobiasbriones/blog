package fs

import java.nio.file.Path

class AppResources {
    companion object {
        fun pathOf(resourcePath: Path): Path = Path
            .of("", "src", "main", "resources", resourcePath.toString())
            .toAbsolutePath()
    }
}
