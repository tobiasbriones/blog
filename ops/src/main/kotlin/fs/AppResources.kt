package fs

import java.nio.file.Path

class AppResources {
    companion object {
        fun pathOf(resourcePath: Path): Path = Path
            .of("", "src", "main", "resources", resourcePath.toString())
            .toAbsolutePath()

//        fun pathOf(resourcePath: Path): Path = Path
//            .of(
//                Companion::class
//                    .java
//                    .classLoader
//                    .getResource(
//                        resourcePath
//                            .toString()
//                    )
//                    ?.toURI()
//                    ?.toPath()
//                    .toString()
//            )
    }
}
