import md.Dictionary

val dic: Dictionary = Dictionary(
    uppercase = setOf(
        "bsd", "mit", "ep", "bi", "unah-vs", "ai", "ml", "swe",
        "sar", "hn", "hn:", "mrm", "fxml", "html", "css", "uml",
        "ssd", "fhd", "hd", "og", "isv", "ocr",

    ),
    composed = mapOf(
        "mathswe" to listOf("math", "swe"),
        "github" to listOf("git", "hub"),
        "gitlab" to listOf("git", "lab"),
        "youtube" to listOf("you", "tube"),
        "listview" to listOf("list", "view"),
        "" to listOf("", ""),
    ),
    acronym = mapOf(
        "ddo" to "Data-Driven Organizations",
    ),
    custom = mapOf(
        "intellij idea" to "IntelliJ IDEA",
        "intellij" to "IntelliJ",
        "intellij:" to "IntelliJ:",
        "picrt" to "PicRT",
        "mckinsey" to "McKinsey"
    )
)
