import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Serializable
data class Base(
    val ref: String
)

@Serializable
data class Repo(
    val full_name: String
)

@Serializable
data class Head(
    val ref: String,
    val repo: Repo,
)

@Serializable
data class User(
    val login: String,
    val html_url: String,
)

@Serializable
data class PullRequest(
    val url: String,
    val html_url: String,
    val id: Int,
    val number: Int,
    val state: String,
    val title: String,
    val body: String?,
    val created_at: String,
    val updated_at: String,
    val closed_at: String,
    val merged_at: String,
    val base: Base,
    val head: Head,
    val user: User,
)

suspend fun fetchClosedPullRequests(
    path: String,
    from: Int,
): Either<HttpStatusCode, List<PullRequest>> {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    val url = "https://api.github.com/repos/$path/pulls"
    val response = client.get(url) {
        parameter("state", "closed")
        parameter("number", ">$from")
    }

    return when (response.status) {
        HttpStatusCode.OK -> response
            .body<List<PullRequest>>()
            .filter { it.number >= from }
            .right()

        else -> response.status.left()
    }
}

fun PullRequest.titleMd() = "**${title}**"

fun PullRequest.subtitleMd(): String {
    val date = mergedShortDate()
    val branch = "`${base.ref} <- ${head.ref}`"
    val pr = "PR [#$number]($html_url)"
    val user = "[${user.login}](${user.html_url})"
    val content = "$date: $pr merged into $branch by $user"
    return """
        |$content
        |{: .pr-subtitle }
        """.trimMargin("|")
}

fun PullRequest.mergedShortDate(): String? {
    val formatter = DateTimeFormatter
        .ofPattern("yyyy-MM-dd'T'HH:mm:ssX", Locale.ENGLISH)
    val zonedDateTime = ZonedDateTime.parse(merged_at, formatter)

    return zonedDateTime.format(
        DateTimeFormatter.ofPattern("MMM d", Locale.ENGLISH)
    )
}
