import md.Dictionary

val dic: Dictionary = Dictionary(
    uppercase = setOf(
        "bsd", "mit", "ep", "bi", "unah-vs", "ai", "ml", "swe",
        "sar", "hn", "hn:", "mrm",
    ),
    composed = mapOf(
        "mathswe" to listOf("math", "swe"),
        "github" to listOf("git", "hub"),
        "gitlab" to listOf("git", "lab"),
        "youtube" to listOf("you", "tube"),
        "" to listOf("", ""),
        "" to listOf("", ""),
    ),
    acronym = mapOf(
        "ddo" to "Data-Driven Organizations",
    ),
    custom = mapOf(
        "intellij idea" to "IntelliJ IDEA"
    )
)
