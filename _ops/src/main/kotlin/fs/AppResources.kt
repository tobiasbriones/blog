package fs

import java.nio.file.Path
import kotlin.io.path.toPath

class AppResources {
    companion object {
        fun pathOf(resourcePath: Path): Path = Path
            .of(
                Companion::class
                    .java
                    .classLoader
                    .getResource(
                        resourcePath
                            .toString()
                    )
                    ?.toURI()
                    ?.toPath()
                    .toString()
            )
    }
}
