import java.nio.file.Path

class TestResources {
    companion object {
        fun pathOf(resourcePath: Path): Path = Path
            .of("", "src", "test", "resources", resourcePath.toString())
            .toAbsolutePath()
    }
}
