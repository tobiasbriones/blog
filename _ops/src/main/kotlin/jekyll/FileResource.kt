package jekyll

data class FileResource(val content: String, val extension: ResourceExtension)

data class ResourceExtension(val value: String)

fun ResourceExtension.langCode() = when (value) {
    "kt" -> "kotlin"
    "java" -> "java"
    "py" -> "python"
    "js" -> "javascript"
    "cpp" -> "cpp"
    "c" -> "c"
    "swift" -> "swift"
    "go" -> "go"
    "rb" -> "ruby"
    "php" -> "php"
    "ts" -> "typescript"
    "cs" -> "csharp"
    "scala" -> "scala"
    "rust" -> "rust"
    "dart" -> "dart"
    "html" -> "html"
    "css" -> "css"
    "xml" -> "xml"
    "json" -> "json"
    "yaml" -> "yaml"
    "hs" -> "haskell"
    "lisp" -> "lisp"
    "clojure" -> "clojure"
    "erl" -> "erlang"
    "purs" -> "purescript"
    else -> ""
}